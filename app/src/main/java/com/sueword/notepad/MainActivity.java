package com.sueword.notepad;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.sueword.notepad.Beans.Note;
import com.sueword.notepad.Utils.DBHelper;
import com.sueword.notepad.Utils.MyAdapter;
import com.sueword.notepad.Utils.OnItemCLickListener;

public class MainActivity extends AppCompatActivity {
    private List<Note> noteList=new ArrayList<>();
    private MyAdapter myAdapter;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private RecyclerView recyclerView;
    static Handler myHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        myHandler=new MyHandler(this);
        FloatingActionButton fab =  findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,CreateNewNoteActivity.class);
                startActivity(intent);
            }
        });
        recyclerView=findViewById(R.id.myRecyclerView);
        Context context=MainActivity.this;
        dbHelper=new DBHelper(context);
        db=dbHelper.getReadableDatabase();
        showNotes();
        myAdapter=new MyAdapter(this,noteList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        myAdapter.setOnItemCLickListener(new OnItemCLickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id=noteList.get(position).getId();
                Bundle b=new Bundle();
                b.putInt("id",id);
                b.putInt("position",position);
                Intent intent=new Intent(MainActivity.this,EditNoteActivity.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager=(SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView=(SearchView)menu.findItem(R.id.ab_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
    public void showNotes(){
        Cursor cursor=db.query("notes",null,null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
                int nid=cursor.getInt(0);
                String ntitle=cursor.getString(1);
                String nbody=cursor.getString(2);
                String date=cursor.getString(3);
                String tagcolor=cursor.getString(4);
                Note note=new Note(nid,ntitle,nbody,date,tagcolor);
                noteList.add(note);
            }while (cursor.moveToNext());
        }
    }
    public void updateView(){
        int count=myAdapter.getItemCount();
        count++;
        String scount=Integer.toString(count);
        Cursor cursor=db.rawQuery("select *from notes order by id desc limit 0,1",null);
        if(cursor.moveToFirst()){
                int nid=cursor.getInt(0);
                String ntitle=cursor.getString(1);
                String nbody=cursor.getString(2);
                String date=cursor.getString(3);
                String tagcolor=cursor.getString(4);
                Note note=new Note(nid,ntitle,nbody,date,tagcolor);
                noteList.add(note);
        }
        myAdapter.notifyDataSetChanged();

    }
    private static class MyHandler extends Handler {
        WeakReference<MainActivity> contentWeakReference;
        public MyHandler(MainActivity activity){
            contentWeakReference=new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            MainActivity activity=contentWeakReference.get();
            switch (msg.what){
                case 0x123:
                    if(activity!=null){
                        activity.updateView();
                    }
                    break;
                case 0x124:
                    activity.noteList.set(msg.arg1,(Note)msg.obj);
                    activity.myAdapter.notifyDataSetChanged();
                    break;
                case 0x125:
                    activity.noteList.remove(msg.arg1);
                    activity.myAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }
}
