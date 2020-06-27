package com.example.nfc.Helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nfc.R;
import com.example.nfc.Classes.TempClass;

import java.util.ArrayList;

public class TempAdapter extends RecyclerView.Adapter<TempAdapter.TempViewHolder>{

    private Context context;
    private ArrayList<TempClass> tempClassArrayList;

    public TempAdapter(Context context, ArrayList<TempClass> tempClassArrayList) {
        this.context = context;
        this.tempClassArrayList = tempClassArrayList;
    }


    @NonNull
    @Override
    public TempViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_temp, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        return new TempViewHolder(layoutView);
    }



    @Override
    public void onBindViewHolder(@NonNull TempViewHolder holder, int position) {

        holder.tempTextView.setText("תוצאת הבדיקה היא: " + tempClassArrayList.get(position).getTemp() + (char) 0x00B0 + "C");
        holder.timeTextView.setText("בוצעה בדיקה בתאריך: " + tempClassArrayList.get(position).getTime());

    }



    @Override
    public int getItemCount() {
        return tempClassArrayList.size();
    }




    public class TempViewHolder extends RecyclerView.ViewHolder{

        TextView tempTextView, timeTextView;
        LinearLayout linearLayout;

        public TempViewHolder(@NonNull View itemView) {
            super(itemView);

            tempTextView = itemView.findViewById(R.id.temp_text_view);
            timeTextView = itemView.findViewById(R.id.time_text_view);
            linearLayout = itemView.findViewById(R.id.linearLayour_item_temp);
        }
    }
}
