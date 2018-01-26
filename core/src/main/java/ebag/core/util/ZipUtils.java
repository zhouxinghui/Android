package ebag.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

public class ZipUtils {

	private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte

	/**
	 * 给定根目录，返回一个相对路径所对应的实际文件名.
	 * 
	 * @param baseDir
	 *            指定根目录
	 * @param absFileName
	 *            相对路径名，来自于ZipEntry中的name
	 * @return java.io.File 实际的文件
	 */

	/**
	 * 解压缩一个文件
	 * 
	 * @param zipFile
	 *            压缩文件
	 * @param folderPath
	 *            解压缩的目标目录
	 * @throws IOException
	 *             当解压缩过程出错时抛出
	 */
	public static void upZipFile(File zipFile, String folderPath)
			throws ZipException, IOException {
		File desDir = new File(folderPath);
		if (!desDir.exists()) {
			desDir.mkdirs();
		}
		ZipFile zf = new ZipFile(zipFile);
		for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();) {
			ZipEntry entry = ((ZipEntry) entries.nextElement());
			InputStream in = zf.getInputStream(entry);
			String str = null;
			if (folderPath.endsWith(File.separator)) {
				str = folderPath + entry.getName();
			} else {
				str = folderPath + File.separator + entry.getName();
			}

			str = new String(str.getBytes("8859_1"), "utf-8");
			System.out.println("str===============>" + str);
			File desFile = new File(str.replaceAll("jpg","ebag"));
			if (str.endsWith(File.separator)) {
				desFile.mkdirs();
				desFile.mkdir();
				in.close();
				continue;
			}
			if (!desFile.exists()) {
				File fileParentDir = desFile.getParentFile();
				if (!fileParentDir.exists()) {
					fileParentDir.mkdirs();
				}
				if (str.endsWith("/")) {
					desFile.mkdirs();
				} else {
					desFile.createNewFile();
				}

			}
			OutputStream out = new FileOutputStream(desFile);
			byte buffer[] = new byte[BUFF_SIZE];
			int realLength;
			while ((realLength = in.read(buffer)) > 0) {
				out.write(buffer, 0, realLength);
			}
			in.close();
			out.flush();
			out.close();
		}
	}

	/**
	 * 获取指定文件夹中所有图片路径
	 * 
	 * @param folderPath
	 * @return
	 */
	public static ArrayList<String> getAllImgs(String folderPath) {
		ArrayList<String> list = new ArrayList<String>();
		File folder = new File(folderPath);
		String[] fileNames = folder.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".ebag") || name.endsWith(".jpg") || name.endsWith(".png")
						|| name.endsWith(".jpeg");
			}
		});

		for (String name : fileNames) {
			if (folderPath.endsWith(File.separator)) {
				name = folderPath + name;
			} else {
				name = folderPath + File.separator + name;
			}

			list.add(name);
		}

		return list;
	}

	/**
	 * 获得压缩文件内文件列表
	 * 
	 * @param zipFile
	 *            压缩文件
	 * @return 压缩文件内文件名称
	 * @throws ZipException
	 *             压缩文件格式有误时抛出
	 * @throws IOException
	 *             当解压缩过程出错时抛出
	 */
	public ArrayList<String> getEntriesNames(File zipFile) throws ZipException,
            IOException {
		ArrayList<String> entryNames = new ArrayList<String>();
		Enumeration<?> entries = getEntriesEnumeration(zipFile);
		while (entries.hasMoreElements()) {
			ZipEntry entry = ((ZipEntry) entries.nextElement());
			entryNames.add(new String(getEntryName(entry).getBytes("GB2312"),
					"8859_1"));
		}
		return entryNames;
	}

	/**
	 * 获得压缩文件内压缩文件对象以取得其属性
	 * 
	 * @param zipFile
	 *            压缩文件
	 * @return 返回一个压缩文件列表
	 * @throws ZipException
	 *             压缩文件格式有误时抛出
	 * @throws IOException
	 *             IO操作有误时抛出
	 */
	public Enumeration<?> getEntriesEnumeration(File zipFile)
			throws ZipException, IOException {
		ZipFile zf = new ZipFile(zipFile);
		return zf.entries();

	}

	/**
	 * 取得压缩文件对象的名称
	 * 
	 * @param entry
	 *            压缩文件对象
	 * @return 压缩文件对象的名称
	 * @throws UnsupportedEncodingException
	 */
	public String getEntryName(ZipEntry entry)
			throws UnsupportedEncodingException {
		return new String(entry.getName().getBytes("GB2312"), "8859_1");
	}
}
