package com.orange.sns.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.orange.utils.codec.Base64;
import com.orange.utils.codec.HMAC_SHA1;


import com.orange.sns.common.CommonSNSRequest;
import com.orange.sns.common.CommonSNSRequestHandler;
import com.orange.sns.sina.SinaSNSRequest;

public class SNSService {
	
	public static final String METHOD_POST = "POST";
	public static final String METHOD_GET = "GET";
	public static final String LOG_CAT = "SNSService";
	
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
	
	public String signParameter(List<OAuthParameter> paramList, String baseURL, String httpMethod, String oauthConsumerSecret, String oauthTokenSecret) throws UnsupportedEncodingException, GeneralSecurityException{

		// sort parameters
		Collections.sort(paramList);
		
		// encode parameter string
		StringBuilder builder = new StringBuilder();
		int lastIndex = paramList.size() - 1;
		for (OAuthParameter param : paramList){
			if (param.name != null && param.value != null){
				builder.append(URLEncoder.encode(param.name, "UTF-8"));
				builder.append("%3D");
				builder.append(
						URLEncoder.encode(
						URLEncoder.encode(param.value, "UTF-8"), "UTF-8"));
				if (paramList.indexOf(param) != lastIndex){
					builder.append("%26");
				}			
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
		Log.i("", "baseString="+baseString);
		
		// build signature key
		// TODO use Token also later
		String signatureKey = oauthConsumerSecret.concat("&");
		if (oauthTokenSecret != null){
			signatureKey = signatureKey.concat(oauthTokenSecret);
		}
		Log.i("", "signatureKey="+signatureKey);
		
		// sign Base String with Key
		byte[] encryptBytes = HMAC_SHA1.encode(baseString, signatureKey);
		String signature = Base64.encode(encryptBytes);
		Log.i("", "signature="+signature);
		
		// add signature into parameter list
		paramList.add(new OAuthParameter("oauth_signature", signature));
		return signature;
	}
	
	public List<OAuthParameter> buildCommonParameterList(String oauthConsumerKey){
		List<OAuthParameter> paramList = new ArrayList<OAuthParameter>();
		paramList.add(new OAuthParameter("oauth_consumer_key", oauthConsumerKey));
		paramList.add(new OAuthParameter("oauth_nonce", UUID.randomUUID().toString()));
		paramList.add(new OAuthParameter("oauth_signature_method", "HMAC-SHA1"));
		paramList.add(new OAuthParameter("oauth_timestamp", String.valueOf(System.currentTimeMillis()/1000)));
		paramList.add(new OAuthParameter("oauth_version", "1.0"));
		
		return paramList;
	}
	
	public String getURLByParameterList(List<OAuthParameter> paramList, String baseURL) throws UnsupportedEncodingException{

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
	
	public String getAuthorizeURL(CommonSNSRequest snsRequest) throws UnsupportedEncodingException, GeneralSecurityException{
		
		// create a prameter list
		List<OAuthParameter> paramList = buildCommonParameterList(snsRequest.getAppKey());
		paramList.add(new OAuthParameter("oauth_token", snsRequest.getOauthToken()));
//		paramList.add(new OAuthParameter("oauth_callback", snsRequest.getCallbackURL()));
		
		// do oauth signature on parameter list
		signParameter(paramList, snsRequest.getAuthorizeBaseURL(), METHOD_GET, snsRequest.getAppSecret(), snsRequest.getOauthTokenSecret());
		
		// get URL by final parameter list
		String url = getURLByParameterList(paramList, snsRequest.getAuthorizeBaseURL());
				
		return url;
	}
	
	public String sendHttpRequest(String url){
		
		Log.i(LOG_CAT, "send request="+url);

		// send request
		HttpClient httpclient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		ResponseHandler<String> strRespHandler = new BasicResponseHandler();
		String response = null;
		try {
			response = httpclient.execute(httpGet, strRespHandler);
			Log.d(LOG_CAT, "Get http response: " + response);
		} catch (Exception e) {
			Log.e(LOG_CAT, "Send http request, exception: "+e.toString());
		}
		
		return response;
	}
	
	public String sendHttpPostRequest(String url, List<NameValuePair> nvList) throws ParseException, IOException{
		
//		Log.i(LOG_CAT, "send request="+url+", post data="+data);

		// send request
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(url);
//		StringEntity se = new StringEntity(data,HTTP.UTF_8);
//		se.setContentType("text/xml");  
//		httpPost.setHeader("Content-Type","application/text;charset=UTF-8");
//		httpPost.setEntity(se);  
//		httpPost.setEntity(new StringEntity(data, HTTP.UTF_8));
		
		httpPost.setEntity(new UrlEncodedFormEntity(nvList, HTTP.UTF_8));
		
		Log.i(LOG_CAT, "post final data="+EntityUtils.toString(httpPost.getEntity()));
		httpPost.getParams().setBooleanParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE,
				false); // avoid 417 Expect Failure
		HttpResponse response = null;
		String responseData = null;
		try {
			response = httpclient.execute(httpPost);
			responseData = EntityUtils.toString(response.getEntity());
			Log.d(LOG_CAT, "Get http response: " + responseData);
		} catch (Exception e) {
			Log.e(LOG_CAT, "Send http request, exception: "+e.toString());
		}
		
		return responseData;
	}
	
	public Map<String, String> parseSimpleResponse(String response){
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
	
	public boolean startAuthorization(CommonSNSRequest snsRequest) throws UnsupportedEncodingException, GeneralSecurityException {
		
		// step 1 : get request token
				
		// create a prameter list
		List<OAuthParameter> paramList = buildCommonParameterList(snsRequest.getAppKey());
//		paramList.add(new OAuthParameter("oauth_callback", snsRequest.getCallbackURL()));
		
		// do oauth signature on parameter list
		signParameter(paramList, snsRequest.getRequestTokenBaseURL(), METHOD_GET, snsRequest.getAppSecret(), null);
		
		// get URL by final parameter list
		String url = getURLByParameterList(paramList, snsRequest.getRequestTokenBaseURL());
		 
		// send request
		String response = sendHttpRequest(url);
		if (response == null)
			return false;
		
		// handle response
		Map<String, String> responseMap = parseSimpleResponse(response);
		String oauthToken = responseMap.get("oauth_token");
		String oauthTokenSecret = responseMap.get("oauth_token_secret");		
		snsRequest.setOauthToken(oauthToken);
		snsRequest.setOauthTokenSecret(oauthTokenSecret);		
		
		Log.i("OAuth", "save oauthToken="+snsRequest.getOauthToken()+", oauthTokenSecret="+snsRequest.getOauthTokenSecret());		
		
		// step 2 : redirect to authorization page
		//authorize(snsRequest);
		
		return true;
	}
	
	public boolean parseAuthorizationResponse(String responseURL, CommonSNSRequest snsRequest){
		return true;
	}
	
	public boolean publishWeibo(CommonSNSRequest snsRequest){
		return true;
	}

	public boolean getAccessToken(CommonSNSRequest snsRequest, String oauthVerfier) {
		// create a prameter list
		try {
			List<OAuthParameter> paramList = buildCommonParameterList(snsRequest.getAppKey());
			paramList.add(new OAuthParameter("oauth_token", snsRequest.getOauthToken()));
			paramList.add(new OAuthParameter("oauth_verifier", oauthVerfier));
//			paramList.add(new OAuthParameter("oauth_token_secret", snsRequest.getOauthTokenSecret()));						
				
			// do oauth signature on parameter list
			signParameter(paramList, snsRequest.getAccessTokenBaseURL(), METHOD_POST, snsRequest.getAppSecret(), snsRequest.getOauthTokenSecret());
			
			// get URL by final parameter list
			String url = snsRequest.getAccessTokenBaseURL(); // getURLByParameterList(paramList, snsRequest.getAccessTokenBaseURL());
			List<NameValuePair> nvList = getNVByParameterList(paramList);
			 
			//sendHttpPostRequest("http://192.168.1.188:8000/test", "?m=hello");
			
			// send request
			String response = sendHttpPostRequest(url, nvList);
			if (response == null)
				return false;
			
			// handle response
			Map<String, String> responseMap = parseSimpleResponse(response);
			String oauthToken = responseMap.get("oauth_token");
			String oauthTokenSecret = responseMap.get("oauth_token_secret");		
			snsRequest.setOauthToken(oauthToken);
			snsRequest.setOauthTokenSecret(oauthTokenSecret);	
			
			JSONObject json = getUserInfo1(snsRequest);
			if (json != null){
				Log.i(LOG_CAT, "User JSON="+json.toString());
			}
			
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return false;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean sendWeibo(CommonSNSRequest snsRequest, String text) {
		
		// create a prameter list
		try {
			List<OAuthParameter> paramList = buildCommonParameterList(snsRequest.getAppKey());
			paramList.add(new OAuthParameter("oauth_token", snsRequest.getOauthToken()));
				
			// do oauth signature on parameter list
			signParameter(paramList, snsRequest.getAccessTokenBaseURL(), METHOD_POST, snsRequest.getAppSecret(), snsRequest.getOauthTokenSecret());
			
			// get URL by final parameter list
			String url = snsRequest.getUserInfoBaseURL(); 
			List<NameValuePair> dataList = getNVByParameterList(paramList);
			 
			//sendHttpPostRequest("http://192.168.1.188:8000/test", "?m=hello");
			
			// send request
			String response = sendHttpPostRequest(url, dataList);
			if (response == null)
				return false;
			
			// handle response
			Map<String, String> responseMap = parseSimpleResponse(response);
			String oauthToken = responseMap.get("oauth_token");
			String oauthTokenSecret = responseMap.get("oauth_token_secret");		
			snsRequest.setOauthToken(oauthToken);
			snsRequest.setOauthTokenSecret(oauthTokenSecret);		
			
			return true;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return false;
		} catch (ParseException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		
	}
	
	public JSONObject getUserInfo(CommonSNSRequest snsRequest) {
		
		// create a prameter list
		try {
			JSONObject userMap = null;
			
			List<OAuthParameter> paramList = buildCommonParameterList(snsRequest.getAppKey());
			paramList.add(new OAuthParameter("oauth_token", snsRequest.getOauthToken()));
				
			String baseURL = snsRequest.getUserInfoBaseURL();
			
			// do oauth signature on parameter list
			signParameter(paramList, baseURL, METHOD_POST, snsRequest.getAppSecret(), snsRequest.getOauthTokenSecret());
			
			// get URL by final parameter list
			// String url = this.getURLByParameterList(paramList, snsRequest.getUserInfoBaseURL());
			String url = baseURL;
			List<NameValuePair> dataList = this.getNVByParameterList(paramList);
						 
			//sendHttpPostRequest("http://192.168.1.188:8000/test", "?m=hello");
			
			// send request
			//String response = sendHttpRequest(url);
			String response = sendHttpPostRequest(url, dataList);
			if (response == null)
				return null;
			
			// handle response			
			userMap = parseJSONResponse(response);			
			return userMap;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			return null;
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
		
	}
	
	public JSONObject getUserInfo1(CommonSNSRequest snsRequest) {
		
		CommonSNSRequestHandler handler = snsRequest.getUserInfoRequestHandler();
		JSONObject result = handler.execute();		
		return result;		
	}

	private JSONObject parseJSONResponse(String response) throws JSONException {
		JSONObject json = new JSONObject(response);
		return json;
	}
	
}
