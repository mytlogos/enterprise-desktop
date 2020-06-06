package com.mytlogos.enterprisedesktop.worker;

import com.mytlogos.enterprisedesktop.ApplicationConfig;
import com.mytlogos.enterprisedesktop.background.ClientModelPersister;
import com.mytlogos.enterprisedesktop.background.ReloadPart;
import com.mytlogos.enterprisedesktop.background.Repository;
import com.mytlogos.enterprisedesktop.background.api.Client;
import com.mytlogos.enterprisedesktop.background.api.NotConnectedException;
import com.mytlogos.enterprisedesktop.background.api.model.*;
import com.mytlogos.enterprisedesktop.preferences.MainPreferences;
import com.mytlogos.enterprisedesktop.tools.Utils;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.util.Duration;
import retrofit2.Response;

import java.io.IOException;
import java.rmi.ServerException;
import java.time.LocalDateTime;
import java.util.*;

/**
 *
 */
public class SynchronizeService extends ScheduledService<Void> {
    public SynchronizeService() {
        this.setPeriod(Duration.minutes(15));
        this.setBackoffStrategy(param -> Duration.seconds(5));
    }

    @Override
    protected Task<Void> createTask() {
        return new SynchronizeTask();
    }
}
