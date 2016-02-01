package com.example.ayberk.notecard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.ayberk.notecard.helper.OnStartDragListener;
import com.example.ayberk.notecard.helper.SimpleItemTouchHelperCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;

public class MainActivity extends Activity implements OnStartDragListener {

    private FloatingActionButton fab;
    private RecyclerView rv;
    private Recycler_Adapter adapter;
    private ArrayList<String> notesData;
    private ArrayList<String> testList;
    private ItemTouchHelper mItemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        notesData = new ArrayList<>();
        getArrayList();
        rv = (RecyclerView) findViewById(R.id.card_list);
        adapter = new Recycler_Adapter(notesData,this);
        rv.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        fab = (FloatingActionButton) findViewById(R.id.add_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), NoteActivity.class);
                startActivityForResult(i, 0);
            }
        });
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(rv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == 1) {
            String noteContent = data.getStringExtra("note");
            notesData.add(noteContent);
        }
        else if(resultCode ==2){
            int notePosition = data.getIntExtra("noteNum", 0);
            String noteContent = data.getStringExtra("note");
            notesData.set(notePosition,noteContent);
        }
        adapter.notifyDataSetChanged();
        updateArrayList();
    }

    @Override
    public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }
    public void createArrayList(){
        ParseObject notesList = new ParseObject("NotesData");
        notesList.put("notes", notesData);
        notesList.pinInBackground();
    }
    public void updateArrayList(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("NotesData");
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject noteList, ParseException e) {
                if (e == null) {
                    noteList.put("notes", notesData);
                    noteList.pinInBackground();
                }
            }
        });

    }
    public void getArrayList(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("NotesData");
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    testList = (ArrayList<String>) object.get("notes");
                    for(String note:testList){
                        notesData.add(note);
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    createArrayList();
                }
            }
        });
    }

}
