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
public class MainListViewAdapter extends RecyclerView.Adapter<MainListViewAdapter.MainListViewHolder> {

   // private final Context mContext;
    private List<String> mData1;
    ClickListener clickListener;


    public MainListViewAdapter(Context mContext, String[] data1) {
       // this.mContext = mContext;
        if (data1 != null)
            mData1 = new ArrayList<String>(Arrays.asList(data1));
        else mData1 = new ArrayList<String>();


    }

    @Override
    public MainListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = inflater.inflate(R.layout.list_item,viewGroup,false);

        return new MainListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MainListViewHolder myViewHolder, final int position) {
        myViewHolder.tv1.setText(mData1.get(position));

        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                clickListener.onClick(v, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mData1.size();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        public void onClick(View v, int pos);

    }

    public static class MainListViewHolder extends RecyclerView.ViewHolder
    {
        protected TextView tv1;

        public MainListViewHolder(View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.txt1);


        }
    }



}
