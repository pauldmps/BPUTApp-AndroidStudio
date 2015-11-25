package com.paulshantanu.bputapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Shantanu Paul on 24-11-2015.
 */
public class ExamScheduleListAdapter extends RecyclerView.Adapter<ExamScheduleListAdapter.ExamScheduleViewHolder> {


    private ArrayList<String> date, sitting, code, subject;

    public ExamScheduleListAdapter(ArrayList<String> date, ArrayList<String> sitting, ArrayList<String> code, ArrayList<String> subject) {
        this.date = date;
        this.sitting = sitting;
        this.code = code;
        this.subject = subject;
    }


    @Override
    public ExamScheduleListAdapter.ExamScheduleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.schedule_item,parent,false);

        return new ExamScheduleViewHolder(itemView);

            }

    @Override
    public void onBindViewHolder(ExamScheduleListAdapter.ExamScheduleViewHolder holder, int position) {
            holder.tv_code.setText(code.get(position));
            holder.tv_date.setText(date.get(position));
            holder.tv_sitting.setText(sitting.get(position));
            holder.tv_subject.setText(subject.get(position));

    }

    @Override
    public int getItemCount() {
        return code.size();
    }


    public static class ExamScheduleViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView tv_date, tv_code, tv_subject, tv_sitting;

        public ExamScheduleViewHolder(View itemView) {
            super(itemView);
            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_code = (TextView) itemView.findViewById(R.id.tv_code);
            tv_subject = (TextView) itemView.findViewById(R.id.tv_subject);
            tv_sitting = (TextView) itemView.findViewById(R.id.tv_sitting);


        }
    }
}
