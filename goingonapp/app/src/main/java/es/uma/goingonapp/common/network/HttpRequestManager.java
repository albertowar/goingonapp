package es.uma.goingonapp.common.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Alb_Erc on 12/04/2015.
 */
public class HttpRequestManager {
    private RequestQueue mRequestQueue;

    private static Context sContext;
    private static HttpRequestManager sInstance;

    private HttpRequestManager(Context context) {
        HttpRequestManager.sContext = context;

        this.mRequestQueue = this.buildRequestQueue();
    }

    public static synchronized HttpRequestManager getInstance(Context context) {
        if (HttpRequestManager.sInstance == null) {
            HttpRequestManager.sInstance = new HttpRequestManager(context);
        }

        return HttpRequestManager.sInstance;
    }

    public RequestQueue getRequestQueue() {
        return this.mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> request) {
        this.getRequestQueue().add(request);
    }

    //region Helper methods

    private RequestQueue buildRequestQueue() {
        if (this.mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            this.mRequestQueue = Volley.newRequestQueue(HttpRequestManager.sContext.getApplicationContext());
        }

        return this.mRequestQueue;
    }

    //endregion
}
