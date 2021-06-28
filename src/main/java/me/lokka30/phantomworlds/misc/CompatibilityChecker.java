package me.lokka30.phantomworlds.misc;

import java.util.ArrayList;

/**
 * Handles various checks that attempt to warn administrators about
 * potential compatibility issues with their server configuration.
 *
 * @author lokka30
 * @since v2.0.0
 */
public class CompatibilityChecker {

    public final ArrayList<Incompatibility> incompatibilities = new ArrayList<>();

    /**
     * Run all PW compatibility checks.
     *
     * @author lokka30
     * @since v2.0.0
     */
    public void checkAll() {
        incompatibilities.clear();

        //checkPlugins();
    }

    /**
     * Enum storing the different types of incompatibility detections.
     *
     * @author lokka30
     * @since v2.0.0
     */
    @SuppressWarnings("unused")
    public enum IncompatibilityType {
        MISSING_DEPENDENCY, // A dependency required for an operation is missing.
        INCOMPATIBLE_PLUGIN, // Another plugin is installed on the server which may be incompatible.
        SERVER_SOFTWARE, // The server software (e.g. Paper/Spigot/Tuinity) is incompatible.
        JAVA_VERSION // The server's version of Java is outdated.
    }

    /**
     * This class is used as an object to store each incompatibility that is detected.
     *
     * @author lokka30
     * @since v2.0.0
     */
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
