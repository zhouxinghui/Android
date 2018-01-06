package ebag.core.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.common.utils.BinaryUtil;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OSSUploadUtils {
	private static OSSUploadUtils mUploadUtils;
	private boolean isUploadSuccess = false;

	public static OSSUploadUtils getInstance() {
		if (null == mUploadUtils) {
			mUploadUtils = new OSSUploadUtils();
		}
		return mUploadUtils;
	}

	public void uploadFileToOss(Context context, File file, String folder, String name,
								   final Handler handler) {
		upload(context, file, folder, name, handler, "application/octet-stream");
	}

	/**
	 * 上传图片到阿里云服务器
	 * 
	 * @param photo
	 *            图片
	 * @param folder
	 *            阿里云上的目录
	 * @param name
	 *            文件名不带后缀
	 * @return
	 */
	public void UploadPhotoToOSS(Context context, final Bitmap photo, String folder,
                                 String name, final Handler handler) {
		byte[] dataToUpload = bitmap2Bytes(photo);
		String photoName = folder + "/" + name;

		String endpoint = "oss-cn-shenzhen.aliyuncs.com";
		// 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的访问控制章节
		OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("c7L7E1xQcG1rgHWU", "kTAh2vQin1hfPskWZ5t0q0Jrxs5lqG");
		ClientConfiguration conf = new ClientConfiguration();
		conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
		conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
		conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
		conf.setMaxErrorRetry(1); // 失败后最大重试次数，默认2次
		OSS oss = new OSSClient(context, endpoint, credentialProvider, conf);

		// 构造上传请求
		PutObjectRequest put = new PutObjectRequest("ebag-public-resource", photoName, dataToUpload);
		ObjectMetadata metadata = new ObjectMetadata();
		// 指定Content-Type
		metadata.setContentType("image/jpeg");
		metadata.setContentMD5(BinaryUtil.calculateBase64Md5(dataToUpload));
		put.setMetadata(metadata);

		// 异步上传时可以设置进度回调
		put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
			@Override
			public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
				Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
			}
		});
		OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
			@Override
			public void onSuccess(PutObjectRequest request, PutObjectResult result) {
				Log.d("PutObject", "UploadSuccess");
				Log.d("ETag", result.getETag());
				Log.d("RequestId", result.getRequestId());
				isUploadSuccess = true;
				Message msg = new Message();
				msg.what = Constants.INSTANCE.getUPLOAD_SUCCESS();
				handler.sendMessage(msg);
			}
			@Override
			public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
				// 请求异常
				if (clientExcepion != null) {
					// 本地异常如网络异常等
					clientExcepion.printStackTrace();
				}
				if (serviceException != null) {
					// 服务异常
					Log.e("ErrorCode", serviceException.getErrorCode());
					Log.e("RequestId", serviceException.getRequestId());
					Log.e("HostId", serviceException.getHostId());
					Log.e("RawMessage", serviceException.getRawMessage());
				}
				Message msg = new Message();
				msg.what = Constants.INSTANCE.getUPLOAD_FAIL();
				handler.sendMessage(msg);
			}
		});
	}

	/**
	 * 上传图片到阿里云服务器
	 *
	 * @param file
	 *            图片
	 * @param folder
	 *            阿里云上的目录
	 * @param name
	 *            文件名不带后缀
	 * @return
	 */
	public void UploadPhotoToOSS(Context context, File file, String folder,
                                 String name, final Handler handler) {
		upload(context, file, folder, name, handler, "image/jpeg");
	}

	private void upload(Context context, File file, String folder,
						String name, final Handler handler, String contentType){
		byte[] dataToUpload = getBytes(file.getAbsolutePath());
		String fileName = folder + "/" + name;

		String endpoint = "oss-cn-shenzhen.aliyuncs.com";
		// 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的访问控制章节
		OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("c7L7E1xQcG1rgHWU", "kTAh2vQin1hfPskWZ5t0q0Jrxs5lqG");
		ClientConfiguration conf = new ClientConfiguration();
		conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
		conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
		conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
		conf.setMaxErrorRetry(1); // 失败后最大重试次数，默认2次
		OSS oss = new OSSClient(context, endpoint, credentialProvider, conf);

		// 构造上传请求
		PutObjectRequest put = new PutObjectRequest("ebag-public-resource", fileName, dataToUpload);
		ObjectMetadata metadata = new ObjectMetadata();
		// 指定Content-Type
		metadata.setContentType(contentType);
//		metadata.setContentMD5(BinaryUtil.calculateBase64Md5(dataToUpload));
		put.setMetadata(metadata);

		// 异步上传时可以设置进度回调
		put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
			@Override
			public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
				Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
			}
		});
		OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
			@Override
			public void onSuccess(PutObjectRequest request, PutObjectResult result) {
				Log.d("PutObject", "UploadSuccess");
				Log.d("ETag", result.getETag());
				Log.d("RequestId", result.getRequestId());
				isUploadSuccess = true;
				Message msg = new Message();
				msg.what = Constants.INSTANCE.getUPLOAD_SUCCESS();
				handler.sendMessage(msg);
			}
			@Override
			public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
				// 请求异常
				if (clientExcepion != null) {
					// 本地异常如网络异常等
					clientExcepion.printStackTrace();
				}
				if (serviceException != null) {
					// 服务异常
					Log.e("ErrorCode", serviceException.getErrorCode());
					Log.e("RequestId", serviceException.getRequestId());
					Log.e("HostId", serviceException.getHostId());
					Log.e("RawMessage", serviceException.getRawMessage());
				}
				Message msg = new Message();
				msg.what = Constants.INSTANCE.getUPLOAD_FAIL();
				handler.sendMessage(msg);
			}
		});
	}

	/**
	 * 上传图片到阿里云服务器 同步方法
	 *
	 * @param photo
	 *            图片
	 * @param folder
	 *            阿里云上的目录
	 * @param name
	 *            文件名不带后缀
	 * @return
	 */
	public static boolean UploadPhotoToOSS(Context context, Bitmap photo, String folder,
                                           String name) {
		byte[] dataToUpload = bitmap2Bytes(photo);
		photo.recycle();

		String photoName = folder + "/" + name;

		String endpoint = "oss-cn-shenzhen.aliyuncs.com";
		// 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的访问控制章节
		OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("c7L7E1xQcG1rgHWU", "kTAh2vQin1hfPskWZ5t0q0Jrxs5lqG");
		ClientConfiguration conf = new ClientConfiguration();
		conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
		conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
		conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
		conf.setMaxErrorRetry(1); // 失败后最大重试次数，默认2次
		OSS oss = new OSSClient(context, endpoint, credentialProvider, conf);

		// 构造上传请求
		PutObjectRequest put = new PutObjectRequest("ebag-public-resource", photoName, dataToUpload);
		ObjectMetadata metadata = new ObjectMetadata();
		// 指定Content-Type
		metadata.setContentType("application/octet-stream");
		metadata.setContentMD5(BinaryUtil.calculateBase64Md5(dataToUpload));
		put.setMetadata(metadata);

		try {
			PutObjectResult putResult = oss.putObject(put);
			Log.d("PutObject", "UploadSuccess");
			Log.d("ETag", putResult.getETag());
			Log.d("RequestId", putResult.getRequestId());
			return true;
		} catch (ClientException e) {
			// 本地异常如网络异常等
			e.printStackTrace();
			return false;
		} catch (ServiceException e) {
			// 服务异常
			Log.e("RequestId", e.getRequestId());
			Log.e("ErrorCode", e.getErrorCode());
			Log.e("HostId", e.getHostId());
			Log.e("RawMessage", e.getRawMessage());
			return false;
		}
	}

	public static byte[] bitmap2Bytes(Bitmap bm) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
		return baos.toByteArray();
	}

	/**
	 * 获得指定文件的byte数组
	 */
	private byte[] getBytes(String filePath) {
		byte[] buffer = null;
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
			byte[] b = new byte[1000];
			int n;
			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			fis.close();
			bos.close();
			buffer = bos.toByteArray();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return buffer;
	}

}
