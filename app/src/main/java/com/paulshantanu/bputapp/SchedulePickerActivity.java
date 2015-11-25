package com.paulshantanu.bputapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Created by Shantanu Paul on 06-11-2015.
 */
public class SchedulePickerActivity extends ActionBarActivity implements AsyncTaskListener
{

    SaxParserHandler handler;
    Intent intent;
    Spinner degree_spinner, semester_spinner, branch_spinner;
    Button confirm_button;
    String [] degree = {"--Select Your Degree--","B.TECH","MBA","B.ARCH","B.PHARM","B.HMCT","MCA","M.PHARM","MAM","MSC","BACK","SPECIAL"};
    int degree_position;
    int semester_position;
    int branch_position;
    boolean userIsInteracting = false;
    boolean semesterSpinnerIsSet = false;
    ProgressDialog progressDialog;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_picker);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        degree_spinner = (Spinner)findViewById(R.id.degree_spinner);
        semester_spinner = (Spinner)findViewById(R.id.sem_spinner);
        branch_spinner = (Spinner) findViewById(R.id.branch_spinner);
        confirm_button = (Button)findViewById(R.id.ok_button);
        progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait");

        handler = new SaxParserHandler(SaxParserHandler.SCHEDULE_PARSER);

        ArrayAdapter<String> degreeAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,degree);
        degree_spinner.setAdapter(degreeAdapter);

        degree_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (userIsInteracting == true) {
                    userIsInteracting = false;
                    semesterSpinnerIsSet = false;
                    degree_position = position;
                    new XMLParser(SchedulePickerActivity.this, handler, null, degree[degree_position])
                            .execute("https://paul-shantanu-bputapp.appspot.com/semester_options.php");
                    progressDialog.setMessage("Fetching semesters");
                    progressDialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        semester_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(userIsInteracting == true) {
                    userIsInteracting = false;
                    semester_position = position;
                    new XMLParser(SchedulePickerActivity.this, handler, null, degree[degree_position], handler.getSchedule().getSemester().get(position))
                            .execute("https://paul-shantanu-bputapp.appspot.com/branch_options.php");
                    progressDialog.setMessage("Fetching branches");
                    progressDialog.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        branch_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch_position = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        confirm_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                intent = new Intent(SchedulePickerActivity.this,ExamScheduleActivity.class);
                intent.putExtra("Degree",degree[degree_position]);
                intent.putExtra("Semester",handler.getSchedule().getSemester().get(semester_position));
                intent.putExtra("Branch", handler.getSchedule().getBranch().get(branch_position));
                startActivity(intent);
            }
        });
    }

       public void onTaskComplete(String result){

           progressDialog.dismiss();

           if(semesterSpinnerIsSet == false) {
               semesterSpinnerIsSet = true;
               ArrayAdapter semesterAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, handler.getSchedule().getSemester());
               semester_spinner.setAdapter(semesterAdapter);
           }
           else
           {
               semester_spinner.setSelection(semester_position,false);
           }

           ArrayAdapter branchAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,handler.getSchedule().getBranch());
           branch_spinner.setAdapter(branchAdapter);


        }

    @Override
    public void onUserInteraction() {
        super.onUserInteraction();
        userIsInteracting = true;
    }
}








