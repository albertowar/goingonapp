package es.uma.goingonapp.common.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Alb_Erc on 11/04/2015.
 */
public class User implements Serializable {
    @Expose
    @SerializedName("Nickname")
    private String mNickname;

    @Expose
    @SerializedName("Password")
    private String mPassword;

    @Expose
    @SerializedName("City")
    private String mCity;

    @Expose
    @SerializedName("Email")
    private String mEmail;

    @SerializedName("Links")
    private List<Link> mLinks;

    public User(String nickname, String password, String city, String email) {
        this.mNickname = nickname;
        this.mPassword = password;
        this.mCity = city;
        this.mEmail = email;
    }

    public String getmNickname() {
        return this.mNickname;
    }

    public String getmPassword() {
        return this.mPassword;
    }

    public String getmCity() {
        return this.mCity;
    }

    public String getmEmail() {
        return this.mEmail;
    }

    public List<Link> getmLinks() {
        return this.mLinks;
    }

    public Link getSelfLink() {
        for (Link link : this.mLinks) {
            if (link.getRel().equalsIgnoreCase("self")) {
                return link;
            }
        }

        return null;
    }
}
