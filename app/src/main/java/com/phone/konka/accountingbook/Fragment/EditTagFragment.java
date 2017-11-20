package com.phone.konka.accountingbook.Fragment;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.phone.konka.accountingbook.Adapter.DragGridAdapter;
import com.phone.konka.accountingbook.Bean.TagBean;
import com.phone.konka.accountingbook.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 廖伟龙 on 2017/11/20.
 */

public class EditTagFragment extends Fragment {

    private List<TagBean> mList;
    private GridView mGvTag;
    private DragGridAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_edit_tag, null);

        mList = new ArrayList<>();
        for (int i = 0; i < 21; i++) {
            TagBean bean = new TagBean();
            bean.setText(i + "tag");
            bean.setIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
            mList.add(bean);
        }

        mGvTag = (GridView) view.findViewById(R.id.dgv_edit_tag);
        mAdapter = new DragGridAdapter(getActivity(), mList);
        mGvTag.setAdapter(mAdapter);
        return view;
    }
}
