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
import com.phone.konka.accountingbook.Bean.DetailTagBean;
import com.phone.konka.accountingbook.Bean.MonthDetailBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.Utils.DoubleTo2Decimal;
import com.phone.konka.accountingbook.Utils.ProviderManager;
import com.phone.konka.accountingbook.Utils.ThreadPoolManager;

import org.xmlpull.v1.XmlSerializer;

import java.util.Calendar;
import java.util.List;

/**
 * 外层ExpandableListView的适配器
 * <p>
 * Created by 廖伟龙 on 2017/11/17.
 */

public class GroupAdapter extends BaseExpandableListAdapter {


    /**
     * 月份账单详情
     */
    private List<MonthDetailBean> mData;


    /**
     * 上下文
     */
    private Context mContext;


    /**
     * 布局加载
     */
    private LayoutInflater mInflater;


    /**
     * 弹出是否删除账单的PopupWindow
     */
    private PopupWindow mPopupWindow;


    /**
     * 线程池
     */
    private ThreadPoolManager mThreadPool;


    /**
     * Provider操作类
     */
    private ProviderManager mDataManager;


    /**
     * 记录内层ExpandableListView长按时，Group的位置
     * <p>
     * 初始化-1代表没进行长按
     */
    private int mGroupLongClickPos = -1;


    /**
     * 记录内层ExpandableListView长按时，Child的位置
     * 初始化-1代表没进行长按
     */
    private int mChildLongClickPos = -1;


    /**
     * 记录内层ExpandableListView长按时，内层ExpandableListView所属的外层Group位置
     * 初始化-1代表没进行长按
     */
    private int mGroupPosition = -1;


