package com.apploads.footwin.profile;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apploads.footwin.MainPageActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.helpers.CustomDialogClass;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.helpers.utils.AppUtils;
import com.apploads.footwin.helpers.utils.StringUtils;
import com.apploads.footwin.loading.CountdownActivity;
import com.apploads.footwin.login.LoginActivity;
import com.apploads.footwin.model.User;
import com.apploads.footwin.model.UserResponse;
import com.apploads.footwin.services.ApiManager;
import com.apploads.footwin.signup.SignupStepThree;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.gson.internal.LinkedTreeMap;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.squareup.picasso.Picasso;
import com.ybs.countrypicker.CountryPicker;
import com.ybs.countrypicker.CountryPickerListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends BaseActivity {
    Button btnSave;
    EditText txtFullname, txtEmail, txtMobile;
    MaterialSpinner spinnerGender;
    TextView txtCountry, txtPhoneCode, txtBack;
    ImageButton btnCamera;
    ImageView imgProfile;
    boolean isImageSet;
    RelativeLayout viewLoading;
    ProgressBar progressBar;

    Bitmap bitmap;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2, REQUEST_READ_EXTERNAL_STORAGE = 3;

    @Override
    public int getContentViewId() {
        return R.layout.edit_profile_activity;
    }

    @Override
    public void doOnCreate() {
        initView();
        initListeners();
    }

    private void initView(){
        btnSave = _findViewById(R.id.btnSave);
        txtFullname = _findViewById(R.id.txtFullname);
        txtEmail = _findViewById(R.id.txtEmail);
        txtMobile = _findViewById(R.id.txtMobile);
        spinnerGender = _findViewById(R.id.spinnerGender);
        txtCountry = _findViewById(R.id.txtCountry);
        txtPhoneCode = _findViewById(R.id.txtPhoneCode);
        viewLoading = _findViewById(R.id.viewLoading);
        btnCamera = _findViewById(R.id.btnCamera);
        txtBack = _findViewById(R.id.txtBack);
        imgProfile = _findViewById(R.id.imgProfile);
        progressBar = _findViewById(R.id.spin_kit);

        viewLoading.setVisibility(View.GONE);

        spinnerGender.setItems("Male", "FEMALE");

        txtFullname.setText(StaticData.user.getFullname());
        txtCountry.setText(StaticData.user.getCountry());
        txtEmail.setText(StaticData.user.getEmail());
        txtMobile.setText(StaticData.user.getPhone());
        txtPhoneCode.setText(StaticData.user.getPhoneCode());

        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        if(StaticData.user.getAvatar() != null && !StaticData.user.getAvatar().isEmpty()) {
            Picasso.with(this)
                    .load(StaticData.config.getMediaUrl() + StaticData.user.getAvatar())
                    .into(imgProfile);
        } else {
            imgProfile.setImageResource(R.drawable.avatar_male);
            if(StaticData.user.getGender() == "female") {
                imgProfile.setImageResource(R.drawable.avatar_female);
            }
        }
    }

    private void initListeners(){
        txtCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final CountryPicker picker = CountryPicker.newInstance("Select Country");  // dialog title
                picker.setListener(new CountryPickerListener() {
                    @Override
                    public void onSelectCountry(String name, String code, String dialCode, int flagDrawableResID) {
                        txtCountry.setText(name);
                        txtPhoneCode.setText(dialCode);
                        picker.dismiss();
                    }
                });
                picker.show(getSupportFragmentManager(), "COUNTRY_PICKER");
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateView()){
                    editUserService();
                }
            }
        });

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageDialog();
            }
        });
    }

    // CAMERA FUNCTIONS
    private void openImageDialog() {
        try {
            PackageManager packageManager = getPackageManager();
            int hasPerm = packageManager.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                hasPerm = packageManager.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE, getPackageName());
                if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                    final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(EditProfileActivity.this);
                    builder.setTitle("Select Option");
                    builder.setItems(options, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            if (options[item].equals("Take Photo")) {
                                dialog.dismiss();
                                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(intent, PICK_IMAGE_CAMERA);
                            } else if (options[item].equals("Choose From Gallery")) {
                                dialog.dismiss();
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                            } else if (options[item].equals("Cancel")) {
                                dialog.dismiss();
                            }
                        }
                    });
                    builder.show();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(EditProfileActivity.this, "Read External Storage Permission error", Toast.LENGTH_SHORT);
                    } else {
                        ActivityCompat.requestPermissions(EditProfileActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                    }
                }
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(EditProfileActivity.this,
                        Manifest.permission.CAMERA)) {
                    Toast.makeText(EditProfileActivity.this, "Camera Permission error", Toast.LENGTH_SHORT);
                } else {
                    ActivityCompat.requestPermissions(EditProfileActivity.this,
                            new String[]{Manifest.permission.CAMERA}, PICK_IMAGE_CAMERA);
                }
            }
        } catch (Exception e) {
            Toast.makeText(EditProfileActivity.this, "Camera Permission error", Toast.LENGTH_SHORT);
            e.printStackTrace();
        }
    }

    public String getRealPathFromURI(Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            try {
                Uri selectedImage = data.getData();
                bitmap = (Bitmap) data.getExtras().get("data");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                destination = new File(Environment.getExternalStorageDirectory() + "/" +
                        getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                FileOutputStream fo;
                try {
                    destination.createNewFile();
                    fo = new FileOutputStream(destination);
                    fo.write(bytes.toByteArray());
                    fo.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imgPath = destination.getAbsolutePath();
                imgProfile.setImageBitmap(bitmap);
                isImageSet = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == PICK_IMAGE_GALLERY) {
            try {
                if(data != null) {
                    Uri selectedImage = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

                    imgPath = getRealPathFromURI(selectedImage);
                    destination = new File(imgPath.toString());
                    imgProfile.setImageBitmap(bitmap);
                    isImageSet = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean validateView(){
        if(StringUtils.isValid(txtFullname.getText())
                && StringUtils.isValid(txtEmail.getText()) && StringUtils.isValid(txtCountry.getText())
                && StringUtils.isValid(txtMobile.getText()) && StringUtils.isValid(spinnerGender.getText())){

            return true;
        }else{
            return false;
        }
    }

    private void editUserService(){
        viewLoading.setVisibility(View.VISIBLE);
        ApiManager.getService().editUser(txtFullname.getText().toString(),
                txtEmail.getText().toString(), txtCountry.getText().toString(),
                txtPhoneCode.getText().toString(),txtMobile.getText().toString(),
                spinnerGender.getText().toString()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if(userResponse.getStatus() == 1){
                    updateAvatar();
                    StaticData.user = userResponse.getUser();
                    AppUtils.saveUser(EditProfileActivity.this, userResponse.getUser());
                    CustomDialogClass dialogClass = new CustomDialogClass(EditProfileActivity.this, new CustomDialogClass.AbstractCustomDialogListener() {
                        @Override
                        public void onConfirm(CustomDialogClass.DialogResponse response) {
                            response.getDialog().dismiss();
                        }

                        @Override
                        public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                        }
                    }, true);

                    dialogClass.setTitle("Success");
                    dialogClass.setMessage("Your user was successfully updated");
                    dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialogClass.show();
                }else {
                    CustomDialogClass dialogClass = new CustomDialogClass(EditProfileActivity.this, new CustomDialogClass.AbstractCustomDialogListener() {
                        @Override
                        public void onConfirm(CustomDialogClass.DialogResponse response) {
                            response.getDialog().dismiss();
                            viewLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                        }
                    }, true);

                    dialogClass.setTitle("Oops");
                    dialogClass.setMessage(userResponse.getMessage());
                    dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialogClass.show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {

            }
        });
    }

    private void updateAvatar() {
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), new File(imgPath));
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", imgPath, requestFile);

        ApiManager.getService(true).updateAvatar(body).enqueue(new Callback<Object>() {
            @Override
            public void onResponse(Call<Object> call, Response<Object> response) {
                try {
                    String avatar = ((LinkedTreeMap) response.body()).get("avatar").toString();

                    User user = StaticData.user;
                    user.setAvatar(avatar);
                    AppUtils.saveUser(EditProfileActivity.this, user);
                    viewLoading.setVisibility(View.GONE);
                    if(StaticData.config.getIsAppActive()){
                        Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Intent intent = new Intent(getApplicationContext(), CountdownActivity.class);
                        startActivity(intent);
                        finish();
                    }

                } catch (Exception ex) {
                    Log.d("", ex.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Call<Object> call, Throwable t) {
                Log.d("", t.getMessage());
                viewLoading.setVisibility(View.GONE);
            }
        });
    }
}
