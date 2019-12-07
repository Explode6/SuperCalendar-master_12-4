package com.hqyxjy.ldf.supercalendar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class NotepadActivity extends AppCompatActivity {
    private EditText inputEvent;
    private Button add;
    private RecyclerView eventRecyclerView;
    private TextView title;
    private List<Event>eventList = new ArrayList<>();
    private EventAdapter eventAdapter;
    private String backEvent;
    private int isSaved;
    private String date;
    private MyDatabaseHelper dbHelper;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                     backEvent = data.getStringExtra("backContent");
                     isSaved = data.getIntExtra("isSaved",0);
                     int backPosition = data.getIntExtra("position",0);
                     if(isSaved == 1){
                         editDb(date,eventList.get(backPosition).getIdentifier(),backEvent);
//                对数据库内容进行修改
                         eventList.set(backPosition,new Event(backEvent));
                         eventAdapter.notifyItemChanged(backPosition);
                         eventRecyclerView.scrollToPosition(eventList.size()-1);
                     }
                }
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try);
        add = (Button)findViewById(R.id.add);
        inputEvent = (EditText)findViewById(R.id.input_event);
        eventRecyclerView = (RecyclerView)findViewById(R.id.today_all_event);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        eventRecyclerView.setLayoutManager(layoutManager);
        Intent intent = getIntent();
        date = intent.getStringExtra("clickDay");
        title = (TextView)findViewById(R.id.title_text);
        title.setText(date.substring(0,4)+"年"+date.substring(4,6)+"月"+date.substring(6)+"日");
        dbHelper = new MyDatabaseHelper(this,"SuperCalendar.db",null,1);
       Event eventArray[] = initArray();
//     从数据库中提取数据并存入一个Event数组中，返回这个数组
        int length = 0;
        if(eventArray[0].getIdentifier()!= Event.NOTHAVE) {
            for (int i = 0; eventArray[i].getIdentifier() != Event.NOTHAVE; i++)
                length++;
            eventArray = sort(eventArray, length);
//       将数组按照id从小到大进行排序
            for (int i = 0; i < length; i++)
                eventList.add(eventArray[i]);
        }
//      遍历数组将数组中元素添加到eventList中
        eventAdapter = new EventAdapter(eventList);
        eventRecyclerView.setAdapter(eventAdapter);
        eventAdapter.setOnItemClickListener(new EventAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, String content) {
                Intent intent = new Intent(NotepadActivity.this,EditActivity.class);
                intent.putExtra("event",content);
                intent.putExtra("position",position);
                startActivityForResult(intent,1);
            }

        });
        eventAdapter.setOnItemLongClickListener(new EventAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {

                PopupMenu popupMenu = new PopupMenu(NotepadActivity.this,view);
                popupMenu.getMenuInflater().inflate(R.menu.main,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.remove_item:{
                                removeFromDb(date,eventList.get(position).getIdentifier());
                                eventList.remove(position);
                                eventAdapter.notifyItemRemoved(position);
                                eventAdapter.notifyItemRangeChanged(position, eventList.size()-position+1);
                                break;
                            }
                            case R.id.top:{
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                Event getEvent = eventList.get(position);
                                ContentValues values = new ContentValues();
                                values.put("id",eventList.get(0).getIdentifier()-1);
                                db.update("Notepad",values,"date = ? and id = ?",
                                        new String[]{date,getEvent.getIdentifier()+""});
                                Event copyEvent = new Event(getEvent.getEventContent(),
                                        eventList.get(0).getIdentifier()-1);
//                                updateEvent(getEvent,copyEvent);
                                eventList.add(0,copyEvent);
                                eventAdapter.notifyItemInserted(0);
                               eventAdapter.notifyItemRangeChanged(0,eventList.size());
                                eventList.remove(position+1);
                                eventAdapter.notifyItemRemoved(position+1);
                                eventAdapter.notifyItemRangeChanged(position+1,
                                        eventList.size()-position-1);
                                break;
                            }
                            default:
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               if(eventList.size() == 1){
                    Event copyEvent = new Event(eventList.get(0).getEventContent(),
                            eventList.get(0).getIdentifier());
                    eventList.add(0,copyEvent);
                    eventAdapter.notifyItemInserted(0);
                   eventAdapter.notifyItemRangeChanged(0,eventList.size());
                    eventList.remove(1);
                    eventAdapter.notifyItemRemoved(1);
                    eventAdapter.notifyItemRangeChanged(1,
                            eventList.size()-1);
                }
//                当列表中的元素个数为1时为了不出现留空白的bug让这个元素置顶
                String content = inputEvent.getText().toString();
                if(!"".equals(content)){
                    Event event = new Event(content);
                    if(eventList.size() == 0){
                        event.setIdentifier(10);
                    }
                    else {
                        event.setIdentifier(eventList.get(eventList.size()-1).getIdentifier()+1);
                    }
//                    第一次初始化时让首个元素的标识符id为10，后面的元素id依次加1
                    eventList.add(event);
                    addToDb(date,event);
//                    将事件添加到数据库中记录
                    eventAdapter.notifyItemInserted(eventList.size()-1);
                    eventRecyclerView.scrollToPosition(eventList.size()-1);
                    inputEvent.setText("");
                }
            }
        });
    }
    public Event[] initArray(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Event eventArray[] = new Event[20];
        for(int i=0;i<20;i++){
            eventArray[i] = new Event();
        }
        Cursor cursor = db.query("Notepad",null,"date = ?",
                new String[]{date},null,null,null);
        int i = 0;
        if(cursor == null) return null;
        else{
            if(cursor.moveToNext()){
                do {
                    String eventCon = cursor.getString(cursor.getColumnIndex("event"));
                    int id = cursor.getInt(cursor.getColumnIndex("id"));
                    eventArray[i].setIdentifier(id);
                    eventArray[i].setEventContent(eventCon);
                    i++;
                }while (cursor.moveToNext());
            }
            cursor.close();
        }
        return eventArray;
    }
    public Event[] sort(Event[]arr,int length){
        for(int i=1;i<length;i++)
            for(int j=i;j>0;j--){
                if(arr[j].getIdentifier()<arr[j-1].getIdentifier()){
                    Event temp = new Event();
                    temp = arr[j];
                    arr[j] = arr[j-1];
                    arr[j-1] = temp;
                }
            }
        return arr;
    }
    public void addToDb(String date,Event event){
        Toast.makeText(NotepadActivity.this,event.getEventContent(),Toast.LENGTH_SHORT).show();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id",event.getIdentifier());
        values.put("date",date);
        values.put("event",event.getEventContent());
        db.insert("Notepad",null,values);
    }
    public void editDb(String date,int id,String content){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("event",content);
        db.update("Notepad",values,"date = ? and id = ?",new String[]{
                date,id+""
        });

    }
    public void removeFromDb(String date,int id){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete("Notepad","date = ? and id = ?",new String[]{
                date,id+""
        });
    }
}
