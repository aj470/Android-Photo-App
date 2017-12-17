package cs213.photoapp78.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * @author Anil Tilve
 * @author Ayush Joshi
 */
public class Album implements Serializable {

    private static final long serialVersionUID = 1891567810783724951L;
    private String name;
    private ArrayList<Photo> photos;

    /**
     * Constructor
     */
    public Album(String name) {
        this.name = name;
        photos = new ArrayList<Photo>();
    }

    /**
     * Gets the name of this album_view
     *
     * @return the name of this album_view
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the name of this album_view
     *
     * @param name the new name of this album_view
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the photos in this album_view
     *
     * @return an arraylist of photos
     */
    public ArrayList<Photo> getPhotos() {
        return this.photos;
    }

    /**
     * Returns the number of photos in this album_view
     *
     * @return the number of photos in this album_view
     */
    public int getPhotoCount() {
        return this.photos.size();
    }

    /**
     * Compares this album_view to another
     *
     * @param other the album_view to be compared
     * @return true if the albums are equal, false otherwise
     */
    public boolean equals(Album other) {
        return name.equals(other.name);
    }

    /**
     * Returns a string representation of this album_view
     */
    public String toString() {
        String result = name + "\n" + photos.size() + " photo";
        if (photos.size() != 1)
            result += "s";
        return result;
    }
}