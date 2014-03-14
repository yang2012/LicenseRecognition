package cn.edu.nju.software.plate.net;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.nju.software.plate.asset.App;
import cn.edu.nju.software.plate.asset.Messages;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class PlateRecognitionThread extends Thread {

	String iamgeUri;
	Handler handler;

	public PlateRecognitionThread(String imageUri, Handler handler) {
		this.iamgeUri = imageUri;

		this.handler = handler;
	}

	public void run() {
		Message msg = Message.obtain();
		HttpResponse response;
		String entity;
		File file = new File(iamgeUri);
		HttpPost postMethod = new HttpPost(App.baseUrl);
		MultipartEntity mpEntity = new MultipartEntity();
		ContentBody cbFile = new FileBody(file);
		mpEntity.addPart("image", cbFile);
		try {
			postMethod.setEntity(mpEntity);
			synchronized (App.GetHttpClient()) {
				response = App.GetHttpClient().execute(postMethod);
				entity = EntityUtils.toString(response.getEntity());
				postMethod.abort();
			}
			Log.v("response", entity);
			msg.what = Messages.MSG_PHOTO_OK;
			msg.obj = entity;
		} catch (IOException e) {
			Log.e("Exception", e.getMessage());
			msg.what = Messages.MSG_NET_CONNECTION_ERROR;
			msg.obj = "无法连接服务器";
		} finally {
			handler.sendMessage(msg);
		}

	}
}
