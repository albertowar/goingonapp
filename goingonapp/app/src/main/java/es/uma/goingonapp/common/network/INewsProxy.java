package es.uma.goingonapp.common.network;

import com.android.volley.Response;

import java.util.UUID;

import es.uma.goingonapp.common.entities.News;

/**
 * Created by Alb_Erc on 21/04/2015.
 */
public interface INewsProxy {
    void createNews(News news, Response.Listener successListener, Response.ErrorListener errorListener);

    void getNews(String newsUri, Response.Listener successListener, Response.ErrorListener errorListener);

    void getHotNews(Response.Listener successListener, Response.ErrorListener errorListener);

    void cancelRequests();

    void createNewsImage(String mCurrentPhotoPath, UUID newsId, Response.Listener successListener, Response.ErrorListener errorListener);
}
