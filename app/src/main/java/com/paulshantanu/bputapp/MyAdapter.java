package com.paulshantanu.bputapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 *
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final Context mContext;
    private List<String> mData1;

    public MyAdapter(Context mContext, String[] data1) {
        this.mContext = mContext;
        if (data1 != null)
            mData1 = new ArrayList<String>(Arrays.asList(data1));
        else mData1 = new ArrayList<String>();


    }

    public void add(String s,int position) {
        position = position == -1 ? getItemCount()  : position;
        mData1.add(position,s);

        notifyItemInserted(position);
    }

    public void remove(int position){
        if (position < getItemCount()  ) {
            mData1.remove(position);
            notifyItemRemoved(position);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.list_item,viewGroup,false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        myViewHolder.tv1.setText(mData1.get(position));



    }

    @Override
    public int getItemCount() {
        return mData1.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView tv1;



        public MyViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.txt1);


        }
    }



}
