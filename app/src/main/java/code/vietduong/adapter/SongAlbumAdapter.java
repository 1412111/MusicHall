package code.vietduong.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import java.util.ArrayList;


import code.vietduong.model.entity.Song;
import code.vietduong.oneplayer.R;

public class SongAlbumAdapter extends ArrayAdapter<Song>
{

        private Context context;
        private boolean allCap = true;


        public SongAlbumAdapter(@NonNull Context context,int resource,ArrayList<Song> data){
                super(context,resource,data);

                this.context=context;
        }



        static class ViewHolder {

            TextView txtTitle;
            TextView txtTime;
            TextView txtPosition;
        }

    public boolean isAllCap() {
        return allCap;
    }

    public void setAllCap(boolean allCap) {
        this.allCap = allCap;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder;

        View v = convertView;
        if (v == null) {

            viewHolder = new ViewHolder();
            v = ((Activity) context).getLayoutInflater().inflate(R.layout.song_album_item, null);

            viewHolder.txtTitle = v.findViewById(R.id.txtSongAlbumTitle);
            viewHolder.txtTime = v.findViewById(R.id.txtSongAlbumTime);
            viewHolder.txtPosition = v.findViewById(R.id.txtSongAlbumPosition);

            v.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) v.getTag();
        }

        Song s = getItem(position);
        viewHolder.txtTitle.setAllCaps(allCap);
        viewHolder.txtTitle.setText(s.getTitle());
        viewHolder.txtTime.setText(s.getDuration());

        long duration = Integer.parseInt(s.getDuration());
        duration/=1000;

        if((duration%60)<10){
            viewHolder.txtTime.setText(duration/60+":"+"0"+duration%60);
        }else {
            viewHolder.txtTime.setText(duration / 60 + ":" + duration % 60);
        }


        viewHolder.txtPosition.setText(1+position+"");
        return v;

    }
}

