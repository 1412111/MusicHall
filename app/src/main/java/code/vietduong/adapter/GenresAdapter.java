package code.vietduong.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import code.vietduong.data.Contanst;
import code.vietduong.model.entity.Genres;
import code.vietduong.model.entity.Song;
import code.vietduong.oneplayer.R;

public class GenresAdapter extends ArrayAdapter<Genres> {

    private Context context;
    private ArrayList<Genres> listGenres;

    public GenresAdapter(@NonNull Context context, int resource, ArrayList<Genres> data) {
        super(context, resource, data);
        this.context = context;
    }



    public void setData(ArrayList<Genres> listGenres){
        this.listGenres = listGenres;
        for(Genres g : listGenres){
            add(g);
            notifyDataSetChanged();
        }

    }
    static class ViewHolder{

        TextView txtSong1, txtSong2, txtSong3, txtGenresName;
        ImageView imgGenres;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        final ViewHolder viewHolder;

        View item = convertView;
        if(item == null){

            viewHolder = new ViewHolder();
            item = ((Activity)context).getLayoutInflater().inflate(R.layout.genres_item, null);

            viewHolder.txtSong1 = item.findViewById(R.id.txtSong1);
            viewHolder.txtSong2 = item.findViewById(R.id.txtSong2);
            viewHolder.txtSong3 = item.findViewById(R.id.txtSong3);
            viewHolder.txtGenresName = item.findViewById(R.id.txtGenresName);
            viewHolder.imgGenres = item.findViewById(R.id.img_genres_item);

            item.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) item.getTag();
        }

        Genres g = getItem(position);
        // set data
        Song s1 = null, s2 = null, s3 = null;
        if(g.getSongs().size() >= 1){
            s1 = g.getSongs().get(0);
        }
        if(g.getSongs().size() >= 2){
            s2 = g.getSongs().get(1);
        }
        if(g.getSongs().size() >= 3){
            s3 = g.getSongs().get(2);
        }

        if(s1!=null){
            viewHolder.txtSong1.setText("1  "+s1.getTitle());
        }
        else{
            viewHolder.txtSong1.setText("");
        }

        if(s2!=null){
            viewHolder.txtSong2.setText("2  "+s2.getTitle());
        }else{
            viewHolder.txtSong2.setText("");
        }

        if(s3!=null){
            viewHolder.txtSong3.setText("3  "+s3.getTitle());
        }else{
            viewHolder.txtSong3.setText("");
        }

        viewHolder.txtGenresName.setText(g.getName());

        Picasso.with(context).load(getItem(position).getPicture())
                .placeholder(R.drawable.noalbum)
                .resize(200, 200)
                .centerCrop()
                .error(R.drawable.noalbum)
                .into(viewHolder.imgGenres);
        return item;

    }


}

