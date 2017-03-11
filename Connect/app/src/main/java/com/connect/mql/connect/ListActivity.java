package com.connect.mql.connect;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.datebase.mql.connect.ConnectDbhelp;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    public static final String CONTENT_NAME="ListActivity_name";
    public static final String CONTENT_TEL="ListActivity_tel";
    public static ConnectDbhelp dbHelper;
    private RecyclerView mRecycleView;
    private ConnectAdapter mAdapter;
    private List<String> names;
    private List<String> tels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        dbHelper=new ConnectDbhelp(this,"Connects.db",null,1);
        dbHelper.getWritableDatabase();//打开数据库，如果没有数据库则创建

        mRecycleView=(RecyclerView)findViewById(R.id.ConnectList);
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter=new ConnectAdapter();
        mRecycleView.setAdapter(mAdapter);

        names=new ArrayList<>();
        tels=new ArrayList<>();

    }
    //此类从数据库获取数据
    private void initdata(){
        if(names!=null){
            names.clear();
            tels.clear();
        }
        SQLiteDatabase db=dbHelper.getWritableDatabase();//打开数据库
        Cursor cursor=db.query("book",null,null,null,null,null,null);//读取数据库中所有的数据
        if(cursor.moveToFirst()){
            do {
                String name=cursor.getString(cursor.getColumnIndex("name"));//通过列名获取数据
                names.add(name);
                String tel=cursor.getString(cursor.getColumnIndex("tel"));
                tels.add(tel);
            }while (cursor.moveToNext());
        }
        cursor.close();
    }
    class ConnectAdapter extends RecyclerView.Adapter<ConnectAdapter.MyViewHolder>{
        //此类绑定item视图
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            MyViewHolder holder=new MyViewHolder(LayoutInflater.from(ListActivity.this).inflate(
                    R.layout.content_item,parent,false
            ));
            return holder;
        }

        //此类用来给item分配数据
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.name.setText(names.get(position));
            holder.tel.setText(tels.get(position));
        }

        @Override
        public int getItemCount() {
            return names.size();
        }
        //此类用来具体部署item
        class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private TextView name;
            private TextView tel;
            public MyViewHolder(View itemView) {
                super(itemView);
                name=(TextView)itemView.findViewById(R.id.name);
                tel=(TextView)itemView.findViewById(R.id.tel);
                itemView.setOnClickListener(this);//实现item的点击效果
            }

            @Override
            public void onClick(View view) {//实现item的点击事件
                Intent intent=new Intent(ListActivity.this,TelActivity.class);//显式Intent
                intent.putExtra(CONTENT_NAME,name.getText());//将name绑定到Intent上
                intent.putExtra(CONTENT_TEL,tel.getText());//将tel绑定到Intent上
                startActivity(intent);//开始意图
            }
        }
    }

    @Override
    protected void onResume() {//每次Activity出现在最顶层会调用onResume方法
        super.onResume();
        initdata();
        mAdapter.notifyDataSetChanged();//更新RecycleView
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.topmenu,menu);//绑定工具栏菜单
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.addmenu){//实现工具栏菜单项的点击效果
            Intent intent=new Intent(this,AddActivity.class);//定义显式Intent
            startActivity(intent);//开始此意图
        }
        return super.onOptionsItemSelected(item);
    }
}