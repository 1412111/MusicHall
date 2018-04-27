package code.vietduong.view.main;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.gigamole.navigationtabstrip.NavigationTabStrip;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import code.vietduong.adapter.MyPagerAdapter;
import code.vietduong.data.Contanst;
import code.vietduong.fragment.Song_Playing_Fragment;
import code.vietduong.impl.MainCallbacks;
import code.vietduong.model.entity.Song;

import code.vietduong.oneplayer.R;
import code.vietduong.presenter.SongPresenter;
import code.vietduong.adapter.SongAdapter;
import jp.wasabeef.blurry.Blurry;
import rm.com.audiowave.AudioWaveView;


public class MainActivity extends AppCompatActivity implements MainCallbacks{
    private SongPresenter mainPresenter;
    private final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 123;

    private boolean isPlaying = false;
    /*Components*/
   // private RecyclerView _songRecyclerView;
    private SongAdapter _songAdapter;
   // private RecyclerView.LayoutManager _songLayoutManager;
    private SlidingUpPanelLayout _slidingLayout;
    private FrameLayout _control_bar;
    private FrameLayout _playing_now;
    private ImageView _img_song, img_bg, img_play, img_previous, img_next;
    private ImageView _btn_play;

    private TextView _txtSongName_control, _txtSinger_control, _txtSinger_main, _txtSongName_main;
    private ListView _listView;

    private ProgressBar progressbar_control;
    private AppBarLayout appBarLayout;
    /************************************/
    private ViewPager _mViewPager, _mViewPagerSong;

    private NavigationTabStrip mNavigationTabStrip;
    private Song_Playing_Fragment song_playing_fragment;
    private SeekBar seekBar;

    private Intent playIntent = null;
    private AudioWaveView wave;
    public  static String LOAD_SONG_FINISHED = "load data finished";
    public  static String UPDATE_SONG_UI = "update song ui";
    public  static String SLIDE_NEXT = "slide next";
    public  static String SLIDE_PREVIOUS = "slide previous";
    /*Service*/


    public static MusicService musicService;

    private static boolean musicBound = false;

    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //service nhận từ hàm onBind của class MusicService
            MusicService.MusicBinder binder = (MusicService.MusicBinder)service;
            //get service

            musicService = binder.getService();
            musicService.registerClient(MainActivity.this);

            musicBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            musicBound = false;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(playIntent == null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        }
        loadSongs();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Contanst.height = displayMetrics.heightPixels;
        Contanst.width = displayMetrics.widthPixels;


    }

