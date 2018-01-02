package com.phone.konka.accountingbook.Adapter;

import android.content.Context;
import android.util.Log;
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
 * 添加账单选择字段的Adapter
 * <p>
 * Created by 廖伟龙 on 2017/11/21.
 */

public class TagGridViewAdapter extends BaseAdapter {


    private Context mContext;


    /**
     * 字段信息
     */
    private List<TagBean> mList;


    /**
     * 布局加载
     */
    private LayoutInflater mInflater;


    /**
     * 图片缓存加载器
     */
    private ImageLoader mCache;


    private int mTextNormalColor;


    private int mTextSelectedColor;


    /**
     * 当前选中的item
     */
    private int mSelected = 0;


    public TagGridViewAdapter(Context mContext, List<TagBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        mInflater = LayoutInflater.from(mContext);
        mCache = ImageLoader.getInstance(mContext);

        mTextNormalColor = mContext.getResources().getColor(R.color.item_text);
        mTextSelectedColor = mContext.getResources().getColor(R.color.item_selected);
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

        mCache.getBitmap(bean.getIconID(), holder.imgIcon);

//        设置选中item的字体颜色,和选中图标
        if (position == mSelected) {
            holder.imgSelected.setVisibility(View.VISIBLE);
            holder.tvText.setTextColor(mTextSelectedColor);
        } else {
            holder.imgSelected.setVisibility(View.GONE);
            holder.tvText.setTextColor(mTextNormalColor);
        }
        return convertView;
    }


    /**
     * 设置字段信息
     *
     * @param mList
     */
    public void setList(List<TagBean> mList) {
        this.mList = mList;
        mSelected = 0;
    }

    /**
     * 获取选中的位置
     *
     * @return
     */
    public int getSelected() {
        return mSelected;
    }


    /**
     * 设置选中的位置
     *
     * @param selected
     */
    public void setSelected(int selected) {
        mSelected = selected;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        TextView tvText;
        ImageView imgIcon;
        ImageView imgSelected;
    }
}
