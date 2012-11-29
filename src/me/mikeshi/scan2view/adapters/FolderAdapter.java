package me.mikeshi.scan2view.adapters;

import java.io.File;

import me.mikeshi.scan2view.R;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FolderAdapter extends BaseAdapter {

	private File[] mFolders = new File[0];
	
	@Override
	public int getCount() {
		return mFolders.length;
	}

	@Override
	public File getItem(int position) {
		return mFolders[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup container) {
		TextView view = (TextView) convertView;
		if (view == null) {
			view = new TextView(container.getContext());
			view.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_folder, 0, 0);
			view.setTextSize(24);
			view.setGravity(Gravity.CENTER_HORIZONTAL);
		}
		
		view.setText(getItem(position).getName());
		return view;
	}

	public void setFolders(File[] folders) {
		this.mFolders = folders;
		notifyDataSetChanged();
	}

}
