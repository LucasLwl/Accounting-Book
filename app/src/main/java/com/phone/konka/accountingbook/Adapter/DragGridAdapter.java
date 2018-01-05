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

import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.ImageLoader;

import java.util.List;

/**
 * 可拖动GridView的适配器
 * <p>
 * Created by 廖伟龙 on 2017/11/20.
 */

public class DragGridAdapter extends BaseAdapter {


    /**
     * 上下文
     */
    private Context mContext;


    /**
     * 数据
     */
    private List<TagBean> mDatas;


    /**
     * 布局加载
     */
    private LayoutInflater mInflater;


    /**
     * item被拖动到的新位置，用于设置Visibility
     */
    private int dragPos;


    /**
     * 标志位，是否被拖动
     */
    private boolean isDrag;


    /**
     * 标志是已有字段,还是推荐字段
     */
    private int mIndex;


    /**
     * 删除小图标
     */
    private final Bitmap DEL_BITMAP;


    /**
     * 添加小图标
     */
    private final Bitmap ADD_BITMAP;


    /**
     * 图片缓存加载器
     */
    private ImageLoader mImgLoader;


    public DragGridAdapter(Context mContext, List<TagBean> mDatas, int index) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);

        mIndex = index;

        mImgLoader = ImageLoader.getInstance(mContext);

        DEL_BITMAP = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_del_tag);
        ADD_BITMAP = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.icon_add_tag);
    }

    public void setList(List<TagBean> mDatas) {
        this.mDatas = mDatas;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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


        TagBean bean = mDatas.get(position);
        holder.tvTag.setText(bean.getText());

        mImgLoader.getBitmap(bean.getIconID(), holder.imgTag);
        if (mIndex == 0) {
            holder.imgOpera.setImageBitmap(DEL_BITMAP);
        } else {
            holder.imgOpera.setImageBitmap(ADD_BITMAP);
        }

        /**
         * 消除移动视图动画效果，避免显示错乱
         */
        convertView.clearAnimation();
        if (isDrag && dragPos == position) {
            convertView.setVisibility(View.INVISIBLE);
            isDrag = false;
        } else {
            convertView.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    /**
     * 修改拖动后List的数据
     *
     * @param oldPos 拖动前的位置
     * @param newPos 拖动后的位置
     * @param isDrag 是否为拖动状态
     */
    public void exchangePosition(int oldPos, int newPos, boolean isDrag) {
        mDatas.add(newPos, mDatas.remove(oldPos));
        this.isDrag = isDrag;
        dragPos = newPos;
        notifyDataSetChanged();
    }


    class ViewHolder {
        TextView tvTag;
        ImageView imgTag;
        ImageView imgOpera;
    }

}
