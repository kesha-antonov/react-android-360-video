package com.vrvideocomponent;

import android.app.Activity;
import android.util.Log;
import android.os.Bundle;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;
import com.facebook.react.ReactActivity;
import com.facebook.react.bridge.LifecycleEventListener;
import com.facebook.react.ReactInstanceManager;
import com.facebook.react.shell.MainReactPackage;
import com.facebook.react.common.LifecycleState;

public class MainActivity extends ReactActivity {

    public static final String TAG = MainActivity.class.getSimpleName();

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "VrVideoComponent";
    }

    @Override
      protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ReactRootView mReactRootView = new ReactRootView(this);

        ReactInstanceManager mReactInstanceManager = ReactInstanceManager.builder()
          .setApplication(getApplication())
          .setBundleAssetName("index.android.bundle")
          .setJSMainModuleName("index.android")
          .addPackage(new MainReactPackage())
          .addPackage(new VrVideoViewPackage(this))              // <------ add here
          .setUseDeveloperSupport(BuildConfig.DEBUG)
          .setInitialLifecycleState(LifecycleState.RESUMED)
          .build();

        mReactRootView.startReactApplication(mReactInstanceManager, "VrVideoComponent", null);

        setContentView(mReactRootView);
      }
}
