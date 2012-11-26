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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkIfDirSpecified();
        
        mScan = (TextView) findViewById(R.id.scan_barcode);
		mScan.setOnClickListener(this);
    }

    private void checkIfDirSpecified() {
    	
    	File external = Environment.getExternalStorageDirectory();
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
		}
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		  if (scanResult != null) {
		    // handle scan result
		  }
		  // else continue with any other code you need in the method
	}
    
	
    
}
