package com.mytlogos.enterprisedesktop.preferences;

import com.mytlogos.enterprisedesktop.profile.AbstractProfile;
import com.mytlogos.enterprisedesktop.profile.DisplayEpisodeProfile;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;

import java.util.prefs.Preferences;

/**
 *
 */
public class ProfilePreferences {
    private final Preferences preferences;
    private ObjectProperty<DisplayEpisodeProfile> displayEpisodeProfile;

    public ProfilePreferences(Preferences preferences) {
        this.preferences = preferences;
        this.displayEpisodeProfile = new PreferenceProperty<>("displayEpisodeProfile", new DisplayEpisodeProfile());
    }

    public DisplayEpisodeProfile getDisplayEpisodeProfile() {
        return displayEpisodeProfile.get();
    }

    public ObjectProperty<DisplayEpisodeProfile> displayEpisodeProfileProperty() {
        return displayEpisodeProfile;
    }

    public void setDisplayEpisodeProfile(DisplayEpisodeProfile displayEpisodeProfile) {
        this.displayEpisodeProfile.set(displayEpisodeProfile);
    }

    private class PreferenceProperty<T extends AbstractProfile> extends ObjectPropertyBase<T> {
        private final String name;
        private final T def;
        private boolean initialised = false;

        private PreferenceProperty(String name, T def) {
            this.name = name;
            this.def = def;
        }

        @Override
        public Object getBean() {
            return ProfilePreferences.this;
        }

        @Override
        public String getName() {
            return this.name;
        }

        @Override
        protected void invalidated() {
            if (!this.initialised) {
                return;
            }
            final T t = this.get();
            final String value = t == null ? "" : t.serialize();
            ProfilePreferences.this.preferences.put(this.getName(), value);
        }

        @Override
        public T get() {
            if (!this.initialised) {
                final String serializedProfile = ProfilePreferences.this.preferences.get(this.getName(), "");

                if (serializedProfile.isEmpty()) {
                    this.initialised = true;
                    this.set(this.def);
                } else {
                    this.set(this.def.deserialize(serializedProfile));
                    this.initialised = true;
                }
            }
            return super.get();
        }
    }
}
