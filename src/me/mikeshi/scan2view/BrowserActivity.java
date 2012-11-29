package me.mikeshi.scan2view;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

import me.mikeshi.scan2view.adapters.FolderAdapter;
import me.mikeshi.scan2view.utils.AppConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView;
import java.util.Comparator;

public class BrowserActivity extends Activity implements AdapterView.OnItemClickListener {

	private GridView mDirList;
	
	private TextView mDir; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_browser);
		
		findViewById(R.id.browser_cancel).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
		findViewById(R.id.browser_choose).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent result = new Intent();
				result.putExtra(AppConstants.FOLDER_PATH, mDir.getText().toString());
				setResult(RESULT_OK, result);
				finish();
			}
		});
		
		mDirList = (GridView) findViewById(R.id.browser_dir_list);
		FolderAdapter fa = new FolderAdapter();
		mDirList.setAdapter(fa);
		
		mDir = (TextView) findViewById(R.id.browser_choosed_dir);
		
		initDir(savedInstanceState);
		
		String path = mDir.getText().toString();
		listDir(path);
		
		mDirList.setOnItemClickListener(this);
		
	}
	
	private void listDir(String path) {
		File f = new File(path);
		File[] files = f.listFiles(new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory() && !pathname.getName().startsWith(".");
			}
		});
		
		Arrays.sort(files, new Comparator<File> () {

			@Override
			public int compare(File lhs, File rhs) {
				return lhs.getName().compareToIgnoreCase(rhs.getName());
			}
			
		});
		
		((FolderAdapter)mDirList.getAdapter()).setFolders(files);
	}

	private void initDir(Bundle savedInstanceState) {
		String defaultPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		if (savedInstanceState != null && savedInstanceState.containsKey(AppConstants.FOLDER_PATH)) {
			String path = savedInstanceState.getString(AppConstants.FOLDER_PATH);
			if (path != null) {
				File dirPath = new File(path);
				if (dirPath.isDirectory()) {
					mDir.setText(path);
				} else {
					mDir.setText(defaultPath);
				}
			} else {
				mDir.setText(defaultPath);
			}
		} else {
			mDir.setText(defaultPath);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> container, View item, int position,
			long rowId) {
		File f = (File) container.getItemAtPosition(position);
		mDir.setText(f.getAbsolutePath());
		listDir(f.getAbsolutePath());
	}

	@Override
	public void onBackPressed() {
		if (mDir.getText().toString().equals(Environment.getExternalStorageDirectory().getAbsolutePath())) {
			super.onBackPressed();
		} else {
			File f = new File(mDir.getText().toString());
			mDir.setText(f.getParent());
			listDir(f.getParent());
		}
	}

}
