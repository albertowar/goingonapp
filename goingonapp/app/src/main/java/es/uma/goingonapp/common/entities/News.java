package es.uma.goingonapp.common.entities;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Alb_Erc on 11/04/2015.
 */
public class News {
    @Expose
    @SerializedName("Title")
    private String mTitle;

    @Expose
    @SerializedName("Content")
    private String mContent;

    @SerializedName("City")
    private String mCity;

    @SerializedName("Date")
    private String mDate;

    @SerializedName("Author")
    private String mAuthor;

    @SerializedName("Links")
    private List<Link> mLinks;

    public News(String title, String content, String city, String date, String author) {
        this.mTitle = title;
        this.mContent = content;
        this.mCity = city;
        this.mDate = date;
        this.mAuthor = author;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContent() {
        return mContent;
    }

    public String getDate() throws ParseException {
        // TODO: quick fix, Date is not in the payload anymore
        if (this.mDate != null)
        {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            return dateFormat.format(dateFormat.parse(this.mDate));
        }

        return "";
    }

    public String getAuthor() {
        return mAuthor;
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
