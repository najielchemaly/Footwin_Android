package com.apploads.footwin.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.apploads.footwin.helpers.BaseActivity;
import com.apploads.footwin.MainPageActivity;
import com.apploads.footwin.R;
import com.apploads.footwin.helpers.CustomDialogClass;
import com.apploads.footwin.helpers.StaticData;
import com.apploads.footwin.loading.CountdownActivity;
import com.apploads.footwin.model.UserResponse;
import com.apploads.footwin.profile.TermsAndConditionsActivity;
import com.apploads.footwin.services.ApiManager;
import com.apploads.footwin.signup.SignupStepOne;
import com.apploads.footwin.helpers.utils.AppUtils;
import com.apploads.footwin.helpers.utils.StringUtils;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.Login;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.ybq.android.spinkit.style.DoubleBounce;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class LoginActivity extends BaseActivity {
    TextView txtRecover, txtCreateAccount, txtTerms, txtPrivacy;
    EditText txtEmail, txtPassword;
    ImageView imgFB, imgGoogle;
    Button btnLogin;
    ProgressBar progressBar;
    LoginButton btnFacebookLogin;

    private static final String EMAIL = "email";

    CallbackManager callbackManager;

    @Override
    public int getContentViewId() {
        return R.layout.login_activity;
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
        btnFacebookLogin = findViewById(R.id.btnFacebookLogin);
        txtCreateAccount = _findViewById(R.id.txtCreateAccount);
        txtPassword = _findViewById(R.id.txtPassword);
        txtRecover = _findViewById(R.id.txtRecover);
        progressBar = _findViewById(R.id.spin_kit);
        imgGoogle = _findViewById(R.id.imgGoogle);
        btnLogin = _findViewById(R.id.btnLogin);
        txtPrivacy = _findViewById(R.id.txtPrivacy);
        txtTerms = _findViewById(R.id.txtTerms);
        txtEmail = _findViewById(R.id.txtEmail);
        imgFB = _findViewById(R.id.imgFB);

        DoubleBounce doubleBounce = new DoubleBounce();
        progressBar.setIndeterminateDrawable(doubleBounce);
        progressBar.setVisibility(View.GONE);
    }

    /**
     * initialize listeners
     */
    private void initListeners() {
        imgFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonFacebookPressed();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateFields()) {
                    callLoginService();
                }
            }
        });

        txtRecover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RetrievePasswordActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        txtCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignupStepOne.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        btnFacebookLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onButtonFacebookPressed();
            }
        });

        txtTerms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TermsAndConditionsActivity.class);
                intent.putExtra("type", "terms");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });

        txtPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), TermsAndConditionsActivity.class);
                intent.putExtra("type", "privacy");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);
            }
        });
    }

    private void onButtonFacebookPressed() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(
                "public_profile", "email", "user_birthday", "user_friends"));

        LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.NATIVE_ONLY);
        LoginManager.getInstance().setLoginBehavior(LoginBehavior.WEB_ONLY);

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        progressBar.setVisibility(View.VISIBLE);
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        try {
                                            String gender = object.has("gender") ? object.getString("gender") : "";
                                            String id = object.has("id") ? object.getString("id") : "";
                                            String name = object.has("name") ? object.getString("name") : "";
                                            String email = object.has("email") ? object.getString("email") : "";
                                            String token = AccessToken.getCurrentAccessToken() != null ?
                                                    AccessToken.getCurrentAccessToken().getToken() : "";

                                            callFacebookLoginService(id, token, name.replace(" ", "").toLowerCase(), name, email, gender);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id, name, email, gender, birthday");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        progressBar.setVisibility(View.GONE);
//                        Toast.makeText(LoginActivity.this, "An error has occured", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                        Toast.makeText(LoginActivity.this, "An error has occured", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * this function is for validating the email and password fields before proceeding
     */
    private boolean validateFields() {
        if (!AppUtils.isEmailValid(txtEmail.getText().toString())) {
            CustomDialogClass dialogClass = new CustomDialogClass(LoginActivity.this, new CustomDialogClass.AbstractCustomDialogListener() {
                @Override
                public void onConfirm(CustomDialogClass.DialogResponse response) {
                    response.getDialog().dismiss();
                    txtEmail.setText("");
                    txtPassword.setText("");
                }

                @Override
                public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                }
            }, true);

            dialogClass.setTitle("FOOTWIN");
            dialogClass.setMessage("You must enter a valid email!");
            dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialogClass.show();
            return false;
        }
        if (!StringUtils.isValid(txtPassword.getText())) {
            CustomDialogClass dialogClass = new CustomDialogClass(LoginActivity.this, new CustomDialogClass.AbstractCustomDialogListener() {
                @Override
                public void onConfirm(CustomDialogClass.DialogResponse response) {
                    response.getDialog().dismiss();
                    txtEmail.setText("");
                    txtPassword.setText("");
                }

                @Override
                public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                }
            }, true);

            dialogClass.setTitle("FOOTWIN");
            dialogClass.setMessage("Password cannot be empty!");
            dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialogClass.show();
            return false;
        }

        return true;
    }

    private void callLoginService() {
        progressBar.setVisibility(View.VISIBLE);
        ApiManager.getService(true).login(txtEmail.getText().toString(), txtPassword.getText().toString()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == 1){
                        UserResponse userResponse = response.body();
                        StaticData.user = userResponse.getUser();
                        AppUtils.saveUser(LoginActivity.this, userResponse.getUser());
                        if(StaticData.config.getIsAppActive()){
                            Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(getApplicationContext(), CountdownActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        progressBar.setVisibility(View.GONE);
                    }else if(response.body().getStatus() == 0){
                        progressBar.setVisibility(View.GONE);
                        CustomDialogClass dialogClass = new CustomDialogClass(LoginActivity.this, new CustomDialogClass.AbstractCustomDialogListener() {
                            @Override
                            public void onConfirm(CustomDialogClass.DialogResponse response) {
                                response.getDialog().dismiss();
                                txtEmail.setText("");
                                txtPassword.setText("");
                            }

                            @Override
                            public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                            }
                        }, true);

                        dialogClass.setTitle("Oops");
                        dialogClass.setMessage("Incorrect username or password");
                        dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialogClass.show();
                    }
                }else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void callFacebookLoginService(String facebook_id, String facebook_token, String username, String fullname, String email, String gender) {
        ApiManager.getService(true).facebookLogin(facebook_id, facebook_token, username, fullname, email, gender).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getStatus() == 1) {
                        UserResponse userResponse = response.body();
                        StaticData.user = userResponse.getUser();
                        AppUtils.saveUser(LoginActivity.this, userResponse.getUser());
                        if(StaticData.config.getIsAppActive()){
                            Intent intent = new Intent(getApplicationContext(), MainPageActivity.class);
                            startActivity(intent);
                            finish();
                        }else {
                            Intent intent = new Intent(getApplicationContext(), CountdownActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        progressBar.setVisibility(View.GONE);
                    } else if (response.body().getStatus() == 0) {
                        progressBar.setVisibility(View.GONE);
                        CustomDialogClass dialogClass = new CustomDialogClass(LoginActivity.this, new CustomDialogClass.AbstractCustomDialogListener() {
                            @Override
                            public void onConfirm(CustomDialogClass.DialogResponse response) {
                                response.getDialog().dismiss();
                                txtEmail.setText("");
                                txtPassword.setText("");
                            }

                            @Override
                            public void onCancel(CustomDialogClass.DialogResponse dialogResponse) {
                            }
                        }, true);

                        dialogClass.setTitle("Oops");
                        dialogClass.setMessage("An erro has occured, please try again!");
                        dialogClass.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                        dialogClass.show();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(LoginActivity.this, "Check your internet connection and try again later", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}
