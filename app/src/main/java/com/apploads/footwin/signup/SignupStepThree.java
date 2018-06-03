package com.apploads.footwin.signup;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.MainPageActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.CustomDialogClass;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.helpers.Utility;
import com.apploads.footwin.helpers.utils.AppUtils;
import com.apploads.footwin.loading.CountdownActivity;
import com.apploads.footwin.login.LoginActivity;
import com.apploads.footwin.login.RetrievePasswordActivity;
import com.apploads.footwin.model.Article;
import com.apploads.footwin.model.User;
import com.apploads.footwin.model.UserResponse;
import com.apploads.footwin.services.ApiManager;
import com.budiyev.android.circularprogressbar.CircularProgressBar;
import com.github.ybq.android.spinkit.style.DoubleBounce;
import com.google.gson.internal.LinkedTreeMap;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupStepThree extends BaseActivity {
    RelativeLayout viewContinue;
    Button btnCancel;
    CircleImageView imgAddImage;
    TextView txtBack;
    ImageView imgCamera;
    Bitmap bitmap;
    User user = new User();
    String password;
    boolean isImageSet;
    ProgressBar progressBar;
    RelativeLayout viewLoading;

    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2, REQUEST_READ_EXTERNAL_STORAGE = 3;

    @Override
    public int getContentViewId() {
        return R.layout.signup_step_three;
    }

    @Override
    public void doOnCreate() {
        initView();
        initListeners();
    }

    /**
     * initialize view
     */
    private void initView() {

        Intent intent = getIntent();
        Bundle b = intent.getExtras();

        if (b != null) {
            user = (User) b.getSerializable("user");
            password = b.getString("password");
        }

        viewContinue = _findViewById(R.id.viewContinue);
        imgAddImage = _findViewById(R.id.imgAddImage);
        txtBack = _findViewById(R.id.txtBack);
        btnCancel = _findViewById(R.id.btnCancel);
        imgCamera = _findViewById(R.id.imgCamera);
        progressBar = _findViewById(R.id.spin_kit);
        viewLoading = _findViewById(R.id.viewLoading);
        imgCamera.setVisibility(View.GONE);
        viewLoading.setVisibility(View.GONE);

        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);

        Animation scaleDown = AnimationUtils.loadAnimation(SignupStepThree.this, R.anim.scale_up);
        imgAddImage.startAnimation(scaleDown);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 1);
            }
        }
    }

    /**
     * initialize listeners
     */
    private void initListeners() {

        viewContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isImageSet){
                    viewLoading.setVisibility(View.VISIBLE);
                    registerUser();
                }else {
                    CustomDialogClass dialogClass = new CustomDialogClass(SignupStepThree.this, new CustomDialogClass.AbstractCustomDialogListener() {
                        @Override
                        public void onConfirm(CustomDialogClass.DialogResponse response) {
                            response.getDialog().dismiss();
                        }

                        @Override
                        public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                        }
                    }, true);

                    dialogClass.setTitle("Oops");
                    dialogClass.setMessage("Make sure you enter a profile picture");
                    dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                    dialogClass.show();
                }
            }
        });

        txtBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_down);
            }
        });

        imgAddImage.setOnClickListener(new View.OnClickListener() {
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
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(SignupStepThree.this);
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
                    if (ActivityCompat.shouldShowRequestPermissionRationale(SignupStepThree.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        Toast.makeText(SignupStepThree.this, "Read External Storage Permission error", Toast.LENGTH_SHORT);
                    } else {
                        ActivityCompat.requestPermissions(SignupStepThree.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXTERNAL_STORAGE);
                    }
                }
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(SignupStepThree.this,
                        Manifest.permission.CAMERA)) {
                    Toast.makeText(SignupStepThree.this, "Camera Permission error", Toast.LENGTH_SHORT);
                } else {
                    ActivityCompat.requestPermissions(SignupStepThree.this,
                            new String[]{Manifest.permission.CAMERA}, PICK_IMAGE_CAMERA);
                }
            }
        } catch (Exception e) {
            Toast.makeText(SignupStepThree.this, "Camera Permission error", Toast.LENGTH_SHORT);
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
                imgAddImage.setImageBitmap(bitmap);
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
                    imgAddImage.setImageBitmap(bitmap);
                    isImageSet = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void registerUser(){
        viewLoading.setVisibility(View.VISIBLE);
        ApiManager.getService(true).registerUser(user.getFullname() ,user.getUsername()
                ,user.getEmail(),password, user.getPhoneCode()
                ,user.getPhone(),user.getGender(),
               user.getCountry(), user.getFavoriteTeam()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if(userResponse.getStatus() == 1){
                    StaticData.user = userResponse.getUser();
                    updateAvatar();
                }else {
                    CustomDialogClass dialogClass = new CustomDialogClass(SignupStepThree.this, new CustomDialogClass.AbstractCustomDialogListener() {
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
                Toast.makeText(SignupStepThree.this, "Something went wrong! please try again later", Toast.LENGTH_SHORT).show();
                viewLoading.setVisibility(View.GONE);
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
                    AppUtils.saveUser(SignupStepThree.this, user);
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
