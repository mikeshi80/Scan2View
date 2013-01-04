package me.mikeshi.scan2view.utils;

import java.util.HashMap;
import java.util.Map;

import me.mikeshi.scan2view.R;

public class AppUtils {

	static Map<String, Integer> mFileTypeExtensionMap;
	static {
		mFileTypeExtensionMap = new HashMap<String, Integer>();
		mFileTypeExtensionMap.put("txt", R.drawable.ic_ft_txt);
		mFileTypeExtensionMap.put("doc", R.drawable.ic_ft_doc);
		mFileTypeExtensionMap.put("docx", R.drawable.ic_ft_doc);
		mFileTypeExtensionMap.put("xls", R.drawable.ic_ft_xls);
		mFileTypeExtensionMap.put("xlsx", R.drawable.ic_ft_xls);
		mFileTypeExtensionMap.put("pdf", R.drawable.ic_ft_pdf);
		mFileTypeExtensionMap.put("htm", R.drawable.ic_ft_htm);
		mFileTypeExtensionMap.put("html", R.drawable.ic_ft_htm);
		mFileTypeExtensionMap.put("bmp", R.drawable.ic_ft_bmp);
		mFileTypeExtensionMap.put("gif", R.drawable.ic_ft_gif);
		mFileTypeExtensionMap.put("jpg", R.drawable.ic_ft_jpg);
		mFileTypeExtensionMap.put("jpeg", R.drawable.ic_ft_jpg);
		mFileTypeExtensionMap.put("png", R.drawable.ic_ft_png);
		mFileTypeExtensionMap.put("tif", R.drawable.ic_ft_tiff);
		mFileTypeExtensionMap.put("tiff", R.drawable.ic_ft_tiff);
	}
	
	public static int getFileTypeIconFromExtension(String extension) {
		Integer result = mFileTypeExtensionMap.get(extension);
		if (result == null) {
			result = R.drawable.ic_ft_generic;
		}
		
		return result;
	}
	
	public static class FileInfo {
		public String filename;
		public String name;
		public String extension;
		public String parent;
	}
	
	public static FileInfo splitFileName(String path) {
		FileInfo fi = new FileInfo();
		
		int index = path.lastIndexOf("/");
		if (index == -1) {
			fi.filename = path;
		} else {
			fi.filename = path.substring(index+1);
			fi.parent = path.substring(0, index);
		}
		
		index = fi.filename.lastIndexOf(".");
		
		if (index == -1) {
			fi.name = fi.filename;
		} else {
		
			fi.name = fi.filename.substring(0, index);
			fi.extension = fi.filename.substring(index+1);
		}
		return fi;
	}
	
}
