package cs213.photoapp78.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * Abstracts a Photo
 *
 * @author Anil Tilve
 * @author Ayush Joshi
 */
public class Photo implements Serializable {


    private static final long serialVersionUID = 6955723612371190680L;
    private ArrayList<Tag> tags;
    private String caption;
    private SerializableBitmap bitmap;

    /**
     * Constructor
     *
     * @param caption the caption of the photo
     */
    public Photo(String caption, Bitmap bitmap) {
        this.caption = caption;
        this.tags = new ArrayList<Tag>();
        this.bitmap = new SerializableBitmap(bitmap);
    }

    /**
     * Returns the caption of this photo
     *
     * @return the caption of this photo
     */
    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public Bitmap getBitmap() {
        return bitmap.getBitmap();
    }

    /**
     * Returns the tags for this photo
     *
     * @return the tags for this photo
     */
    public ArrayList<Tag> getTags() {
        return tags;
    }

    /**
     * Compares this photo to another
     *
     * @param other the photo to be compared to
     * @return true if the photos are equal, false otherwise
     */
    public boolean equals(Photo other) {
        return this.caption.equals(other.caption);
    }
}
