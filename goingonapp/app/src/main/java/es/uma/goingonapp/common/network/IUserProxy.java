package es.uma.goingonapp.common.network;

import com.android.volley.Response;

import es.uma.goingonapp.common.entities.User;

/**
 * Created by Alb_Erc on 21/04/2015.
 */
public interface IUserProxy {
    void getUser(String nickname, String password, Response.Listener successListener, Response.ErrorListener errorListener);

    void createUser(User user, Response.Listener successListener, Response.ErrorListener errorListener);

    void cancelRequests();
}
