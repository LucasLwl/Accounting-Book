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

import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/20.
 */

public class DragGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<TagBean> mDatas;
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
     * 回调接口的响应
     */
    private OnClickListener onClickListener;

    private int mIndex;


    private final Bitmap DEL_BITMAP;
    private final Bitmap ADD_BITMAP;


    public DragGridAdapter(Context mContext, List<TagBean> mDatas, int index) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(mContext);

        mIndex = index;

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


        TagBean data = mDatas.get(position);
        holder.tvTag.setText(data.getText());
        holder.imgTag.setImageBitmap(data.getIcon());
        if (mIndex == 0) {
            holder.imgOpera.setImageBitmap(DEL_BITMAP);
        } else {
            holder.imgOpera.setImageBitmap(ADD_BITMAP);
        }

        holder.imgOpera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onclick(position);
            }
        });

        /**
         * 消除移动视图动画效果，避免显示错乱
         */
        convertView.clearAnimation();
        if (isDrag && dragPos == position) {
            convertView.setVisibility(View.INVISIBLE);
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
        TagBean data = mDatas.get(oldPos);
        mDatas.remove(oldPos);
        mDatas.add(newPos, data);
        this.isDrag = isDrag;
        dragPos = newPos;
        notifyDataSetChanged();
    }


    class ViewHolder {
        TextView tvTag;
        ImageView imgTag;
        ImageView imgOpera;
    }


    /**
     * 设置回调接口
     *
     * @param listener
     */
    public void setOnClickListener(OnClickListener listener) {
        this.onClickListener = listener;
    }

    /**
     * 回调接口，用于对外实现item的添加或者删除
     */
    public interface OnClickListener {
        void onclick(int pos);
    }
}
