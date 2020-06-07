package com.mytlogos.enterprisedesktop.profile;

import com.google.gson.Gson;

import java.lang.invoke.SerializedLambda;

/**
 *
 */
public abstract class AbstractProfile {
    public String serialize() {
        return new Gson().toJson(this);
    }

    public <T extends AbstractProfile> T deserialize(String data) {
        Gson gson = new Gson();
        //noinspection unchecked
        return (T) gson.fromJson(data, this.getClass());
    }
}
