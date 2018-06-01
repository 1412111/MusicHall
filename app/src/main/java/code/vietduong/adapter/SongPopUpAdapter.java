package code.vietduong.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import code.vietduong.data.Contanst;
import code.vietduong.model.entity.Song;
import code.vietduong.oneplayer.R;

public class SongPopUpAdapter extends RecyclerView.Adapter<SongPopUpAdapter.ViewHolder> implements SectionTitleProvider {

    private Context mContext;
    private RecyclerItemClickListener listener;

    @Override
    public String getSectionTitle(int position) {
        return Contanst.list_songs.get(position).getTitle().substring(0, 1);
    }

    public interface RecyclerItemClickListener{
        void onItemClick(Song song);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public SongPopUpAdapter(Context mContext) {

        this.mContext = mContext;
    }

    public void setOnItemClickListener(RecyclerItemClickListener listener) {
        this.listener = listener;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return Contanst.list_songs.size();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SongPopUpAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_popup_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Song song = Contanst.list_songs.get(position);
        holder.txtTitle.setText(song.getTitle());
        holder.txtPosition.setText(position+1+"");

        long duration = Integer.parseInt(song.getDuration());
        duration/=1000;

        if((duration%60)<10){
            holder.txtTime.setText(duration/60+":"+"0"+duration%60);
        }else {
            holder.txtTime.setText(duration / 60 + ":" + duration % 60);
        }


        holder.bind(Contanst.list_songs.get(position), listener);

    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case


        TextView txtTitle;
        TextView txtTime;
        TextView txtPosition;

        public ViewHolder(View v) {
            super(v);
            txtTitle = v.findViewById(R.id.txtSongPopUpTitle);
            txtTime = v.findViewById(R.id.txtPopUpTime);
            txtPosition = v.findViewById(R.id.txtPopUpPosition);

        }

        public void bind(final Song song, final RecyclerItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onItemClick(song);
                    }
                }
            });
        }
    }

}