package code.vietduong.oneplayer;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.audiofx.Equalizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.SeekBar;


import com.sdsmdg.harjot.crollerTest.Croller;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import code.vietduong.view.main.MainActivity;

public class EQActivity extends AppCompatActivity {

    private SeekBar seekBar1, seekBar2, seekBar3, seekBar4, seekBar5;

    private Equalizer mEqualizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eq);
      /*  seekBar1 = findViewById(R.id.seekBar1);
        seekBar2 = findViewById(R.id.seekBar2);
        seekBar3 = findViewById(R.id.seekBar3);
        seekBar4 = findViewById(R.id.seekBar4);
        seekBar5 = findViewById(R.id.seekBar5);

        setupEQ();*/

        NiceSpinner niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner);
        List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setBackgroundColor(Color.parseColor("#F8F8F8"));

        Croller croller = (Croller) findViewById(R.id.croller);
        croller.setIndicatorWidth(10);
        croller.setBackCircleColor(Color.parseColor("#182436"));
        croller.setMainCircleColor(Color.parseColor("#192F51"));
        croller.setMax(50);
        croller.setStartOffset(45);
        croller.setIsContinuous(false);
        croller.setLabelColor(Color.BLACK);
        croller.setProgressPrimaryColor(Color.parseColor("#AB1A62"));
        croller.setIndicatorColor(Color.parseColor("#AB1A62"));
        croller.setProgressSecondaryColor(Color.parseColor("#00EEEEEE"));
        croller.setProgressPrimaryCircleSize(10);
        croller.setMax(20);
        croller.setLabel("Bass");
        croller.setMainCircleRadius(200);


        //  croller.setProgressSecondaryStrokeWidth(20);
        //croller.setMainCircleRadius(215);

        Croller croller1 = (Croller) findViewById(R.id.croller1);
        croller1.setIndicatorWidth(10);
        croller1.setBackCircleColor(Color.parseColor("#182436"));
        croller1.setMainCircleColor(Color.parseColor("#192F51"));
        croller1.setMax(50);
        croller1.setStartOffset(45);
        croller1.setIsContinuous(false);
        croller1.setLabelColor(Color.BLACK);
        croller1.setProgressPrimaryColor(Color.parseColor("#AB1A62"));
        croller1.setIndicatorColor(Color.parseColor("#AB1A62"));
        croller1.setProgressSecondaryColor(Color.parseColor("#00EEEEEE"));
        croller1.setProgressPrimaryCircleSize(10);
        croller1.setMax(20);
        croller1.setLabel("Treble");
        croller1.setMainCircleRadius(200);



    }

    private void setupEQ() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mEqualizer = new Equalizer(0, MainActivity.musicService.player.getAudioSessionId());
        mEqualizer.setEnabled(true);
        final short minEQLevel = mEqualizer.getBandLevelRange()[0];
        final short maxEQLevel = mEqualizer.getBandLevelRange()[1];

        //-1500 1500
        for(short i = 0 ; i < mEqualizer.getNumberOfBands(); i++){
            System.out.println("Band"+i+": "+mEqualizer.getBandLevel(i));
        }



        seekBar1.setMax(maxEQLevel - minEQLevel);
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

    }
}
