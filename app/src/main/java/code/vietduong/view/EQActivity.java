package code.vietduong.view;

import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Virtualizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;


import com.h6ah4i.android.widget.verticalseekbar.VerticalSeekBar;
import com.sdsmdg.harjot.crollerTest.Croller;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;

import code.vietduong.data.Contanst;
import code.vietduong.oneplayer.R;

public class EQActivity extends AppCompatActivity {

   // private Equalizer mEqualizer;

    private NiceSpinner niceSpinner;

    private Croller bassCroller, virtualCroller;

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
               finish();
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
       /* List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));
        niceSpinner.attachDataSource(dataset);*/
        niceSpinner.setBackgroundColor(Color.parseColor("#F8F8F8"));

        bassCroller = (Croller) findViewById(R.id.crollerBass);
        bassCroller.setIndicatorWidth(10);
        bassCroller.setBackCircleColor(Color.parseColor("#182436"));
        bassCroller.setMainCircleColor(Color.parseColor("#192F51"));
     /*   bassCroller.setMax(50);*/
        bassCroller.setStartOffset(45);
        bassCroller.setIsContinuous(false);
        bassCroller.setLabelColor(Color.WHITE);
        bassCroller.setProgressPrimaryColor(Color.parseColor("#AB1A62"));
        bassCroller.setIndicatorColor(Color.parseColor("#AB1A62"));
        bassCroller.setProgressSecondaryColor(Color.parseColor("#00EEEEEE"));
        bassCroller.setProgressPrimaryCircleSize(10);
        bassCroller.setMax(20);
        bassCroller.setLabel("Bassboost");
        bassCroller.setMainCircleRadius(200);


        virtualCroller = (Croller) findViewById(R.id.crollerVirtualizer);
        virtualCroller.setIndicatorWidth(10);
        virtualCroller.setBackCircleColor(Color.parseColor("#182436"));
        virtualCroller.setMainCircleColor(Color.parseColor("#192F51"));
        virtualCroller.setStartOffset(45);
        virtualCroller.setIsContinuous(false);
        virtualCroller.setLabelColor(Color.WHITE);
        virtualCroller.setProgressPrimaryColor(Color.parseColor("#AB1A62"));
        virtualCroller.setIndicatorColor(Color.parseColor("#AB1A62"));
        virtualCroller.setProgressSecondaryColor(Color.parseColor("#00EEEEEE"));
        virtualCroller.setProgressPrimaryCircleSize(10);
        virtualCroller.setMax(20);
        virtualCroller.setLabel("Virtualizer");
        virtualCroller.setMainCircleRadius(200);

        mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Contanst.mEqualizer.setEnabled(isChecked);
                Contanst.bassBoost.setEnabled(isChecked);
                Contanst.virtualizer.setEnabled(isChecked);

            }
        });

        if(Contanst.mEqualizer!=null){
            mySwitch.setChecked(Contanst.mEqualizer.getEnabled());
        }

    }

    private void setupEQ() {
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        if(Contanst.mEqualizer == null){
            Contanst.mEqualizer = new Equalizer(0, MainActivity.musicService.player.getAudioSessionId());
            Contanst.mEqualizer.setEnabled(false);
        }


        final short minEQLevel = Contanst.mEqualizer.getBandLevelRange()[0];
        final short maxEQLevel = Contanst.mEqualizer.getBandLevelRange()[1];


        for(int i = 0 ; i < 5; i++){

            final int j = i;
            listSeekBar[i].setMax(maxEQLevel - minEQLevel);
            //listSeekBar[i].setProgress(mEqualizer.getBandLevel((short)i));

            listSeekBar[i].setProgress(Contanst.listPreset[i]);

            listSeekBar[i].setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                public void onProgressChanged(SeekBar seekBar, int progress,
                                              boolean fromUser) {

                    Contanst.mEqualizer.setBandLevel((short)j, (short) (progress + minEQLevel));


                    Contanst.listPreset[j] = progress;

                   // Contanst.positionPreset = -1;
                   // Log.e("seek index "+j, listSeekBar[j].getProgress()+"/"+listSeekBar[j].getMax());
                }

                public void onStartTrackingTouch(SeekBar seekBar) {}
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });
        }


        /*pre-set*/

        ArrayList<String> equalizerPresetNames = new ArrayList<>();
        /*  equalizerPresetNames.add("Flat");*/

        for(short i=0 ; i<Contanst.mEqualizer.getNumberOfPresets(); i++){
            equalizerPresetNames.add(Contanst.mEqualizer.getPresetName(i));
        }

        /* List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));*/
        niceSpinner.attachDataSource(equalizerPresetNames);
        niceSpinner.setSelectedIndex(3);
        niceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                Contanst.positionPreset = position;
                Contanst.mEqualizer.usePreset((short) position);

                for (short i = 0; i < 5; i++) {
                    listSeekBar[i].setProgress(Contanst.mEqualizer.getBandLevel(i) - minEQLevel);

                   // Contanst.listPreset[i] = 1500;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if(Contanst.positionPreset >= 0) {
            niceSpinner.setSelectedIndex(Contanst.positionPreset);
           // Contanst.mEqualizer.usePreset((short) Contanst.positionPreset);
            /*for (short i = 0; i < 5; i++) {
                listSeekBar[i].setProgress(Contanst.mEqualizer.getBandLevel(i) - minEQLevel);
            }*/
            // Contanst.mEqualizer.usePreset((short) Contanst.positionPreset);
        }



        if(Contanst.bassBoost == null)
        {
             Contanst.bassBoost = new BassBoost(0, MainActivity.musicService.player.getAudioSessionId());
             Contanst.bassBoost.setEnabled(false);

        }else{
            bassCroller.setProgress(Contanst.bassBoost.getRoundedStrength()/50);
        }
        if (Contanst.bassBoost.getStrengthSupported())
        {

            bassCroller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
                @Override
                public void onProgressChanged(int progress) {

                  Contanst.bassBoost.setStrength((short) (progress*50));
                  Log.e("bassboost",progress*50+"");
                }
            });
        }else{
            Toast.makeText(EQActivity.this, "Bassboost isn't supported",Toast.LENGTH_SHORT).show();

        }


        /****************************/
        if(Contanst.virtualizer == null){

            Contanst.virtualizer = new Virtualizer(0, MainActivity.musicService.player.getAudioSessionId());
            Contanst.virtualizer.setEnabled(false);
        }else{
            virtualCroller.setProgress(Contanst.virtualizer.getRoundedStrength()/50);
        }

        if (Contanst.virtualizer.getStrengthSupported())
        {
           ;
            virtualCroller.setOnProgressChangedListener(new Croller.onProgressChangedListener() {
                @Override
                public void onProgressChanged(int progress) {

                    Contanst.virtualizer.setStrength((short) (progress*50));
                    Log.e("virtualizer",progress*50+"");
                }
            });
        }else{

            Toast.makeText(EQActivity.this, "Virtualizer isn't supported",Toast.LENGTH_SHORT).show();
        }


    }






    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
    }
}
