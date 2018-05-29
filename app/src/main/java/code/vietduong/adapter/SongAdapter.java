package code.vietduong.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.futuremind.recyclerviewfastscroll.SectionTitleProvider;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.util.ArrayList;

import code.vietduong.custom.CircleTransform;
import code.vietduong.data.Contanst;
import code.vietduong.model.entity.Song;
import code.vietduong.oneplayer.R;

/**
 * Created by Codev on 30/01/2018.
 */
/*
public class SongAdapter extends ArrayAdapter<Song>{


    private Context context;
    private ArrayList<Song> listSong;

    public SongAdapter(@NonNull Context context, int resource) {
        super(context, resource);
        this.context = context;
    }



    public void setData(ArrayList<Song> listSong){
        this.listSong = listSong;
        for(Song s : listSong){
            add(s);
        }
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
        viewHolder.txtTitle.setText(getItem(position).getTitle());
        viewHolder.txtSinger.setText(getItem(position).getArtist());

        Picasso.with(getContext()).load(getItem(position).getAlbumArtPath())
                .placeholder(R.drawable.rounded_corners)
                .resize(100, 100)
                */
/*.transform(new CircleTransform())*//*

                .centerCrop()
                .error(R.drawable.rounded_corners)
                .into(viewHolder.imgAlbum);


        return item;

    }
}
*/
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> implements SectionTitleProvider {

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
    public SongAdapter(Context mContext) {

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
    public SongAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item, parent, false);
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
        holder.txtSinger.setText(song.getArtist());

        long duration = Integer.parseInt(song.getDuration());

        duration/=1000;

        if((duration%60)<10){
            holder.txtTime.setText(duration/60+":"+"0"+duration%60);
        }else {
            holder.txtTime.setText(duration / 60 + ":" + duration % 60);
        }

        Picasso.with(mContext).load(Contanst.list_songs.get(position).getAlbumArtPath())
                .placeholder(R.drawable.noalbum_round_256)
                .resize(100, 100)
                .transform(new CircleTransform(13))
                .centerCrop()
                .error(R.drawable.noalbum_round_256)
                .into(holder.imgAlbum);

        holder.bind(Contanst.list_songs.get(position), listener);

    }

    /* class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);

            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);

            paint.setAntiAlias(true);

            paint.setStyle(Paint.Style.STROKE);

            float r = size / 2f;

            paint.setStrokeWidth((r-13));

            canvas.drawCircle(r, r, r-(r-13)/2, paint);


            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }
    }*/


    public static class ViewHolder extends RecyclerView.ViewHolder{
        // each data item is just a string in this case


        TextView txtTitle;
        TextView txtSinger;
        TextView txtTime;
        ImageView imgAlbum;

        public ViewHolder(View v) {
            super(v);
            txtTitle = v.findViewById(R.id.txtSongTitle);
            txtSinger = v.findViewById(R.id.txtSongSinger);
            txtTime = v.findViewById(R.id.txtTime);
            imgAlbum = v.findViewById(R.id.imgSong);
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
