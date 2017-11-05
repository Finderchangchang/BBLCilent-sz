package com.beibeilian.util.fileupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import com.beibeilian.receiver.ReceiverConstant;
import com.beibeilian.util.HelperUtil;
import com.beibeilian.util.HttpConstantUtil;
import com.beibeilian.util.fileupload.CustomMultiPartEntity.ProgressListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * ץ���ϴ�
 *
 */
public class CrashFileUploadMultipartPost extends AsyncTask<String, Integer, String> {

	long totalSize;// �ļ��ܴ�С
	Context context;// ����������
	private String path;// ����·��

	public CrashFileUploadMultipartPost(Context context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {

	}

	@Override
	protected void onProgressUpdate(Integer... progress) {

	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}

	@Override
	protected String doInBackground(String... params) {
		try {
			path = params[0];
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext httpContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost(HttpConstantUtil.FILEUpload);
			CustomMultiPartEntity multipartContent = new CustomMultiPartEntity(new ProgressListener() {

				@Override
				public void transferred(long num) {
					// TODO Auto-generated method stub
					publishProgress((int) ((num / (float) totalSize) * 100));
				}
			});
			multipartContent.addPart("file", new FileBody(new File(path)));
			totalSize = multipartContent.getContentLength();
			httpPost.setEntity(multipartContent);
			HttpResponse response;
			response = httpClient.execute(httpPost, httpContext);
			String serverResponse = EntityUtils.toString(response.getEntity());
			JSONObject jsonObject = new JSONObject(serverResponse);
			int res = jsonObject.getInt("result");
			if (res > 0) {
				new File(path).delete();
			}
			return String.valueOf(res);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}