package com.phone.konka.accountingbook.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.phone.konka.accountingbook.Bean.DayDetailBean;
import com.phone.konka.accountingbook.Bean.DetailTagBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.DoubleTo2Decimal;
import com.phone.konka.accountingbook.Utils.ImageLoader;
import com.phone.konka.accountingbook.View.CustomExpandableListview;
import com.phone.konka.accountingbook.View.DateTextView;
import com.phone.konka.accountingbook.View.LineCircleTextView;

import java.util.List;

/**
 * 内层ExpandableListView的适配器
 * <p>
 * <p>
 * Created by 廖伟龙 on 2017/12/12.
 */

public class ChildAdapter extends BaseExpandableListAdapter {


    /**
     * 上下文对象
     */
    private Context mContext;

    /**
     * 显示的数据
     */
    private List<DayDetailBean> mData;


    /**
     * 图片缓存加载器
     */
    private ImageLoader mCache;


    public ChildAdapter(Context mContext, List<DayDetailBean> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mCache = ImageLoader.getInstance(mContext);
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {

        return mData.get(groupPosition).getTagList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mData.get(groupPosition).getTagList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {


        GroupViewHolder holder;
        if (convertView == null) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_detail_two, null);
            holder.dateTextView = (DateTextView) convertView.findViewById(R.id.dv_two_date);
            holder.tvIn = (TextView) convertView.findViewById(R.id.tv_two_in);
            holder.tvOut = (TextView) convertView.findViewById(R.id.tv_two_out);
            holder.tvLeft = (TextView) convertView.findViewById(R.id.tv_two_left);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }


        /**
         * 判断当前是否处于onMeasure状态，是则直接返回
         * 直到onLayout后才设置具体显示
         * 减少多次onMeasure调用getView的影响
         */
        if (((CustomExpandableListview) parent).isOnMeasure())
            return convertView;

        Log.i("ddd", "22222-Group   GroupPos:  " + groupPosition);


        DayDetailBean bean = mData.get(groupPosition);
        holder.dateTextView.setDate(bean.getDate() + "");

        /**
         * 计算每日总的收支、结余情况
         */
        double in = 0, out = 0;
        for (DetailTagBean child : bean.getTagList()) {
            if (child.getMoney() > 0)
                in += child.getMoney();
            else
                out -= child.getMoney();
        }

        holder.tvIn.setText(DoubleTo2Decimal.doubleTo2Decimal(in));
        holder.tvOut.setText(DoubleTo2Decimal.doubleTo2Decimal(out));
        holder.tvLeft.setText(DoubleTo2Decimal.doubleTo2Decimal(in - out));

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {


        ChildViewHolder holder;
        if (convertView == null) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_detail_three, null);

            holder.lineCircleTextView = (LineCircleTextView) convertView.findViewById(R.id.img_detail_date);
            holder.imgIcon = (ImageView) convertView.findViewById(R.id.img_detail_tag);
            holder.tvTag = (TextView) convertView.findViewById(R.id.tv_detail_tag);
            holder.tvMoney = (TextView) convertView.findViewById(R.id.tv_detail_money);

            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }


        /**
         * 判断当前是否处于onMeasure状态，是则直接返回
         * 直到onLayout后才设置具体显示
         * 减少多次onMeasure调用getView的影响
         */
        if (((CustomExpandableListview) parent).isOnMeasure())
            return convertView;


        Log.i("ddd", "22222-Child   GroupPos:  " + groupPosition + "   childPos:   " + childPosition);

        DetailTagBean bean = mData.get(groupPosition).getTagList().get(childPosition);

        /**
         * 设置该item是否为第一个
         */
        if (childPosition == 0) {
            holder.lineCircleTextView.setFirst(true);
        } else {
            holder.lineCircleTextView.setFirst(false);
        }

        /**
         * 设置该item是否为最后一个
         */
        if (getChildrenCount(groupPosition) == childPosition + 1) {
            holder.lineCircleTextView.setEnd(true);
        } else {
            holder.lineCircleTextView.setEnd(false);
        }

        mCache.getBitmap(bean.getIconID(), holder.imgIcon);

        holder.tvTag.setText(bean.getTag());

        if (bean.getMoney() > 0) {
            holder.tvMoney.setText("收入+" + DoubleTo2Decimal.doubleTo2Decimal(bean.getMoney()));
        } else {
            holder.tvMoney.setText("支出" + DoubleTo2Decimal.doubleTo2Decimal(bean.getMoney()));
        }
        return convertView;
    }

    /**
     * 设置Child为可点击
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        DateTextView dateTextView;
        TextView tvIn;
        TextView tvOut;
        TextView tvLeft;
    }

    class ChildViewHolder {
        LineCircleTextView lineCircleTextView;
        ImageView imgIcon;
        TextView tvTag;
        TextView tvMoney;
    }
}
