package code.vietduong.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.ArrayList;

import code.vietduong.adapter.SongAlbumAdapter;
import code.vietduong.data.Contanst;
import code.vietduong.model.entity.Song;
import code.vietduong.oneplayer.R;

public class SearchActivity extends AppCompatActivity {

    View scaleBack, scaleFront;
    LinearLayout main;
    TextView txt;
    ImageView img, imgDelete;
    EditText edit;

    ListView lvSearch;

    boolean scale = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        main = findViewById(R.id.main);
        imgDelete = findViewById(R.id.imgDelete);
        lvSearch = findViewById(R.id.lvSearch);
        scaleBack = findViewById(R.id.scaleBack);
        scaleFront = findViewById(R.id.scaleFront);

        txt = findViewById(R.id.txtSeach);
        img = findViewById(R.id.imgBack);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity.super.onBackPressed();
            }
        });

        edit = findViewById(R.id.editText);


        scaleFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyBoard();
            }

        });

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(edit.getText().length() > 0){

                    edit.setText("");

                }else{
                    hideKeyBoard();
                }

               // Toast.makeText(getApplicationContext(),edit.getTextSize()+"",Toast.LENGTH_SHORT).show();
            }
        });


        //lvSearch.setAdapter(adapter);

       /* edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {



                return true;
            }
        });*/


       edit.addTextChangedListener(new TextWatcher() {

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

               searchSong(s+"");
           }

           @Override
           public void beforeTextChanged(CharSequence s, int start, int count,
                                         int after) {
               // TODO Auto-generated method stub
           }

           @Override
           public void afterTextChanged(Editable s) {
               // TODO Auto-generated method stub

           }
       });
    }

    private void searchSong(final String str) {


       final ArrayList<Song> rs = new ArrayList<>();
       for(Song s : Contanst.list_songs){

           if(s.getTitleSearch().toLowerCase().startsWith(str.toLowerCase())){
               rs.add(s);
           }
       }

        if(str.length() == 0){
           rs.clear();
        }
        SongAlbumAdapter adapter = new SongAlbumAdapter(this, R.layout.song_album_item,rs);
        adapter.setAllCap(false);
        lvSearch.setAdapter(adapter);
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                edit.requestFocus();
                ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                        .showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);
                //SearchActivity.this.onBackPressed();
                MainActivity.playSongMain(rs.get(position));
                SearchActivity.this.finish();
            }
        });

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {

        Log.e("typing", KeyEvent.keyCodeToString(keyCode)+"");
        return super.onKeyUp(keyCode, event);
    }

    private void showKeyBoard(){
        if(scale == false){
            Animation anim = new ScaleAnimation(
                    1f, 1.1f, // Start and end values for the X axis scaling
                    1, 1.4f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            anim.setFillAfter(true); // Needed to keep the result of the animation
            anim.setDuration(300);



            scaleBack.startAnimation(anim);

            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    txt.setVisibility(View.INVISIBLE);
                    scale = true;
                }

                @Override
                public void onAnimationEnd(Animation animation) {

                    edit.setVisibility(View.VISIBLE);
                    img.setVisibility(View.VISIBLE);
                    imgDelete.setVisibility(View.VISIBLE);

                    edit.requestFocus();
                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                            .showSoftInput(edit, InputMethodManager.SHOW_IMPLICIT);

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


        }
    }
    private void hideKeyBoard(){
        if(scale == true)
        {
            Animation anim1 = new ScaleAnimation(
                    1.1f, 1f, // Start and end values for the X axis scaling
                    1.4f, 1f, // Start and end values for the Y axis scaling
                    Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                    Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
            anim1.setFillAfter(true); // Needed to keep the result of the animation
            anim1.setDuration(300);

            scaleBack.startAnimation(anim1);

            anim1.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                    edit.clearFocus();

                    ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(edit.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                    edit.setVisibility(View.INVISIBLE);
                    img.setVisibility(View.INVISIBLE);

                    imgDelete.setVisibility(View.INVISIBLE);

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    txt.setVisibility(View.VISIBLE);
                    scale = false;
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if(scale == true){
            hideKeyBoard();
        }else{
            super.onBackPressed();
        }

    }
}
