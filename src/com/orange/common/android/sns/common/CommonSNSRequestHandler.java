package com.orange.common.android.sns.common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.orange.common.android.utils.codec.Base64;
import com.orange.common.android.utils.codec.HMAC_SHA1;

public abstract class CommonSNSRequestHandler {

	private final static String LOG_TAG = "SNSRequest";
	
	protected CommonSNSRequest		request;
	private   List<OAuthParameter>	paramList = new ArrayList<OAuthParameter>();
	
	public CommonSNSRequestHandler(CommonSNSRequest req){
		this.request = req;
	}
	
	protected CommonSNSRequestHandler(){		
	}
	
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";
	
	class OAuthParameter implements Comparable<OAuthParameter> {
		public String name;
		public String value;
		
		public OAuthParameter(String n, String v){
			this.name = n;
			this.value = v;
		}

		@Override
		public int compareTo(OAuthParameter obj) {
			int result = name.compareTo(obj.name);			
			if (result == 0){
				return value.compareTo(obj.value);
			}
			else{
				return result;
			}
		}
	}
	
	public JSONObject execute(Map<String, String> params) {
				
		try {
			if (buildCommonParameters() == false)
				return null;
			
			if (params != null){			
				if (addParameters(params) == false)
					return null;
			}

			if (signParameters() == false)
				return null;
			
			String response = null;
			if (this.getHttpMethod().equalsIgnoreCase(METHOD_GET)){
				// send get request
				String url = this.getURLByParameterList(paramList, getBaseURL());
				response = this.sendHttpGetRequest(url);
			}
			else{
				// send post request
				List<NameValuePair> dataList = this.getNVByParameterList(paramList);
				response = sendHttpPostRequest(getBaseURL(), dataList);
			}
			
			return parseResponse(response);
			
		} catch (UnsupportedEncodingException e) {
			Log.e(LOG_TAG, "<execute> catch UnsupportedEncodingException="+e.toString());
		} catch (GeneralSecurityException e) {
			Log.e(LOG_TAG, "<execute> catch GeneralSecurityException="+e.toString());
		} catch (ParseException e) {
			Log.e(LOG_TAG, "<execute> catch ParseException="+e.toString());
		} catch (IOException e) {
			Log.e(LOG_TAG, "<execute> catch IOException="+e.toString());
		} catch (JSONException e) {
			Log.e(LOG_TAG, "<execute> catch JSONException="+e.toString());
		}
		
		return null;
	}

	
	// don't override this method
	public boolean buildCommonParameters(){
		paramList = buildCommonParameterList(request);
		return (paramList != null);
	}

	// don't overried this method
	// return signature
	public boolean signParameters() throws UnsupportedEncodingException, GeneralSecurityException{
		String signature = this.signParameter(
								this.paramList, 
								this.getBaseURL(), 
								this.getHttpMethod(), 
								request.getAppSecret(),
								request.getOauthTokenSecret());

		return (signature != null);
	}
	
	abstract public boolean addParameters(Map<String, String> params);
	
	abstract public String getBaseURL();
	
	abstract public String getHttpMethod();
	
	abstract public JSONObject parseResponse(String response) throws JSONException;		
	
	private String signParameter(List<OAuthParameter> paramList, String baseURL, String httpMethod, String oauthConsumerSecret, String oauthTokenSecret) throws UnsupportedEncodingException, GeneralSecurityException{

		// sort parameters
		Collections.sort(paramList);
		
		// encode parameter string
		StringBuilder builder = new StringBuilder();
		int lastIndex = paramList.size() - 1;
		for (OAuthParameter param : paramList){
			builder.append(URLEncoder.encode(param.name, "UTF-8"));
			builder.append("%3D");
			builder.append(
					URLEncoder.encode(
					URLEncoder.encode(param.value, "UTF-8"), "UTF-8"));
			if (paramList.indexOf(param) != lastIndex){
				builder.append("%26");
			}			
		}		
		String paramString = builder.toString();
		
		// now we can build Base String for signature
		builder = new StringBuilder();
		builder.append(httpMethod);
		builder.append("&");
		builder.append(URLEncoder.encode(baseURL, "UTF-8"));
		builder.append("&");
		builder.append(paramString);
		String baseString = builder.toString();
		Log.d(LOG_TAG, "<signParameter> baseString="+baseString);
		
		// build signature key
		// TODO use Token also later
		String signatureKey = oauthConsumerSecret.concat("&");
		if (oauthTokenSecret != null){
			signatureKey = signatureKey.concat(oauthTokenSecret);
		}
		Log.d(LOG_TAG, "<signParameter> signatureKey="+signatureKey);
		
		// sign Base String with Key
		byte[] encryptBytes = HMAC_SHA1.encode(baseString, signatureKey);
		String signature = Base64.encode(encryptBytes);
		Log.d(LOG_TAG, "<signParameter> signature="+signature);
		
		// add signature into parameter list
		paramList.add(new OAuthParameter("oauth_signature", signature));
		return signature;
	}
	
