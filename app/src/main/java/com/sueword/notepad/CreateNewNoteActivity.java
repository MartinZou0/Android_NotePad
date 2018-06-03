package com.sueword.notepad;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sueword.notepad.Utils.DBHelper;

public class CreateNewNoteActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private EditText titleEdit;
    private EditText bodyEdit;

    private Button cancelButton;
    private String colortag;
    private ImageView tagimageview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.createnotelayout);
        titleEdit=findViewById(R.id.editTitle);
        bodyEdit=findViewById(R.id.editBody);
        cancelButton=findViewById(R.id.editCancel);
        tagimageview=findViewById(R.id.tagimage);
        dbHelper=new DBHelper(this);
        db=dbHelper.getWritableDatabase();
        bindListening();
    }
    public void bindListening(){

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_create,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.create_save){
            String title=titleEdit.getText().toString();
            String body=bodyEdit.getText().toString();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            Date date=new Date(System.currentTimeMillis());
            String cdate=simpleDateFormat.format(date);
            db.execSQL("insert into notes(title,body,date,tagcolor) values('"+title+"','"+body+"','"+cdate+"','"+colortag+"')");
            Toast.makeText(CreateNewNoteActivity.this,"添加成功!",Toast.LENGTH_SHORT).show();
            MainActivity.myHandler.sendEmptyMessage(0x123);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCheckTag(View v){
        ImageView img = (ImageView) v;
        colortag = img.getTag() + "";
    }
}
