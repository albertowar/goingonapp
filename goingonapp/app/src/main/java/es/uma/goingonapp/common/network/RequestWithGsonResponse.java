package es.uma.goingonapp.common.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by Alb_Erc on 12/04/2015.
 */
public class RequestWithGsonResponse<T> extends Request<T> {
    /** Default charset for JSON request. */
    protected static final String PROTOCOL_CHARSET = "utf-8";

    /** Content type for request. */
    private static final String PROTOCOL_CONTENT_TYPE = String.format("application/json; charset=%s", PROTOCOL_CHARSET);

    private final Gson gson = new Gson();
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Response.Listener<T> listener;
    private final Response.ErrorListener errorListener;
    private final String requestBody;

    /**
     * Make a request and return a parsed object from JSON.
     * @param url URL of the request to make
     * @param clazz Relevant class object, for Gson's reflection
     * @param headers Map of request headers
     * @param listener
     * @param httpMethod
     */
    public RequestWithGsonResponse(
            String url,
            Class clazz,
            Map<String, String> headers,
            Response.Listener listener,
            Response.ErrorListener errorListener,
            int httpMethod) {
        super(httpMethod, url, errorListener);

        Log.d("DEBUG", "Building Request. URL: " + url);

        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;
        this.errorListener = errorListener;
        this.requestBody = null;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        Log.d("DEBUG", "Delivering response");

        this.listener.onResponse(response);
    }

    @Override
    public String getBodyContentType() {
        return PROTOCOL_CONTENT_TYPE;
    }

    @Override
    public byte[] getBody() {
        try {
            return requestBody == null ? null : requestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException uee) {
            Log.d("DEBUG",
                    String.format("Unsupported Encoding while trying to get the bytes of %s using %s",
                            requestBody,
                            PROTOCOL_CHARSET));
            return null;
        }
    }

    @Override
    public void deliverError(VolleyError error) {
        Log.d("DEBUG", "Delivering error: " + error.getMessage());

        this.errorListener.onErrorResponse(error);
    }

    @Override
    protected VolleyError parseNetworkError(VolleyError volleyError) {
        return volleyError;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));

            Log.d("DEBUG", "Received JSON: " + json);

            return Response.success(
                    gson.fromJson(json, clazz),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException unsupportedEncodingException) {
            Log.d("DEBUG", "Exception: " + unsupportedEncodingException.getMessage());

            return Response.error(new ParseError(unsupportedEncodingException));
        } catch (JsonSyntaxException jsonSyntaxException) {
            Log.d("DEBUG", "Exception: " + jsonSyntaxException.getMessage());

            return Response.error(new ParseError(jsonSyntaxException));
        }
    }
}
