package com.phone.konka.accountingbook.Adapter;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.phone.konka.accountingbook.Bean.DayDetailBean;
import com.phone.konka.accountingbook.Bean.MonthDetailBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.DBOperator;
import com.phone.konka.accountingbook.Utils.DoubleTo2Decimal;
import com.phone.konka.accountingbook.Utils.ThreadPoolManager;

import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/17.
 */

public class GroupAdapter extends BaseExpandableListAdapter {


    /**
     * 月份账单详情
     */
    private List<MonthDetailBean> mDatas;


    /**
     * 上下文
     */
    private Context mContext;

    /**
     * 布局加载
     */
    private LayoutInflater mInflater;

    private PopupWindow mPopupWindow;

    private ThreadPoolManager mThreadPool;

    private DBOperator mDBOperator;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ChildAdapter adapter = (ChildAdapter) msg.obj;
            adapter.notifyDataSetChanged();
//            notifyDataSetChanged();
        }
    };


    public GroupAdapter(Context mContext, List<MonthDetailBean> mDatas) {
        this.mDatas = mDatas;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        mThreadPool = ThreadPoolManager.getInstance();
        mDBOperator = new DBOperator(mContext);
    }

    @Override
    public int getGroupCount() {
        return mDatas.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDatas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mDatas.get(groupPosition).getDayList().get(childPosition);
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
            convertView = mInflater.inflate(R.layout.item_detail_one, null);
            holder.llHead = convertView.findViewById(R.id.view_divider);
            holder.tvMonth = (TextView) convertView.findViewById(R.id.tv_detail_moon);
            holder.tvIn = (TextView) convertView.findViewById(R.id.tv_detain_moon_in);
            holder.tvOut = (TextView) convertView.findViewById(R.id.tv_detail_moon_out);
            holder.tvLeft = (TextView) convertView.findViewById(R.id.tv_detail_moon_left);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        MonthDetailBean moonData = mDatas.get(groupPosition);

        holder.tvMonth.setText(moonData.getMonth() + "月");
        holder.tvIn.setText(DoubleTo2Decimal.doubleTo2Decimal(moonData.getIn()));
        holder.tvOut.setText(DoubleTo2Decimal.doubleTo2Decimal(moonData.getOut()));
        holder.tvLeft.setText(DoubleTo2Decimal.doubleTo2Decimal(moonData.getIn() - moonData.getOut()));

        /**
         * 设置父ListView的Divider
         */
        if (groupPosition == 0) {
            holder.llHead.setVisibility(View.GONE);
        } else {
            holder.llHead.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {

        final ChildViewHolder holder;
        if (convertView == null) {

            holder = new ChildViewHolder();
            convertView = mInflater.inflate(R.layout.chila_expandable, null);
            holder.elv = (ExpandableListView) convertView.findViewById(R.id.lv_child);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        final List<DayDetailBean> data = mDatas.get(groupPosition).getDayList();

        final ChildAdapter adapter = new ChildAdapter(mContext, data);

        holder.elv.setAdapter(adapter);


        holder.elv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent1, View view, int position, long id) {
                int npos = holder.elv.pointToPosition((int) view.getX(), (int) view.getY());
                if (npos != AdapterView.INVALID_POSITION) {
                    long pos = holder.elv.getExpandableListPosition(npos);
                    int childPos = ExpandableListView.getPackedPositionChild(pos);
                    int groupPos = ExpandableListView.getPackedPositionGroup(pos);
                    if (childPos == AdapterView.INVALID_POSITION) {
                        showPopupWindow(parent, view, adapter, mDatas, groupPosition, groupPos, childPos);
                    } else {
                        showPopupWindow(parent, view, adapter, mDatas, groupPosition, groupPos, childPos);
                    }
                }
                return true;
            }
        });
        return convertView;
    }

    private void dismissPopupWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
    }

    private void showPopupWindow(ViewGroup parent, View view, final ChildAdapter adapter, final List<MonthDetailBean> mData, final int groupPosition, final int groupPos, final int childPos) {


        Button btn = new Button(mContext);
        btn.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        btn.setText("是否删除该账单");
        btn.setTextColor(mContext.getResources().getColor(R.color.item_text));
        btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        btn.setGravity(Gravity.CENTER);
        btn.setPadding(20, 0, 20, 0);
        btn.setLayoutParams(lp);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mThreadPool.execute(new Runnable() {
                    @Override
                    public void run() {

                        if (childPos == AdapterView.INVALID_POSITION) {
                            Log.i("ddd", mData.get(groupPos).getYear() + "  " + mData.get(groupPos).getMonth() + "  " + mData.get(groupPosition).getDayList().get(groupPos).getDate() + "");
                            mDBOperator.delete("account", "year = ? and month = ? and day = ?",
                                    new String[]{mData.get(groupPosition).getDayList().get(groupPos).getYear() + "", mData.get(groupPosition).getDayList().get(groupPos).getMonth() + "", mData.get(groupPosition).getDayList().get(groupPos).getDate() + ""});
                            mData.get(groupPosition).getDayList().remove(groupPos);
                            if (mData.get(groupPosition).getDayList().size() == 0)
                                mData.remove(groupPosition);
                        } else {
                            mDBOperator.delete("account", "_id = ?",
                                    new String[]{mData.get(groupPosition).getDayList().get(groupPos).getTagList().get(childPos).getId() + ""});
                            mData.get(groupPosition).getDayList().get(groupPos).getTagList().remove(childPos);
                            if (mData.get(groupPosition).getDayList().get(groupPos).getTagList().size() == 0)
                                mData.get(groupPosition).getDayList().remove(groupPos);
                        }
                        Message msg = mHandler.obtainMessage();
                        msg.obj = adapter;
                        msg.sendToTarget();
                    }
                });
                dismissPopupWindow();
            }
        });

        if (mPopupWindow == null) {
            mPopupWindow = new PopupWindow(RelativeLayout.LayoutParams.WRAP_CONTENT, 150);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setTouchable(true);
        }

        mPopupWindow.setContentView(btn);

        if (!mPopupWindow.isShowing()) {
            Log.i("ddd", view.getBottom() + " " + mPopupWindow.getHeight() + " " + parent.getHeight() + "  ");
            if (view.getBottom() + mPopupWindow.getHeight() > parent.getHeight())
                mPopupWindow.showAsDropDown(view, (view.getWidth() - mPopupWindow.getWidth()) / 2, -(view.getHeight() + mPopupWindow.getHeight()));
            else {
                mPopupWindow.showAsDropDown(view, (view.getWidth() - mPopupWindow.getWidth()) / 2, 0);
            }
        }
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

class GroupViewHolder {
    View llHead;
    TextView tvMonth;
    TextView tvIn;
    TextView tvOut;
    TextView tvLeft;
}

class ChildViewHolder {

    ExpandableListView elv;

}
