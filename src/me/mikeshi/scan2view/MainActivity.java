package me.mikeshi.scan2view;

import java.io.File;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity implements View.OnClickListener{

	private TextView mDir;
	private TextView mScan;
	private static final int SET_DIR = 1;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkIfDirSpecified();
        
        mScan = (TextView) findViewById(R.id.main_scan_barcode);
		mScan.setOnClickListener(this);
		
		mDir = (TextView) findViewById(R.id.main_selected_folder);
		mDir.setOnClickListener(this);
    }

    private void checkIfDirSpecified() {
    	
    	File external = Environment.getExternalStorageDirectory();
    	if (!external.isDirectory()) {
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
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		  if (scanResult != null) {
		    // handle scan result
		  } else {
			  switch (requestCode) {
			  case SET_DIR:
				  if (data != null) {
					  mDir.setText(data.getStringExtra(BrowserActivity.FOLDER_PATH));
				  }
				  break;
			  }
		  }
	}
    
	
    
}
