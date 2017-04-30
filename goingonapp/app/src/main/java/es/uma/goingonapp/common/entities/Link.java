package es.uma.goingonapp.common.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.net.URI;

/**
 * Created by Alb_Erc on 18/04/2015.
 */
public class Link implements Serializable {

    @Expose
    @SerializedName("Rel")
    private String mRel;

    @Expose
    @SerializedName("Href")
    private URI mHref;

    public String getRel() {
        return this.mRel;
    }

    public URI getHref() {
        return this.mHref;
    }
}
