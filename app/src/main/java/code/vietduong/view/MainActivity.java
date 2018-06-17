package code.vietduong.view;

import android.Manifest;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.FastScroller;

import com.mingle.entity.MenuEntity;
import com.mingle.sweetpick.CustomDelegate;
import com.mingle.sweetpick.SweetSheet;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.orhanobut.dialogplus.DialogPlus;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import code.vietduong.adapter.SongPopUpAdapter;
import code.vietduong.data.Contanst;
import code.vietduong.fragment.Album_Fragment;
import code.vietduong.fragment.Artist_Fragment;
import code.vietduong.fragment.Genres_Fragment;
import code.vietduong.fragment.List_Fragment;
import code.vietduong.fragment.Song_Playing_Fragment;
import code.vietduong.impl.MainCallbacks;
import code.vietduong.model.entity.Album;
import code.vietduong.model.entity.Artist;
import code.vietduong.model.entity.Song;

import code.vietduong.oneplayer.R;
import code.vietduong.presenter.SongPresenter;
import code.vietduong.adapter.SongAdapter;
import jp.wasabeef.blurry.Blurry;



public class MainActivity extends AppCompatActivity implements MainCallbacks{
    private SongPresenter mainPresenter;
    private final int MY_PERMISSIONS_REQUEST_READ_MEDIA = 123;

    private final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 321;

    private static boolean isPlaying = false;
    /*Components*/
   // private RecyclerView _songRecyclerView;
    private SongAdapter _songAdapter;
   // private RecyclerView.LayoutManager _songLayoutManager;
    private SlidingUpPanelLayout _slidingLayout;
    private FrameLayout _control_bar;
    private FrameLayout _playing_now;
    private static ImageView _img_song, img_play, img_previous, img_next;
    private static ImageView btnPlayControl, imgSearch;
    private ImageView img_bg;
    private ImageView img_down_main;
    private ImageView img_eq;
    private ImageView img_random;
    private ImageView img_repeat;
    private ImageView img_list;

    private TextView _txtSongName_control, _txtSinger_control, txtSongName_main, txtSinger_main;
    private TextView txtStart_main, txtEnd_main;
    private ListView _listView;

    private ProgressBar progressbar_control;
    /************************************/
    private ViewPager _mViewPager, _mViewPagerSong;

   /* private NavigationTabStrip mNavigationTabStrip;*/
    private Song_Playing_Fragment song_playing_fragment;
    private SeekBar seekBar;

    private Intent playIntent = null;
    private  SmartTabLayout viewPagerTab;

    public  static String LOAD_SONG_FINISHED = "load data finished";
    public  static String UPDATE_SONG_UI = "update song ui";
    public  static String SLIDE_NEXT = "slide next";
    public  static String SLIDE_PREVIOUS = "slide previous";
    /*Service*/

    private BottomSheetBehavior mBottomSheetBehavior;
    public static MusicService musicService;

    private static boolean musicBound = false;

    private int current_tab = 0, previous_tab = 0, current_pixel = 0;

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

   // private Window window;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(playIntent == null){
            playIntent = new Intent(this, MusicService.class);

