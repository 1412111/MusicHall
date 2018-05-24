package code.vietduong.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import code.vietduong.data.Contanst;
import code.vietduong.model.entity.Song;
import code.vietduong.oneplayer.R;

public class SongPopUpAdapter extends ArrayAdapter<Song>{


    private Context context;
    private ArrayList<Song> listSong;

    public SongPopUpAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
    }



    public void setData(ArrayList<Song> listSong){
        this.listSong = listSong;
        for(Song s : listSong){
            add(s);
        }
        notifyDataSetChanged();
    }
    static class ViewHolder{

        TextView txtTitle;
        TextView txtSinger;
        ImageView imgAlbum;
    }
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            final ViewHolder viewHolder;

            View item = convertView;
            if(item == null){

                viewHolder = new ViewHolder();
                item = ((Activity)context).getLayoutInflater().inflate(R.layout.song_item, null);

                viewHolder.txtTitle = item.findViewById(R.id.txtSongTitle);
                viewHolder.txtSinger = item.findViewById(R.id.txtSongSinger);
                viewHolder.imgAlbum = item.findViewById(R.id.imgSong);

                item.setTag(viewHolder);
            }
            else{
                viewHolder = (ViewHolder) item.getTag();
            }

            // set data
            viewHolder.txtTitle.setText(listSong.get(position).getTitle());
            viewHolder.txtSinger.setText(listSong.get(position).getArtist());

            Picasso.with(context).load(listSong.get(position).getAlbumArtPath())
                    .placeholder(R.drawable.noalbum_100)
                    .resize(100, 100)
                    /*  .transform(new CircleTransform())*/
                    .centerCrop()
                    .error(R.drawable.noalbum_100)
                    .into(viewHolder.imgAlbum);


            return item;
    }

}