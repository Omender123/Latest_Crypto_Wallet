package com.crypto.croytowallet.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Toast;

import com.crypto.croytowallet.Extra_Class.AppUtils;
import com.crypto.croytowallet.R;
import com.crypto.croytowallet.utils.FullScreenHelper;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerUtils;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.PlayerUiController;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.ui.menu.MenuItem;

public class PlayVideoScreen extends AppCompatActivity {
    String VideoId;
    YouTubePlayerView youTubePlayerView;
    private FullScreenHelper fullScreenHelper = new FullScreenHelper(this);
    private float currentSeconds;
    private float duration;
    private YouTubePlayer youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_video_screen);
        AppUtils.FullScreen(PlayVideoScreen.this);
        youTubePlayerView = findViewById(R.id.youtube_player_view);


        Bundle bundle = getIntent().getExtras();
        VideoId = bundle.getString("VideoId");

      /*  getLifecycle().addObserver(youTubePlayerView);
        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                  youTubePlayer.loadVideo(VideoId, 0);
            }
        });
        youTubePlayerView.enterFullScreen();
        youTubePlayerView.exitFullScreen();
        youTubePlayerView.isFullScreen();
        youTubePlayerView.toggleFullScreen();
*/

        initYouTubePlayerView();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        youTubePlayerView.getPlayerUiController().getMenu().dismiss();
    }

    @Override
    public void onBackPressed() {
        if (youTubePlayerView.isFullScreen())
            youTubePlayerView.exitFullScreen();
        else
            super.onBackPressed();
    }

    private void initYouTubePlayerView() {
        // initPlayerMenu();

        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new YouTubePlayerListener() {

            @Override
            public void onReady(@NonNull YouTubePlayer player) {
                youTubePlayer = player;
                YouTubePlayerUtils.loadOrCueVideo(
                        player,
                        getLifecycle(),
                        VideoId,
                        0f
                );
                addFullScreenListenerToPlayer();
            }

            @Override
            public void onApiChange(YouTubePlayer youTubePlayer) {

            }

            @Override
            public void onVideoId(YouTubePlayer youTubePlayer, String videoId) {

            }

            @Override
            public void onVideoLoadedFraction(YouTubePlayer youTubePlayer, float loadedFraction) {

            }

            @Override
            public void onVideoDuration(YouTubePlayer youTubePlayer, float d) {
                duration = d;
            }

            @Override
            public void onCurrentSecond(YouTubePlayer youTubePlayer, float second) {
                currentSeconds = second;
            }

            @Override
            public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError error) {

            }

            @Override
            public void onPlaybackRateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackRate playbackRate) {

            }

            @Override
            public void onPlaybackQualityChange(YouTubePlayer youTubePlayer, PlayerConstants.PlaybackQuality playbackQuality) {

            }

            @Override
            public void onStateChange(YouTubePlayer youTubePlayer, PlayerConstants.PlayerState state) {

            }
        });
    }

    /**
     * Shows the menu button in the player and adds an item to it.
     */
    private void initPlayerMenu() {
        youTubePlayerView.getPlayerUiController()
                .showMenuButton(true)
                .getMenu()
                .addItem(new MenuItem("menu item1", R.drawable.ic_android_black_24dp,
                        view -> Toast.makeText(this, "item1 clicked", Toast.LENGTH_SHORT).show())
                ).addItem(new MenuItem("menu item2", R.drawable.ic_mood_black_24dp,
                view -> Toast.makeText(this, "item2 clicked", Toast.LENGTH_SHORT).show())
        ).addItem(new MenuItem("menu item no icon",
                view -> Toast.makeText(this, "item no icon clicked", Toast.LENGTH_SHORT).show()));
    }

    private void addFullScreenListenerToPlayer() {
        youTubePlayerView.addFullScreenListener(new YouTubePlayerFullScreenListener() {
            @Override
            public void onYouTubePlayerEnterFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                fullScreenHelper.enterFullScreen();

                addCustomActionsToPlayer();
            }

            @Override
            public void onYouTubePlayerExitFullScreen() {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                fullScreenHelper.exitFullScreen();

                removeCustomActionsFromPlayer();
            }
        });
    }

    /**
     * This method adds a new custom action to the player.
     * Custom actions are shown next to the Play/Pause button in the middle of the player.
     */
    private void addCustomActionsToPlayer() {
        Drawable customAction1Icon = ContextCompat.getDrawable(this, R.drawable.ic_fast_rewind_white_24dp);
        Drawable customAction2Icon = ContextCompat.getDrawable(this, R.drawable.ic_fast_forward_white_24dp);
        assert customAction1Icon != null;
        assert customAction2Icon != null;

        youTubePlayerView.getPlayerUiController().setCustomAction1(customAction1Icon, view ->
                youTubePlayer.seekTo(currentSeconds - 10)
        );


        youTubePlayerView.getPlayerUiController().setCustomAction2(customAction2Icon, view ->
                youTubePlayer.seekTo(currentSeconds + 10)

        ); }

    private void removeCustomActionsFromPlayer() {
        youTubePlayerView.getPlayerUiController().showCustomAction1(false);
        youTubePlayerView.getPlayerUiController().showCustomAction2(false);
    }


    @Override
    protected void onResume() {
        super.onResume();
        AppUtils.FullScreen(PlayVideoScreen.this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }
}