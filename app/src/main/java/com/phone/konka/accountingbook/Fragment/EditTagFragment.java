package com.phone.konka.accountingbook.Fragment;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.phone.konka.accountingbook.Adapter.DragGridAdapter;
import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.R;
import com.phone.konka.accountingbook.View.DragGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/20.
 */

public class EditTagFragment extends Fragment {


    private DragGridView mGvMyTag;
    private DragGridView mGvRecomTag;
    private DragGridAdapter mMyTagAdapter;
    private DragGridAdapter mRecomTagAdapter;

    private List<TagBean> mMyTagList;
    private List<TagBean> mRecomTagList;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_tag, null);
        initView(view);
        return view;
    }

    private void initView(View view) {


        mMyTagList = new ArrayList<>();
        mRecomTagList = new ArrayList<>();


        for (int i = 0; i < 21; i++) {
            TagBean bean = new TagBean();
            bean.setText(i + "tag");
            bean.setIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            mMyTagList.add(bean);
            mRecomTagList.add(bean);
        }

        mGvMyTag = (DragGridView) view.findViewById(R.id.dgv_edit_mytag);
        mMyTagAdapter = new DragGridAdapter(getActivity(), mMyTagList);
        mMyTagAdapter.setOnClickListener(new DragGridAdapter.OnClickListener() {
            @Override
            public void onclick(int pos) {
                mRecomTagList.add(mMyTagList.get(pos));
                mMyTagList.remove(pos);
                mMyTagAdapter.notifyDataSetChanged();
                mRecomTagAdapter.notifyDataSetChanged();
            }
        });
        mGvMyTag.setAdapter(mMyTagAdapter);


        mGvRecomTag = (DragGridView) view.findViewById(R.id.dgv_edit_recomtag);
        mRecomTagAdapter = new DragGridAdapter(getActivity(), mRecomTagList);
        mRecomTagAdapter.setOnClickListener(new DragGridAdapter.OnClickListener() {
            @Override
            public void onclick(int pos) {
                mMyTagList.add(mRecomTagList.get(pos));
                mRecomTagList.remove(pos);
                mMyTagAdapter.notifyDataSetChanged();
                mRecomTagAdapter.notifyDataSetChanged();
            }
        });
        mGvRecomTag.setAdapter(mRecomTagAdapter);

    }

}
