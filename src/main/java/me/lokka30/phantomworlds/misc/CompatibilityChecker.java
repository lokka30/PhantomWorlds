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
     * @author lokka30
     * @since v2.0.0
     */
    public enum IncompatibilityType {
        //PLUGIN
    }

    /**
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
