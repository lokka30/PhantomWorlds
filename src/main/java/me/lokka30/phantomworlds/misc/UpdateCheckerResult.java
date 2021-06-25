package me.lokka30.phantomworlds.misc;

/**
 * @author lokka30
 * @since v2.0.0
 */
public class UpdateCheckerResult {

    private final boolean outdated;
    private final String currentVersion;
    private final String latestVersion;

    public UpdateCheckerResult(boolean outdated, String currentVersion, String latestVersion) {
        this.outdated = outdated;
        this.currentVersion = currentVersion;
        this.latestVersion = latestVersion;
    }

    public boolean isOutdated() {
        return outdated;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public String getLatestVersion() {
        return latestVersion;
    }
}
