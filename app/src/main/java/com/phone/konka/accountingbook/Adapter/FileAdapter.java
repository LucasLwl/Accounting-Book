package com.phone.konka.accountingbook.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phone.konka.accountingbook.R;

import java.io.File;

/**
 * Created by 廖伟龙 on 2018-1-8.
 */

public class FileAdapter extends BaseAdapter {


    private File[] mFiles;

    private Context mContext;

    private Bitmap mDirBitmap;

    private Bitmap mFileBitmap;

    public FileAdapter(Context mContext, File[] mFiles) {
        this.mFiles = mFiles;
        this.mContext = mContext;

        mDirBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_dir);
        mFileBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_file);
    }

    @Override
    public int getCount() {
        return mFiles.length;
    }

    @Override
    public Object getItem(int position) {
        return mFiles[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_file, null);
            holder = new ViewHolder();
            holder.imgFileIcon = (ImageView) convertView.findViewById(R.id.img_item_fileIcon);
            holder.tvFileName = (TextView) convertView.findViewById(R.id.tv_item_fileName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        File file = mFiles[position];
        if (file.isDirectory())
            holder.imgFileIcon.setImageBitmap(mDirBitmap);
        else
            holder.imgFileIcon.setImageBitmap(mFileBitmap);

        holder.tvFileName.setText(file.getName());

        return convertView;
    }


    public void setDatas(File[] files) {
        mFiles = files;
        notifyDataSetChanged();
    }

    class ViewHolder {
        ImageView imgFileIcon;
        TextView tvFileName;
    }
}
