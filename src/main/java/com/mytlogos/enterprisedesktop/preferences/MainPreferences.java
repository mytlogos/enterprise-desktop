package com.mytlogos.enterprisedesktop.preferences;

import com.dlsc.preferencesfx.PreferencesFx;
import com.mytlogos.enterprisedesktop.Starter;

import java.time.LocalDateTime;
import java.util.prefs.Preferences;

/**
 *
 */
public class MainPreferences {
    private static final String LAST_SYNC = "last_time_sync";
    private final DownloadPreferences downloadPreferences = new DownloadPreferences();
    private final ProfilePreferences profilePreferences = new ProfilePreferences(Preferences.userRoot().node("profile"));

    public static LocalDateTime getLastSync() {
        final String value = Preferences.userRoot().get(LAST_SYNC, null);
        return value == null ? null : LocalDateTime.parse(value);
    }

    public static void setLastSync(LocalDateTime dateTime) {
        Preferences.userRoot().put(LAST_SYNC, dateTime == null ? null : dateTime.toString());
    }

    public PreferencesFx getPreferences() {
        return PreferencesFx.of(
                Starter.class,
                this.downloadPreferences.getCategory()
        );
    }

    public ProfilePreferences getProfilePreferences() {
        return profilePreferences;
    }

    public DownloadPreferences getDownloadPreferences() {
        return downloadPreferences;
    }
}