/*

    @Override
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        song_fragment = (Song_Fragment) fragment;
    }
*/

    private void loadSongs() {
        mainPresenter = new SongPresenter(this,this);
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            //if denied request again
            //if first time denied then request again
            //if start first then request
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_MEDIA);
        }else{

            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    mainPresenter.loadData();
                }
            });
            t.start();

        }
    }


    private void initUI() {

        img_bg = findViewById(R.id.img_bg);
        img_play = findViewById(R.id.img_play);
        img_play.setAlpha(0.5f);
        img_previous = findViewById(R.id.img_previous);
        img_next = findViewById(R.id.img_next);
        _btn_play = findViewById(R.id.btn_play);

        progressbar_control = findViewById(R.id.progressBar_Control);
        progressbar_control.setProgress(0);
        progressbar_control.setMax(100);
        _control_bar = findViewById(R.id.control_bar);
        _playing_now = findViewById(R.id.playing_now);

        _txtSongName_control = findViewById(R.id.txtSongName_control);
        _txtSongName_control.setSingleLine();
        _txtSongName_control.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        _txtSongName_control.setMarqueeRepeatLimit(10);
        _txtSongName_control.setFocusable(true);
        _txtSongName_control.setHorizontallyScrolling(true);
        _txtSongName_control.requestFocus();
        _txtSongName_control.setFocusableInTouchMode(true);

        _txtSinger_control = findViewById(R.id.txtSinger_control);
        _txtSinger_control.setSingleLine();

        _txtSinger_main = findViewById(R.id.txtSinger_main);
        _txtSongName_main = findViewById(R.id.txtSongName_main);
        _txtSongName_main.setSingleLine();
        _txtSinger_main.setSingleLine();
        _slidingLayout = findViewById(R.id.sliding_layout);



        /*_listView = findViewById(R.id.list_song);

        _songAdapter = new SongAdapter(this, R.layout.song_item);*/

       // _listView.setAdapter(_songAdapter);

        _mViewPager = findViewById(R.id.vp);

        _mViewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        mNavigationTabStrip = findViewById(R.id.nts_center);
        mNavigationTabStrip.setViewPager(_mViewPager, 1);
        mNavigationTabStrip.setTabIndex(0);
        mNavigationTabStrip.setTitleSize(45);
        mNavigationTabStrip.setStripFactor(2);
        mNavigationTabStrip.setStripColor(Color.parseColor("#f74e76"));
        mNavigationTabStrip.setStripGravity(NavigationTabStrip.StripGravity.BOTTOM);
        mNavigationTabStrip.setStripType(NavigationTabStrip.StripType.LINE);

        mNavigationTabStrip.setCornersRadius(4);
        mNavigationTabStrip.setAnimationDuration(300);
        mNavigationTabStrip.setInactiveColor(Color.parseColor("#CDC8C5"));
        mNavigationTabStrip.setActiveColor(Color.parseColor("#282828"));

        mNavigationTabStrip.setTypeface("fonts/roboto_medium.ttf");

        appBarLayout = findViewById(R.id.appbar_layout);
        _mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               // appBarLayout.setExpanded(true);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        song_playing_fragment = (Song_Playing_Fragment)getFragmentManager().findFragmentById( R.id.song_fragment);
        song_playing_fragment.getAdaper().setSize(Contanst.list_songs.size());
        song_playing_fragment.onMsgFromMainToSlideFragment(MainActivity.LOAD_SONG_FINISHED);

        seekBar = findViewById(R.id.seekBar);
        ///seekBar.getThumb().mutate().setAlpha(0);

        /*wave = findViewById(R.id.wave);

        final byte[] data = {1, 3, 37, 117, 69, 0, 0, 58};


        wave.setScaledData(data);*/
       /* FragmentTransaction ft = getFragmentManager(). beginTransaction();
        song_fragment = Song_List_Fragment.newInstance();
        ft.replace(R.id.song_fragment, song_fragment);
        ft.commit();*/

        /*ViewPager of playing*/
        /*_mViewPagerSong = findViewById(R.id.mPager);
        _mViewPagerSong.setOffscreenPageLimit(Contanst.list_songs.size());
        _mViewPagerSong.setAdapter(new MyPagerAdaperSong(getSupportFragmentManager()));
        _mViewPagerSong.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               // playSong(Contanst.list_songs.get(position));
                Log.e("Scroll",position+"");
            }

            @Override
            public void onPageSelected(int position) {
               playSong(Contanst.list_songs.get(position));
               // playSong(Contanst.list_songs.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
*/
        //_mViewPagerSong.setPageTransformer(false, new ParallaxPagerTransformer(R.id.img_song));
        /*_mViewPagerSong.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return false;
            }
        });*/

    }




    @Override
    protected void onStart() {
        super.onStart();

       /* if(playIntent == null){
            playIntent = new Intent(this, MusicService.class);
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);

            //startService(playIntent);
        }*/
     /*   playIntent = new Intent(this, MusicService.class);
        bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);*/


    }

    @Override
    protected void onDestroy() {
        if(playIntent != null){
           // stopService(playIntent);
           // musicService = null;
        }

        super.onDestroy();

    }

    @Override
    protected void onPause(){
        super.onPause();

    }
    @Override
    protected void onResume(){

        super.onResume();

    }
    @Override
    protected void onStop() {
        super.onStop();
        //unbindService(musicConnection);
       // musicBound = false;

    }


    private void addEvents() {


        img_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){

                    musicService.pauseSong();
                }else{

                    musicService.continueSong();
                }

            }
        });
        img_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                song_playing_fragment.onMsgFromMainToSlideFragment(SLIDE_NEXT);

            }
        });
        img_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                song_playing_fragment.onMsgFromMainToSlideFragment(SLIDE_PREVIOUS);
            }
        });
        img_previous.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });


        _control_bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    _slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }

                return true;
            }
        });
        _playing_now.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                _slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                return true;
            }
        });
        _slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
               // Log.e("slide up position", slideOffset+"");
                _control_bar.setAlpha(1.0f - slideOffset*2);
                /*_btn_play.setAlpha(1.0f - slideOffset);
                _txtSinger_control.setAlpha(1.0f - slideOffset);
                _txtSongName_control.setAlpha(1.0f - slideOffset);*/

            }

            @Override
            public void onPanelStateChanged(View panel,
                                            SlidingUpPanelLayout.PanelState previousState,
                                            SlidingUpPanelLayout.PanelState newState) {

                Log.e("slide up state", previousState+" "+newState);



            }
        });


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_MEDIA: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    mainPresenter.loadData();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    System.exit(0);
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.
        }
    }
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            String returnedValue = (String)msg.obj;
            if(returnedValue.equals("loadDataSuccess")){
                initUI();
                addEvents();

            }
            return true;
        }
    });
    Song song;
    @Override
    public void onDisplaySongList(final ArrayList<Song> listSong) {
       // controlAudio = ControlAudio.getInstance(getApplicationContext(), listSong);
        //listener when service
        //controlAudio.setMusicService(this);

        listSong.sort(new Comparator<Song>() {
            public int compare(Song left, Song right) {
                return left.getTitle().compareTo(right.getTitle());
            }
        });


        Contanst.list_songs = listSong;

        song = listSong.get(22);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Message msg = mHandler.obtainMessage(1, "loadDataSuccess");
                mHandler.sendMessage(msg);
            }
        });
        t.start();


    }


    private void playSongMain(Song song)
    {
        musicService.playSong(song);
        img_play.setImageResource(R.drawable.pause_main);
        isPlaying = true;

       //updateUI(song);

    }
   /* public Bitmap blur(Bitmap image) {
        if (null == image) return null;

        Bitmap outputBitmap = Bitmap.createScaledBitmap(image,200,300,true);
        final RenderScript renderScript = RenderScript.create(this);
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, image);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        //Intrinsic Gausian blur filter
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript));
        theIntrinsic.setRadius(25f);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        return outputBitmap;
    }*/

    private TimerTask timerTask = null;
    private Timer timer = new Timer();
   /* public void changeSeekbarColor(SeekBar s,int colorp,int colors,int color b)
    {
        PorterDuff.Mode mMode = PorterDuff.Mode.SRC_ATOP;
        LayerDrawable layerDrawable = (LayerDrawable) s.getProgressDrawable();
        Drawable progress = (Drawable) layerDrawable.findDrawableByLayerId(android.R.id.progress);
        Drawable background = (Drawable) layerDrawable.findDrawableByLayerId(android.R.id.background);


        // Setting colors
        progress.setColorFilter(colorp,mMode);

        background.setColorFilter(colorb, mMode);


        // Applying Tinted Drawables
        layerDrawable.setDrawableByLayerId(android.R.id.progress, progress);



        layerDrawable.setDrawableByLayerId(android.R.id.background, background);

    }*/
    private void updateUI(Song song) {


       /* seekBar.setProgressDrawableTiled(getResources().getDrawable(R.drawable.gradient_color));
        seekBar.setBackgroundColor(Color.WHITE);*/
        final Song s = song;

        song_playing_fragment.onMsgFromMainToSlideFragment(UPDATE_SONG_UI);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        _txtSongName_control.setText(s.getTitle());
                        _txtSinger_control.setText(s.getArtist());

                        _txtSongName_main.setText(s.getTitle());
                        _txtSinger_main.setText(s.getArtist());

                        progressbar_control.setProgress(0);
                        seekBar.setProgress(2);
                    }
                });
            }
        });
        t.start();

        if(timerTask != null && timer != null){
            timer.cancel();
            timer.purge();
        }
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {

                int mCurrentPosition = 0;
                int totalDuration = musicService.getDur();
                if (totalDuration <= 0) {
                    mCurrentPosition = 0;
                } else {
                    int currentDuration = musicService.getPosn();

                    // Updating progress bar
                    mCurrentPosition = ((currentDuration * 100) / totalDuration);

                }

                if(mCurrentPosition >= 100){
                    mCurrentPosition = 100;
                }
                progressbar_control.setProgress(mCurrentPosition);
                seekBar.setProgress(mCurrentPosition);
            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 100);

        /***************************/

        Bitmap bitmap= null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(song.getAlbumArtPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bitmap == null){
            img_bg.setBackgroundColor(Color.WHITE);
        }else {
            Blurry.with(getApplicationContext())
                    .radius(40)
                    .sampling(1)
                    .async()
                    .animate(2000).from(bitmap).into(img_bg);
/*
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(@NonNull Palette palette) {
                    Palette.Swatch swatch = palette.getVibrantSwatch();
                    if (swatch == null) {
                        swatch = palette.getMutedSwatch(); // Sometimes vibrant swatch is not available
                    }
                    if (swatch != null) {
                        // Set the background color of the player bar based on the swatch color
                        //_playing_now.setBackgroundColor(swatch.getRgb());


                        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT,
                                new int[]{swatch.getRgb(), swatch.getTitleTextColor()});
                        gradient.setShape(GradientDrawable.RECTANGLE);
                        gradient.setCornerRadius(10.f);
                        seekBar.setProgressDrawable(gradient);

                    }
                }
            });*/

          /*  Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                public void onGenerated(Palette palette) {
                    Palette.Swatch swatch = palette.getVibrantSwatch();
                    if (swatch == null) {
                        swatch = palette.getMutedSwatch(); // Sometimes vibrant swatch is not available
                    }
                    if (swatch != null) {
                        // Set the background color of the player bar based on the swatch color
                        //_playing_now.setBackgroundColor(swatch.getRgb());


                        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                                new int[]{swatch.getRgb(), swatch.getTitleTextColor()});
                        gradient.setShape(GradientDrawable.RECTANGLE);
                        gradient.setCornerRadius(10.f);
                        seekBar.setProgressDrawable(gradient);

                    }
                }
            }*/
        }
    }

    @Override
    public void onMsgFromListFragToMain(Song song) {

        playSongMain(song);


    }

    @Override
    public void onMsgFromServiceToMain(Song song) {
        updateUI(song);

    }

    @Override
    public void onControlFromServiceToMain(String msg) {
        switch (msg){
            case MusicService.ACTION_PLAY:
                img_play.setImageResource(R.drawable.pause_main);
                isPlaying = true;
                break;

            case MusicService.ACTION_PAUSE:
                img_play.setImageResource(R.drawable.play_main);
                isPlaying = false;
                break;

            case MusicService.ACTION_NEXT:
                img_play.setImageResource(R.drawable.pause_main);
                isPlaying = true;
                break;

            case MusicService.ACTION_PREVIOUS:
                img_play.setImageResource(R.drawable.pause_main);
                isPlaying = true;
                break;

                default:

                break;
        }

    }


   /* public void showNotification() {
        Intent activityIntent = new Intent(this,MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, activityIntent, 0);
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            notification = new Notification.Builder(this)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setSmallIcon(Icon.createWithResource(this,R.mipmap.ic_launcher))
                    .addAction(new Notification.Action
                            .Builder(Icon.createWithResource(this, android.R.drawable.ic_media_previous),
                            "Previous", pendingIntent).build())

                    .addAction(new Notification.Action
                            .Builder(Icon.createWithResource(this,android.R.drawable.ic_media_pause),
                            "Pause", pendingIntent).build())

                    .addAction(new Notification.Action
                            .Builder( Icon.createWithResource(this, android.R.drawable.ic_media_next),
                            "Next", pendingIntent).build())
                    .setContentTitle("Music")
                    .setContentText("Now playing...")
                    .setLargeIcon(Icon.createWithResource(this, R.mipmap.ic_launcher))
                    .setStyle(new Notification.MediaStyle().setShowActionsInCompactView(1)).build();
        } else {
            notification = new Notification.Builder(this)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setSmallIcon(R.mipmap.ic_launcher)

                    .addAction(new Notification.Action.Builder(android.R.drawable.ic_media_previous,
                            "Previous", pendingIntent).build())

                    .addAction(new Notification.Action.Builder(android.R.drawable.ic_media_pause,
                            "Pause",pendingIntent).build())

                    .addAction(new Notification.Action.Builder(android.R.drawable.ic_media_next,
                            "Next",pendingIntent).build())
                    .setContentTitle("Music")
                    .setContentText("Now playing...")
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                    .setStyle(new Notification.MediaStyle().setShowActionsInCompactView(1)).build();
        }
        NotificationManager notificationManager =
                (NotificationManager) this.getSystemService( Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }*/

}
