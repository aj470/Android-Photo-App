package cs213.photoapp78.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;


public class SerializableBitmap implements Serializable {
    private byte[] bytes;

    public SerializableBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        bytes = stream.toByteArray();
    }

    public Bitmap getBitmap() {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
