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

        TextView txtTitle;
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

            viewHolder.txtTitle = item.findViewById(R.id.txt_name_genres_item);
            viewHolder.imgGenres = item.findViewById(R.id.img_genres_item);

            item.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) item.getTag();
        }

        // set data
        viewHolder.txtTitle.setText(getItem(position).getName());


        Picasso.with(context).load(getItem(position).getPicture())
                .placeholder(R.drawable.noalbum)
                .resize(200, 200)
                .centerCrop()
                .error(R.drawable.noalbum)
                .into(viewHolder.imgGenres);
        return item;

    }
}

