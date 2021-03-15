package at.fhooe.mc.android;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import at.fhooe.mc.android.filter.Filter;
import at.fhooe.mc.android.filter.FilterAdapter;
import at.fhooe.mc.android.tools.Tools;
import at.fhooe.mc.android.tools.ToolsAdapter;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import ja.burhanrashid52.photoeditor.PhotoFilter;

import static at.fhooe.mc.android.MainActivity.TAG;

public class EditingActivity extends Activity implements View.OnClickListener {

    private static final String RED = "#af0000";
    private static final String GREY = "#e7e7e7";

    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FILTER = -1;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer ROTATE = 1;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FLIP_VERTICAL = 2;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FLIP_HORIZONTAL = 3;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FILTER_AUTOFIX = 4;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer TEXT = 5;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer BRUSH = 6;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer ERASE = 7;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer CROP = 8;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FILTER_NONE = 9;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FILTER_GRAYSCALE = 10;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FILTER_BRIGHTNESS = 11;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FILTER_DOCUMENTARY = 12;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FILTER_CONTRAST = 13;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FILTER_SEPIA = 14;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FILTER_TEMPERATURE = 15;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FILTER_NEGATIVE = 16;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FILTER_FISHEYE = 17;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FILTER_FILLLIGHT = 18;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FILTER_VIGNETTE = 19;
    /**
     * Code für die verschiedenen Bearbeitungsfunktionen
     * werden in der Edit-Liste verwendet um die Undo/Redo Funktionalität zu ermöglichen
     * werden von den Adaptern verwendet um den Buttons einen eindeutigen Code zuzuweisen
     */
    public static final Integer FILTER_SATURATE = 20;

    static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 123;

    /**
     * Editor von unserer PhotoEditor Libary, ermöglicht das Anwenden von Filtern
     */
    private PhotoEditor mPhotoEditor;
    /**
     * View von PhotoEditor Library, hat .getSource() um die ImageView zu erhalten
     */
    private PhotoEditorView mPhotoEditorView;

    private String mCurrentPhotoPath;

    private CropImageView mCropImageView;
    public RecyclerView mFilterRv;

    /**
     * Liste für {@link ToolsAdapter}, wird von {@link ToolsAdapter} verwendet um RecyclerView aufzubauen
     */
    private List<Tools> tools;
    /**
     * Liste für {@link FilterAdapter}, wird von {@link FilterAdapter} verwendet um RecyclerView aufzubauen
     */
    private List<Filter> filter;

    private Bitmap image;

    private int rotateAngle = 90;
    public boolean brushActive = false;
    public boolean eraserActive = false;


    /**
     * Liste die die Bearbeitungen speichert. Wird benötigt um die Undo/Redo Funktionalität zu ermöglichen
     */
    private ArrayList<Integer> edits = new ArrayList<>();
    private int currentEditIndex = -1;
    /**
     * Liste die die Filter speichert. edits fragt diese Liste ab um Filterdaten zu erhalten
     */
    private ArrayList<Integer> filters = new ArrayList<>();
    private int currentFilterIndex = -1;

