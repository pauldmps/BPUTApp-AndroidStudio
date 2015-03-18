package com.paulshantanu.bputapp;

/*
 * This class is the main entry point of the app. It is the first activity that is shown when the
 * user opens the app. 
 * This activity implements SwipeRefreshHintLayout.java for "Swipe down to refresh" gesture.
 * This activity also implements ButteryProgressBar.java for showing the indeterminate progressbar while
 * loading the required data.
 * 
 * This activity shows a listview with all the notices that are downloaded and parsed using 
 * XMLParser.java with the help of SaxParserHandler.java handler class.
 * 
 * TODO: UI Enhancements, Code Optimizations
 * 
*/

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class MainActivity extends ActionBarActivity implements OnRefreshListener,AsyncTaskListener {

	SaxParserHandler handler;
	SwipeRefreshLayout mSwipeRefreshLayout;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    ProgressBar mProgressBar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		//getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.ic_launcher));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(false);
	
		mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_container);
 	    mSwipeRefreshLayout.setOnRefreshListener(this);
 	    mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.theme_red),getResources().getColor(R.color.theme_accent),getResources().getColor(R.color.theme_red),getResources().getColor(R.color.theme_accent));

        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setVisibility(View.VISIBLE);


        checkConnectivity();



 	    //onRefresh();
    }

	@Override
	public void onRefresh() {
		mSwipeRefreshLayout.setRefreshing(true);
		mSwipeRefreshLayout.setEnabled(false);
		getSupportActionBar().setSubtitle("Loading...");
		handler = new SaxParserHandler();

        new XMLParser(this, handler, null).execute("http://pauldmps.url.ph/default.php");
		
	}
	
	public void checkConnectivity() {	
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    // test for internet connection
	    if (!(cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected())){
    	AlertDialog.Builder b =new AlertDialog.Builder(this);
    	b.setTitle("No Connection");
    	b.setMessage("Cannot connect to the internet!");
    	b.setPositiveButton("Retry", new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
	               checkConnectivity();
			}
		});
    	b.setNegativeButton("Exit", new AlertDialog.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
                 finish();
			}
		});
    	b.create().show();
          }
	    else{
	    	onRefresh();
	    }
	}
	
	    @Override
		public void onTaskComplete(String result) {
	    	
	    	mSwipeRefreshLayout.setEnabled(true);
	    	if(result.equals("OK")){
	   		//handler.getNotice().getNotice_head().remove(handler.getNotice().getNotice_head().size()-1);
	  		//handler.getNotice().getNotice_head().remove(handler.getNotice().getNotice_head().size()-1);

                mRecyclerView.setHasFixedSize(true);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                MyAdapter mAdapter = new MyAdapter(this,handler.getNotice().getNotice_head().toArray(new String[handler.getNotice().getNotice_head().size()]));
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));
                mSwipeRefreshLayout.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);


                mAdapter.setClickListener(new MyAdapter.ClickListener() {
                    @Override
                    public void onClick(View v, int pos) {

                        String link = URLDecoder.getDecodedUrl(handler.getNotice().getUrl().get(pos).trim());
                        Log.i("link",link);
                        if(URLDecoder.getUrlType(link)==URLDecoder.PDFFILE){ //If the notice is PDF, start PDF opening activity.
                            Intent pdfintent = new Intent(MainActivity.this,PdfViewerAcitvity.class);
                            pdfintent.putExtra("link", link);
                            startActivity(pdfintent);
                        }
                        else
                        {
                            Intent i_notice = new Intent(MainActivity.this,NoticeAcitivity.class);
                            i_notice.putExtra("link", link);
                            startActivity(i_notice);
                        }

                    }
                });

	    	
			/* ArrayAdapter<String> adp =new ArrayAdapter<String>(MainActivity.this,
					 android.R.layout.simple_list_item_1,
					 handler.getNotice().getNotice_head().toArray(new String[handler.getNotice().getNotice_head().size()]));
			 lv= (ListView)findViewById(R.id.lv_notices_main);
			 lv.setAdapter(adp);
   			 lv.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1, int itemClicked,
							long arg3) {
						String link = URLDecoder.getDecodedUrl(handler.getNotice().getUrl().get(itemClicked).trim());
						Log.i("link",link);
						if(URLDecoder.getUrlType(link)==URLDecoder.PDFFILE){ //If the notice is PDF, start PDF opening activity.
							Intent pdfintent = new Intent(MainActivity.this,PdfViewerAcitvity.class);
							pdfintent.putExtra("link", link);
							startActivity(pdfintent);
						}
						else
						{
						 Intent i_notice = new Intent(MainActivity.this,NoticeAcitivity.class);
		                 i_notice.putExtra("link", link);
		                 startActivity(i_notice);
						}
						
					} */

   			    mSwipeRefreshLayout.setRefreshing(false);
         	   // mSwipeRefreshLayout.setColorScheme(R.color.theme_red,R.color.transparent,R.color.transparent,R.color.transparent);
		        getSupportActionBar().setSubtitle(null);
	    	}
	    }
	    
	    @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	super.onCreateOptionsMenu(menu);
	    	MenuInflater inflater = getMenuInflater();
	        inflater.inflate(R.menu.main, menu);
	        return true;
	    }
	    
	    @Override
	    public boolean onOptionsItemSelected(MenuItem item) {

	    	switch (item.getItemId()) {
			case R.id.action_settings:		
					
				return true;
				
			case R.id.about:	
                 AlertDialog.Builder b = new AlertDialog.Builder(this);
                 b.setTitle("About");
                 
                 WebView about_view = new WebView(this);
                 about_view.loadUrl("file:///android_asset/about.htm");
                 
                 b.setView(about_view);
                 b.setPositiveButton("OK", null);
                 b.create().show();
				return true;

			default:
		    	return super.onOptionsItemSelected(item);
			}
	    }
	}


