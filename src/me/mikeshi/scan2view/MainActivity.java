package me.mikeshi.scan2view;

import java.io.File;
import java.io.FilenameFilter;

import me.mikeshi.scan2view.utils.AppConstants;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class MainActivity extends Activity implements View.OnClickListener{

	private TextView mDir;
	private TextView mScan;
	private TextView mManual;
	private static final int SET_DIR = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        mScan = (TextView) findViewById(R.id.main_scan_barcode);
		mScan.setOnClickListener(this);
		
		mManual = (TextView) findViewById(R.id.main_enter_barcode);
		mManual.setOnClickListener(this);
		
		mDir = (TextView) findViewById(R.id.main_selected_folder);
		mDir.setOnClickListener(this);
		
		initDir();
    }

    private void initDir() {
    	
    	File external = Environment.getExternalStorageDirectory();
    	
    	SharedPreferences preferences = getPreferences(Activity.MODE_PRIVATE);
    	if (preferences.contains(AppConstants.FOLDER_PATH)) {
    		mDir.setText(preferences.getString(AppConstants.FOLDER_PATH, 
    				external.getAbsolutePath()));
    	}
    	
    	File f = new File(mDir.getText().toString());
    	if (!f.isDirectory()) {
    		mDir.setText(external.getAbsolutePath());
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
			  
			final String barcode = scanResult.getContents();
			showFileByBarcode(barcode);
		    
		} else {
			switch (requestCode) {
			case SET_DIR:
				if (data != null) {
					String path = data.getStringExtra(AppConstants.FOLDER_PATH);
					mDir.setText(path);
					getPreferences(Activity.MODE_PRIVATE).edit()
							.putString(AppConstants.FOLDER_PATH, path).commit();
				}
				  break;
			  }
		  }
	}

	private void showFileByBarcode(final String barcode) {
		File d = new File(mDir.getText().toString());
		
		String[] files = d.list(new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				if (!new File(dir, filename).isFile()) return false; 
				
				int index = filename.lastIndexOf(".");
				
				if (index == -1) return false;
				
				String name = filename.substring(0, index);
				return name.compareToIgnoreCase(barcode) == 0;
			}
		});
		
		switch (files.length) {
		case 0:
			Toast.makeText(this, "There is no corresponding document for barcode " + barcode, Toast.LENGTH_LONG).show();
			break;
		case 1:
			String filename = files[0];
			showFileByPath(d, filename);
			break;
		default:
			break;
		}
	}
    
	private void showFileByPath(File dir, String filename) {
    	Intent view = new Intent(Intent.ACTION_VIEW);
    	int ext_dot = filename.lastIndexOf(".");
    	
    	if (ext_dot == -1) return;
    	
		String extension = filename.substring(ext_dot + 1);
    	String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
		view.setDataAndType(Uri.fromFile((new File(dir, filename))), mimeType);
    	startActivity(view);
	}
    
}
