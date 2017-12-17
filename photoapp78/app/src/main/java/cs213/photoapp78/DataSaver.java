package cs213.photoapp78;

import android.widget.ArrayAdapter;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import cs213.photoapp78.model.Album;

/**
 * Created by Anil on 12/3/2017.
 */

/**
 * Utility class to save the data onto the data file.
 */
public class DataSaver {
    /**
     * Saves the data from the ArrayList for the albums
     * @param albums all the albums
     * @param path path to data file
     */
    public static void saveData(ArrayList<Album> albums, String path) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(albums);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Saves the data from the ArrayAdapter for a give album
     * @param arrayAdapter current arrayadapter
     * @param path path to data file
     */
    public static void saveData(ArrayAdapter<Album> arrayAdapter, String path) {
        ArrayList<Album> albums = new ArrayList<Album>();

        for (int index = 0; index < arrayAdapter.getCount(); index++)
            albums.add(arrayAdapter.getItem(index));

        try {


            FileOutputStream fileOutputStream = new FileOutputStream(path);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(albums);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
