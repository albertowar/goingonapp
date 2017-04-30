package es.uma.goingonapp.common.network;

import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alb_Erc on 22/04/2015.
 */
public class RequestWithStringResponse extends StringRequest {
    private Response.Listener<String> listener;
    private Response.ErrorListener errorListener;
    private byte[] mBody;

    public RequestWithStringResponse(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, byte[] body) {
        super(method, url, listener, errorListener);

        this.mBody = body;
        this.listener = listener;
        this.errorListener = errorListener;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        Log.d("Request", "getBody");

        return this.mBody;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Log.d("Request", "getHeaders");

        Map<String, String>  params = new HashMap<>();

        String userPassword = "GoingOnApp:GoingOnApp";
        String authorizationHeader = Base64.encodeToString(userPassword.getBytes(), Base64.DEFAULT);

        params.put("Authorization", "Basic " + authorizationHeader);

        return params;
    }

    @Override
    public void deliverError(VolleyError error) {
        Log.d("DEBUG", "Delivering error: " + error.getMessage());

        this.errorListener.onErrorResponse(error);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        if(volleyError.networkResponse != null && volleyError.networkResponse.data != null){
            VolleyError error = new VolleyError(new String(volleyError.networkResponse.data));
            volleyError = error;
        }

        return volleyError;
    }

    @Override
    protected void deliverResponse(String response) {
        Log.d("DEBUG", "Delivering response: " + response);

        this.listener.onResponse(response);
    }

    // TODO: improve if there is time
    @Override
    public String getBodyContentType() {
        Log.d("Request", "getBodyContentType");

        return "image/png";
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String responseString = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            return Response.success(
                    responseString,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            Log.d("DEBUG", "Exception: " + unsupportedEncodingException.getMessage());

            return Response.error(new ParseError(unsupportedEncodingException));
        } catch (Exception exception) {
            return Response.error(new ParseError(exception));
        }
    }
}