    private int mNowYear;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            notifyDataSetChanged();
        }
    };


    public GroupAdapter(Context mContext, List<MonthDetailBean> mData) {
        this.mData = mData;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
        mThreadPool = ThreadPoolManager.getInstance();

        mDataManager = new ProviderManager(mContext);

        mNowYear = Calendar.getInstance().get(Calendar.YEAR);
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }


    /**
     * 2、3层的显示交由内层的ExpandableListView处理，故返回1
     *
     * @param groupPosition
     * @return
     */
    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mData.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mData.get(groupPosition).getDayList().get(childPosition);
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
            holder.tvMonth = (TextView) convertView.findViewById(R.id.tv_detail_month);
            holder.tvIn = (TextView) convertView.findViewById(R.id.tv_detain_month_in);
            holder.tvOut = (TextView) convertView.findViewById(R.id.tv_detail_month_out);
            holder.tvLeft = (TextView) convertView.findViewById(R.id.tv_detail_month_left);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }

        MonthDetailBean monthData = mData.get(groupPosition);


        if (groupPosition != 0) {
            if (monthData.getYear() == mData.get(groupPosition - 1).getYear()) {
                holder.tvMonth.setText(monthData.getMonth() + "月");
            } else {
                holder.tvMonth.setText(monthData.getYear() + "年" + monthData.getMonth() + "月");
            }
        } else {
            if (monthData.getYear() == mNowYear) {
                holder.tvMonth.setText(monthData.getMonth() + "月");
            } else {
                holder.tvMonth.setText(monthData.getYear() + "年" + monthData.getMonth() + "月");
            }
        }

        holder.tvIn.setText(DoubleTo2Decimal.doubleTo2Decimal(monthData.getIn()));
        holder.tvOut.setText(DoubleTo2Decimal.doubleTo2Decimal(monthData.getOut()));
        holder.tvLeft.setText(DoubleTo2Decimal.doubleTo2Decimal(monthData.getIn() - monthData.getOut()));

        /**
         * 设置父ListView的Divider
         * 第一个不显示
         */
        if (groupPosition == 0)

        {
            holder.llHead.setVisibility(View.GONE);
        } else

        {
            holder.llHead.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {

        final ChildViewHolder holder;
        if (convertView == null) {

            holder = new ChildViewHolder();
            convertView = mInflater.inflate(R.layout.item_dedail_child, null);
            holder.elv = (ExpandableListView) convertView.findViewById(R.id.lv_child);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }

        List<DayDetailBean> data = mData.get(groupPosition).getDayList();
        ChildAdapter adapter = new ChildAdapter(mContext, data);
        holder.elv.setAdapter(adapter);


        /**
         * 删除第三层的账单，会导致重绘内外两层的ExpandableListView
         * 而内层的ExpandableListView再重绘时，会重新设置Adapter，
         * 导致保存了展开信息的ExpandableListConnector会被重新创建，失去了内层展开信息
         */

        /**
         * 根据长按时确定的位置信息，恢复删除后子ExpandableListView的展开
         */
        if (mGroupPosition == groupPosition && mGroupLongClickPos == childPosition) {
            holder.elv.expandGroup(mGroupLongClickPos);
            mGroupPosition = -1;
            mGroupLongClickPos = -1;
            mChildLongClickPos = -1;
        }

        /**
         * 给子ExpandableListView设置longClickListener
         *
         * 当长按的是外层GroupItem时，mChildLongClickPos的值为AdapterView.INVALID_POSITION
         */
        holder.elv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent1, View view, int position, long id) {
                int nPos = holder.elv.pointToPosition((int) view.getX(), (int) view.getY());
                if (nPos != AdapterView.INVALID_POSITION) {
                    long pos = holder.elv.getExpandableListPosition(nPos);

                    mChildLongClickPos = ExpandableListView.getPackedPositionChild(pos);
                    mGroupLongClickPos = ExpandableListView.getPackedPositionGroup(pos);
                    mGroupPosition = groupPosition;

                    /**
                     * 显示是否删除账单栏
                     */
                    if (mChildLongClickPos == AdapterView.INVALID_POSITION) {
                        showPopupWindow(parent, view);
                    } else {
                        showPopupWindow(parent, view);
                    }
                }
                return true;
            }
        });
        return convertView;
    }


    /**
     * 隐藏是否删除账单栏
     */
    public void dismissPopupWindow() {
        if (mPopupWindow != null && mPopupWindow.isShowing())
            mPopupWindow.dismiss();
    }

    /**
     * 显示是否删除账单栏
     *
     * @param parent
     * @param view
     */
    public void showPopupWindow(ViewGroup parent, View view) {

        /**
         * 如mPopupWindow为空，先创建
         */
        if (mPopupWindow == null) {

            /**
             * 创建PopupWindow显示的View
             */
            Button btn = new Button(mContext);
            btn.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            btn.setText("是否删除该账单");
            btn.setTextColor(mContext.getResources().getColor(R.color.item_text));
            btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            btn.setBackground(mContext.getResources().getDrawable(R.drawable.delete_account_bg));
            btn.setGravity(Gravity.CENTER);
            btn.setPadding(20, 0, 20, 0);
            btn.setLayoutParams(lp);

//            设置点击事件
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    /**
                     * 使用线程池来进行操作数据表
                     */
                    mThreadPool.execute(new Runnable() {
                        @Override
                        public void run() {

                            DayDetailBean dayBean = mData.get(mGroupPosition).getDayList().get(mGroupLongClickPos);

//                            根据长按情况，删除数据表中的账单详情信息
                            if (mChildLongClickPos == AdapterView.INVALID_POSITION) {
                                mDataManager.deleteData("year = ? and month = ? and day = ?",
                                        new String[]{dayBean.getYear() + "", dayBean.getMonth() + "", dayBean.getDate() + ""});
                            } else {
                                DetailTagBean detailBean = dayBean.getTagList().get(mChildLongClickPos);
                                mDataManager.deleteData("_id = ?",
                                        new String[]{detailBean.getId() + ""});
                            }

//                            从数据表中获取新的账单详情信息
                            mData = mDataManager.getDetailList();

//                            删除完成后通过Handler，在UIThread刷新ExpandableListView
                            mHandler.obtainMessage().sendToTarget();
                        }
                    });
                    dismissPopupWindow();
                }
            });

//            初始化PopupWindow
            mPopupWindow = new PopupWindow(btn, RelativeLayout.LayoutParams.WRAP_CONTENT, 150, true);
            mPopupWindow.setOutsideTouchable(true);
            mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
            mPopupWindow.setTouchable(true);
        }

        /**
         * 显示PopupWindow
         * 显示在长按的item下面
         * 若长按的item下面显示不下，则显示在长按item的上面
         */
        if (!mPopupWindow.isShowing()) {
            if (view.getBottom() + mPopupWindow.getHeight() > parent.getHeight())
                mPopupWindow.showAsDropDown(view, (view.getWidth() - mPopupWindow.getWidth()) / 2, -(view.getHeight() + mPopupWindow.getHeight()));
            else {
                mPopupWindow.showAsDropDown(view, (view.getWidth() - mPopupWindow.getWidth()) / 2, 0);
            }
        }
    }

    /**
     * 设置Child可点击为true
     *
     * @param groupPosition
     * @param childPosition
     * @return
     */
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
