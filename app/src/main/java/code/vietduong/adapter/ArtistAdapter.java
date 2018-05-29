package code.vietduong.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import code.vietduong.custom.CircleTransform;
import code.vietduong.data.Contanst;
import code.vietduong.model.entity.Album;
import code.vietduong.model.entity.Artist;
import code.vietduong.oneplayer.R;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder>{

    private Context mContext;
    private RecyclerArtistItemClickListener listener;



    public interface RecyclerArtistItemClickListener{
        void onItemClick(Artist artist);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ArtistAdapter(Context mContext) {

        this.mContext = mContext;
    }

    public void setOnArtistItemClickListener(RecyclerArtistItemClickListener listener) {
        this.listener = listener;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(Contanst.list_artists.size() > 0){
            return Contanst.list_artists.size();
        }
        return 0;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ArtistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Artist artist = Contanst.list_artists.get(position);
        holder.txtName.setText(artist.getName());
        Picasso.with(mContext).load(Contanst.list_artists.get(position).getPicture())
                .placeholder(R.drawable.noalbum_round_256)
                .transform(new CircleTransform(26))
                .resize(200, 200)
                .centerCrop()
                .error(R.drawable.noalbum_round_256)
                .into(holder.imgArtist);

        holder.bind(Contanst.list_artists.get(position), listener);

    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case


        TextView txtName;
        ImageView imgArtist;

        public ViewHolder(View v) {
            super(v);

            txtName = v.findViewById(R.id.txt_name_artist_item);
            imgArtist = v.findViewById(R.id.img_artist_item);
        }

        public void bind(final Artist artist, final RecyclerArtistItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onItemClick(artist);
                    }
                }
            });
        }
    }

}
