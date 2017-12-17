package cs213.photoapp78;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import cs213.photoapp78.model.Album;
import cs213.photoapp78.model.Photo;
import cs213.photoapp78.model.Tag;

/**
 * Activity to implement the functionality in the photo view.
 */
public class PhotoActivity extends AppCompatActivity {
    private ArrayList<Album> albums;
    private Album album;
    private Photo photo;
    private ListView listView;
    private ImageView imageView;
    private String path;
    private int albumPosition, photoPosition;

    /**
     * On create get the photo and update the photo view.
     * @param savedInstanceState as Saved Instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        path = this.getApplicationInfo().dataDir + "/data.dat";
        Intent intent = getIntent();
        albums = (ArrayList<Album>) intent.getSerializableExtra("albums");
        albumPosition = intent.getIntExtra("albumPosition", 0);
        album = albums.get(albumPosition);
        photoPosition = intent.getIntExtra("photoPosition", 0);
        photo = album.getPhotos().get(photoPosition);

        ArrayAdapter<Tag> adapter = new ArrayAdapter<>(this, R.layout.album_view, photo.getTags());
        adapter.setNotifyOnChange(true);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setItemChecked(0, true);

        imageView = findViewById(R.id.photo);
        imageView.setImageBitmap(photo.getBitmap());
    }

    /**
     * functionality to add the tag on a photo, giving user option for Person and Location.
     * @param view  as view
     */
    public void addTag(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayAdapter<Tag> adapter = (ArrayAdapter<Tag>) listView.getAdapter();
        final EditText input = new EditText(this);
        final Tag tag = new Tag("person", "");

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setFocusableInTouchMode(true);
        input.requestFocus();
        builder.setView(input);

        builder.setSingleChoiceItems(R.array.tag_types, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tag.setName(which == 0 ? "person" : "location");
            }
        })
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tag.setValue(input.getText().toString());

                        for (int index = 0; index < adapter.getCount(); index++)
                            if (tag.equals(adapter.getItem(index))) {
                                new AlertDialog.Builder(builder.getContext())
                                        .setMessage("This tag already exists.")
                                        .setPositiveButton("OK", null)
                                        .show();

                                return;
                            }
                        adapter.add(tag);
                        DataSaver.saveData(albums, path);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        dialog.show();
    }

    /**
     * Remove the selected Tag for the photo.
     * @param view as view
     */
    public void removeTag(View view) {
        final ArrayAdapter<Tag> adapter = (ArrayAdapter<Tag>) listView.getAdapter();

        if (adapter.getCount() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("This photo does not have any tags.")
                    .setPositiveButton("OK", null)
                    .show();

            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final int checkedItemPosition = listView.getCheckedItemPosition();
        final Tag checkedTag = adapter.getItem(checkedItemPosition);

        builder.setMessage("Are you sure you want to remove \"" + checkedTag.toString() + "\"?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        adapter.remove(checkedTag);
                        DataSaver.saveData(albums, path);
                        listView.setItemChecked(checkedItemPosition, true);
                    }
                });

        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        builder.show();
    }

    /**
     * Get the previous photo and update the photo view
     * @param view as view
     */
    public void previousPhoto(View view) {
        photoPosition = photoPosition > 0 ? photoPosition - 1 : album.getPhotoCount() - 1;
        photo = album.getPhotos().get(photoPosition);
        imageView.setImageBitmap(photo.getBitmap());
        ArrayAdapter<Tag> adapter = new ArrayAdapter<>(this, R.layout.album_view, photo.getTags());
        listView.setAdapter(adapter);
        listView.setItemChecked(0, true);
    }

    /**
     * Get the next photo and update the photo view
     * @param view as view
     */
    public void nextPhoto(View view) {
        photoPosition = photoPosition < album.getPhotoCount() - 1 ? photoPosition + 1 : 0;
        photo = album.getPhotos().get(photoPosition);
        imageView.setImageBitmap(photo.getBitmap());
        ArrayAdapter<Tag> adapter = new ArrayAdapter<>(this, R.layout.album_view, photo.getTags());
        listView.setAdapter(adapter);
        listView.setItemChecked(0, true);
    }

    /**
     * Handles the functionality for the up/Home button. Gets back to album.
     * @param item as Menu Item
     * @return boolean value
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this, AlbumActivity.class);

                intent.putExtra("albums", albums);
                intent.putExtra("albumPosition", albumPosition);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
