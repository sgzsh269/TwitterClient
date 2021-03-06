package com.sagarnileshshah.twitterclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sshah on 2/19/16.
 */

@Parcel(value = Parcel.Serialization.BEAN, analyze = {UrlRetweeted_.class})
public class UrlRetweeted_ {

    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("expanded_url")
    @Expose
    private String expandedUrl;
    @SerializedName("display_url")
    @Expose
    private String displayUrl;
    @SerializedName("indices")
    @Expose
    private List<Long> indices = new ArrayList<Long>();

    /**
     *
     * @return
     *     The url
     */
    public String getUrl() {
        return url;
    }

    /**
     *
     * @param url
     *     The url
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     *
     * @return
     *     The expandedUrl
     */
    public String getExpandedUrl() {
        return expandedUrl;
    }

    /**
     *
     * @param expandedUrl
     *     The expanded_url
     */
    public void setExpandedUrl(String expandedUrl) {
        this.expandedUrl = expandedUrl;
    }

    /**
     *
     * @return
     *     The displayUrl
     */
    public String getDisplayUrl() {
        return displayUrl;
    }

    /**
     *
     * @param displayUrl
     *     The display_url
     */
    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    /**
     *
     * @return
     *     The indices
     */
    public List<Long> getIndices() {
        return indices;
    }

    /**
     *
     * @param indices
     *     The indices
     */
    public void setIndices(List<Long> indices) {
        this.indices = indices;
    }

}
