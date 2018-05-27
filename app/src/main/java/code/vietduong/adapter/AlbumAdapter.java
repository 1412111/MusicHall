package code.vietduong.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import code.vietduong.data.Contanst;
import code.vietduong.oneplayer.R;

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


}