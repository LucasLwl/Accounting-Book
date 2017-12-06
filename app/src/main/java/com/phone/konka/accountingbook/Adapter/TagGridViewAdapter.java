package com.phone.konka.accountingbook.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.ImageLoader;

import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/21.
 */

public class TagGridViewAdapter extends BaseAdapter {


    private Context mContext;
    private List<TagBean> mList;
    private LayoutInflater mInflater;

    private ImageLoader mCache;

    private int mSelected = 0;

    public void setList(List<TagBean> mList) {
        this.mList = mList;
    }

    public TagGridViewAdapter(Context mContext, List<TagBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = LayoutInflater.from(mContext);
        mCache = ImageLoader.getInstance(mContext);
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
            convertView = mInflater.inflate(R.layout.item_add_account, null);
            holder.tvText = (TextView) convertView.findViewById(R.id.tv_addAccount_tag);
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.img_addAccount_item);
            holder.imgSelected = (ImageView) convertView.findViewById(R.id.img_addAccount_item_selected);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TagBean bean = mList.get(position);
        holder.tvText.setText(bean.getText());
        holder.imgIcon.setImageBitmap(mCache.getBitmap(bean.getIconID()));

        if (position == mSelected) {
            holder.imgSelected.setVisibility(View.VISIBLE);
            holder.tvText.setTextColor(Color.parseColor("#FD9801"));
        } else {
            holder.imgSelected.setVisibility(View.GONE);
            holder.tvText.setTextColor(Color.parseColor("#555555"));
        }

        return convertView;
    }


    public int getSelected() {
        return mSelected;
    }

    public void setSelected(int selected) {
        mSelected = selected;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView tvText;
        ImageView imgIcon;
        ImageView imgSelected;
    }
}
