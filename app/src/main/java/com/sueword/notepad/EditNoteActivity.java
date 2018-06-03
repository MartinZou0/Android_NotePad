package com.sueword.notepad;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.sueword.notepad.Beans.Note;
import com.sueword.notepad.Utils.DBHelper;

public class EditNoteActivity extends AppCompatActivity {
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private EditText titleEdit;
    private EditText bodyEdit;

    private Button cancelButton;
    private String tagcolor;
    private int id;
    private int position;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_activity);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle b=getIntent().getExtras();
         id=b.getInt("id");
         position=b.getInt("position");
        titleEdit=findViewById(R.id.editTitle);
        bodyEdit=findViewById(R.id.editBody);
        cancelButton=findViewById(R.id.editCancel);
        dbHelper=new DBHelper(this);
        getDetail(id);
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
    public void getDetail(int id){
        db=dbHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery("select * from notes where id="+id,null);
        if(cursor.moveToFirst()){
            titleEdit.setText(cursor.getString(1));
            bodyEdit.setText(cursor.getString(2));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_edit,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id==R.id.ed_delete){
            db=dbHelper.getWritableDatabase();
            db.execSQL("delete from notes where id="+this.id);
            Message message=new Message();
            message.arg1=position;
            message.what=0x125;
            MainActivity.myHandler.sendMessage(message);
            finish();
        }

        if(id==R.id.ed_save){
            db=dbHelper.getWritableDatabase();
            String title=titleEdit.getText().toString();
            String body=bodyEdit.getText().toString();
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
            Date date=new Date(System.currentTimeMillis());
            String cdate=simpleDateFormat.format(date);
            db.execSQL("update notes set title=?,body=?,date=?,where id="+id,new String[]{title,body,cdate});
            Toast.makeText(EditNoteActivity.this,"修改成功!",Toast.LENGTH_SHORT).show();
            Note note=new Note(id,title,body,cdate,tagcolor);
            Message message=new Message();
            message.what=0x124;
            message.obj=note;
            message.arg1=position;
            MainActivity.myHandler.sendMessage(message);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
