package es.uma.goingonapp.common.entities;

import java.io.Serializable;

/**
 * Created by Alb_Erc on 12/04/2015.
 */
public class NewsThumbnail implements Serializable {
    public static final String NEWS_EXTRA = "News";

    private String mTitle;
    private String mContent;
    private String mHrefUri;

    public NewsThumbnail(String title, String content, String hrefUri) {
        this.mTitle = title;
        this.mContent = content;
        this.mHrefUri = hrefUri;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getContent() {
        return this.mContent;
    }

    public String getHrefUri() {
        return mHrefUri;
    }

    public String getThumbnailContent() {
        int maxLength = this.mContent.length() / 3 > 60 ? 60 : this.mContent.length() / 3;

        return this.mContent.substring(0, maxLength) + "...";
    }

    @Override
    public boolean equals(Object o) {
        return (o != null) && (o instanceof NewsThumbnail) && this.mTitle.equals(((NewsThumbnail) o).mTitle);
    }
}
