package at.fhooe.mc.android;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener {

    public  static final String TAG = "PhotoDoc";
    private static final int PERMISSION_REQUEST_CODE = 666;

    private static final int GALLERY_REQUEST = 1;
    static final int CAMERA_REQUEST = 14;

    private String[] PERMISSION_LIST = new String[] {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private Uri mCurrentPhotoUri;

    @Override
    protected void onCreate(Bundle _savedInstanceState) {
        super.onCreate(_savedInstanceState);
        setContentView(R.layout.activity_main);

        setButtonListener();
    }

    private void setButtonListener() {
        Button b;
        b = findViewById(R.id.main_activity_btn_camera);
        b.setOnClickListener(this);
        b = findViewById(R.id.main_activity_btn_gallery);
        b.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().    setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        checkPermissions();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_activity_btn_camera : {
                dispatchTakePictureIntent();
            } break;
            case R.id.main_activity_btn_gallery : {
                openGalleryIntent();
            } break;
            default : Log.i (TAG, "unexpected ID encountered!");
        }
    }

    private void openGalleryIntent() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        if (galleryIntent.resolveActivity(getPackageManager()) != null)
            startActivityForResult(galleryIntent, GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, requestCode + " " + resultCode);
        if (resultCode == RESULT_OK) {
            Intent intent;
            intent = new Intent(this, EditingActivity.class);
            switch (requestCode) {
                case CAMERA_REQUEST: {
                    intent.putExtra("imagePath", mCurrentPhotoUri.toString());
                    startActivity(intent);
                } break;
                case GALLERY_REQUEST: {
                    Uri uri = data.getData();
                    assert uri != null;
                    intent.putExtra("imagePath", uri.toString());
                    Log.i(TAG, uri.toString());
                    startActivity(intent);
                } break;
                default : Log.e (TAG, "unexpected Request encountered!");
            }
        }
    }

    private void dispatchTakePictureIntent() {
        getCameraPermissionIfNotGranted();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = createTemporaryJPEGFile();
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    Log.i(TAG, photoFile.getAbsolutePath());
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "at.fhooe.mc.android.photodoc.fileprovider",
                            photoFile);
                    mCurrentPhotoUri = photoURI;
                    Log.i(TAG, photoURI.toString());
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAMERA_REQUEST);
                }
            }
        }
    }

    private void getCameraPermissionIfNotGranted() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Erzeuge temporäre JPEG Datei um das Bild von der Kamera dorthin zu speichern.
     * @return temporäre JPEG Datei
     */
    private File createTemporaryJPEGFile() {
        // Create an image file name
        String imageFileName = "JPEG_PhotoDoc_tempFile.jpg";
        File image=new File(new File(getFilesDir(), "photos"), imageFileName);
        if (image.exists()) {
            image.delete();
        }
        else {
            image.getParentFile().mkdirs();
        }
        return image;
    }

    @Override
    public void onRequestPermissionsResult(int      _requestCode,
                                           @NonNull String[] _permissions,
                                           @NonNull int[]    _grantResults) {
        if (_requestCode == PERMISSION_REQUEST_CODE) {
            for (int i = 0 ; i < _grantResults.length ; i++) {
                int ret = _grantResults[i];
                if (ret != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "permission: " + _permissions[i] + " needed for app",Toast.LENGTH_LONG).show();
                    finish();
                } // if not granted
            } // for i
        } // if code
    }

    private void checkPermissions() {
        for (String permission : PERMISSION_LIST) {
            checkPermission(permission);
        } // for each permission
    }

    private void checkPermission(String _permission) {
        Log.i(TAG, "checkSelfPermission on " + _permission);
        if (checkSelfPermission(_permission) != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, " ... NOT granted! Requesting ... ");
            requestPermissions(new String[]{_permission}, PERMISSION_REQUEST_CODE);
        } else {
            Log.i(TAG, " ... granted!");
        }
    }
}
