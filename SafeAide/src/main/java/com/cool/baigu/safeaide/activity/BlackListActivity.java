package com.cool.baigu.safeaide.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cool.baigu.safeaide.R;
import com.cool.baigu.safeaide.beans.BlackContacts;
import com.cool.baigu.safeaide.dao.Impl.BlackListDAO;
import com.cool.baigu.safeaide.utils.Utils;

import java.util.Collections;
import java.util.List;

/**
 * Created by baigu on 2017/9/7.
 */

public class BlackListActivity extends Activity {
    List<BlackContacts> list = null;
    ListView listView = null;
    BlackAdate blackAdate = null;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            loadLayer.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            blackAdate = new BlackAdate();
            listView.setAdapter(blackAdate);

        }
    };
    private LinearLayout loadLayer;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.black_list);

        initData();
        initView();
        initEvent();

    }

    /**
     * 初始化时间
     */
    private void initEvent() {
        Button n1 = (Button) findViewById(R.id.n1);
        n1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //设置对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(BlackListActivity.this);
                final AlertDialog dialog = builder.create();
                final View view = View.inflate(BlackListActivity.this, R.layout.alertdialog_blacklist, null);
                //适配2.2.3
                dialog.setView(view, 0, 0, 0, 0);
                dialog.show();
                //事件
                Button ok = (Button) view.findViewById(R.id.d1);
                Button cancel = (Button) view.findViewById(R.id.d2);

                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditText editText = (EditText) view.findViewById(R.id.e1);
                        RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.g1);
                        //获取数据
                        String phone = editText.getText().toString();
                        int id = radioGroup.getCheckedRadioButtonId();
                        String idStr = "";

                        Integer[] ints = {R.id.s1, R.id.s2, R.id.s3};

                        //校验数据
                        if (Utils.isEmpty(phone)) {
                            Toast.makeText(getApplicationContext(), "黑名单号码不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (Utils.indexOf(ints, id) == -1) {
                            Toast.makeText(getApplicationContext(), "请选择屏蔽类型", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        BlackListDAO blackListDAO = new BlackListDAO(getApplicationContext());
                        if(id == R.id.s1) {
                            idStr = "1";
                        } else if(id == R.id.s2) {
                            idStr = "2";
                        } else {
                            idStr = "0";
                        }
                        blackListDAO.add(new BlackContacts(0, phone, idStr));
                        //更新数据
                        list.add(0, new BlackContacts(0, phone, idStr));
                        blackAdate.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

    }

    /**
     * 初始化显示
     */
    private void initView() {
        listView = (ListView) findViewById(R.id.kk);
        loadLayer = (LinearLayout) findViewById(R.id.isload);
        loadLayer.setVisibility(View.VISIBLE);
        listView.setVisibility(View.GONE);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                BlackListDAO dao = new BlackListDAO(getApplicationContext());
                List<BlackContacts> list1 = dao.getList();
                Collections.reverse(list1);
                BlackListActivity.this.list = list1;
                handler.sendEmptyMessage(0);

            }
        }).start();
    }

    class BlackAdate extends BaseAdapter {

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                View blackList = View.inflate(getApplicationContext(), R.layout.item_black_contact, null);
                TextView i1 = (TextView) blackList.findViewById(R.id.i1);
                TextView i2 = (TextView) blackList.findViewById(R.id.i2);
                ImageView del = (ImageView) blackList.findViewById(R.id.imageView2);
                final ViewG g = new ViewG(i1, i2, del);
                blackList.setTag(g);
                convertView = blackList;

                del.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(BlackListActivity.this);
                        builder.setTitle("移除黑名单");
                        builder.setMessage("是否确认将该号码从黑名单移除");
                        builder.setNegativeButton("算了吧", null);
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                BlackListDAO listDAO = new BlackListDAO(getApplicationContext());
                                String s = list.get(position).getPhone();
                                listDAO.remove(new BlackContacts(0, s, ""));

                                list.remove(position);
                                blackAdate.notifyDataSetChanged();


                            }
                        });
                        builder.show();


                    }
                });

            }
            ViewG g = (ViewG) convertView.getTag();
            g.phone.setText(list.get(position).getPhone());
            if (list.get(position).getMode().equals("0")) {
                g.mode.setText("全部拦截");
            } else if (list.get(position).getMode().equals("1")) {
                g.mode.setText("电话拦截");
            } else {
                g.mode.setText("短信拦截");
            }

            return convertView;
        }
    }

    private class ViewG {
        TextView phone;
        TextView mode;
        ImageView del;

        ViewG(TextView phone, TextView mode, ImageView del) {
            this.phone = phone;
            this.mode = mode;
            this.del = del;
        }
    }

}
