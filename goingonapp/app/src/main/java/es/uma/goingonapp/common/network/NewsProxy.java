package es.uma.goingonapp.common.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Date;
import java.util.Hashtable;
import java.util.UUID;

import es.uma.goingonapp.common.entities.News;

/**
 * Created by Alb_Erc on 21/04/2015.
 */
public class NewsProxy implements INewsProxy {
    // TODO: make the tags instance independent
    private static final String TAG = "NewsProxy";

    private Context mContext;
    private HttpRequestManager mRequestManager;

    public NewsProxy(Context context) {
        this.mContext = context;
        this.mRequestManager = HttpRequestManager.getInstance(this.mContext);
    }

    @Override
    public void createNews(News news, Response.Listener successListener, Response.ErrorListener errorListener) {
        GsonRequestWithStringResponse request = new GsonRequestWithStringResponse(
                Request.Method.POST,
                GOUriBuilder.getCreateNewsUri("Malaga", new Date()),
                successListener,
                errorListener,
                news);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        this.mRequestManager.addToRequestQueue(request);
    }

    @Override
    public void getNews(String newsUri, Response.Listener successListener, Response.ErrorListener errorListener) {
        RequestWithGsonResponse request = new RequestWithGsonResponse(
                newsUri,
                News.class,
                new Hashtable<String, String>() {
                    {
                        put("Accept", "application/json");
                    }
                },
                successListener,
                errorListener,
                Request.Method.GET);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        this.mRequestManager.addToRequestQueue(request);
    }

    @Override
    public void getHotNews(Response.Listener successListener, Response.ErrorListener errorListener) {
        RequestWithGsonResponse request = new RequestWithGsonResponse(
                GOUriBuilder.getHotNewsUri("Malaga"),
                News[].class,
                new Hashtable<String, String>() {
                    {
                        put("Accept", "application/json");
                    }
                },
                successListener,
                errorListener,
                Request.Method.GET);

        request.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        this.mRequestManager.addToRequestQueue(request);

        Log.d(NewsProxy.TAG, "Adding the request to the queue");
    }

    @Override
    public void cancelRequests() {
        this.mRequestManager.getRequestQueue().cancelAll(NewsProxy.TAG);
    }

    @Override
    public void createNewsImage(String mCurrentPhotoPath, UUID newsId, Response.Listener successListener, Response.ErrorListener errorListener) {
        File imgFile = new File(mCurrentPhotoPath);

        if (imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            Log.d("PostNews", "Final size: " + (myBitmap.getByteCount()/(1024*1024)));

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            myBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            Log.d("NewsProxy", "Image size: " + byteArray.length/(1024*1024) + " MB");

            Log.d("ImageURI", GOUriBuilder.getCreateImageUri("Malaga", new Date(), newsId));

            RequestWithStringResponse request = new RequestWithStringResponse(
                    Request.Method.POST,
                    GOUriBuilder.getCreateImageUri("Malaga", new Date(), newsId),
                    successListener,
                    errorListener,
                    byteArray);

            request.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            this.mRequestManager.addToRequestQueue(request);

            Log.d("Image request", "Added to the queue");
        }
    }
}
