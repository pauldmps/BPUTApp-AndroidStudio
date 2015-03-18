package com.paulshantanu.bputapp;

/*
 * This activity downloads and displays those notices which are in PDF format. 
 * This activity uses PDF.js javascript library to display the PDF inside a WebView "webView".
 * 
 * PDF.js is licensed under the APACHE-V2 License. Copyright Mozilla Foundation, all rights reserved.
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

import com.epapyrus.plugpdf.SimpleDocumentReader;
import com.epapyrus.plugpdf.SimpleReaderFactory;
import com.epapyrus.plugpdf.core.PlugPDF;
import com.epapyrus.plugpdf.core.PlugPDFException.InvalidLicense;




@SuppressLint("NewApi")
public class PdfViewerAcitvity extends ActionBarActivity {
	String url, path;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pdf_notice);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setSubtitle("View Notice");
		
		String link = getIntent().getExtras().getString("link");
		Log.i("debug", "pdfintent: "+link);

        try {
            // Initialize PlugPDF with a license key.
            PlugPDF.init(getApplicationContext(),
                    "ACF5AE3D5H4FD3DAED34D3A7FGHHGB9DF48GG35932CCF2F2EC25FBF5");

        } catch (InvalidLicense ex) {
            Log.e("KS", "error ", ex);
            // Handle invalid license exceptions.
        }



		url = URLDecoder.getDecodedUrl(link);
		
		


	
	    new DownloadTask(PdfViewerAcitvity.this).execute(url);
	}

		
	private class DownloadTask extends AsyncTask<String, Integer, String>{	
		private Context context;
	    private PowerManager.WakeLock mWakeLock;

	    public DownloadTask(Context context) {
	        this.context = context;
	    }
		

		@Override
		protected void onPreExecute() {
		   super.onPreExecute();
		   PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
	             getClass().getName());
	        mWakeLock.acquire();
		}

		@Override
		protected String doInBackground(String... sUrl) {       
			InputStream input = null;
	        OutputStream output = null;
	        HttpURLConnection connection = null;
	        try {
	            URL url = new URL(sUrl[0]);
	            connection = (HttpURLConnection) url.openConnection();
	            connection.connect();
	            
	            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) { //error, not HTTP 200- OK
	                return "Server returned HTTP " + connection.getResponseCode()
	                        + " " + connection.getResponseMessage();
	            }
	            
	            int fileLength = connection.getContentLength();
	            input = connection.getInputStream();
	            output = openFileOutput("notice.pdf", Context.MODE_PRIVATE);
	            byte data[] = new byte[4096];
	            long total = 0;
	            int count;
	            while ((count = input.read(data)) != -1) {
	                // allow canceling with back button
	                if (isCancelled()) {
	                    input.close();
	                    return null;
	                }
	                total += count;
	                // publishing the progress....
	                if (fileLength > 0) // only if total length is known
	                    publishProgress((int) (total * 100 / fileLength));
	                output.write(data, 0, count);
	                }
	        }
			catch(Exception e){
				e.printStackTrace();
			}
	        finally {
	        	try {
	                if (output != null)
	                    output.close();
	                if (input != null)
	                    input.close();
	            } catch (IOException ignored) {}
	            if (connection != null)
	                connection.disconnect();
	        }
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			super.onProgressUpdate(progress);
			//getActionBar().setSubtitle("Loading " + progress[0] + "%");
		}
		
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mWakeLock.release();
//			if (result != null)
//	            Toast.makeText(context,"Download error: "+result, Toast.LENGTH_LONG).show();
//	        else
//	            Toast.makeText(context,"File downloaded", Toast.LENGTH_SHORT).show();

			path = context.getFilesDir().toString()+ "/notice.pdf";

            Log.i("debug",path);

            try {

                InputStream is = new FileInputStream(path);
                int size = is.available();

                if(size>0)
                {
                    byte[] data = new byte[size];
                    is.read(data);

                    SimpleDocumentReader v = SimpleReaderFactory.createSimpleViewer(PdfViewerAcitvity.this,null);
                    v.openData(data,data.length,"");
                    Log.i("debug","reader executed");

                }
                is.close();

            }
            catch (FileNotFoundException e)
            {
                  Log.i("debug","File not found");
            }

            catch (Exception e)
            {
                   e.printStackTrace();
            }

		}
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
	//	webView.loadUrl( "javascript:window.location.reload( true )" );
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		//webView.clearCache(true);
	}
	
	protected void  onDestroy() {
		super.onDestroy();
		//webView.clearCache(true);
		File file = new File(getFilesDir(), "notice.pdf");
        file.delete();			
	}	

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	super.onCreateOptionsMenu(menu);
    	MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pdfmenu, menu);
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
			
		case android.R.id.home:
			 NavUtils.navigateUpFromSameTask(this);
		        return true;
		        
        case R.id.action_next:
			

			//webView.loadUrl("javascript:onNextPage()");
	    	return super.onOptionsItemSelected(item);
			
		
		case R.id.action_previous:
			
			
			//webView.loadUrl("javascript:onPrevPage()");
	    	return super.onOptionsItemSelected(item);

			        

		default:
	    	return super.onOptionsItemSelected(item);
		}
    }

}


	

