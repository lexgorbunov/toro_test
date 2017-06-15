package com.example.lex.torotest;


import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.ads.interactivemedia.v3.api.AdDisplayContainer;
import com.google.ads.interactivemedia.v3.api.AdErrorEvent;
import com.google.ads.interactivemedia.v3.api.AdEvent;
import com.google.ads.interactivemedia.v3.api.AdsLoader;
import com.google.ads.interactivemedia.v3.api.AdsManager;
import com.google.ads.interactivemedia.v3.api.AdsManagerLoadedEvent;
import com.google.ads.interactivemedia.v3.api.AdsRequest;
import com.google.ads.interactivemedia.v3.api.ImaSdkFactory;
import com.google.ads.interactivemedia.v3.api.player.ContentProgressProvider;
import com.google.ads.interactivemedia.v3.api.player.VideoProgressUpdate;

import im.ene.toro.exoplayer.ExoVideoView;
import im.ene.toro.exoplayer.Media;
import im.ene.toro.exoplayer.PlayerCallback;
import im.ene.toro.exoplayer.internal.ExoMediaPlayer;

public class VideoFeedHolder extends FeedHolder implements
        AdEvent.AdEventListener, AdErrorEvent.AdErrorListener {

    private ExoVideoView toro;

    private Button start;
    private Button close;

    // The container for the ad's UI.
    private ViewGroup mAdUiContainer;

    // Factory class for creating SDK objects.
    private ImaSdkFactory mSdkFactory;

    // The AdsLoader instance exposes the requestAds method.
    private AdsLoader mAdsLoader;

    // AdsManager exposes methods to control ad playback and listen to ad events.
    private AdsManager mAdsManager;

    // Whether an ad is displayed.
    private boolean mIsAdDisplayed;
    private boolean isAdsCompleted = false;

    private void assignViews(final View root) {
        toro = (ExoVideoView) root.findViewById(R.id.demo_video_view);
        start = (Button) root.findViewById(R.id.start);
        close = (Button) root.findViewById(R.id.close);
    }

    public VideoFeedHolder(final View root) {
        super(root);
        assignViews(root);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (isAdsCompleted) {
                    toro.start();
                } else {
                    requestAds(root.getContext().getString(R.string.ad_tag_url));
                }
            }
        });
        mAdUiContainer = (ViewGroup) root.findViewById(R.id.videoPlayerWithAdPlayback);

        adInit(itemView.getContext());
    }

    @Override
    public void setContent(@NonNull final String content) {
        toro.releasePlayer();
        toro.setMedia(new Media(Uri.parse(content)));
    }

    private void adInit(final Context context) {
        // Create an AdsLoader.
        mSdkFactory = ImaSdkFactory.getInstance();
        mAdsLoader = mSdkFactory.createAdsLoader(context);
        // Add listeners for when ads are loaded and for errors.
        mAdsLoader.addAdErrorListener(this);
        mAdsLoader.addAdsLoadedListener(new AdsLoader.AdsLoadedListener() {
            @Override
            public void onAdsManagerLoaded(AdsManagerLoadedEvent adsManagerLoadedEvent) {
                // Ads were successfully loaded, so get the AdsManager instance. AdsManager has
                // events for ad playback and errors.
                mAdsManager = adsManagerLoadedEvent.getAdsManager();

                // Attach event and error event listeners.
                mAdsManager.addAdErrorListener(VideoFeedHolder.this);
                mAdsManager.addAdEventListener(VideoFeedHolder.this);
                mAdsManager.init();
            }
        });

        // Add listener for when the content video finishes.
        toro.setPlayerCallback(new PlayerCallback() {
            @Override
            public void onPlayerStateChanged(final boolean playWhenReady, final int playbackState) {
                if (playbackState == ExoMediaPlayer.STATE_ENDED) {
                    // Handle completed event for playing post-rolls.
                    if (mAdsLoader != null) {
                        mAdsLoader.contentComplete();
                    }
                }
            }

            @Override
            public boolean onPlayerError(final Exception error) {
                return false;
            }
        });
    }

    /**
     * Request video ads from the given VAST ad tag.
     * @param adTagUrl URL of the ad's VAST XML
     */
    private void requestAds(String adTagUrl) {
        if (mIsAdDisplayed) return;

        AdDisplayContainer adDisplayContainer = mSdkFactory.createAdDisplayContainer();
        adDisplayContainer.setAdContainer(mAdUiContainer);

        // Create the ads request.
        AdsRequest request = mSdkFactory.createAdsRequest();
        request.setAdTagUrl(adTagUrl);
        request.setAdDisplayContainer(adDisplayContainer);
        request.setContentProgressProvider(new ContentProgressProvider() {
            @Override
            public VideoProgressUpdate getContentProgress() {
                if (mIsAdDisplayed || toro == null || toro.getDuration() <= 0) {
                    return VideoProgressUpdate.VIDEO_TIME_NOT_READY;
                }
                return new VideoProgressUpdate(toro.getCurrentPosition(),
                        toro.getDuration());
            }
        });

        // Request the ad. After the ad is loaded, onAdsManagerLoaded() will be called.
        mAdsLoader.requestAds(request);
    }

    @Override
    public void onAdError(final AdErrorEvent adErrorEvent) {
        Log.e("AD", "Ad Error: " + adErrorEvent.getError().getMessage());
        toro.start();
    }

    @Override
    public void onAdEvent(final AdEvent adEvent) {
        Log.i("AD", "Event: " + adEvent.getType());

        // These are the suggested event types to handle. For full list of all ad event
        // types, see the documentation for AdEvent.AdEventType.
        switch (adEvent.getType()) {
            case LOADED:
                // AdEventType.LOADED will be fired when ads are ready to be played.
                // AdsManager.start() begins ad playback. This method is ignored for VMAP or
                // ad rules playlists, as the SDK will automatically start executing the
                // playlist.
                mAdsManager.start();
                break;
            case CONTENT_PAUSE_REQUESTED:
                // AdEventType.CONTENT_PAUSE_REQUESTED is fired immediately before a video
                // ad is played.
                mIsAdDisplayed = true;
                toro.pause();
                break;
            case CONTENT_RESUME_REQUESTED:
                // AdEventType.CONTENT_RESUME_REQUESTED is fired when the ad is completed
                // and you should start playing your content.
                mIsAdDisplayed = false;
                toro.start();
                break;
            case ALL_ADS_COMPLETED:
                if (mAdsManager != null) {
                    mAdsManager.destroy();
                    mAdsManager = null;
                }
                isAdsCompleted = true;
                break;
            default:
                break;
        }
    }

    public void onResume() {
        if (mAdsManager != null && mIsAdDisplayed) {
            mAdsManager.resume();
        } else {
            //            toro.start();
        }
    }

    public void onPause() {
        if (mAdsManager != null && mIsAdDisplayed) {
            mAdsManager.pause();
        } else {
            toro.pause();
        }
    }
}
