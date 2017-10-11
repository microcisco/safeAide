package com.cool.baigu.safeaide.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cool.baigu.safeaide.R;
import com.cool.baigu.safeaide.dao.Impl.TeleCommon;

/**
 * Created by baigu on 2017/9/18.
 */

public class Tool2 extends Activity {

    private TeleCommon teleCommon;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tool2);
        teleCommon = new TeleCommon(Tool2.this.getFilesDir().getAbsolutePath() + "/commonnum.db");

        initView();
        initEvent();
    }

    /**
     * 初始化事件
     */
    private void initEvent() {
        ExpandableListView expListView = (ExpandableListView) findViewById(R.id.expListView);
        expListView.setAdapter(new ExpListAda());

    }

    /**
     * 初始化显示View
     */
    private void initView() {

    }


    class ExpListAda extends BaseExpandableListAdapter {
        @Override
        public int getGroupCount() {
            return teleCommon.getBigNum();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return teleCommon.getChildNum(groupPosition + 1);
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            TextView textView = new TextView(Tool2.this);
            textView.setText(teleCommon.getBigName(groupPosition + 1));

            return textView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            TextView textView = new TextView(Tool2.this);
            textView.setText(teleCommon.getChildItems(groupPosition + 1, childPosition + 1));

            return textView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }

}
