
package com.sagarnileshshah.twitterclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import javax.annotation.Generated;

@Parcel(value = Parcel.Serialization.BEAN, analyze = {Large___.class})
@Generated("org.jsonschema2pojo")
public class Large___ {

    @SerializedName("h")
    @Expose
    private Long h;
    @SerializedName("resize")
    @Expose
    private String resize;
    @SerializedName("w")
    @Expose
    private Long w;

    /**
     * 
     * @return
     *     The h
     */
    public Long getH() {
        return h;
    }

    /**
     * 
     * @param h
     *     The h
     */
    public void setH(Long h) {
        this.h = h;
    }

    /**
     * 
     * @return
     *     The resize
     */
    public String getResize() {
        return resize;
    }

    /**
     * 
     * @param resize
     *     The resize
     */
    public void setResize(String resize) {
        this.resize = resize;
    }

    /**
     * 
     * @return
     *     The w
     */
    public Long getW() {
        return w;
    }

    /**
     * 
     * @param w
     *     The w
     */
    public void setW(Long w) {
        this.w = w;
    }

}
