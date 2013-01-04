package me.mikeshi.scan2view;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import me.mikeshi.scan2view.adapters.HistoryAdapter;
import me.mikeshi.scan2view.utils.AppConstants;
import me.mikeshi.scan2view.utils.AppUtils;
import me.mikeshi.scan2view.utils.AppUtils.FileInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends Activity implements View.OnClickListener{

	private TextView mDir;
	private TextView mScan;
	private TextView mManual;
	private ListView mHistory;
	private Map<String, String> mPaths;
	
	private static final int SET_DIR = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	mPaths =  new HashMap<String, String>();
    	
    	pb = new ProgressDialog(this); 
    	pb.setTitle("Scanning Files");
		pb.setCancelable(false);
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mScan = (TextView) findViewById(R.id.main_scan_barcode);
		mScan.setOnClickListener(this);
		
		mManual = (TextView) findViewById(R.id.main_enter_barcode);
		mManual.setOnClickListener(this);
		
		mDir = (TextView) findViewById(R.id.main_selected_folder);
		mDir.setOnClickListener(this);
		
		initDir();
		
		mHistory = (ListView) findViewById(R.id.main_history);
		
		findViewById(R.id.main_history_clear).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
				builder.setMessage("Are you sure to clear the history list?")
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						HistoryAdapter ha = (HistoryAdapter)mHistory.getAdapter();
						ha.getHistories().clear();
						ha.notifyDataSetChanged();						
					}
				}).setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).create().show();
			}
		});
		
		initHistory();
    }
    
    @Override
	protected void onResume() {
    	
    	final String path = mDir.getText().toString();
    	
    	
		scanDir(path);
		
		super.onResume();
	}

	private void scanDir(final String path) {
		new AsyncTask<Void, Void, Boolean>() {
			
			@Override
			protected void onPreExecute() {
				pb.show();
			}

			@Override
			protected Boolean doInBackground(final Void... params) {
				scanDir(new File(path));
				return null;
			}

			@Override
			protected void onPostExecute(final Boolean result) {
				pb.dismiss();
			}
			
		}.execute();
	}


    private ProgressDialog pb;

	private void scanDir(final File file) {
		if (!file.isDirectory()) return;
		
		File[] files = file.listFiles();
		
		if (files == null) return;
		
		for (final File f : files) {
			if (f.isFile()) {
				mPaths.put(f.getName(), f.getAbsolutePath());
			} else if (f.isDirectory()) {
				scanDir(f);
			}
		}
				
	}

	@Override
	protected void onDestroy() {
    	HistoryAdapter ha = (HistoryAdapter) mHistory.getAdapter();
    	
    	StringBuilder sb = new StringBuilder();
    	for(String s : ha.getHistories()) {
    		sb.append(":").append(s);
    	}
    	
    	SharedPreferences preferences = getPreferences(Activity.MODE_PRIVATE);
    	preferences.edit().putString(AppConstants.HISTORY, sb.length()>0?sb.substring(1) : "").commit();
		super.onDestroy();
	}



	private void initHistory() {
    	HistoryAdapter ha = new HistoryAdapter(this);
    	
    	SharedPreferences preferences = getPreferences(Activity.MODE_PRIVATE);
    	
    	String historyStr = preferences.getString(AppConstants.HISTORY, null);
    	if (historyStr != null && historyStr.length() > 0) {
	    	ArrayList<String> histories = new ArrayList<String>(Arrays.asList(historyStr.split(":")));
	    	ha.setHistories(histories);
    	} else {
    		ha.setHistories(new ArrayList<String>());
    	}
    	mHistory.setAdapter(ha);
    	
    	mHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> list, View item,
					int position, long rowId) {
				showFileByPath(list.getItemAtPosition(position).toString(), false);
			}
		});
	}

	private void initDir() {
    	
    	File external = Environment.getExternalStorageDirectory();
    	
    	SharedPreferences preferences = getPreferences(Activity.MODE_PRIVATE);
		String defaultPath = external.getAbsolutePath() + "/" + AppConstants.DEFAULT_PATH;
		mDir.setText(preferences.getString(AppConstants.FOLDER_PATH, 
				defaultPath));
		File f = new File(mDir.getText().toString());
		
		if (!f.isDirectory()) {
			mDir.setText(defaultPath);
		}

	}

	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	public void onClick(View v) {
		if (v == mScan) {
			IntentIntegrator integrator = new IntentIntegrator(this);
			integrator.initiateScan();
		} else if (v == mDir) {
			Intent intent = new Intent(getApplicationContext(), BrowserActivity.class);
			intent.putExtra(AppConstants.FOLDER_PATH, mDir.getText());
			startActivityForResult(intent, SET_DIR);
		} else if (v == mManual) {
			onManualEnter();
		}
		
	}

	private void onManualEnter() {
		LayoutInflater li = LayoutInflater.from(this);
		View dialogView = li.inflate(R.layout.dialog_enter_barcode, null);
		final EditText barcode = (EditText) dialogView.findViewById(R.id.dialog_enter_barcode);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(dialogView).setPositiveButton("OK", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				showFileByBarcode(barcode.getText().toString());
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		}).create().show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (scanResult != null) {
			if (resultCode == Activity.RESULT_OK) {
				final String barcode = scanResult.getContents();
				showFileByBarcode(barcode);
			}
		    
		} else {
			switch (requestCode) {
			case SET_DIR:
				if (data != null) {
					String path = data.getStringExtra(AppConstants.FOLDER_PATH);
					mDir.setText(path);
					getPreferences(Activity.MODE_PRIVATE).edit()
							.putString(AppConstants.FOLDER_PATH, path).commit();
					
			    	scanDir(path);
				}
				  break;
			  }
		  }
	}

	private void showFileByBarcode(final String barcode) {

		String found = null;
		for (String name : mPaths.keySet()) {
			FileInfo fileInfo = AppUtils.splitFileName(name);
			if (fileInfo.name.compareToIgnoreCase(barcode) == 0) {
				found = mPaths.get(name);
				break;
			}
		}
		
		
		if (found == null || found.length() == 0) {
			Toast.makeText(this, "There is no corresponding document for barcode " + barcode, Toast.LENGTH_LONG).show();
		} else {
			showFileByPath(found, true);
		}
	}
    
	private void showFileByPath(String filename, boolean addToHistory) {
    	Intent view = new Intent(Intent.ACTION_VIEW);
    	
    	FileInfo fileInfo = AppUtils.splitFileName(filename);
    	
    	if (fileInfo.extension == null) return;
    	
    	String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(fileInfo.extension);
		view.setDataAndType(Uri.fromFile((new File(filename))), mimeType);
    	startActivity(view);
    	
    	if (addToHistory) {
	    	HistoryAdapter ha = (HistoryAdapter)mHistory.getAdapter();
	    	ha.getHistories().add(filename);
	    	ha.notifyDataSetChanged();
    	}
	}
	
}