	protected boolean addParam(String name, String value){
		if (name == null || value == null)
			return false;
		
		paramList.add(new OAuthParameter(name, value));
		return true;
	}
	
	private List<OAuthParameter> buildCommonParameterList(CommonSNSRequest request){
		List<OAuthParameter> paramList = new ArrayList<OAuthParameter>();
		paramList.add(new OAuthParameter("oauth_consumer_key", request.getAppKey()));
		paramList.add(new OAuthParameter("oauth_nonce", UUID.randomUUID().toString().replaceAll("-", "")));
		paramList.add(new OAuthParameter("oauth_signature_method", "HMAC-SHA1"));
		paramList.add(new OAuthParameter("oauth_timestamp", String.valueOf(System.currentTimeMillis()/1000)));
		paramList.add(new OAuthParameter("oauth_version", "1.0"));
		if (request.getOauthToken() != null){
			paramList.add(new OAuthParameter("oauth_token", request.getOauthToken()));
		}
		
		return paramList;
	}
	
	private String getURLByParameterList(List<OAuthParameter> paramList, String baseURL) throws UnsupportedEncodingException{

		String queryString = getDataByParameterList(paramList, true);
		
		// build the final URL request
		String fullURL = baseURL.concat("?").concat(queryString);
		return fullURL;
	}
	
	private String getDataByParameterList(List<OAuthParameter> paramList, boolean needURLEncode) throws UnsupportedEncodingException{
		StringBuilder builder = new StringBuilder();
		int lastIndex = paramList.size() - 1;
		int index = 0;
		for (OAuthParameter param : paramList){
			builder.append(param.name);
			builder.append("=");
			if (needURLEncode)
				builder.append(URLEncoder.encode(param.value, "UTF-8"));
			else
				builder.append(param.value);
			if (index != lastIndex)
				builder.append("&");
			index ++;
		}
		return builder.toString();
	}
	
	private List <NameValuePair> getNVByParameterList(List<OAuthParameter> paramList){
		List <NameValuePair> nvList = new ArrayList <NameValuePair>();
		for (OAuthParameter param : paramList){
			nvList.add(new BasicNameValuePair(param.name, param.value));
		}		
		return nvList;
	}
	
	private String sendHttpGetRequest(String url){
		
		Log.i(LOG_TAG, "[SEND] HTTP GET : "+url);

		// send request
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		ResponseHandler<String> strRespHandler = new BasicResponseHandler(); // TODO change implementation, refer to POST
		String response = null;
		try {
			response = httpclient.execute(httpGet, strRespHandler);
			Log.i(LOG_TAG, "[RECV] HTTP GET Response : " + response);
		} catch (Exception e) {
			Log.e(LOG_TAG, "[RECV] HTTP GET Response but Catch Exception : "+e.toString());
		}
		
		return response;
	}
	
	private String sendHttpPostRequest(String url, List<NameValuePair> nvList) throws ParseException, IOException{
		
		// send request
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);		
		httpPost.setEntity(new UrlEncodedFormEntity(nvList, HTTP.UTF_8));
		httpPost.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE,
				false); // avoid 417 Expect Failure		
		
		HttpResponse response = null;
		String responseData = null;
		try {
			Log.i(LOG_TAG, "[SEND] HTTP POST, URL = "+url+", POST DATA = "+EntityUtils.toString(httpPost.getEntity()));
			response = httpclient.execute(httpPost);
			if (response.getStatusLine().getStatusCode() == 200){
				responseData = EntityUtils.toString(response.getEntity());
				Log.i(LOG_TAG, "[RECV] HTTP 200 OK, Data : "+ responseData); 
			}
			else{
				Log.i(LOG_TAG, "[RECV] HTTP Response Error, Status = " + response.getStatusLine().getStatusCode() 
						+ ", Info = " + EntityUtils.toString(response.getEntity()));
			}
		} catch (Exception e) {
			Log.e(LOG_TAG, "[SEND] HTTP POST, URL = "+url+", Catch Exception=" + e.toString());
		}
		
		return responseData;
	}
	
	protected Map<String, String> parseSimpleResponse(String response){
		Map<String, String> responseMap = new HashMap<String, String>();
		if (response == null){
			return responseMap;
		}
		
		String[] keyValues = response.split("&");
		if (keyValues != null && keyValues.length > 0){
			for (int i=0; i<keyValues.length; i++){
				String[] keyValue = keyValues[i].split("=");
				if (keyValue != null && keyValue.length >= 2){
					responseMap.put(keyValue[0], keyValue[1]);
				}
			}
		}

		return responseMap;
	}
	
	protected JSONObject parseJSONResponse(String response) throws JSONException {
		if (response == null){
			return null;
		}
		
		JSONObject json = new JSONObject(response);
		return json;
	}
	
	protected void safeSetKeyValue(JSONObject fromObj, JSONObject toObj, String fromKey, String toKey){
		if (!fromObj.has(fromKey))
			return;
		
		try {
			String value = fromObj.getString(fromKey);
			if (value == null)
				return;
			toObj.put(toKey, value);
		} catch (JSONException e) {
			// do nothing
		}
	}

}
