package me.lokka30.phantomworlds.misc;

import me.lokka30.microlib.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * This class makes it easier and cleaner to get & send colorized multi-placeholder multi-line messages.
 * Reduces 2-3 lines of code and removes a lot of repeating code as if it was not used. This is for each message in the plugin...
 * it adds up quickly and becomes quite annoying.
 *
 * @author lokka30
 * @since v2.0.0
 */
@SuppressWarnings("unused")
public class MultiMessage {

    public List<String> content;
    public List<Placeholder> placeholders;

    public MultiMessage(List<String> content, List<Placeholder> placeholders) {
        this.content = content;
        this.placeholders = placeholders;
    }

    public ArrayList<String> getTranslatedContent() {
        ArrayList<String> translated = new ArrayList<>();

        for (String line : content) {
            String translatedLine = MessageUtils.colorizeAll(line);

            for (Placeholder placeholder : placeholders) {
                translatedLine = placeholder.translateInMessage(translatedLine);
            }

            translated.add(translatedLine);
        }

        return translated;
    }

    public List<String> getUntranslatedContent() {
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }

    public List<Placeholder> getPlaceholders() {
        return placeholders;
    }

    public void setPlaceholders(List<Placeholder> placeholders) {
        this.placeholders = placeholders;
    }

    public void send(CommandSender sender) {
        getTranslatedContent().forEach(sender::sendMessage);
    }

    public void send(Player player) {
        getTranslatedContent().forEach(player::sendMessage);
    }

    public static class Placeholder {
        public final String id;
        public final String value;
        public final boolean colorizeValue;

        public Placeholder(String id, String value, boolean colorizeValue) {
            this.id = id;
            this.value = value;
            this.colorizeValue = colorizeValue;
        }

        public String translateInMessage(String msg) {
            if (colorizeValue) {
                return msg.replace("%" + id + "%", MessageUtils.colorizeAll(value));
            } else {
                return msg.replace("%" + id + "%", value);
            }
        }
    }
}
