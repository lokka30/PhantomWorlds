package me.lokka30.phantomworlds.commands.phantomworlds.subcommands;

import me.lokka30.phantomworlds.PhantomWorlds;
import me.lokka30.phantomworlds.commands.ISubcommand;
import me.lokka30.phantomworlds.misc.CompatibilityChecker;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lokka30
 * @since v2.0.0
 */
public class CompatibilitySubcommand implements ISubcommand {

    /*
    TODO:
    - Test
    - Messages.yml
    - Test
     */

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public void parseCommand(@NotNull PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        if (!sender.hasPermission("phantomworlds.command.phantomworlds.compatibility")) {
            sender.sendMessage("No permission");
            return;
        }

        if (args.length != 1) {
            sender.sendMessage("Invalid usage. Try '/" + label + " compatibility'");
            return;
        }

        sender.sendMessage("Running compatibility checker...");
        main.compatibilityChecker.checkAll();

        if (main.compatibilityChecker.incompatibilities.isEmpty()) {
            sender.sendMessage("No incompatibilities were found.");
            return;
        }

        sender.sendMessage(main.compatibilityChecker.incompatibilities.size() + " incompatibilities were found:");

        for (int i = 0; i < main.compatibilityChecker.incompatibilities.size(); i++) {
            CompatibilityChecker.Incompatibility incompatibility = main.compatibilityChecker.incompatibilities.get(i);

            sender.sendMessage("#" + (i + 1) + " (Type: " + incompatibility.type + "):");
            sender.sendMessage(" -> Reason: " + incompatibility.reason);
            sender.sendMessage(" -> Recommendation: " + incompatibility.recommendation);
            sender.sendMessage(" ");
        }
    }

    /**
     * @author lokka30
     * @since v2.0.0
     */
    @Override
    public List<String> parseTabCompletion(PhantomWorlds main, CommandSender sender, Command cmd, String label, String[] args) {
        return new ArrayList<>();
    }
}
