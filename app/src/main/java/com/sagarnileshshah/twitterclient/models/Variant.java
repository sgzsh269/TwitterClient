
package com.sagarnileshshah.twitterclient.models;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("org.jsonschema2pojo")
public class Variant {

    @SerializedName("bitrate")
    @Expose
    private Long bitrate;
    @SerializedName("content_type")
    @Expose
    private String contentType;
    @SerializedName("url")
    @Expose
    private String url;

    /**
     * 
     * @return
     *     The bitrate
     */
    public Long getBitrate() {
        return bitrate;
    }

    /**
     * 
     * @param bitrate
     *     The bitrate
     */
    public void setBitrate(Long bitrate) {
        this.bitrate = bitrate;
    }

    /**
     * 
     * @return
     *     The contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * 
     * @param contentType
     *     The content_type
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

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

}
