package cn.edu.nju.software.plate.asset;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

public class App {

	public static String baseUrl = "http://192.168.61.132:8183";
	public static final String basePath = "/mnt/sdcard/PlateRecognition";
	private static HttpClient client;

	public static HttpClient GetHttpClient() {
		if (client == null) {
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params, 120000);
			HttpConnectionParams.setSoTimeout(params, 120000);
			client = new DefaultHttpClient(params);
		}
		return client;

	}

}
