package me.mikeshi.scan2view.adapters;

import java.io.File;

import me.mikeshi.scan2view.R;
import android.content.Context;
import android.util.DisplayMetrics;
import android.app.Activity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FolderAdapter extends BaseAdapter {

	private int mWidth;

	@SuppressWarnings("deprecation")
	public FolderAdapter(Context ctx) {
		WindowManager wm = (WindowManager) ctx.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		mWidth = display.getWidth();
		 DisplayMetrics dm = new DisplayMetrics();
		((Activity) ctx).getWindowManager().getDefaultDisplay().getMetrics(dm);
		double x = Math.pow(dm.widthPixels / dm.xdpi, 2);
		double y = Math.pow(dm.heightPixels / dm.ydpi, 2);
		mScreenInches = Math.sqrt(x + y);
	}
	
	private File[] mFolders = new File[0];
	private double mScreenInches;
	
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
			if (mScreenInches > 8) {
				view.setTextSize(24);
			} else {
				view.setTextSize(14);
			}
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
