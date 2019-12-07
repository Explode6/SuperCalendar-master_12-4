package com.hqyxjy.ldf.supercalendar;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {
    static public int isSaved = 0;
    private EditText editText;
    private Button save;
    private String editedContent;
    private int position;
    private int isBack = 0;
    @Override
    public void onBackPressed() {
            Intent intent1 = new Intent();
            intent1.putExtra("isSaved", isSaved);
            intent1.putExtra("backContent", editedContent);
            intent1.putExtra("position", position);
            setResult(RESULT_OK, intent1);
            finish();
        }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Intent intent = getIntent();
        String content = intent.getStringExtra("event");
        position = intent.getIntExtra("position",0);
        editText = (EditText)findViewById(R.id.edit_event);
        editText.setText(content);
        save = (Button)findViewById(R.id.save_content);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editedContent = editText.getText().toString();
                isSaved = 1;
                if(isSaved == 1)
                    Toast.makeText(EditActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
            }
        });


    }
}