            /*start and bind de taskremoved dc goi*/
            startService(new Intent(this, MusicService.class));
            bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        }
        loadSongs();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Contanst.height = displayMetrics.heightPixels;
        Contanst.width = displayMetrics.widthPixels;



       /* ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);
        for(ActivityManager.RunningTaskInfo taskInfo: taskList){
            Log.e("stack main"+taskInfo.id,taskInfo.toString());
        }*/
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

       /* if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.RECORD_AUDIO},
                    MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
        }else{

        }*/


    }

    private long mLastClickTime = 0;

    DialogPlus dialog;
    private SweetSheet popup_listsong;
    private void initUI() {

        imgSearch = findViewById(R.id.imgSearch);
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              actionClickSearch();

            }
        });
        _playing_now = findViewById(R.id.playing_now);

        popup_listsong = new SweetSheet(_playing_now);

        CustomDelegate customDelegate = new CustomDelegate(true,
                CustomDelegate.AnimationType.AlphaAnimation);


        customDelegate.setContentHeight(1500);
        View view = LayoutInflater.from(this).inflate(R.layout.popup_listsong, null, false);


        customDelegate.setCustomView(view);

        popup_listsong.setDelegate(customDelegate);

        SongPopUpAdapter popUpAdapter = new SongPopUpAdapter(this);

        popUpAdapter.setOnItemClickListener(new SongPopUpAdapter.RecyclerItemClickListener() {
            @Override
            public void onItemClick(Song song) {
                playSongMain(song);
                _slidingLayout.setTouchEnabled(true);
                popup_listsong.dismiss();

            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        RecyclerView pop_list = view.findViewById(R.id.popup_list);

        pop_list.setLayoutManager(layoutManager);
        pop_list.setAdapter(popUpAdapter);
        FastScroller fastScroller = view.findViewById(R.id.fastscrollpopup);
        //has to be called AFTER RecyclerView.setAdapter()
        fastScroller.setRecyclerView(pop_list);


        img_random = findViewById(R.id.img_radom);
        img_random.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicService.isRandom()){
                    img_random.setImageResource(R.drawable.random_main);
                    musicService.setRandom(false);
                    song_playing_fragment.setRandom(false);

                }else{
                    img_random.setImageResource(R.drawable.random_main_selected);
                    musicService.setRandom(true);
                    song_playing_fragment.setRandom(true);
                }
            }
        });
        img_repeat = findViewById(R.id.img_repeat);
        img_repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(musicService.isRepeat()){
                    img_repeat.setImageResource(R.drawable.repeat_main);
                    musicService.setRepeat(false);
                }else{
                    img_repeat.setImageResource(R.drawable.repeat_main_selected);
                    musicService.setRepeat(true);
                }
            }
        });


                img_list = findViewById(R.id.img_list);
        img_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SystemClock.elapsedRealtime() - mLastClickTime < 2000) {
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                    popupListSong();



            }
        });
        img_eq = findViewById(R.id.img_eq);
        txtStart_main = findViewById(R.id.txtStart_main);
        txtEnd_main = findViewById(R.id.txtEnd_main);

        txtEnd_main.setText(convertTimeToString(Integer.parseInt(Contanst.list_songs.get(0).getDuration())));

        img_down_main = findViewById(R.id.img_down_main);
        txtSongName_main  = findViewById(R.id.txtSongName_main);

        txtSinger_main = findViewById(R.id.txtSingle_main);

        img_bg = findViewById(R.id.img_bg);
        img_play = findViewById(R.id.img_play);
        img_previous = findViewById(R.id.img_previous);
        img_next = findViewById(R.id.img_next);
        img_play.setAlpha(0.75f);
        img_previous.setAlpha(0.75f);
        img_next.setAlpha(0.75f);
        btnPlayControl = findViewById(R.id.btnPlayControl);

        btnPlayControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){

                    musicService.pauseSong();
                }else{

                    musicService.continueSong();
                }
            }
        });

        progressbar_control = findViewById(R.id.progressBar_Control);
        progressbar_control.setProgress(0);
        progressbar_control.setMax(100);
        _control_bar = findViewById(R.id.control_bar);


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

        Song s = Contanst.list_songs.get(0);
        txtSinger_main.setText(s.getArtist());
        txtSongName_main.setText(s.getTitle());
        _txtSinger_control.setText(s.getArtist());
        _txtSongName_control.setText(s.getTitle());


        _slidingLayout = findViewById(R.id.sliding_layout);


        _mViewPager = findViewById(R.id.vp);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
               /* .add("Home", Home_Fragment.class)*/
                .add("Songs", List_Fragment.class)
                .add("Artists", Artist_Fragment.class)
                .add("Albums", Album_Fragment.class)
                .add("Genres", Genres_Fragment.class)
                .create());
        _mViewPager.setAdapter(adapter);


        viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);


        int[][] states = new int[][] {
                new int[] { android.R.attr.state_selected}, // enabled
                new int[] {-android.R.attr.state_selected}
        };

        int[] colors = new int[] {
                Color.parseColor("#ffffff"),
                Color.parseColor("#35ffffff")
        };

        ColorStateList myList = new ColorStateList(states, colors);
        viewPagerTab.setDefaultTabTextColor(myList);

        viewPagerTab.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {



            }

            @Override
            public void onPageSelected(int position) {
              /* for(int i = 0 ; i < viewPagerTab.getChildCount(); i++){
                   viewPagerTab.getTabAt(i).setScaleX(1.5f);
                   viewPagerTab.getTabAt(i).setScaleY(1.5f);
               }*/
                  previous_tab = current_tab;
                  current_tab = position;

                  viewPagerTab.getTabAt(current_tab).setScaleX(1.5f);
                  viewPagerTab.getTabAt(current_tab).setScaleY(1.5f);
                  viewPagerTab.getTabAt(previous_tab).setScaleX(1f);
                  viewPagerTab.getTabAt(previous_tab).setScaleY(1f);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPagerTab.setViewPager(_mViewPager);
        viewPagerTab.getTabAt(current_tab).setScaleX(1.5f);
        viewPagerTab.getTabAt(current_tab).setScaleY(1.5f);


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


        /*Set first*/
        Bitmap bitmap= null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),
                    Uri.parse(Contanst.list_songs.get(0).getAlbumArtPath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(bitmap == null){
            img_bg.setBackgroundColor(Color.WHITE);
        }else {
            Blurry.with(getApplicationContext())
                    .radius(100)
                    .sampling(20)
                    .async()
                    .from(bitmap).into(img_bg);
        }
    }

    private void actionClickSearch() {
        Intent intent = new Intent(this, SearchActivity.class);
        startActivity(intent);
    }

    private void popupListSong() {

        popup_listsong.show();
        _slidingLayout.setTouchEnabled(false);
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
        super.onDestroy();

        if(musicBound){
            unbindService(musicConnection);
            musicBound = false;

            if(timerTask != null && timer != null){
                timerTask.cancel();
                timer.cancel();
            }

            finish();
            Log.e("onDestroy","123");

        }
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
       /* unbindService(musicConnection);
        musicBound = false;*/

    }


    private boolean dragging_seekbar = false;
    private void addEvents() {
        img_eq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, EQActivity.class);
                MainActivity.this.startActivity(myIntent);
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                if(dragging_seekbar){
                    txtStart_main.setText(convertTimeToString(
                            (musicService.getDur()*progress)/100));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                dragging_seekbar = true;

                Log.e("change",seekBar.getProgress()+"");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                musicService.seekTo(seekBar.getProgress());
                dragging_seekbar = false;
            }
        });
       img_down_main.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               _slidingLayout.setTouchEnabled(true);
               if(event.getAction() == MotionEvent.ACTION_UP){
                   _slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
               }

               return true;
           }
       });
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

                /*_btn_play.setAlpha(1.0f - slideOffset);
                _txtSinger_control.setAlpha(1.0f - slideOffset);
                _txtSongName_control.setAlpha(1.0f - slideOffset);*/

                _control_bar.setAlpha(1.0f - slideOffset*2);

                img_down_main.setAlpha(1.0f - _control_bar.getAlpha());

              /*  float percent = slideOffset*100;
                int p = (int)percent;


                window.setStatusBarColor(Color.argb(slideOffset,144, 146, 252));*/


            }

            @Override
            public void onPanelStateChanged(View panel,
                                            SlidingUpPanelLayout.PanelState previousState,
                                            SlidingUpPanelLayout.PanelState newState) {

                Log.e("slide up state", previousState+" "+newState);

                if(newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)){
                    _control_bar.setEnabled(false);
                    img_down_main.setEnabled(true);

                    //window.setStatusBarColor(Color.WHITE);


                }else if(newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
                    _control_bar.setEnabled(true);
                    img_down_main.setEnabled(false);

                   // window.setStatusBarColor(Color.parseColor("#9092FC"));
                }else if(newState.equals(SlidingUpPanelLayout.PanelState.DRAGGING)){

                }


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
    /*Song song;*/
    @RequiresApi(api = Build.VERSION_CODES.N)
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

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                Message msg = mHandler.obtainMessage(1, "loadDataSuccess");
                mHandler.sendMessage(msg);
            }
        });
        t.start();


    }


    public static void playSongMain(Song song)
    {
        musicService.playSong(song);
        img_play.setImageResource(R.drawable.pause_main);
        btnPlayControl.setImageResource(R.drawable.pause_control);
        isPlaying = true;


    }

   private String convertTimeToString(int mili){
       String rs = "";
       int total = mili/1000;
       int minute = total/60;
       int second = total%60;
       rs = minute+":";
       if(second < 10){
           rs +="0"+second;
       }else{
           rs += second;
       }
      return rs;

   }
    private TimerTask timerTask = null;
    private Timer timer = null;
    private void updateUI(Song song) {


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

                       // txtTitle_main.setText(s.getTitle()+" - "+s.getArtist());
                        txtSongName_main.setText(s.getTitle());
                        txtSinger_main.setText(s.getArtist());

                        progressbar_control.setProgress(0);
                        seekBar.setProgress(0);
                    }
                });
            }
        });
        t.start();

        if(timerTask != null && timer != null){
            timer.cancel();
            timer.purge();
            timerTask.cancel();
            timerTask = null;
            timer = null;
        }

        final int totalDuration = musicService.getDur();
        txtEnd_main.setText(convertTimeToString(musicService.getDur()));

        timerTask = new TimerTask() {
            @Override
            public void run() {

                int mCurrentPosition = 0;
                /*final int currentDuration = 0;*/
               // int totalDuration = musicService.getDur();
                if (totalDuration <= 0) {
                    mCurrentPosition = 0;
                } else {
                    int currentDuration = musicService.getPosn();

                    Log.e("seek", currentDuration+"/"+totalDuration);
                   if(currentDuration<=totalDuration){

                       runOnUiThread(new Runnable() {
                           @Override
                           public void run() {
                               txtStart_main.setText(convertTimeToString(musicService.getPosn()));
                           }
                       });

                   }else{
                       musicService.completeSong();
                   }

                    // Updating progress bar
                    mCurrentPosition = ((currentDuration * 100) / totalDuration);

                }
                if(mCurrentPosition >= 100){
                    mCurrentPosition = 100;
                }

                progressbar_control.setProgress(mCurrentPosition);
                if(!dragging_seekbar){
                    seekBar.setProgress(mCurrentPosition);

                  /*  runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txtStart_main.setText(convertTimeToString(cu));
                        }
                    });*/
                }

            }
        };
        timer = new Timer();
        timer.schedule(timerTask, 0, 10);


        Bitmap bitmap= null;
        Log.e("bg","update");
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(song.getAlbumArtPath()));
            if(bitmap == null){
                img_bg.setImageDrawable(null);
                img_bg.setBackgroundColor(Color.parseColor("#90000000"));
            }else {

            /*    Blurry.with(getApplicationContext())
                        .radius(25)
                        .sampling(20)
                        .async()
                        .animate(2000)
                        .from(bitmap).into(img_bg);*/
                Blurry.with(getApplicationContext())
                        .radius(3)
                        .sampling(3)
                        .async()
                        .animate(2000)
                        .from(bitmap).into(img_bg);

               /* Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    public void onGenerated(Palette palette) {
                        Palette.Swatch swatch = palette.getVibrantSwatch();
                        if (swatch == null) {
                            swatch = palette.getMutedSwatch(); // Sometimes vibrant swatch is not available
                        }
                        if (swatch != null) {
                            img_bg.setBackgroundColor(swatch.getRgb());
                        }
                    }
                });*/

            }
        } catch (IOException e) {
            img_bg.setImageDrawable(null);
            img_bg.setBackgroundColor(Color.parseColor("#90000000"));
            e.printStackTrace();

        }
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
                btnPlayControl.setImageResource(R.drawable.pause_control);
                isPlaying = true;
                break;

            case MusicService.ACTION_PAUSE:
                img_play.setImageResource(R.drawable.play_main);
                btnPlayControl.setImageResource(R.drawable.play_cotrol);
                isPlaying = false;
                break;

            case MusicService.ACTION_NEXT:
                img_play.setImageResource(R.drawable.pause_main);
                btnPlayControl.setImageResource(R.drawable.pause_control);
                isPlaying = true;
                break;

            case MusicService.ACTION_PREVIOUS:
                img_play.setImageResource(R.drawable.pause_main);
                btnPlayControl.setImageResource(R.drawable.pause_control);
                isPlaying = true;
                break;

                default:

                break;
        }

    }
    @Override
    public void onMsgFromListFragToMain(Song song) {
        playSongMain(song);
        _slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);


    }


    @Override
    public void onMsgFromAlbumFragToMain(Album album) {
        Intent intent = new Intent(this, AlbumActivity.class);

        intent.putExtra(Contanst.MSG_MAIN_ALBUM_ACTIVITY, album.getId() + "");
        startActivity(intent);

    }

    @Override
    public void onMsgFromArtistFragToMain(Artist artist) {
        Intent intent = new Intent(this, ArtistActivity.class);

        intent.putExtra(Contanst.MSG_MAIN_ARTIST_ACTIVITY, artist.getId() + "");
        startActivity(intent);
    }

    @Override
    public void onMsgFromGenresFragToMain(int position) {
        Intent intent = new Intent(this, GenresActivity.class);

        intent.putExtra(Contanst.MSG_MAIN_GENRES_ACTIVITY, position + "");
        startActivity(intent);

    }



    @Override
    public void onBackPressed() {
        if(popup_listsong.isShow()){

            popup_listsong.dismiss();
        }
        else{
            if(_slidingLayout.getPanelState().equals(SlidingUpPanelLayout.PanelState.EXPANDED)){
                _slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }else{
                moveTaskToBack(true);
            }
        }



    }

}
