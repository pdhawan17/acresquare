package com.acresqaure.acresqaure.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.acresqaure.acresqaure.Models.ImageInfo;
import com.acresqaure.acresqaure.R;

import java.util.ArrayList;

/**
 * Created by parasdhawan on 05/03/16.
 */
public class ImagesViewAdapter extends BaseAdapter {

    ArrayList<ImageInfo> images =new ArrayList<>();
    LayoutInflater layoutInflater;

    public ImagesViewAdapter(ArrayList<ImageInfo> bitmapArrayList,Context context){
        this.images =bitmapArrayList;
        this.layoutInflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public Object getItem(int i) {
        return images.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view=convertView;
        ViewHolder viewHolder;

        if(convertView==null){

            view = layoutInflater.inflate(R.layout.list_item_images,null);

            viewHolder =new ViewHolder();
            viewHolder.imageView= (ImageView)view.findViewById(R.id.ivImage);
            view.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)view.getTag();
        }

        ImageInfo imageInfo= images.get(i);

        viewHolder.imageView.setImageBitmap(imageInfo.getBitmap());

        return view;
    }

    public class ViewHolder{
        ImageView imageView;
    }
}
