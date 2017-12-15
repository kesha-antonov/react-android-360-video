package com.vrvideocomponent;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.google.vr.sdk.widgets.video.VrVideoEventListener;
import com.google.vr.sdk.widgets.video.VrVideoView;

import java.io.IOException;

import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.ReactApplicationContext;

/**
 * Created by root on 23/3/17.
 */

public class VrVideoViewManager extends SimpleViewManager<VrVideoView> {


    public static final String TAG = VrVideoViewManager.class.getSimpleName();

    public static final String PROP_VOLUME = "volume";
    public static final String PROP_VIDEO = "video";
    public static final String PROP_ENABLE_FULL_SCREEN_BUTTON = "enableFullscreenButton";
    public static final String PROP_ENABLE_CARDBOARD_BUTTON= "enableCardboardButton";
    public static final String PROP_ENABLE_TOUCH_TRACKING = "enableTouchTracking";
    public static final String PROP_ENABLE_INFO_BUTTON = "enableInfoButton";
    public static final String PROP_HIDES_TRANSITION_VIEW = "hidesTransitionView";
    public static final String PROP_DISPLAY_MODE = "displayMode";
    // public static final String PROP_KILL= "kill";

    private ReactApplicationContext mReactContext;
    private ThemedReactContext mContext;
    public boolean activated = true;

    public VrVideoViewManager(ReactApplicationContext reactContext) {
        mReactContext = reactContext;
    }

    @Override
    public String getName() {
        return "CVrVideoView";
    }

    @Override
    protected VrVideoView createViewInstance(ThemedReactContext reactContext) {
        Log.d(TAG, "[CVrVideoView] Component init");
        this.mContext = reactContext;
        VrVideoView vrView = new VrVideoView(reactContext.getCurrentActivity());
        vrView.setEventListener(new ActivityEventListener(vrView, this));
        vrView.setDisplayMode(VrVideoView.DisplayMode.FULLSCREEN_MONO);
        // vrView.pauseVideo();
        return vrView;
    }

    @ReactProp(name = PROP_VOLUME, defaultFloat = 1.0f)
    public void setVolume(final VrVideoView videoView, float volume) {
        videoView.setVolume(volume);
        Log.d(TAG, "[CVrVideoView] volume="+volume);
    }

    @ReactProp(name = PROP_VIDEO)
    public void setVideo(final VrVideoView videoView, @Nullable ReadableMap params) {
        String uriString = params.getString("uri");
        String videoType = params.getString("type");
        loadVideo(videoView, uriString, videoType);
        Log.d(TAG, "[CVrVideoView] uri="+uriString+", videoType="+videoType);
    }

    @ReactProp(name = PROP_ENABLE_FULL_SCREEN_BUTTON)
    public void enableFullscreenButton(final VrVideoView videoView, boolean b) {
        videoView.setFullscreenButtonEnabled(b);
        Log.d(TAG, "[CVrVideoView] enableFullscreenButton="+b);
    }

    @ReactProp(name = PROP_ENABLE_CARDBOARD_BUTTON, defaultBoolean = true)
    public void enableCardboardButton(final VrVideoView videoView, boolean b) {
        videoView.setStereoModeButtonEnabled(b);
        Log.d(TAG, "[CVrVideoView] enableCardboardButton="+b);
    }

    @ReactProp(name = PROP_ENABLE_TOUCH_TRACKING, defaultBoolean = true)
    public void enableTouchTracking(final VrVideoView videoView, boolean b) {
        Log.d(TAG, "[CVrVideoView] enableTouchTracking="+b);
    }

    @ReactProp(name = PROP_ENABLE_INFO_BUTTON, defaultBoolean = true)
    public void enableInfoButton(final VrVideoView videoView, boolean b) {
        videoView.setInfoButtonEnabled(b);
        if (b == true) videoView.shutdown();
        Log.d(TAG, "[CVrVideoView] enableInfoButton="+b);
    }

    @ReactProp(name = PROP_HIDES_TRANSITION_VIEW, defaultBoolean = false)
    public void hidesTransitionView(final VrVideoView videoView, boolean b) {
        videoView.setTransitionViewEnabled(b);
        Log.d(TAG, "[CVrVideoView] hidesTransitionView="+b);
    }

    // @ReactProp(name = PROP_KILL, defaultBoolean = false)
    // public void setKill(final VrVideoView videoView, boolean b) {
    //     //if (b == true) videoView.shutdown();
    //     Log.d(TAG, "[CVrVideoView] kill");
    // }

    @ReactProp(name = PROP_DISPLAY_MODE)
    public void displayMode(final VrVideoView videoView, @Nullable String mode) {
        Log.d(TAG, "[CVrVideoView] displayMode="+mode);
        videoView.setDisplayMode(VrVideoView.DisplayMode.FULLSCREEN_MONO);
        //  switch (mode) {
        //      case "fullscreen":
        //          videoView.setDisplayMode(VrVideoView.DisplayMode.FULLSCREEN_MONO);
        //      case "cardboard":
        //          videoView.setDisplayMode(VrVideoView.DisplayMode.FULLSCREEN_STEREO);
        //      case "embedded":
        //      default:
        //          videoView.setDisplayMode(VrVideoView.DisplayMode.EMBEDDED);
        //  }
    }

