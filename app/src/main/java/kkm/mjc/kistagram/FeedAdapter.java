package kkm.mjc.kistagram;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class FeedAdapter extends BaseAdapter {
    ArrayList<FeedItem> items = new ArrayList<>();

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void addItem(FeedItem item) {
        items.add(item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Context context = parent.getContext();
        final FeedItem feedItem = items.get(position);

        if (convertView == null) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.gridview_list_item, parent, false);

            ImageView image_img = convertView.findViewById(R.id.image_img);
            Glide.with(context).load(Uri.parse(feedItem.getStrImgUri())).apply(new RequestOptions().centerCrop()).into(image_img);
        }

        else {
            View view = new View(context);
            view = convertView;

            ImageView image_img = view.findViewById(R.id.image_img);
            Glide.with(context).load(Uri.parse(feedItem.getStrImgUri())).apply(new RequestOptions().centerCrop()).into(image_img);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context.getApplicationContext(), position + "입니다.", Toast.LENGTH_SHORT).show();



            }
        });

        return convertView;
    }
}
