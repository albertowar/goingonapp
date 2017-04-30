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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Alb_Erc on 22/04/2015.
 */
public class GsonRequestWithStringResponse<T> extends StringRequest {
    private Response.Listener<String> listener;
    private Response.ErrorListener errorListener;
    private T mBody;

    public GsonRequestWithStringResponse(int method, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, T body) {
        super(method, url, listener, errorListener);

        this.mBody = body;
        this.listener = listener;
        this.errorListener = errorListener;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

        return gson.toJson(this.mBody).getBytes();
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String>  params = new HashMap<String, String>();
        params.put("Content-Type", "application/json");

        String userPassword = "GoingOnApp:GoingOnApp";
        String authorizationHeader = Base64.encodeToString(userPassword.getBytes(), Base64.DEFAULT);

        params.put("Authorization", "Basic " + authorizationHeader);

        return params;
    }

    @Override
    public void deliverError(VolleyError error) {
        Log.d("DEBUG", "Delivering error");

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
        Log.d("DEBUG", "Delivering response");

        this.listener.onResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return "application/json";
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String responseString;
            if (response.headers.containsKey("Location"))
            {
                Log.d("NetworkResponse", "Location header: " + response.headers.get("Location"));
                responseString = response.headers.get("Location");
            }
            else
            {
                responseString = new String(
                        response.data,
                        HttpHeaderParser.parseCharset(response.headers));
            }

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
