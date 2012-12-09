package me.mikeshi.scan2view.adapters;

import java.util.ArrayList;

import me.mikeshi.scan2view.utils.AppUtils;
import me.mikeshi.scan2view.utils.AppUtils.FileInfo;
import android.content.Context;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class HistoryAdapter extends BaseAdapter {

	private int mWidth;

	@SuppressWarnings("deprecation")
	public HistoryAdapter(Context ctx) {
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		mWidth = display.getWidth();
	}
	
	private ArrayList<String> mHistories;
	
	@Override
	public int getCount() {
		return mHistories.size();
	}

	@Override
	public String getItem(int position) {
		return mHistories.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		TextView tv = (TextView) convertView;
		if (tv == null) {
			tv = new TextView(parent.getContext());
			if (mWidth > 1000) {
				tv.setTextSize(24);
				tv.setTextColor(0xff000000);
				tv.setCompoundDrawablePadding(10);
			} else {
				tv.setTextSize(10);
				tv.setTextColor(0xff000000);
				tv.setCompoundDrawablePadding(5);
			}
		}
		
		FileInfo fileInfo = AppUtils.splitFileName(getItem(position));
		tv.setText(fileInfo.filename);
		tv.setCompoundDrawablesWithIntrinsicBounds(AppUtils.getFileTypeIconFromExtension(fileInfo.extension), 0, 0, 0);
		
		return tv;
	}

	public ArrayList<String> getHistories() {
		return mHistories;
	}

	public void setHistories(ArrayList<String> histories) {
		this.mHistories = histories;
	}

}
