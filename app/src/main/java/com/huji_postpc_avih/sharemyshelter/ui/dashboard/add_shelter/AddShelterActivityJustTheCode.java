package com.huji_postpc_avih.sharemyshelter.ui.dashboard.add_shelter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.data.ShelterDB;
import com.huji_postpc_avih.sharemyshelter.navigation.Navigator;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddShelterActivityJustTheCode extends AppCompatActivity implements IPickResult {
    public static final int REQUEST_CODE = 1;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shelter);

        Spinner dropdown = findViewById(R.id.locationSpinner);
        String[] items = new String[]{"Current Location", "Choose from Map"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = dropdown.getSelectedItem().toString();
                if (choice.equals("Current Location")) {
                    if (location != null) {
                        Toast.makeText(AddShelterActivityJustTheCode.this, location.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    //TODO let user enter address

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        EditText name = findViewById(R.id.name);
        Switch isPrivate = findViewById(R.id.shelterMode);
        isPrivate.setOnClickListener(v -> {
            if (isPrivate.getText().toString().equals("Public")) {
                isPrivate.setText("Private");
            } else {
                isPrivate.setText("Public");
            }
        });

        ImageView addPhotoButton = findViewById(R.id.add_photo);

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
                PickImageDialog.build(new PickSetup()).show(AddShelterActivityJustTheCode.this);

            }
        });


        ImageButton addShelterButton = findViewById(R.id.addShelter);
        addShelterButton.setOnClickListener(v -> {
//            //TODO: only for testing.
//            if (location == null) {
//                location = new Location("idan");
//                location.setLongitude(35.217018d);
//                location.setLatitude(31.771959d);ll
//                Toast.makeText(this, "location is null,\n so uploading FAKE shelter", Toast.LENGTH_SHORT).show();
//
//            }
            SheltersApp app = (SheltersApp) getApplicationContext();
            ShelterDB db = app.getDb();
            String currentUser = db.getManager().getCurrentUser();
            Shelter newShelter;
            if (isPrivate.isChecked()) {
                newShelter = new Shelter(this, null, name.getText().toString(), Shelter.ShelterType.PRIVATE, currentUser);
            } else {
                newShelter = new Shelter(this, null, name.getText().toString(), Shelter.ShelterType.PUBLIC, null);
            }
            Navigator navigator = new Navigator(AddShelterActivityJustTheCode.this);
            navigator.getCurrentLocation().addOnSuccessListener(location -> {
                if (location == null) {
                    Toast.makeText(this, "Failed to add new shelter :(", Toast.LENGTH_LONG).show();
                    return;
                }

                newShelter.setLat(location.getLatitude());
                newShelter.setLng(location.getLongitude());
                if (newShelter.getShelterType() == Shelter.ShelterType.PRIVATE) {
                    db.addPrivateShelter(newShelter);
                } else {
                    db.addPublicShelter(newShelter);
                }
                Toast.makeText(this, "Shelter added successfully!", Toast.LENGTH_LONG).show();
            });
            finish();
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {

                case REQUEST_CODE:
                    Bitmap bitmap = getImageFromDataIntent(resultCode, data);
                    //refer the image to a shelter in the db.
                    break;
            }
        } catch (Exception e) {
            Log.e("test", "Exception in onActivityResult : " + e.getMessage());
        }
    }

    private Bitmap getImageFromDataIntent(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            //data gives you the image uri. Try to convert that to bitmap
            Uri selectedImageUri = data.getData();
            String fileString = selectedImageUri.getPath();

            Pattern lastLongPattern = Pattern.compile("([0-9]+)$");
            Matcher matcher = lastLongPattern.matcher(fileString);
            boolean b = matcher.find();
            String someNumberStr = matcher.group(1);
            long lastNumberLong = Long.parseLong(someNumberStr);


//                        Bitmap image = BitmapFactory.decodeFile(fileString);
            Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(
                    getContentResolver(), lastNumberLong,
                    MediaStore.Images.Thumbnails.FULL_SCREEN_KIND,
                    (BitmapFactory.Options) null);
            return bitmap;
        } else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("test", "Selecting picture cancelled");
        }
        return null;
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            Toast.makeText(AddShelterActivityJustTheCode.this, "choose photo", Toast.LENGTH_SHORT).show();
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            //Setting the real returned image.
            //getImageView().setImageURI(r.getUri());

            //If you want the Bitmap.
//            getImageView().setImageBitmap(r.getBitmap());
            Bitmap image = r.getBitmap();


            //Image path
            //r.getPath();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(this, r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}