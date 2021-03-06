
package com.sagarnileshshah.twitterclient.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import javax.annotation.Generated;

@Parcel(value = Parcel.Serialization.BEAN, analyze = {Sizes__.class})
@Generated("org.jsonschema2pojo")
public class Sizes__ {

    @SerializedName("large")
    @Expose
    private Large__ large;
    @SerializedName("medium")
    @Expose
    private Medium_____ medium;
    @SerializedName("small")
    @Expose
    private Small__ small;
    @SerializedName("thumb")
    @Expose
    private Thumb__ thumb;

    /**
     * 
     * @return
     *     The large
     */
    public Large__ getLarge() {
        return large;
    }

    /**
     * 
     * @param large
     *     The large
     */
    public void setLarge(Large__ large) {
        this.large = large;
    }

    /**
     * 
     * @return
     *     The medium
     */
    public Medium_____ getMedium() {
        return medium;
    }

    /**
     * 
     * @param medium
     *     The medium
     */
    public void setMedium(Medium_____ medium) {
        this.medium = medium;
    }

    /**
     * 
     * @return
     *     The small
     */
    public Small__ getSmall() {
        return small;
    }

    /**
     * 
     * @param small
     *     The small
     */
    public void setSmall(Small__ small) {
        this.small = small;
    }

    /**
     * 
     * @return
     *     The thumb
     */
    public Thumb__ getThumb() {
        return thumb;
    }

    /**
     * 
     * @param thumb
     *     The thumb
     */
    public void setThumb(Thumb__ thumb) {
        this.thumb = thumb;
    }

}
