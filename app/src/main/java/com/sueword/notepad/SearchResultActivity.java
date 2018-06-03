package com.sueword.notepad;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.sueword.notepad.Beans.Note;
import com.sueword.notepad.Utils.DBHelper;
import com.sueword.notepad.Utils.MyAdapter;
import com.sueword.notepad.Utils.OnItemCLickListener;

public class SearchResultActivity extends AppCompatActivity {
    private List<Note> noteList=new ArrayList<>();
    private MyAdapter myAdapter;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);
        String SearchContent=getIntent().getStringExtra(SearchManager.QUERY);
        recyclerView=findViewById(R.id.myRecyclerView);
        Context context=SearchResultActivity.this;
        dbHelper=new DBHelper(context);
        db=dbHelper.getReadableDatabase();
        showNotes(SearchContent);
        myAdapter=new MyAdapter(this,noteList);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        myAdapter.setOnItemCLickListener(new OnItemCLickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int id=noteList.get(position).getId();
                Bundle b=new Bundle();
                b.putInt("id",id);
                b.putInt("position",position);
                Intent intent=new Intent(SearchResultActivity.this,EditNoteActivity.class);
                intent.putExtras(b);
                startActivity(intent);
                finish();
            }
        });
    }
    public void showNotes(String str){
        Cursor cursor=db.rawQuery("select * from notes where title like '%"+str+"%'",null);
        if(cursor.moveToFirst()){
            do {
                int nid=cursor.getInt(0);
                String ntitle=cursor.getString(1);
                String nbody=cursor.getString(2);
                String date=cursor.getString(3);
                String tag=cursor.getString(4);
                Note note=new Note(nid,ntitle,nbody,date,tag);
                noteList.add(note);
            }while (cursor.moveToNext());
        }
    }
}
