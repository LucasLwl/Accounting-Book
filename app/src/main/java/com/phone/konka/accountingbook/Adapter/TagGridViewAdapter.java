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
 * Created by 廖伟龙 on 2017/11/21.
 */

public class TagGridViewAdapter extends BaseAdapter {


    private Context mContext;
    private List<TagBean> mList;
    private LayoutInflater mInflater;

    public TagGridViewAdapter(Context mContext, List<TagBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
            holder.tvText = (TextView) convertView.findViewById(R.id.tv_dragGridView_item_tag);
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.img_dragGridView_item_tag);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TagBean bean = mList.get(position);
        holder.tvText.setText(bean.getText());
//        holde

        return convertView;
    }

    class ViewHolder {
        TextView tvText;
        ImageView imgIcon;

    }
}
