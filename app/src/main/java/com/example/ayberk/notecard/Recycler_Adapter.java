package com.example.ayberk.notecard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ayberk.notecard.helper.ItemTouchHelperAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * Created by Ayberk on 9/28/2015.
 */
public class Recycler_Adapter extends RecyclerView.Adapter<Recycler_Adapter.ViewHolder> implements ItemTouchHelperAdapter {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView recyclerText;
        private RelativeLayout relativeLayout;
        private Context context;
        private Activity mActivity;
        private String mNote;
        private int mPosition;

        public ViewHolder(View itemView){
            super(itemView);
            recyclerText = (TextView)itemView.findViewById(R.id.card_text);
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.my_layout);
            itemView.setOnClickListener(this);
            context = itemView.getContext();
            mActivity = (Activity)context;

        }
        public void bindNote(String note,int position){
            mNote = note;
            mPosition = position;
        }
        public void cardRemoved(int position){
            if(position<mPosition){
                mPosition--;
            }
        }
        @Override
        public void onClick(View v){
            Intent i = new Intent(context, NoteActivity.class);
            i.putExtra("data", mNote);
            i.putExtra("noteNum",mPosition);
            mActivity.startActivityForResult(i, 0);
        }

    }
    //Instance vars for Adapter
    private ArrayList<String> notes;
    private ArrayList<Integer> colors = new ArrayList<>();
    private float densityMultiplierx;
    private Recycler_Adapter.ViewHolder mviewHolder;
    private Context mContext;

    public Recycler_Adapter(ArrayList<String> input,Context context){
        notes = input;
        mContext = context;


    }
    public Recycler_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View recyclerItemView = inflater.inflate(R.layout.card_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(recyclerItemView);
        mviewHolder = viewHolder;

        return viewHolder;
    }

    public void onBindViewHolder(Recycler_Adapter.ViewHolder viewHolder,int position){
        String noteData = notes.get(position);
        TextView textView = viewHolder.recyclerText;
        RelativeLayout rLayout = viewHolder.relativeLayout;
        colors.add(Color.rgb(155 + (int) (Math.random() * 100), 155 + (int) (Math.random() * 100), 155 + (int) (Math.random() * 100)));
        rLayout.setBackgroundColor(colors.get(position));
        putTextOnCard(textView, noteData);

        viewHolder.bindNote(notes.get(position), position);

    }
    public void putTextOnCard(TextView textView,String noteData){
        Paint paint = new Paint();
        String firstLine;
        String title;
        densityMultiplierx = mContext.getResources().getDisplayMetrics().density;
        Scanner text = new Scanner(noteData);
        firstLine = text.nextLine();
        float textSizePixels;
        final float scaledPx = 20 * densityMultiplierx;
        paint.setTextSize(scaledPx);
        textSizePixels = paint.measureText(firstLine);
        int i = firstLine.length();
        while (textSizePixels > 1200) {
            i--;
            textSizePixels = paint.measureText(firstLine.substring(0, i));
        }
        title = firstLine.substring(0, i);
        textView.setText(title);
    }



    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public void onItemDismiss(int position) {
        notes.remove(position);
        colors.remove(position);
        mviewHolder.cardRemoved(position);
        ((MainActivity)mContext).updateArrayList();
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(notes, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public int getItemCount(){
        return notes.size();
    }
}