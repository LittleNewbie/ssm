package com.svili.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

/**
 * FTP上传/下载
 * 
 * @author lishiwei
 * @data 2017年4月18日
 *
 */
public class FtpUtil {
	/**
	 * 上传文件至FTP服务器</br>
	 * 此方法不会关闭输入流(input)
	 * 
	 * @param host
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口,默认21
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param filePath
	 *            FTP服务器文件存放路径。例如分日期存放：/2015/01/01.可为空,空值表示根目录
	 * @param filename
	 *            上传到FTP服务器上的文件名,不能为空
	 * @param input
	 *            输入流
	 * @return 成功返回true，否则返回false
	 */
	public static boolean uploadFile(String host, int port, String username, String password, String filePath,
			String filename, InputStream input) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			// 连接FTP服务器
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftp.connect(host, port);
			ftp.login(username, password);
			int reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				throw new Exception("failed to get ftp connection ");
			}
			if (filePath == null || filePath.trim().equals("")) {
				filePath = "/";
			}
			// 切换到上传目录
			if (!ftp.changeWorkingDirectory(filePath)) {
				// 如果目录不存在创建目录
				String[] dirs = filePath.split("/");
				String path = "";
				for (String dir : dirs) {
					if (null == dir || "".equals(dir)) {
						continue;
					}
					path += "/" + dir;
					if (!ftp.changeWorkingDirectory(path)) {
						if (!ftp.makeDirectory(path)) {
							ftp.disconnect();
							throw new Exception("create dir erro : " + path);
						}
					}
				}
			}
			// 设置上传文件的类型为二进制类型
			ftp.setFileType(FTP.BINARY_FILE_TYPE);
			//文件名不能为空
			if(filename == null || filename.trim().equals("")){
				throw new Exception("fileName is not allow to null ");
			}
			// 上传文件
			if (ftp.storeFile(filename, input)) {
				result = true;
			} else {
				ftp.disconnect();
				throw new Exception("failed to upload file ");
			}
			ftp.logout();
		} catch (Exception e) {
			LogUtil.info("", e);
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException e) {
					LogUtil.info("ftp disconnect erro", e);
				}
			}
		}
		return result;
	}
	
	/**
	 * Description: 从FTP服务器下载文件
	 * 
	 * @param host
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param fileName
	 *            要下载的文件名
	 * @return InputStream
	 */
	public static InputStream downloadFile(String host, int port, String username, String password, String remotePath,
			String fileName) {
		InputStream result = null;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host, port);
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				throw new Exception("failed to get ftp connection ");
			}
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					result = ftp.retrieveFileStream(ff.getName());
				}
			}
			ftp.logout();
		} catch (Exception e) {

		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
					
				}
			}
		}
		return result;
	}

	/**
	 * Description: 从FTP服务器下载文件
	 * 
	 * @param host
	 *            FTP服务器hostname
	 * @param port
	 *            FTP服务器端口
	 * @param username
	 *            FTP登录账号
	 * @param password
	 *            FTP登录密码
	 * @param remotePath
	 *            FTP服务器上的相对路径
	 * @param fileName
	 *            要下载的文件名
	 * @param localPath
	 *            下载后保存到本地的路径
	 * @return
	 */
	public static boolean downloadFile(String host, int port, String username, String password, String remotePath,
			String fileName, String localPath) {
		boolean result = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(host, port);
			// 如果采用默认端口，可以使用ftp.connect(host)的方式直接连接FTP服务器
			ftp.login(username, password);// 登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return result;
			}
			ftp.changeWorkingDirectory(remotePath);// 转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for (FTPFile ff : fs) {
				if (ff.getName().equals(fileName)) {
					File localFile = new File(localPath + "/" + ff.getName());

					OutputStream is = new FileOutputStream(localFile);
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}

			ftp.logout();
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return result;
	}
}
