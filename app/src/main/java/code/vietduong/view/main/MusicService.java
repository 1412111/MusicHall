package code.vietduong.view.main;



import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Icon;
import android.media.AudioManager;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.net.Uri;
import android.os.Binder;

import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;

import android.provider.MediaStore;
import android.support.v4.media.session.MediaControllerCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;

import code.vietduong.data.Contanst;
import code.vietduong.impl.MainCallbacks;
import code.vietduong.model.entity.Song;
import code.vietduong.oneplayer.R;

import static android.media.session.MediaSession.*;


/**
 * Created by Codev on 30/01/2018.
 */

public class MusicService extends Service implements
        MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener,
        MediaPlayer.OnCompletionListener
{
    public static final String ACTION_PLAY = "action_play";
    public static final String ACTION_PAUSE = "action_pause";

    public static final String ACTION_REWIND = "action_rewind";
    public static final String ACTION_FAST_FORWARD = "action_fast_foward";

    public static final String ACTION_NEXT = "action_next";
    public static final String ACTION_PREVIOUS = "action_previous";

    public static final String ACTION_STOP = "action_stop";


    private IBinder musicBind;

    private Song currentSong;
    private int position = 0;

    private int duration = 0;

    private MediaPlayer player;
 /*   private MediaSessionManager mManager;*/
    private MediaSession mSession;
    private MediaControllerCompat.TransportControls mController;

    private MainCallbacks mainCallbacks;
    private boolean isPlaying = true;


    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        player.stop();
        player.release();
        return false;
    }
    public class MusicBinder extends Binder{
        MusicService getService() {
            return MusicService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        musicBind = new MusicBinder();
        initMusicPlayer();
        initMediaSession();
    }

    private void initMediaSession() {
    /*    mManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);*/
        mSession = new MediaSession(getApplicationContext(),"AudioPlayer");
        mSession.setActive(true);

        mSession.setCallback(new Callback() {
            @Override
            public void onPlay() {
                super.onPlay();
                // dang stop
               // buildNotification(ACTION_PLAY);
                continueSong();
            }

            @Override
            public void onPause() {
                super.onPause();
               // buildNotification(ACTION_PAUSE);
                pauseSong();
            }

            @Override
            public void onSkipToNext() {
                super.onSkipToNext();
                isPlaying = true;
                playNext();
            }

            @Override
            public void onSkipToPrevious() {
                super.onSkipToPrevious();
                isPlaying = true;
                playPrevious();
            }
        });


    }




    /*
        private void updateMetaData() {
            Bitmap albumArt = BitmapFactory.decodeResource(getResources(),
                    R.drawable.bg); //replace with medias albumArt
            // Update the current metadata
            mSession.setMetadata(new MediaMetadata.Builder()
                    .putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, albumArt)
                    .putString(MediaMetadata.METADATA_KEY_ARTIST, currentSong.getArtist())
                    .putString(MediaMetadata.METADATA_KEY_TITLE, currentSong.getTitle())
                    .build());
        }*/
    @Override
    public void onDestroy() {
        super.onDestroy();
      //  stopForeground(true);
    }

    public void initMusicPlayer(){
        player = new MediaPlayer();
        //set player properties
        player.setWakeMode(getApplicationContext(),
                PowerManager.PARTIAL_WAKE_LOCK);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //set listeners
        player.setOnPreparedListener(this);
        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
    }


    public void playSong(Song song)
    {

        currentSong = song;
        position = Contanst.list_songs.indexOf(currentSong);
        Contanst.position = position;
        player.reset();
        Uri trackUri = ContentUris.withAppendedId(
                android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                song.getId());

        try {
            player.setDataSource(getApplicationContext(), trackUri);
            player.prepareAsync();
            buildNotification(ACTION_PLAY);
        } catch (IOException e) {
            e.printStackTrace();
        }
        buildNotification(ACTION_PLAY);


    }
    public void continueSong() {
        player.start();

        isPlaying = false;
        buildNotification(ACTION_PLAY);
        mainCallbacks.onControlFromServiceToMain(ACTION_PLAY);


    }
    public void pauseSong(){
        player.pause();

        /*if(isPlaying){
            player.pause();

        }else{
            player.start();
        }*/
        isPlaying = true;
        buildNotification(ACTION_PAUSE);
        mainCallbacks.onControlFromServiceToMain(ACTION_PAUSE);

    }
    public void playNext() {
        if(position ==  Contanst.list_songs.size()-1){
            position = -1;
        }
        mainCallbacks.onControlFromServiceToMain(ACTION_NEXT);
        playSong(Contanst.list_songs.get(position+1));
       // buildNotification(ACTION_NEXT);

    }
    public void playPrevious(){
        if(position == 0){
            position = Contanst.list_songs.size();
        }
        mainCallbacks.onControlFromServiceToMain(ACTION_PREVIOUS);
        playSong(Contanst.list_songs.get(position-1));

        //buildNotification(ACTION_PREVIOUS);
    }

    private int currentPosition = 0;
    public int getPosn(){
        if(player.isPlaying()){
            currentPosition = player.getCurrentPosition();
            return currentPosition;
        }
        return currentPosition;
    }
    public int getDur(){
        return duration;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Log.v("MUSIC PLAYER", "Playback Error");
        mp.reset();
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
        duration = player.getDuration();
        mainCallbacks.onMsgFromServiceToMain(currentSong);


    }

    public void registerClient(MainActivity mainActivity) {
        mainCallbacks = mainActivity;

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIncomingActions(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    private void handleIncomingActions(Intent intent) {
        if (intent == null || intent.getAction() == null) return;

        String actionString = intent.getAction();
        if (actionString.equalsIgnoreCase(ACTION_PLAY)) {

            mSession.getController().getTransportControls().play();

        } else if (actionString.equalsIgnoreCase(ACTION_PAUSE)) {

            mSession.getController().getTransportControls().pause();

        } else if (actionString.equalsIgnoreCase(ACTION_NEXT)) {

            mSession.getController().getTransportControls().skipToNext();

        } else if (actionString.equalsIgnoreCase(ACTION_PREVIOUS)) {

            mSession.getController().getTransportControls().skipToPrevious();

        } else if (actionString.equalsIgnoreCase(ACTION_STOP)) {
           // mSession.getController().getTransportControls().play();
        }
    }

    private Notification.Action generateAction(int icon, String title, String intentAction ) {
        Intent intent = new Intent( getApplicationContext(), MusicService.class );
        intent.setAction( intentAction );
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        return new Notification.Action.Builder( icon, title, pendingIntent ).build();
    }
    public void buildNotification(String action) {

        Bitmap bitmap= null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(currentSong.getAlbumArtPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bitmap == null){
            bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.noalbum_500);
        }
        int icon;
        if(action.equals(ACTION_PAUSE)){
            icon = android.R.drawable.ic_media_play;
            action = ACTION_PLAY;
        }else{
            icon = android.R.drawable.ic_media_pause;
            action = ACTION_PAUSE;
        }
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notification = new Notification.Builder(getApplicationContext(),"default")
                    .setSmallIcon(R.drawable.logo)
                    .setContentIntent(pendingIntent)
                    .addAction(generateAction(android.R.drawable.ic_media_previous,"Previous", ACTION_PREVIOUS))
                    .addAction(generateAction(icon,"", action))
                    .addAction(generateAction(android.R.drawable.ic_media_next,"Next", ACTION_NEXT))
               /*     .addAction(new Notification.Action
                            .Builder(Icon.createWithResource(this, android.R.drawable.ic_media_previous),
                            "Previous", pendingIntent).build())

                    .addAction(new Notification.Action
                            .Builder(Icon.createWithResource(this, android.R.drawable.ic_media_pause),
                            "Pause", pendingIntent).build())

                    .addAction(new Notification.Action
                            .Builder(Icon.createWithResource(this, android.R.drawable.ic_media_next),
                            "Next", pendingIntent).build())*/
                    .setContentTitle(currentSong.getTitle())
                    .setContentText(currentSong.getArtist())
                    .setLargeIcon(bitmap)
                    .setPriority(Notification.PRIORITY_MAX)
                    .setStyle(new Notification.MediaStyle().setShowActionsInCompactView(0, 1, 2).setMediaSession(mSession.getSessionToken())).build();
        }else{
            notification = new Notification.Builder(this)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.mipmap.ic_launcher)

                    .addAction(new Notification.Action.Builder(android.R.drawable.ic_media_previous,
                            "Previous", pendingIntent).build())

                    .addAction(new Notification.Action.Builder(android.R.drawable.ic_media_pause,
                            "Pause", pendingIntent).build())

                    .addAction(new Notification.Action.Builder(android.R.drawable.ic_media_next,
                            "Next", pendingIntent).build())
                    .setContentTitle("Music")
                    .setContentText("Now playing...")
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setStyle(new Notification.MediaStyle().setShowActionsInCompactView(1))
                   /* .setOngoing(true)*/.build();
        }

        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = null;

        /*uninstall and test because only have effect when fresh installation*/
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            channel = new NotificationChannel("default",
                    "Channel name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setSound(null, null);
            channel.setShowBadge(false);
            notificationManager.createNotificationChannel(channel);
        }


        notificationManager.notify(0, notification);
    }
}
