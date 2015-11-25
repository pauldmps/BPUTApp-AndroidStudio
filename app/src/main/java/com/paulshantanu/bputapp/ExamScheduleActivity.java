package com.paulshantanu.bputapp;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

/**
 * Created by Shantanu Paul on 11-11-2015.
 */
public class ExamScheduleActivity extends ActionBarActivity implements AsyncTaskListener{

String degree, semester , branch;
SaxParserHandler handler;
RecyclerView recyclerView;
RecyclerView.LayoutManager layoutManager;
ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        recyclerView = (RecyclerView)findViewById(R.id.schedule_list);
        progressBar = (ProgressBar)findViewById(R.id.progressBar4);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        degree = getIntent().getExtras().getString("Degree");
        semester = getIntent().getExtras().getString("Semester");
        branch = getIntent().getExtras().getString("Branch");

        Log.i("Debug", "Degree " + degree);
        Log.i("Debug","semester "+semester);
        Log.i("Debug","branch "+branch);


        handler = new SaxParserHandler(SaxParserHandler.SCHEDULE_PARSER);

        new XMLParser(this,handler,null,degree,semester,branch)
                .execute("https://paul-shantanu-bputapp.appspot.com/exam_schedule.php");

    }

    @Override
    public void onTaskComplete(String result) {

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);

        ExamScheduleListAdapter adapter = new ExamScheduleListAdapter(handler.getSchedule().getDate(),handler.getSchedule().getSitting(),
                handler.getSchedule().getCode(),handler.getSchedule().getSubject());

        progressBar.setVisibility(View.INVISIBLE);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
