package com.mytlogos.enterprisedesktop.preferences;

import com.dlsc.preferencesfx.PreferencesFx;
import com.mytlogos.enterprisedesktop.Starter;

/**
 *
 */
public class MainPreferences {
    private DownloadPreferences downloadPreferences = new DownloadPreferences();

    public PreferencesFx getPreferences() {
        return PreferencesFx.of(
                Starter.class,
                this.downloadPreferences.getCategory()
        );
    }

    public DownloadPreferences getDownloadPreferences() {
        return downloadPreferences;
    }
}
