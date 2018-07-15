package com.x1unix.cashlytics;

import android.support.v7.app.AppCompatDelegate;
import com.x1unix.cashlytics.core.ServiceContainer;
import net.danlew.android.joda.JodaTimeAndroid;

public class Application extends android.app.Application {

    private ServiceContainer serviceContainer;

    @Override
    public void onCreate() {
        super.onCreate();

        // Joda time init
        JodaTimeAndroid.init(this);

        // Initialize Cashlytics service container
        serviceContainer = new ServiceContainer(getApplicationContext());

        // Enable vector images support
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    /**
     * Gets Cashlytics service container
     * @return Cashlytics service container
     */
    public ServiceContainer getServiceContainer() {
        return serviceContainer;
    }
}
