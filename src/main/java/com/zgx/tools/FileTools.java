package com.zgx.tools;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import io.netty.buffer.ByteBuf;

public class FileTools {
	public static String[] getFileDir(String path) {
		String result[] = null;
		ArrayList<String> dirList = new ArrayList<String>();
		File file = new File(path);
		File[] tmpList = file.listFiles();
		for (int i = 0; i < tmpList.length; i++) {
			if (tmpList[i].isDirectory()) {
				String dir = tmpList[i].toURI().getPath();
				dirList.add(dir.substring(0, dir.length() - 1));
			}
		}
		result = new String[dirList.size()];
		dirList.toArray(result);
		return result;

	}

	public static boolean exitsOrCreateFile(String fileName) {
		File file = new File(fileName);
		if (file.exists())
			return true;
		else
			return FileTools.createFile(fileName);

	}
	
	public static boolean createDir(String dirPath) {
		File file = new File(dirPath);
		if(!file.exists()) {
			return file.mkdirs();
		}
		if(file.isDirectory()) {
			return true;
		}
		return false;
	}

	public static boolean createFile(String fileName) {
		boolean result = false;
		File file = new File(fileName);
		if (file.exists()) {
			System.out.println("创建单个文件" + fileName + "失败，目标文件已存在！");
			return false;
		}
		if (fileName.endsWith(File.separator)) {
			System.out.println("创建单个文件" + fileName + "失败，目标文件不能为目录！");
			return false;
		}
		// 判断目标文件所在的目录是否存在
		if (!file.getParentFile().exists()) {
			// 如果目标文件所在的目录不存在，则创建父目录
			System.out.println("目标文件所在目录不存在，准备创建它！");
			if (!file.getParentFile().mkdirs()) {
				System.out.println("创建目标文件所在目录失败！");
				return false;
			}
		}
		// 创建目标文件
		try {
			if (file.createNewFile()) {
				System.out.println("创建单个文件" + fileName + "成功！");
				return true;
			} else {
				System.out.println("创建单个文件" + fileName + "失败！");
				return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("创建单个文件" + fileName + "失败！" + e.getMessage());
			return false;
		}

	}

	public static boolean overrideFileByBytes(byte[] buf, String fileName) {
		boolean result = false;
		FileTools.exitsOrCreateFile(fileName);
		File file = new File(fileName);
		FileOutputStream fileOs = null;
		try {
			fileOs = new FileOutputStream(file);
			fileOs.write(buf);
			result = true;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			result = false;
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (fileOs != null) {
				try {
					fileOs.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return result;

	}

	public static void main(String[] args) {
		String path = Class.class.getClass().getResource("/").getPath();
		System.out.println(path);
		String dirs[] = FileTools.getFileDir(path);
		for (int i = 0; i < dirs.length; i++) {
			System.out.println(dirs[i]);
		}
		exitsOrCreateFile(path + "/work/com/zgx/jsp/test_jsp.java");
	    String fileName = path + "/work/com/zgx/jsp/test_jsp.java";
	    FileTools.overrideFileByBytes("package fdsfasd".getBytes(), fileName);

	}
}
