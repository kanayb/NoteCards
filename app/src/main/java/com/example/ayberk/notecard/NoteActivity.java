package com.example.ayberk.notecard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NoteActivity extends Activity {

    private EditText editText;
    private Button doneButton;
    private int notePosition;
    private boolean existingNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        editText = (EditText)findViewById(R.id.note_edit_text);
        doneButton = (Button)findViewById(R.id.done_button);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editTextContent = editText.getText().toString();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                if(editTextContent.length()>0) {
                    if (existingNote == false) {
                        i.putExtra("note", editTextContent);
                        setResult(1, i);
                        finish();
                    } else {
                        i.putExtra("note", editTextContent);
                        i.putExtra("noteNum", notePosition);
                        setResult(2, i);
                        finish();
                    }
                }
                else{
                    finish();
                }
            }
        });
        Intent i = getIntent();
        if(i.hasExtra("data")){
            existingNote =true;
        }
        editText.setText(i.getStringExtra("data"));
        notePosition = i.getIntExtra("noteNum",0);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