    private int chosenColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editing);

        initTools();
        initFilter();
        initViewsAndSetAdapter();

        mPhotoEditor = new PhotoEditor.Builder(this, mPhotoEditorView).build(); // build photo editor sdk

        setImageFromIntent();
        setListenerAndCreateHomeButton();
        createBrushColorPicker();
        mPhotoEditor.setBrushDrawingMode(false);
    }

    private void initTools() {
        tools = new ArrayList<>();
        tools.add(new Tools(R.drawable.ic_crop, R.string.toolbar_crop, CROP));
        tools.add(new Tools(R.drawable.ic_rotate, R.string.toolbar_rotate, ROTATE));
        tools.add(new Tools(R.drawable.ic_hflip, R.string.toolbar_hflip, FLIP_HORIZONTAL));
        tools.add(new Tools(R.drawable.ic_vflip, R.string.toolbar_vflip, FLIP_VERTICAL));
        tools.add(new Tools(R.drawable.ic_filter, R.string.toolbar_filter, FILTER));
        tools.add(new Tools(R.drawable.ic_text, R.string.toolbar_text, TEXT));
        tools.add(new Tools(R.drawable.ic_brush, R.string.toolbar_brush, BRUSH));
        tools.add(new Tools(R.drawable.ic_erase, R.string.toolbar_erase, ERASE));
    }

    private void initFilter() {
        filter = new ArrayList<>();
        filter.add(new Filter("filter_thumbnail/original.jpg", R.string.filterbar_none, FILTER_NONE));
        filter.add(new Filter("filter_thumbnail/auto_fix.png", R.string.filterbar_auto_fix, FILTER_AUTOFIX));
        filter.add(new Filter("filter_thumbnail/gray_scale.png", R.string.filterbar_black_white, FILTER_GRAYSCALE));
        filter.add(new Filter("filter_thumbnail/contrast.png", R.string.filterbar_contrast, FILTER_CONTRAST));
        filter.add(new Filter("filter_thumbnail/brightness.png", R.string.filterbar_brightness, FILTER_BRIGHTNESS));
        filter.add(new Filter("filter_thumbnail/documentary.png", R.string.filterbar_documentary, FILTER_DOCUMENTARY));
        filter.add(new Filter("filter_thumbnail/sepia.png", R.string.filterbar_sepia, FILTER_SEPIA));
        filter.add(new Filter("filter_thumbnail/saturate.png", R.string.filterbar_saturate, FILTER_SATURATE));
        filter.add(new Filter("filter_thumbnail/temperature.png", R.string.filterbar_temperature, FILTER_TEMPERATURE));
        filter.add(new Filter("filter_thumbnail/negative.png", R.string.filterbar_negative, FILTER_NEGATIVE));
        filter.add(new Filter("filter_thumbnail/fish_eye.png", R.string.filterbar_fish_eye, FILTER_FISHEYE));
        filter.add(new Filter("filter_thumbnail/fill_light.png", R.string.filterbar_fill_light, FILTER_FILLLIGHT));
        filter.add(new Filter("filter_thumbnail/vignette.png", R.string.filterbar_vignette, FILTER_VIGNETTE));
    }

    private void initViewsAndSetAdapter() {
        mCropImageView = findViewById(R.id.cropImageView);
        mPhotoEditorView = findViewById(R.id.photoEditorView);
        RecyclerView mToolsRv = findViewById(R.id.editing_activity_rv_tools);
        mFilterRv = findViewById(R.id.editing_activity_rv_filter);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mToolsRv.setHasFixedSize(true);
        mFilterRv.setHasFixedSize(true);
        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mToolsRv.setLayoutManager(mLayoutManager);

        RecyclerView.LayoutManager mLayoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mFilterRv.setLayoutManager(mLayoutManager2);

        // specify an adapter (see also next example)
        RecyclerView.Adapter mToolsAdapter = new ToolsAdapter(tools, this);
        mToolsRv.setAdapter(mToolsAdapter);

        RecyclerView.Adapter mFilterAdapter = new FilterAdapter(filter, this);
        mFilterRv.setAdapter(mFilterAdapter);
    }

    /**
     * Erzeugt den ColorPicker für das Brush-Tool in einem unsichtbaren Linearlayout.
     */
    private void createBrushColorPicker() {
        LinearLayout ll = findViewById(R.id.editing_activity_ll_brush);
        PaletteBar paletteBar = new PaletteBar(this);
        PaletteBar.PaletteBarListener listener = new PaletteBar.PaletteBarListener() {
            @Override
            public void onColorSelected(int _color) {
                chosenColor = _color;
                mPhotoEditor.setBrushColor(chosenColor);
            }
        };
        paletteBar.setListener(listener);
        paletteBar.setColorMarginPx(10);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.setMargins(5, 5,5,5);
        paletteBar.setLayoutParams(params);
        ll.addView(paletteBar);
        mPhotoEditor.setBrushDrawingMode(false);
    }

    protected void setListenerAndCreateHomeButton() {
        Button b;
        b = findViewById(R.id.editing_activity_btn_home);
        SpannableString text = new SpannableString("< PhotoDoc");
        // make "Clicks" (characters 0 to 6) Black
        text.setSpan(new ForegroundColorSpan(Color.BLACK), 0, 6, 0);
        // make "Here" (characters 7 to 10) Red
        text.setSpan(new ForegroundColorSpan(Color.parseColor(RED)), 7, 10, 0);
        // shove our styled text into the Button
        b.setText(text, TextView.BufferType.SPANNABLE);
        b.setOnClickListener(this);
        b = findViewById(R.id.editing_activity_btn_cancel);
        b.setOnClickListener(this);
        b = findViewById(R.id.editing_activity_btn_crop);
        b.setOnClickListener(this);

        LinearLayout ll;
        ll = findViewById(R.id.editing_activity_ll_undo);
        ll.setOnClickListener(this);
        ll = findViewById(R.id.editing_activity_ll_redo);
        ll.setOnClickListener(this);
        ll = findViewById(R.id.editing_activity_ll_save);
        ll.setOnClickListener(this);
    }

    /**
     * Erzeugt eine Bitmap aus dem übergebenen imagePath vom Intent
     * Nach dem Erzeugen wird die Auflösung des Bildes reduziert um die Bearbeitungsgeschwindigkeit zu erhöhen
     * Unsere gewählte Breite ist 1079px, da nach Speichern des Bildes die Breite auf 1079px reduziert wird
     */
    private void setImageFromIntent() {
        try {
            Log.i(TAG,"setting image from intent");
            Uri uri = Uri.parse(getIntent().getStringExtra("imagePath"));
            image = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
            resizeBitmap(1079);
            mPhotoEditorView.getSource().setImageBitmap(image);
        } catch (IOException e) {
            Log.i(TAG,"in exeption in setting image from intent");
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    protected Integer getLastFilterID() {
        if(currentFilterIndex > 0) {
            return filters.get(currentFilterIndex -1);
        }
        else {
            return FILTER_NONE;
        }
    }

    protected Integer getNextFilterID() {
        if(currentFilterIndex < filters.size()-1) {
            return filters.get(currentFilterIndex +1);
        }
        else {
            return filters.get(currentFilterIndex);
        }
    }

    protected void applyActiveFilter() {
        if(currentFilterIndex >= 0) {
            Integer currentFilter = filters.get(currentFilterIndex);
            setFilterEffectWithoutPushOnList(currentFilter);
        }
        else {
            setFilterEffectWithoutPushOnList(FILTER_NONE);
        }
    }

    /**
     * Fragt die edit List ab um die vorherige Bearbeitung zu erhalten. Nach Erhalt dieser Daten wird das Image mit der rücksetzenden Funktion bearbeitet
     * bei Filtern wird die filters Liste abgefragt um den vorherigen Filter zu setzen
     * Sollte keine Änderung mehr aktiv sein, wird das Bild nicht bearbeitet.
     */
    protected void undo() {
        Integer lastEdit = 0;
        if(currentEditIndex >= 0 && currentEditIndex < edits.size()) {
            lastEdit = edits.get(currentEditIndex);
            currentEditIndex--;
        }
        else if(currentEditIndex == edits.size()) {
            lastEdit = edits.get(edits.size()-1);
            currentEditIndex -=2;
        }
        if(lastEdit.equals(ROTATE)) {
            rotateBitmap(-rotateAngle);
        }
        else if (lastEdit.equals(FLIP_VERTICAL)) {
            flipBitmapVertical();
        }
        else if (lastEdit.equals(FLIP_HORIZONTAL)) {
            flipBitmapHorizontal();
        }
        else if(lastEdit.equals(FILTER)) {
            Integer lastFilter = getLastFilterID();
            currentFilterIndex--;
            Log.i(TAG,"lastFilter : " + lastFilter.toString());
            setFilterEffectWithoutPushOnList(lastFilter);
        }
        else if(lastEdit.equals(0)) {
            setFilterEffectWithoutPushOnList(FILTER_NONE);
        }
        else {
            mPhotoEditor.undo();
        }

    }

    /**
     * Fragt die edit List ab um die nächste Bearbeitung zu erhalten. Nach Erhalt dieser Daten wird das Image mit der Funktion bearbeitet
     * bei Filtern wird die filters Liste abgefragt um den nächsten Filter zu setzen
     * Sollte die letzte Änderung aktiv sein wird das Bild nicht bearbeitet.
     */
    protected void redo() {
        Log.i(TAG, edits.toString());
            Integer nextEdit = 0;
            if(currentEditIndex <= 0 && !edits.isEmpty()) {
                nextEdit = edits.get(0);
                currentEditIndex = 1;
            }
            else if(currentEditIndex < edits.size() && !edits.isEmpty()) {
                nextEdit = edits.get(currentEditIndex);
                currentEditIndex++;
            }
        Log.i(TAG, nextEdit.toString());
            if(nextEdit.equals(ROTATE)) {
                rotateBitmap(rotateAngle);
            }
            else if (nextEdit.equals(FLIP_VERTICAL)) {
                flipBitmapVertical();
            }
            else if (nextEdit.equals(FLIP_HORIZONTAL)) {
                flipBitmapHorizontal();
            }
            else if(nextEdit.equals(FILTER)) {
                Integer nextFilter = getNextFilterID();
                currentFilterIndex++;
                Log.i(TAG,"nextFilter : " + nextFilter.toString());
                setFilterEffectWithoutPushOnList(nextFilter);
            }
            else {
                mPhotoEditor.redo();
            }
    }

    /**
     * Erzeugt eine JPEG Datei im Folder Pictures/PhotoDoc in der das Bild gespeichert wird, erzeugt.
     * Es wird auch eine Folder Pictures/PhotoDoc erstellt, falls nicht vorhanden.
     * @return JPEG Datei
     */
    private File createImageFile(){
        // Create an image file name
        createPicturesDirectory();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG" + timeStamp + ".jpg";
        File storageDir = Environment.getExternalStorageDirectory();
        File image =  new File(storageDir
                + File.separator + "Pictures/PhotoDoc"
                + File.separator + ""
                + imageFileName);

        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    private void createPicturesDirectory() {
        File directory = new File(Environment.getExternalStorageDirectory() + File.separator + "Pictures/PhotoDoc");
        directory.mkdir();
    }

    private void addImageToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
        Log.i(TAG, "saved Pic to Gallery");
    }

    private void saveImageAndFinish() {
        Log.i(TAG,"currently saving image");
        getWriteExternalStoragePermissionIfNotGranted();
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, R.string.saving, Toast.LENGTH_SHORT).show();
            File file = createImageFile();
            Log.i(TAG, file.getAbsolutePath());
            mPhotoEditor.saveAsFile(file.getAbsolutePath(), new PhotoEditor.OnSaveListener() {
                @Override
                public void onSuccess(@NonNull String imagePath) {
                    Toast.makeText(EditingActivity.this, R.string.save_sucess, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Successfully saved the image" + imagePath);
                    addImageToGallery();
                    edits.clear();
                    finish();
                }

                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(EditingActivity.this, R.string.save_failure, Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Failure while saving the image");
                }
            });
        }
    }

    private void getWriteExternalStoragePermissionIfNotGranted() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.editing_activity_btn_home: {
                saveDialog();
            } break;
            case  R.id.editing_activity_ll_undo: {
                undo();
            } break;
            case  R.id.editing_activity_ll_redo: {
                redo();
            } break;
            case  R.id.editing_activity_ll_save: {
                saveImageAndFinish();
            } break;
            case R.id.editing_activity_btn_cancel: {
                closeCropViewAndShowTools();
            } break;
            case R.id.editing_activity_btn_crop: {
                crop();
            } break;
            default : Log.e(MainActivity.TAG, "unexpected ID encountered!");
        }
    }

    @Override
    public void onBackPressed() {
        saveDialog();
    }

    protected void pushEditIDOnList(Integer typeID) {
        Log.i(TAG,"PushEdit : " + typeID);
        if(currentEditIndex < edits.size()) {
            for(int i = edits.size()-1; i>= currentEditIndex +1; i--) {
                edits.remove(i);
            }
        }
        edits.add(typeID);
        currentEditIndex = edits.size()-1;
    }

    protected void pushFilterIDOnList(Integer filterID) {
        Log.i(TAG,"PushFilter : " + filterID);
        if(currentFilterIndex < filters.size()) {
            for(int i = filters.size()-1; i>= currentFilterIndex +1; i--) {
                filters.remove(i);
            }
        }
        filters.add(filterID);
        currentFilterIndex = filters.size()-1;
    }

    public void openCropViewAndHideTools() {
        pushEditIDOnList(CROP);
        closeBrushMode();
        hideTools();
        mCropImageView.setVisibility(View.VISIBLE);
        mCropImageView.setImageBitmap(image);
    }

    private void hideTools() {
        LinearLayout ll;
        ll = findViewById(R.id.editing_activity_ll_undo);
        ll.setVisibility(View.GONE);
        ll = findViewById(R.id.editing_activity_ll_redo);
        ll.setVisibility(View.GONE);
        ll = findViewById(R.id.editing_activity_ll_save);
        ll.setVisibility(View.GONE);
        mPhotoEditorView.setVisibility(View.GONE);
        RecyclerView rv;
        rv = findViewById(R.id.editing_activity_rv_tools);
        rv.setVisibility(View.GONE);
        rv = findViewById(R.id.editing_activity_rv_filter);
        rv.setVisibility(View.GONE);
        ll = findViewById(R.id.editing_activity_ll_crop);
        ll.setVisibility(View.VISIBLE);
    }

    private void closeCropViewAndShowTools() {
        LinearLayout ll;
        ll = findViewById(R.id.editing_activity_ll_crop);
        ll.setVisibility(View.GONE);
        mCropImageView.setVisibility(View.GONE);

        ll = findViewById(R.id.editing_activity_ll_undo);
        ll.setVisibility(View.VISIBLE);
        ll = findViewById(R.id.editing_activity_ll_redo);
        ll.setVisibility(View.VISIBLE);
        ll = findViewById(R.id.editing_activity_ll_save);
        ll.setVisibility(View.VISIBLE);
        mPhotoEditorView.setVisibility(View.VISIBLE);
        RecyclerView rv = findViewById(R.id.editing_activity_rv_tools);
        rv.setVisibility(View.VISIBLE);
    }

    private void crop() {
        image = mCropImageView.getCroppedImage();
        mPhotoEditorView.getSource().setImageBitmap(image);
        applyActiveFilter();
        closeCropViewAndShowTools();
        closeBrushMode();
    }

    public void rotate() {
        closeBrushMode();
        pushEditIDOnList(ROTATE);
        rotateBitmap(rotateAngle);
    }

    public void flipVertical() {
        closeBrushMode();
        pushEditIDOnList(FLIP_VERTICAL);
        flipBitmapVertical();
    }

    public void flipHorizontal() {
        closeBrushMode();
        pushEditIDOnList(FLIP_HORIZONTAL);
        flipBitmapHorizontal();
    }

    public void addText(String text,int color) {
        pushEditIDOnList(TEXT);
        mPhotoEditor.addText(text, color);
    }

    public void toggleBrush() {
        if(!brushActive) {
            pushEditIDOnList(BRUSH);
            LinearLayout ll = findViewById(R.id.editing_activity_ll_brush);
            ll.setVisibility(View.VISIBLE);
            mPhotoEditor.setBrushDrawingMode(true);
            mPhotoEditor.setBrushColor(chosenColor);
            brushActive = true;
            eraserActive = false;
        }
        else {
            hideColorPicker();
            mPhotoEditor.setBrushDrawingMode(false);
            brushActive = false;
        }
    }

    private void hideColorPicker() {
        LinearLayout ll = findViewById(R.id.editing_activity_ll_brush);
        ll.setVisibility(View.GONE);
    }

    public void toggleErase() {
        if(!eraserActive) {
            hideColorPicker();
            pushEditIDOnList(ERASE);
            mPhotoEditor.setBrushDrawingMode(true);
            mPhotoEditor.brushEraser();
            eraserActive = true;
            brushActive = false;
        }
        else {
            mPhotoEditor.setBrushDrawingMode(false);
            eraserActive = false;
        }
    }

    public void closeBrushMode() {
        hideColorPicker();
        eraserActive = false;
        brushActive = false;
        mPhotoEditor.setBrushDrawingMode(false);
    }

    public void setFilterEffect(Integer filterID) {
        pushEditIDOnList(FILTER);
        pushFilterIDOnList(filterID);
        setFilterEffectWithoutPushOnList(filterID);
    }

    private void setFilterEffectWithoutPushOnList(Integer filterID) {
        if(filterID.equals(FILTER_AUTOFIX)) {
            mPhotoEditor.setFilterEffect(PhotoFilter.AUTO_FIX);
        }
        else if(filterID.equals(FILTER_NONE)) {
            mPhotoEditor.setFilterEffect(PhotoFilter.NONE);
        }
        else if(filterID.equals(FILTER_GRAYSCALE)) {
            mPhotoEditor.setFilterEffect(PhotoFilter.GRAY_SCALE);
        }
        else if(filterID.equals(FILTER_BRIGHTNESS)) {
            mPhotoEditor.setFilterEffect(PhotoFilter.BRIGHTNESS);
        }
        else if(filterID.equals(FILTER_DOCUMENTARY)) {
            mPhotoEditor.setFilterEffect(PhotoFilter.DOCUMENTARY);
        }
        else if(filterID.equals(FILTER_CONTRAST)) {
            mPhotoEditor.setFilterEffect(PhotoFilter.CONTRAST);
        }
        else if(filterID.equals(FILTER_SEPIA)) {
            mPhotoEditor.setFilterEffect(PhotoFilter.SEPIA);
        }
        else if(filterID.equals(FILTER_TEMPERATURE)) {
            mPhotoEditor.setFilterEffect(PhotoFilter.TEMPERATURE);
        }
        else if(filterID.equals(FILTER_NEGATIVE)) {
            mPhotoEditor.setFilterEffect(PhotoFilter.NEGATIVE);
        }
        else if(filterID.equals(FILTER_FISHEYE)) {
            mPhotoEditor.setFilterEffect(PhotoFilter.FISH_EYE);
        }
        else if(filterID.equals(FILTER_FILLLIGHT)) {
            mPhotoEditor.setFilterEffect(PhotoFilter.FILL_LIGHT);
        }
        else if(filterID.equals(FILTER_VIGNETTE)) {
            mPhotoEditor.setFilterEffect(PhotoFilter.VIGNETTE);
        }
        else if(filterID.equals(FILTER_SATURATE)) {
            mPhotoEditor.setFilterEffect(PhotoFilter.SATURATE);
        }
    }

    private void rotateBitmap(float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        mPhotoEditorView.getSource().setImageBitmap(image);
        applyActiveFilter();
    }

    private void flipBitmapVertical() {
        Matrix matrix = new Matrix();
        matrix.preScale(1.0f, -1.0f);
        image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        mPhotoEditorView.getSource().setImageBitmap(image);
        applyActiveFilter();
    }

    private void flipBitmapHorizontal() {
        Matrix matrix = new Matrix();
        matrix.preScale(-1.0f, 1.0f);
        image = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(), matrix, true);
        mPhotoEditorView.getSource().setImageBitmap(image);
        applyActiveFilter();
    }

    private void resizeBitmap(int newWidth) {
        int width = image.getWidth();
        int height = image.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleWidth);
        // "RECREATE" THE NEW BITMAP
        image = Bitmap.createBitmap(image, 0, 0, width, height, matrix, false);
    }

    private void saveDialog()
    {
        if(currentEditIndex == -1)
            finish();
        else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(EditingActivity.this);
            builder.setMessage(R.string.savedialog_title);
            builder.setCancelable(true);
            builder.setPositiveButton(R.string.savedialog_positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    saveImageAndFinish();
                }
            });
            builder.setNegativeButton(R.string.savedialog_negative, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNeutralButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog ad = builder.create();
            ad.show();
        }
    }

    /**
     * Erstellt Alert Dialog mit den Elementen
     * TextEdit
     * Buttons
     * ColorPicker
     *
     * Nach Klicken auf OK Button wird dieser Text in der View geaddet
     *
     */
    public void textDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        //set up the top message
        TextView textView = new TextView(getApplicationContext());
        textView.setText(R.string.textdialog_text);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.topMargin = 20;
        params.bottomMargin = 20;
        textView.setTextSize(22);
        textView.setTextColor(Color.BLACK);
        textView.setFontFeatureSettings("@font/comic_sans_mc");
        textView.setLayoutParams(params);
        layout.addView(textView);

        // Set up the input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setBackgroundColor(Color.parseColor(GREY));
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.leftMargin = 20;
        params.rightMargin = 20;
        input.setLayoutParams(params);
        layout.addView(input);
        input.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                input.post(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager inputMethodManager= (InputMethodManager) EditingActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.showSoftInput(input, InputMethodManager.SHOW_IMPLICIT);
                    }
                });
            }
        });
        input.requestFocus();

        int dpValue = 43;
        float d = this.getResources().getDisplayMetrics().density;
        int height = (int)(dpValue * d);

        PaletteBar paletteBar = new PaletteBar(this);
        PaletteBar.PaletteBarListener listener = new PaletteBar.PaletteBarListener() {
            @Override
            public void onColorSelected(int _color) {
                chosenColor = _color;
            }
        };
        paletteBar.setListener(listener);
        paletteBar.setColorMarginPx(10);
        params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.height = height;
        params.setMargins(20,20,20,20);
        paletteBar.setLayoutParams(params);
        layout.addView(paletteBar);

        // Set up the buttons
        builder.setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addText(input.getText().toString(), chosenColor);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setView(layout);
        builder.show();
    }
}