    // public void onDisplayModeChanged(int type) {
    //   Log.d(TAG, "[CVrVideoView] onDisplayModeChanged:"+type);
    // }

    public void callBack(final VrVideoView videoView) {
      this.activated = false;
      Log.d(TAG, "[CVrVideoView][callBack] send event");
      // this.onDisplayModeChanged();
      WritableMap event = Arguments.createMap();
      event.putString("type", "displayModeChanged");
      mContext.getJSModule(RCTEventEmitter.class).receiveEvent(videoView.getId(), "topChange", event);
    }

    /**
     * Listen to the important events from widget.
     */
    private class ActivityEventListener extends VrVideoEventListener {

        private VrVideoView mVrView;
        private VrVideoViewManager manager;

        public ActivityEventListener(VrVideoView vrView, VrVideoViewManager manager) {
            this.mVrView = vrView;
            this.manager = manager;
            Log.d(TAG, "[CVrVideoView][Listener] init");
        }

        @Override
        public void onLoadSuccess() {
            Log.d(TAG, "[CVrVideoView][Listener] Successfully loaded video. Duration:" + mVrView.getDuration());
            if (this.manager.activated == true) {
              mVrView.setDisplayMode(VrVideoView.DisplayMode.FULLSCREEN_MONO);
            }
        }

        /**
         * Called by video widget on the UI thread on any asynchronous error.
         */
        @Override
        public void onLoadError(String errorMessage) {
            // An error here is normally due to being unable to decode the video format.
            Log.e(TAG, "[CVrVideoView][Listener] Error loading video: " + errorMessage);
        }

        /**
         * Update the UI every frame.
         */
        @Override
        public void onNewFrame() {
            // Log.d(TAG, "NatureVRComponent onNewFrame");
        }

        /**
         * Make the video play in a loop. This method could also be used to move to the next video in
         * a playlist.
         */
        @Override
        public void onCompletion() {
            Log.d(TAG, "[CVrVideoView][Listener] onCompletion");
            // if(mVrView != null) mVrView.seekTo(0);
            mVrView.setDisplayMode(VrVideoView.DisplayMode.EMBEDDED);
        }

        @Override
        public void onDisplayModeChanged(int newDisplayMode) {
            Log.d(TAG, "[CVrVideoView][Listener] onDisplayModeChanged " + newDisplayMode);
            if (newDisplayMode == 1) {
                mVrView.pauseVideo();
                this.manager.callBack(mVrView);
            }
        }
    }

    private void loadVideo(VrVideoView vrView, String uriString, String videoType) {
        try {
            VrVideoView.Options videoOptions = new VrVideoView.Options();
            videoOptions.inputFormat = VrVideoView.Options.FORMAT_DEFAULT;
            videoOptions.inputType = VrVideoView.Options.TYPE_MONO;
            Uri fileUri = Uri.parse(uriString);
            VideoLoaderTask backgroundVideoLoaderTask = new VideoLoaderTask(vrView);
            backgroundVideoLoaderTask.execute(Pair.create(fileUri, videoOptions));
        } catch (Exception e) {
            Log.e(TAG, "[CVrVideoView][Listener] video source not valid: " + e);
        }
    }

    /**
     * Helper class to manage threading.
     */
    class VideoLoaderTask extends AsyncTask<Pair<Uri, VrVideoView.Options>, Void, Boolean> {

        private VrVideoView mVrView;

        public VideoLoaderTask(VrVideoView vrView) {
            mVrView = vrView;
        }

        // @Override
        // protected void onDestroy() {
        //     //mVrVideoView.shutdown();
        //     super.onDestroy();
        // }

        @Override
        protected Boolean doInBackground(Pair<Uri, VrVideoView.Options>... fileInformation) {
            try {
                if (fileInformation == null || fileInformation.length < 1
                        || fileInformation[0] == null || fileInformation[0].first == null) {
                    // No intent was specified, so we default to playing the local stereo-over-under video.
                    VrVideoView.Options options = new VrVideoView.Options();
                    options.inputType = VrVideoView.Options.TYPE_STEREO_OVER_UNDER;
                    mVrView.loadVideoFromAsset("congo.mp4", options);
                } else {
                    mVrView.loadVideo(fileInformation[0].first, fileInformation[0].second);
                }
            } catch (IOException e) {
                // An error here is normally due to being unable to locate the file.
                //loadVideoStatus = LOAD_VIDEO_STATUS_ERROR;
                // Since this is a background thread, we need to switch to the main thread to show a toast.
                mVrView.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(mContext, "[CVrVideoView][AsyncTask] Error opening file. ", Toast.LENGTH_LONG).show();
                    }
                });
                Log.e(TAG, "[CVrVideoView][AsyncTask] Could not open video: " + e);
            }

            return true;
        }
    }
}
