package com.orange.common.android.network.http;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;

import android.os.Message;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.orange.common.android.utils.FileUtil;
import com.orange.common.android.utils.MemoryUtil;

public class ProtocolBufferHandler extends AsyncHttpResponseHandler {

	private static final String TAG = "ProtocolBufferHandler";
	volatile boolean isStop = false;

	/**
     * Fired when a request returns successfully, override to handle in your own code
     * @param content the body of the HTTP response from the server
     */
    public void onSuccess(byte[] binaryData) {}

    /**
     * Fired when a request fails to complete, override to handle in your own code
     * @param error the underlying cause of the failure
     * @param content the response body, if any
     */
    public void onFailure(Throwable error, byte[] binaryData) {
        // By default, call the deprecated onFailure(Throwable) for compatibility
        onFailure(error);
    }

    protected void sendSuccessMessage(byte[] responseBody) {
        sendMessage(obtainMessage(SUCCESS_MESSAGE, responseBody));
    }

    @Override
	protected void sendFailureMessage(Throwable e, byte[] responseBody) {
        sendMessage(obtainMessage(FAILURE_MESSAGE, new Object[]{e, responseBody}));
    }

    //
    // Pre-processing of messages (in original calling thread, typically the UI thread)
    //

    protected void handleSuccessMessage(byte[] responseBody) {
        onSuccess(responseBody);
    }

    protected void handleFailureMessage(Throwable e, byte[] responseBody) {
        onFailure(e, responseBody);
    }
    
    // Methods which emulate android's Handler and Message methods
    @Override
	protected void handleMessage(Message msg) {
        switch(msg.what) {
            case SUCCESS_MESSAGE:
                handleSuccessMessage((byte[])msg.obj);
                break;
            case FAILURE_MESSAGE:
                Object[] response = (Object[])msg.obj;
                handleFailureMessage((Throwable)response[0], (byte[])response[1]);
                break;
            default:
                super.handleMessage(msg);
                break;
        }
    }
    
    protected void bytesReceived(long fileTotalLength, long downloadLength, int addedLength){
    	
    }
    
    // Interface to AsyncHttpRequest
    void sendResponseMessage(HttpResponse response) {
    	
    	StatusLine status = response.getStatusLine();
    	Log.i(TAG, "<download file> http status code="+status.getStatusCode());
    	byte[] responseBody = null;
    	
    	if (status.getStatusCode() == 416){
    		sendSuccessMessage(responseBody);    		
    		return;
    	}
    	
    	if (status.getStatusCode() == 405){   		
    		return;
    	}
    	
    	if(status.getStatusCode() >= 300) {
    		sendFailureMessage(new HttpResponseException(status.getStatusCode(), status.getReasonPhrase()), responseBody);
    		return;
        }    	        
                
        try
		{
        	Header[] contentLengthHeaders = response.getHeaders("Content-Length");
        	if (contentLengthHeaders.length < 0){
        		return;
        	}
        	
        	if (contentLengthHeaders.length == 0){
        		// no more data
        		sendSuccessMessage(responseBody);
        		return;
        	}        	
        	
        	InputStream inputStream = new BufferedInputStream(response.getEntity().getContent());
        	int offset = -1;
        	byte[] buffer = new byte[10240];
        	while ((offset = inputStream.read(buffer, 0, 10240)) != -1) {        		        		
//        		output.write(buffer, 0, offset);
//        		output.flush();
//        		downloadLength += offset;
//        		
//        		bytesReceived(fileTotalLength, downloadLength, offset);		
        		
        		if (isStop){
        			Log.d(TAG, "detect stop flag, stop download");
        			break;
        		}
			}
        	
           	inputStream.close();
           	
           	if (isStop){
           		return;
           	}
           	
        } catch (FileNotFoundException e1)
		{
			sendFailureMessage(new HttpResponseException(status.getStatusCode(), "FileNotFound!"), responseBody);
			return;
		} catch(IOException e) {			
            sendFailureMessage(e, (byte[]) null);
            return;
        } catch(Exception e){
        	sendFailureMessage(e, (byte[]) null);
        	return;
        }

        
        sendSuccessMessage(responseBody);

    }    
}
