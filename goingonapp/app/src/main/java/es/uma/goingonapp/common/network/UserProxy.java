package es.uma.goingonapp.common.network;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;

import java.util.Hashtable;

import es.uma.goingonapp.common.entities.User;

/**
 * Created by Alb_Erc on 21/04/2015.
 */
public class UserProxy implements IUserProxy {
    // TODO: make the tags instance independent
    private static final String TAG = "UserProxy";

    private Context mContext;
    private HttpRequestManager mRequestManager;

    public UserProxy(Context context) {
        this.mContext = context;
        this.mRequestManager = HttpRequestManager.getInstance(this.mContext);
    }

    @Override
    public void getUser(String nickname, String password, Response.Listener successListener, Response.ErrorListener errorListener) {
        final String authenticationHeader = this.generateBasicAuthenticationHeader(nickname, password);

        RequestWithGsonResponse request = new RequestWithGsonResponse(
                GOUriBuilder.getUserUri(nickname),
                User.class,
                new Hashtable<String, String>() {
                    {
                        put("Authorization", authenticationHeader);
                    }
                },
                successListener,
                errorListener,
                Request.Method.GET);

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        this.mRequestManager.addToRequestQueue(request);

        Log.d(UserProxy.TAG, "Adding the request to the queue");
    }

    @Override
    public void createUser(User user, Response.Listener successListener, Response.ErrorListener errorListener) {
        GsonRequestWithStringResponse request = new GsonRequestWithStringResponse(
            Request.Method.POST,
            GOUriBuilder.getCreateUserUri(),
            successListener,
            errorListener,
            user);

        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        this.mRequestManager.addToRequestQueue(request);
    }

    private String generateBasicAuthenticationHeader(String nickname, String password) {
        String combinedString = nickname + ":" + password;

        return "Basic " + Base64.encodeToString(combinedString.getBytes(), Base64.DEFAULT);
    }

    @Override
    public void cancelRequests() {
        this.mRequestManager.getRequestQueue().cancelAll(UserProxy.TAG);
    }
}
