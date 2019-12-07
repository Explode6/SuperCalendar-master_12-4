package com.hqyxjy.ldf.supercalendar;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TryActivity extends AppCompatActivity {
    private EditText inputEvent;
    private Button add;
    private RecyclerView eventRecyclerView;
    private List<Event>eventList = new ArrayList<>();
    private EventAdapter eventAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try);
        add = (Button)findViewById(R.id.add);
        inputEvent = (EditText)findViewById(R.id.input_event);
        eventRecyclerView = (RecyclerView)findViewById(R.id.today_all_event);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3,
                StaggeredGridLayoutManager.VERTICAL);
        eventRecyclerView.setLayoutManager(layoutManager);
        eventAdapter = new EventAdapter(eventList);
        eventRecyclerView.setAdapter(eventAdapter);
        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String content = inputEvent.getText().toString();
                if(!"".equals(content)){
                    Event event = new Event(content);
                    eventList.add(event);
                    eventAdapter.notifyItemInserted(eventList.size()-1);
                    eventRecyclerView.scrollToPosition(eventList.size()-1);
                    inputEvent.setText("");
                }
            }
        });

    }
}
