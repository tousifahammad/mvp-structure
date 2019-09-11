package com.app.baseproject.profile;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.baseproject.BuildConfig;
import com.app.baseproject.R;
import com.app.baseproject.baseclasses.SharedMethods;
import com.app.baseproject.utils.Alert;
import com.app.baseproject.utils.SettingSharedPreferences;
import com.hbb20.CountryCodePicker;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText et_name, et_mobile, et_email, et_address1, et_address2, et_suburb, et_post_code, et_country;
    EditText et_medicare_no, et_medical_history, et_emergency_contacts, et_fb_id, et_twitter;
    TextView tv_dob;
    int year, month, day;
    final int DATE_PICKER_ID = 11;
    ImageView iv_edit, iv_medical_history;
    Button btn_update, btn_delete;
    LinearLayout ll_root_view;
    CircleImageView iv_profile_pic;
    boolean isEditModeEnable;
    List<View> all_view = new ArrayList<>();
    int onClickedImageId;
    String str_profile_pic = "", str_medical_history = "";
    private Uri fileUri = null;
    private String picturePath = null;

    Spinner spinner_country, spinner_state, spinner_suburb;
    ArrayList<String> countryList = new ArrayList<>();
    ArrayList<String> stateList = new ArrayList<>();
    ArrayList<String> suburbList = new ArrayList<>();
    JSONArray counties, states, suburbs;

    String country_id, state_id, suburb_id, country_name = "", state_name = "", suburb_name = "";
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int PHOTO_REQUEST_CUT = 300;
    private static final int GELLARY_PICK_IMAGE = 200;
    public static final String IMAGE_DIRECTORY_NAME = "capitalmela";
    CountryCodePicker countryCodePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // set Toolbar
        setToolBar();

        initUI();

        all_view = getAllChildren(ll_root_view);

        //for first time profile open in edit mode
        if (new SettingSharedPreferences(this).getIS_FIRST_TIME_PROFILE()) {
            isEditModeEnable = true;
        }
        changeEditMode();

        //new ProfilePresenter(this).requestProfileDetails();

    }

    private void setToolBar() {
        AppCompatTextView tv_tooltitle = findViewById(R.id.tv_app_name);
        tv_tooltitle.setVisibility(View.VISIBLE);
        tv_tooltitle.setText(getString(R.string.title_profile));
        findViewById(R.id.ib_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        iv_edit = findViewById(R.id.ib_filter);
        iv_edit.setImageResource(R.mipmap.edit);
        iv_edit.setVisibility(View.VISIBLE);
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeEditMode();
            }
        });

    }


    private void initUI() {
        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_mobile = findViewById(R.id.et_mobile);
        et_address1 = findViewById(R.id.et_address1);
        et_address2 = findViewById(R.id.et_address2);
        et_suburb = findViewById(R.id.et_suburb);
        et_post_code = findViewById(R.id.et_post_code);
        et_country = findViewById(R.id.et_country);
        et_medicare_no = findViewById(R.id.et_medicare_no);
        tv_dob = findViewById(R.id.tv_dob);
        et_emergency_contacts = findViewById(R.id.et_emergency_contacts);
        et_fb_id = findViewById(R.id.et_fb_id);
        et_twitter = findViewById(R.id.et_twitter);
        iv_profile_pic = findViewById(R.id.iv_profile_pic);
        iv_medical_history = findViewById(R.id.iv_medical_history);
        et_medical_history = findViewById(R.id.et_medical_history);
        ll_root_view = findViewById(R.id.ll_root_view);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        countryCodePicker = findViewById(R.id.countryCodePicker);

        countryCodePicker.setDefaultCountryUsingNameCode("AU");
        countryCodePicker.resetToDefaultCountry();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void changeEditMode() {
        if (isEditModeEnable) { // edit mode on
            Toast.makeText(this, "Edit mode on", Toast.LENGTH_SHORT).show();

            isEditModeEnable = false;
            iv_edit.setImageResource(R.mipmap.edit);
            btn_update.setVisibility(View.VISIBLE);

            for (int i = 0; i < all_view.size(); i++) {
                if (all_view.get(i) instanceof EditText) {
                    EditText editText = (EditText) all_view.get(i);
                    editText.setEnabled(true);

                } else if (all_view.get(i) instanceof CircleImageView) {
                    CircleImageView imageView = (CircleImageView) all_view.get(i);
                    imageView.setClickable(true);

                } else if (all_view.get(i) instanceof ImageView) {
                    ImageView imageView = (ImageView) all_view.get(i);
                    imageView.setClickable(true);
                }
            }
        } else { // edit mode off -- read mode on
            Toast.makeText(this, "Read mode on", Toast.LENGTH_SHORT).show();

            isEditModeEnable = true;
            iv_edit.setImageResource(R.mipmap.list);
            btn_update.setVisibility(View.GONE);
            btn_delete.setVisibility(View.GONE);

            for (int i = 0; i < all_view.size(); i++) {
                if (all_view.get(i) instanceof EditText) {
                    EditText editText = (EditText) all_view.get(i);
                    editText.setEnabled(false);
                    editText.setTextColor(getResources().getColor(R.color.darkGray));

                } else if (all_view.get(i) instanceof CircleImageView) {
                    CircleImageView imageView = (CircleImageView) all_view.get(i);
                    imageView.setClickable(false);

                } else if (all_view.get(i) instanceof ImageView) {
                    ImageView imageView = (ImageView) all_view.get(i);
                    imageView.setClickable(false);
                }
            }
        }
    }


    //    //========================= Button clicked ====================================
    public void onUpdateClick(View view) {
        if (validateEveryField()) {
            new ProfilePresenter(this).requestProfileUpload();
        }
    }


    public void onProfileImageClick(View view) {
        onClickedImageId = 0;
        onClickedImageId = view.getId();
        requestPermission();
    }

    public void pickDOB(View view) {
        final Calendar c = Calendar.getInstance();
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        showDialog(DATE_PICKER_ID);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:
                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                DatePickerDialog dialog = new DatePickerDialog(this, pickerListener, year, month, day);
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                return dialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;

            // Show selected date
            tv_dob.setText(new StringBuilder().append(year).append("-").append(month + 1).append("-").append(day).append(" "));
        }
    };

    private boolean validateEveryField() {
        if (et_name.getText().toString().isEmpty()) {
            Alert.showError(this, "First name : " + getString(R.string.field_empty));
            return false;

        } else if (!SharedMethods.validateName(et_name.getText().toString().trim())) {
            Alert.showError(this, "Invalid first name");
            return false;

        } else if (et_address1.getText().toString().isEmpty()) {
            Alert.showError(this, "Address 1 : " + getString(R.string.field_empty));
            return false;

        } else if (et_address2.getText().toString().isEmpty()) {
            Alert.showError(this, "Address 2 : " + getString(R.string.field_empty));
            return false;

        } else if (et_email.getText().toString().isEmpty()) {
            Alert.showError(this, "Email : " + getString(R.string.field_empty));
            return false;

        } else if (!SharedMethods.validateEmail(et_email.getText().toString().trim())) {
            Alert.showError(this, "Invalid email");
            return false;

        } else if (et_mobile.getText().toString().trim().length() != 9) {
            Alert.showError(this, "please enter 9 digit mobile no");
            return false;

        } else if (country_name.isEmpty() && country_id.isEmpty()) {
            Alert.showError(this, "Please select Country ");
            return false;

        } else if (state_name.isEmpty() && state_id.isEmpty()) {
            Alert.showError(this, "Please select state ");
            return false;

        } else if (suburb_name.isEmpty() && suburb_id.isEmpty()) {
            Alert.showError(this, "Please select suburb ");
            return false;

        } else if (et_post_code.getText().toString().isEmpty()) {
            Alert.showError(this, "Post Code : " + getString(R.string.field_empty));
            return false;

        } else if (tv_dob.getText().toString().isEmpty()) {
            Alert.showError(this, "DOB : " + getString(R.string.field_empty));
            return false;
        }
        return true;
    }


    void setSpinnerAdapter(String type) {
        Spinner spinner;
        ArrayAdapter arrayAdapter;
        if (type.equals("countries")) {
            spinner = findViewById(R.id.spinner_country);
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, countryList);
            //spinner.setSelection(countryList.indexOf(getNameById(counties, country_id)));

        } else if (type.equals("states")) {
            spinner = findViewById(R.id.spinner_state);
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, stateList);
            //spinner.setSelection(stateList.indexOf(getNameById(states, state_id)));

        } else {
            spinner = findViewById(R.id.spinner_suburb);
            arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, suburbList);
            //spinner.setSelection(suburbList.indexOf(getNameById(suburbs, suburb_id)));
        }

        spinner.setOnItemSelectedListener(this);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        if (type.equals("countries")) {
            spinner.setSelection(countryList.indexOf(country_name));
        } else if (type.equals("states")) {
            spinner.setSelection(stateList.indexOf(state_name));
        } else {
            spinner.setSelection(suburbList.indexOf(suburb_name));
        }
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
        String selected_type = "";
        if (parent.getId() == R.id.spinner_country) {
            selected_type = countryList.get(position);
            if (!selected_type.isEmpty()) {
                country_name = selected_type;
                //stateList.clear();
                //new ProfilePresenter(ProfileActivity.this).requestStateList();

                String temp_id = getIdByName(counties, country_name);
                if (temp_id != null) {
                    country_id = temp_id;
                    stateList.clear();
                    suburbList.clear();
                    //Alert.showError(ProfileActivity.this, country_id);
                    new ProfilePresenter(ProfileActivity.this).requestStateList();
                }
            } else {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            }

        } else if (parent.getId() == R.id.spinner_state) {
            selected_type = stateList.get(position);
            if (!selected_type.isEmpty()) {
                state_name = selected_type;
                String temp_id = getIdByName(states, state_name);
                if (temp_id != null) {
                    state_id = temp_id;
                    suburbList.clear();
                    new ProfilePresenter(ProfileActivity.this).requestSuburbList();
                }
            } else {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            }

        } else if (parent.getId() == R.id.spinner_suburb) {
            selected_type = suburbList.get(position);
            if (!selected_type.isEmpty()) {
                suburb_name = selected_type;
                String temp_id = getIdByName(suburbs, suburb_name);
                if (temp_id != null) {
                    suburb_id = temp_id;
                }
            } else {
                Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
    }

    private String getNameById(JSONArray jsonArray, String id) {
        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (id.equals(jsonObject.getString("id"))) {
                        //Alert.showMessage(ProfileActivity.this, "id : " +id + " == "+ jsonObject.getString("name"));
                        return jsonObject.getString("name");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getIdByName(JSONArray jsonArray, String name) {
        try {
            if (jsonArray != null) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (name.equalsIgnoreCase(jsonObject.getString("name"))) {
                        //Alert.showMessage(ProfileActivity.this, "id : " +id + " == "+ jsonObject.getString("name"));
                        return jsonObject.getString("id");
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {

        public Activity c;
        public LinearLayout camera, file;

        public CustomDialogClass(Activity a) {
            super(a);
            this.c = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.file_picker_dialog);

            camera = findViewById(R.id.camera);
            file = findViewById(R.id.file);
            camera.setOnClickListener(this);
            file.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.camera:
                    // open camera
                    Toast.makeText(ProfileActivity.this, "Please capture lower resolution image", Toast.LENGTH_SHORT).show();
                    cameraIntent();
                    dismiss();
                    break;

                case R.id.file:
                    // open file picker
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), GELLARY_PICK_IMAGE);

                    Toast.makeText(ProfileActivity.this, "Please select small file", Toast.LENGTH_SHORT).show();

                    dismiss();
                    break;
                default:
                    break;
            }
            dismiss();
        }
    }

    private void requestPermission() {
        Dexter.withActivity(ProfileActivity.this)
                .withPermissions(
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // do you work now
                            CustomDialogClass cdd = new CustomDialogClass(ProfileActivity.this);
                            cdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            cdd.show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // permission is denied permenantly, navigate user to app settings
                            Alert.showError(ProfileActivity.this, "Please allow us to use this features");
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    private void cameraIntent() {
        Intent in = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            in.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(in, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } else {
            if (in.resolveActivity(getPackageManager()) != null) {
                try {
                    File photoFile = createImageFile();
                    fileUri = FileProvider.getUriForFile(this,
                            BuildConfig.APPLICATION_ID + ".provider",
                            photoFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                in.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
                startActivityForResult(in, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            }

        }

    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("HomeActivity", "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + "USER_IMG_" + timeStamp + ".jpg");

        picturePath = mediaFile.getPath();
        //  Log.e("image_path", picturePath);
        //Bitmap takenImage = BitmapFactory.decodeFile(mediaFile.getAbsolutePath());

        return mediaFile;
    }

    private Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("HomeActivity", "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {

            mediaFile = new File(mediaStorageDir.getPath() + File.separator + "USER_IMG_" + timeStamp + ".jpg");

        } else {
            return null;
        }
        return mediaFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
                crop(fileUri);
//                try {
//                    //Bitmap mImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
//                    final InputStream imageStream = getContentResolver().openInputStream(fileUri);
//                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                    String encodedImage = SharedMethods.encodeImageBitmap(selectedImage);
//                    if (!encodedImage.isEmpty()) {
//                        setImage(selectedImage, encodedImage);
//                    } else {
//                        Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (Exception e) {
//                    Alert.showError(this, e.getMessage());
//                }
            }

            if (requestCode == GELLARY_PICK_IMAGE) {
                try {
                    if (data != null && data.getData() != null) {
                        final InputStream imageStream = getContentResolver().openInputStream(data.getData());
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        String encodedImage = SharedMethods.encodeImageBitmap(selectedImage);
                        if (!encodedImage.isEmpty()) {
                            setImage(selectedImage, encodedImage);
                        } else {
                            Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Alert.showError(this, "No image found");
                    }
                } catch (Exception e) {
                    Alert.showError(this, e.getMessage());
                }
            }

            if (requestCode == PHOTO_REQUEST_CUT) {
                try {
                    if (data != null) {
//                        Bundle extras = data.getExtras();
//                        Bitmap selectedImage = extras.getParcelable("data");
                        final InputStream imageStream = getContentResolver().openInputStream(data.getData());
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        String encodedImage = SharedMethods.encodeImageBitmap(selectedImage);
                        if (!encodedImage.isEmpty()) {
                            setImage(selectedImage, encodedImage);
                        } else {
                            Toast.makeText(this, "No image found", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    Alert.showError(this, e.getMessage());
                }
            }
        }
    }

    private void setImage(Bitmap selectedImage, String encodedImage) {
        if (onClickedImageId == R.id.iv_profile_pic) {
            iv_profile_pic.setImageBitmap(selectedImage);
            str_profile_pic = encodedImage;

        } else if (onClickedImageId == R.id.iv_medical_history) {
            iv_medical_history.setImageBitmap(selectedImage);
            str_medical_history = encodedImage;
        }
//
//        } else if (onClickedImageId == R.id.iv_address) {
//            iv_address.setImageBitmap(selectedImage);
//            str_address_pic = encodedImage;
//        }
    }

    private List<View> getAllChildren(View v) {
        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            //Do not add any parents, just add child elements
            result.addAll(getAllChildren(child));
        }
        return result;
    }

    public void crop(Uri uri) {
        this.grantUriPermission("com.android.camera", uri,
                Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //Android N need set permission to uri otherwise system camera don't has permission to access file wait crop
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.putExtra("crop", "true");
        //The proportion of the crop box is 1:1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //Crop the output image size
        intent.putExtra("outputX", 800);
        intent.putExtra("outputY", 800);
        //image type
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);
        //true - don't return uri |  false - return uri
        intent.putExtra("return-data", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }


}
