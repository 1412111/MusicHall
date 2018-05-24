package code.vietduong.oneplayer;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;


import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;
import com.sdsmdg.harjot.crollerTest.Croller;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import code.vietduong.view.main.MainActivity;

public class EQActivity extends AppCompatActivity {

    private Equalizer mEqualizer;

    private NiceSpinner niceSpinner;

    private Croller bassCroller, trebleCroller;

    private Switch mySwitch;


    private VerticalSeekBar[] listSeekBar = new VerticalSeekBar[5];
    private VerticalSeekBar seekBar1, seekBar2, seekBar3, seekBar4, seekBar5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eq);

        initControl();

        setupEQ();

    }

    private void initControl() {

        ImageView back = findViewById(R.id.img_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveTaskToBack(true);
            }
        });
        mySwitch = findViewById(R.id.mySwitch);

        seekBar1 = findViewById(R.id.seekBar1);
        seekBar2 = findViewById(R.id.seekBar2);
        seekBar3 = findViewById(R.id.seekBar3);
        seekBar4 = findViewById(R.id.seekBar4);
        seekBar5 = findViewById(R.id.seekBar5);

        listSeekBar[0] = seekBar1;
        listSeekBar[1] = seekBar2;
        listSeekBar[2] = seekBar3;
        listSeekBar[3] = seekBar4;
        listSeekBar[4] = seekBar5;

        niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setBackgroundColor(Color.parseColor("#F8F8F8"));

        bassCroller = (Croller) findViewById(R.id.croller);
        bassCroller.setIndicatorWidth(10);
        bassCroller.setBackCircleColor(Color.parseColor("#182436"));
        bassCroller.setMainCircleColor(Color.parseColor("#192F51"));
        bassCroller.setMax(50);
        bassCroller.setStartOffset(45);
        bassCroller.setIsContinuous(false);
        bassCroller.setLabelColor(Color.BLACK);
        bassCroller.setProgressPrimaryColor(Color.parseColor("#AB1A62"));
        bassCroller.setIndicatorColor(Color.parseColor("#AB1A62"));
        bassCroller.setProgressSecondaryColor(Color.parseColor("#00EEEEEE"));
        bassCroller.setProgressPrimaryCircleSize(10);
        bassCroller.setMax(20);
        bassCroller.setLabel("Bass");
        bassCroller.setMainCircleRadius(200);


        trebleCroller = (Croller) findViewById(R.id.croller1);
        trebleCroller.setIndicatorWidth(10);
        trebleCroller.setBackCircleColor(Color.parseColor("#182436"));
        trebleCroller.setMainCircleColor(Color.parseColor("#192F51"));
        trebleCroller.setMax(50);
        trebleCroller.setStartOffset(45);
        trebleCroller.setIsContinuous(false);
        trebleCroller.setLabelColor(Color.BLACK);
        trebleCroller.setProgressPrimaryColor(Color.parseColor("#AB1A62"));
        trebleCroller.setIndicatorColor(Color.parseColor("#AB1A62"));
        trebleCroller.setProgressSecondaryColor(Color.parseColor("#00EEEEEE"));
        trebleCroller.setProgressPrimaryCircleSize(10);
        trebleCroller.setMax(20);
        trebleCroller.setLabel("Treble");
        trebleCroller.setMainCircleRadius(200);
    }

    private void setupEQ() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mEqualizer = new Equalizer(0, MainActivity.musicService.player.getAudioSessionId());
        mEqualizer.setEnabled(false);



     /*   final BassBoost bassBoost = new BassBoost(0, MainActivity.musicService.player.getAudioSessionId());
        bassBoost.setEnabled(false);*/

        final short minEQLevel = mEqualizer.getBandLevelRange()[0];
        final short maxEQLevel = mEqualizer.getBandLevelRange()[1];

        //-1500 1500
        for(short i = 0 ; i < mEqualizer.getNumberOfBands(); i++){
            System.out.println("Band"+i+": "+mEqualizer.getBandLevel(i));
        }


        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mEqualizer.setEnabled(isChecked);
               // bassBoost.setEnabled(isChecked);

            }
        });

        for(int i = 0 ; i < 5; i++){

            final int j = i;
            listSeekBar[i].setMax(maxEQLevel - minEQLevel);
            //listSeekBar[i].setProgress(mEqualizer.getBandLevel((short)i));

            listSeekBar[i].setProgress(listSeekBar[i].getMax()/2);

            listSeekBar[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {

                    mEqualizer.setBandLevel((short)j, (short) (progress + minEQLevel));

                    Log.e("seek index "+j, listSeekBar[j].getProgress()+"/"+listSeekBar[j].getMax());
                }

                public void onStartTrackingTouch(SeekBar seekBar) {}
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }


        /*if (bassBoost.getStrengthSupported())
        {

            bassCroller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
                @Override
                public void onProgressChanged(int progress) {

                    BassBoost.Settings settings;
                    if(progress>1){
                        settings = new BassBoost.Settings(
                                "BassBoost;strength=" + progress*1000);
                    }else{
                        settings = new BassBoost.Settings(
                                "BassBoost;strength=" + 0);
                    }

                    Log.e("true bass",settings+"");
                    bassBoost.setProperties(settings);
                }
            });


        }else{
            bassCroller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
                @Override
                public void onProgressChanged(int progress) {

                    short strength = (short) ((1000/bassCroller.getMax())*progress);
                    bassBoost.setStrength(strength);
                    Log.e("false bass",bassBoost.getProperties().strength+"  "+strength);
                }
            });
        }*/


        /*seekBar1.setMax(maxEQLevel - minEQLevel);
        seekBar1.setProgress(mEqualizer.getBandLevel((short)0));
        seekBar1.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mEqualizer.setBandLevel((short)0, (short) (progress + minEQLevel));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBar2.setMax(maxEQLevel - minEQLevel);
        seekBar2.setProgress(mEqualizer.getBandLevel((short)1));
        seekBar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mEqualizer.setBandLevel((short)1, (short) (progress + minEQLevel));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBar3.setMax(maxEQLevel - minEQLevel);
        seekBar3.setProgress(mEqualizer.getBandLevel((short)2));
        seekBar3.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mEqualizer.setBandLevel((short)2, (short) (progress + minEQLevel));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBar4.setMax(maxEQLevel - minEQLevel);
        seekBar4.setProgress(mEqualizer.getBandLevel((short)3));
        seekBar4.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mEqualizer.setBandLevel((short)3, (short) (progress + minEQLevel));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        seekBar5.setMax(maxEQLevel - minEQLevel);
        seekBar5.setProgress(mEqualizer.getBandLevel((short)4));
        seekBar5.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                mEqualizer.setBandLevel((short)4, (short) (progress + minEQLevel));
            }

            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
*/


    }
}
