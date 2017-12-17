package cs213.photoapp78;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import cs213.photoapp78.adapter.PhotoAdapter;
import cs213.photoapp78.model.Album;
import cs213.photoapp78.model.Photo;
import cs213.photoapp78.model.Tag;

/**
 * MainActivity to run the photoapp
 * author Anil and Ayush
 */
public class MainActivity extends AppCompatActivity {
    private ArrayList<Album> albums;
    private ListView listView;
    private String path;

    /**
     * On create loads the albums from the data file and initializes the listview for albums.
     *
     * @param savedInstanceState as saved Instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        path = this.getApplicationInfo().dataDir + "/data.dat";

        File data = new File(path);

        /*boolean deleteFlag = false;

        //to delete data file after changes to model.
        //Android Device Monitor won't let me see contents of data folder even with rwxrwxrwx permissions
        if (deleteFlag)
            data.delete();*/

        if (!data.exists() || !data.isFile()) {
            try {
                data.createNewFile();
                albums = new ArrayList<Album>();
                albums.add(new Album("stock"));

                DataSaver.saveData(albums, path);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        // File exists, proceed to read it
        try {
            FileInputStream fileInputStream = new FileInputStream(path);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            albums = (ArrayList<Album>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        ArrayAdapter<Album> adapter = new ArrayAdapter<>(this, R.layout.album_view, albums);
        adapter.setNotifyOnChange(true);
        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setItemChecked(0, true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listView.setItemChecked(position, true);
            }
        });
    }

    /**
     * removes the album from the view after confirming.
     *
     * @param view current view
     */
    public void removeAlbum(View view) {
        final ArrayAdapter<Album> adapter = (ArrayAdapter<Album>) listView.getAdapter();

        if (adapter.getCount() == 0) {
            new AlertDialog.Builder(this)
                    .setMessage("There are no albums.")
                    .setPositiveButton("OK", null)
                    .show();

            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final int checkedItemPosition = listView.getCheckedItemPosition();
        final Album checkedAlbum = adapter.getItem(checkedItemPosition);

        builder.setMessage("Are you sure you want to remove \"" + checkedAlbum.getName() + "\"?");
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        adapter.remove(checkedAlbum);
                        DataSaver.saveData(adapter, path);
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
     * Adds the new ablum to the listview. Updates the view and Saves the data.
     *
     * @param view current view
     */
    public void addAlbum(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayAdapter<Album> adapter = (ArrayAdapter<Album>) listView.getAdapter();
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String albumName = input.getText().toString();
                Album newAlbum = new Album(albumName);

                for (int index = 0; index < adapter.getCount(); index++)
                    if (newAlbum.equals(adapter.getItem(index))) {
                        new AlertDialog.Builder(builder.getContext())
                                .setMessage("An album with the name \"" + albumName + "\" already exists.")
                                .setPositiveButton("OK", null)
                                .show();

                        return;
                    }

                adapter.add(newAlbum);
                DataSaver.saveData(adapter, path);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
     * renames the album after comfirming. Updates the view and Saves the data then.
     *
     * @param view current view
     */
    public void renameAlbum(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final ArrayAdapter<Album> adapter = (ArrayAdapter<Album>) listView.getAdapter();
        final EditText input = new EditText(this);

        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(adapter.getItem(listView.getCheckedItemPosition()).getName());
        input.setSelection(input.getText().length());
        builder.setView(input);

        builder.setPositiveButton("Rename", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String albumName = input.getText().toString();

                for (int index = 0; index < adapter.getCount(); index++)
                    if (albumName.equals(adapter.getItem(index).getName())) {
                        new AlertDialog.Builder(builder.getContext())
                                .setMessage("An album_view with the name \"" + albumName + "\" already exists.")
                                .setPositiveButton("OK", null)
                                .show();

                        return;
                    }
                adapter.getItem(listView.getCheckedItemPosition()).setName(albumName);
                adapter.notifyDataSetChanged();
                DataSaver.saveData(adapter, path);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
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
     * Opens the selected album by creating a new Intent
     *
     * @param view current view
     */
    public void openAlbum(View view) {
        if (listView.getAdapter().getCount() == 0)
            return;

        Intent intent = new Intent(this, AlbumActivity.class);

        intent.putExtra("albums", albums);
        intent.putExtra("albumPosition", listView.getCheckedItemPosition());
        startActivity(intent);
    }

    /**
     * Searches all albums for relevant photos
     *
     * @param view current view
     */
    public void searchAlbums(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        final EditText personInput = new EditText(this);
        personInput.setHint("Person");
        layout.addView(personInput);

        final EditText locationInput = new EditText(this);
        locationInput.setHint("Location");
        layout.addView(locationInput);

        builder.setView(layout)
                .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, int which) {
                        ArrayList<Photo> searchResults = new ArrayList<>();
                        String personString = personInput.getText().toString(),
                                locationString = locationInput.getText().toString();
                        boolean alreadyAdded = false;

                        for (Album currentAlbum : albums) {
                            for (Photo currentPhoto : currentAlbum.getPhotos()) {
                                for (Tag currentTag : currentPhoto.getTags()) {
                                    String tagValue = currentTag.getValue();

                                    if (!tagValue.isEmpty()) {
                                        if (!personString.isEmpty() && tagValue.contains(personString) ||
                                                !locationString.isEmpty() && tagValue.contains(locationString)) {
                                            for (Photo currentAddedPhoto : searchResults) {
                                                if (currentAddedPhoto.equals(currentPhoto)) {
                                                    alreadyAdded = true;
                                                    break;
                                                }
                                            }

                                            if (!alreadyAdded) {
                                                searchResults.add(currentPhoto);
                                                alreadyAdded = false;
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (searchResults.isEmpty()) {
                            new AlertDialog.Builder(builder.getContext())
                                    .setMessage("No search results for your query.")
                                    .setPositiveButton("OK", null)
                                    .show();

                            return;
                        } else {
                            AlertDialog.Builder searchBuilder = new AlertDialog.Builder(builder.getContext());
                            PhotoAdapter photoAdapter = new PhotoAdapter(builder.getContext(), R.layout.photo_view, searchResults);
                            ListView searchView = new ListView(builder.getContext());
                            searchView.setAdapter(photoAdapter);

                            searchBuilder.setView(searchView)
                                    .setPositiveButton("Search Again", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            searchAlbums(null);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialog.cancel();
                                        }
                                    });

                            searchBuilder.show();
                        }
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
}
