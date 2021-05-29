package me.lokka30.phantomworlds.misc;

import java.util.ArrayList;

public class CompatibilityChecker {

    public final ArrayList<Incompatibility> incompatibilities = new ArrayList<>();

    public void checkAll() {
        incompatibilities.clear();

        checkServerSoftware();
        //checkPlugins();
    }

    public void checkServerSoftware() {
        // Warn console if the server is not running Spigot - required for MicroLib's message methods
        try {
            Class.forName("org.bukkit.entity.Player.Spigot");
        } catch (ClassNotFoundException ex) {
            incompatibilities.add(new Incompatibility(IncompatibilityType.SERVER_SOFTWARE, "The plugin requires code only present in SpigotMC and its derivatives such as Paper and Tuinity.", "Use PaperMC or SpigotMC."));
        }
    }

    public enum IncompatibilityType {
        SERVER_SOFTWARE//, PLUGIN
    }

    public static class Incompatibility {
        public final IncompatibilityType type;
        public final String reason;
        public final String recommendation;

        public Incompatibility(IncompatibilityType type, String reason, String recommendation) {
            this.type = type;
            this.reason = reason;
            this.recommendation = recommendation;
        }
    }
}
