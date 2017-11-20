package com.phone.konka.accountingbook.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.R;

import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/20.
 */

public class DragGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<TagBean> mDatas;
    private LayoutInflater mInflater;

    private int movePos;
    private boolean isDrag;

    public DragGridAdapter(Context mContext, List<TagBean> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_draggridview, null);
            holder.tvTag = (TextView) convertView.findViewById(R.id.tv_dragGridView_item_tag);
            holder.imgTag = (ImageView) convertView.findViewById(R.id.img_dragGridView_item_tag);
            holder.imgOpera = (ImageView) convertView.findViewById(R.id.img_dragGridView_item_opera);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        if (isDrag && position == movePos) {
//            convertView.setVisibility(View.INVISIBLE);
        }
        TagBean data = mDatas.get(position);
        holder.tvTag.setText(data.getText());
        holder.imgTag.setImageBitmap(data.getIcon());
        return convertView;
    }

    public void exchangePosition(int oldPos, int newPos, boolean isDrag) {
        TagBean data = mDatas.get(oldPos);
        mDatas.remove(oldPos);
        mDatas.add(newPos, data);
        this.isDrag = isDrag;
        movePos = newPos;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tvTag;
        ImageView imgTag;
        ImageView imgOpera;
    }
}
