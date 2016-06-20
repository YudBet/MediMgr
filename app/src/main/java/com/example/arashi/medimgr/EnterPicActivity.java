package com.example.arashi.medimgr;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class EnterPicActivity extends ActionBarActivity {
    private static final int PICTURE_CHOOSER_REQ = 0;

    private ImageView mediImg;
    private AppCompatEditText mediDosage;
    private AppCompatEditText mediName;

    private Button confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_pic);

        initCamera();

        mediName = (AppCompatEditText) findViewById(R.id.medi_name);
        mediDosage = (AppCompatEditText) findViewById(R.id.medi_dosage);

        initConfirmButton();
    }

    public void initCamera() {
        mediImg = (ImageView) findViewById(R.id.medi_img);
        //cameraBtn = (Button) findViewById(R.id.camera_btn);

        mediImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPictureChooserActivity();
            }
        });
    }

    public void initConfirmButton() {
        confirm = (Button)findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(EnterPicActivity.this, EnterResultActivity.class);
                intent.putExtra("ENTER_OPTION", MainActivity.ENTER_PICTURE);
                intent.putExtra("MEDI_NAME", mediName.getText().toString());
                intent.putExtra("MEDI_DOSAGE", mediDosage.getText().toString());
                intent.putExtra("PICTURE_URI_STR", outputFileUri.toString());
                startActivity(intent);
                finish();
            }
        });
    }

    private void startPictureChooserActivity() {
        List<Intent> cameraIntents = getCameraIntents();

        Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        cropSettingsForIntent(galleryIntent);

        Intent chooserIntent = Intent.createChooser(galleryIntent, getResources().getString(R.string.pic_chooser_prompt));
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, cameraIntents.toArray(new Parcelable[cameraIntents.size()]));

        startActivityForResult(chooserIntent, PICTURE_CHOOSER_REQ);
    }

    private Uri outputFileUri;
    private List<Intent> getCameraIntents() {
        setupOutputFileUri();

        List<Intent> cameraIntents = new ArrayList<Intent>();
        final Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        final PackageManager packageManager = getPackageManager();
        final List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            final String packageName = res.activityInfo.packageName;
            final Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(packageName);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            cameraIntents.add(intent);
        }

        return cameraIntents;
    }

    private void cropSettingsForIntent(Intent intent) {
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 4);
        intent.putExtra("aspectY", 3);
        intent.putExtra("outputX", 335);
        intent.putExtra("outputY", 250);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
    }

    private void setupOutputFileUri() {
        final File root = new File(Environment.getExternalStorageDirectory() + File.separator + "MediMgr" + File.separator);
        root.mkdirs();
        final String fname = "img_"+ System.currentTimeMillis() + ".jpg";
        final File sdImageMainDirectory = new File(root, fname);
        outputFileUri = Uri.fromFile(sdImageMainDirectory);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICTURE_CHOOSER_REQ && resultCode == RESULT_OK) {
            boolean isCamera;
            if (data == null)
                isCamera = true;
            else {
                final String action = data.getAction();
                if (action == null)
                    isCamera = false;
                else
                    isCamera = action.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            }

            Uri selectedImageUri;
            if (isCamera) selectedImageUri = outputFileUri;
            else selectedImageUri = data == null ? null : data.getData();

            if (selectedImageUri != null)
                mediImg.setImageURI(selectedImageUri);
            else {
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                outputFileUri = getImageUri(getApplicationContext(), bp);
                mediImg.setImageBitmap(bp);
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_enter_pic, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
