package me.mikeshi.scan2view.adapters;

import java.util.ArrayList;
import java.util.List;

import me.mikeshi.scan2view.utils.AppUtils;
import me.mikeshi.scan2view.utils.AppUtils.FileInfo;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class HistoryAdapter extends BaseAdapter {

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
//			tv.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			tv.setTextSize(24);
			tv.setCompoundDrawablePadding(10);
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
