package code.vietduong.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import code.vietduong.data.Contanst;
import code.vietduong.model.entity.Album;
import code.vietduong.model.entity.Song;
import code.vietduong.oneplayer.R;

/*
public class AlbumAdapter extends BaseAdapter {
    private Context mContext;

    public AlbumAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return Contanst.list_albums.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }
    static class ViewHolder{

        TextView txtTitle;
        ImageView imgAlbum;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        View item = convertView;
        if(item == null){
            viewHolder = new ViewHolder();
            item = ((Activity)mContext).getLayoutInflater().inflate(R.layout.album_item, null);
            viewHolder.imgAlbum = item.findViewById(R.id.img_album_item);
            viewHolder.txtTitle  = item.findViewById(R.id.txt_name_album_item);
            item.setTag(viewHolder);

        }
        else{
            viewHolder = (ViewHolder) item.getTag();
        }



        viewHolder.txtTitle.setText(Contanst.list_albums.get(position).getName());
        Picasso.with(mContext).load(Contanst.list_albums.get(position).getPicture())
                .placeholder(R.drawable.noalbum)
                .resize(200, 200)
                .centerCrop()
                .error(R.drawable.noalbum)
                .into(viewHolder.imgAlbum);

        return item;

    }


}*/
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder>{

    private Context mContext;
    private RecyclerAlbumItemClickListener listener;



    public interface RecyclerAlbumItemClickListener{
        void onItemClick(Album album);
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AlbumAdapter(Context mContext) {

        this.mContext = mContext;
    }

    public void setOnAlbumItemClickListener(RecyclerAlbumItemClickListener listener) {
        this.listener = listener;
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return Contanst.list_albums.size();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public AlbumAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_item, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Album album = Contanst.list_albums.get(position);
        holder.txtName.setText(album.getName());




        Picasso.with(mContext).load(Contanst.list_albums.get(position).getPicture())
                .placeholder(R.drawable.noalbum)
                .resize(200, 200)
                .centerCrop()
                .error(R.drawable.noalbum)
                .into(holder.imgAlbum);

        holder.bind(Contanst.list_albums.get(position), listener);

    }



    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case


        TextView txtName;
        ImageView imgAlbum;

        public ViewHolder(View v) {
            super(v);

            txtName = v.findViewById(R.id.txt_name_album_item);
            imgAlbum = v.findViewById(R.id.img_album_item);
        }

        public void bind(final Album album, final RecyclerAlbumItemClickListener listener){
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onItemClick(album);
                    }
                }
            });
        }
    }

}
