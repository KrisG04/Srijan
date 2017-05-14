package com.hash.android.srijan;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;

import static com.hash.android.srijan.DashboardActivity.PREFS_NAME;
import static com.hash.android.srijan.fragment.SubscriptionFragment.arrayList;


public class LogInActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener {
    private static final int RC_SIGN_IN = 0;
    private static final String TAG = "LogInActivity";
    public static FirebaseUser user;
    public static GoogleApiClient mGoogleApiClient;
    public User authUser;
    EditText name, college, phone;
    String nameValue, collegeValue, phoneValue;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_activity);

        //Create a progress dialog to show the signing in pd
        pd = new ProgressDialog(LogInActivity.this);
        pd.setMessage("Signing you in...");


        name = (EditText) findViewById(R.id.editText2);
        college = (EditText) findViewById(R.id.editText4);
        phone = (EditText) findViewById(R.id.editTextPhone);


        mAuth = FirebaseAuth.getInstance(); // Get the present user instance.
//


        arrayList = new ArrayList<>();

        //Build the Google Sign in Options object and ask for the email address and the profile information.
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        //Create a google sing in client using the previously created Google Sign In Options object
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        mGoogleApiClient.connect(); //Connect to the server

        ImageView googleSignInButton = (ImageView) findViewById(R.id.googleSignInImageView); //Find the button
        googleSignInButton.setOnClickListener(this);
        //Set a onClick Listener
        mAuthListener = new FirebaseAuth.AuthStateListener() { //Checks for any change in auth
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser(); //Fetch the current user
                //If non empty user object is returned
                if (user != null) {

                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    String nameStored = settings.getString("nameUser" + user.getUid(), null);
                    String collegeStored = settings.getString("collegeUser" + user.getUid(), null);
                    String phoneStored = settings.getString("phoneUser" + user.getUid(), null);


                    if (nameStored == null) {
                        nameValue = name.getText().toString().trim();
                        collegeValue = college.getText().toString().trim();
                        phoneValue = phone.getText().toString().trim();
                        if (TextUtils.isEmpty(nameValue)) {
                            Toast.makeText(LogInActivity.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (TextUtils.isEmpty(collegeValue)) {
                            Toast.makeText(LogInActivity.this, "Please enter your college", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.putString("nameUser" + user.getUid(), nameValue);
                        editor.putString("collegeUser" + user.getUid(), collegeValue);
                        editor.putString("phoneUser" + user.getUid(), phoneValue);
                        editor.commit();

                    } else {
                        nameValue = nameStored;
                        collegeValue = collegeStored;
                        phoneValue = phoneStored;
                    }


//                    SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);


                    nameValue = settings.getString("nameUser" + user.getUid(), null);
                    collegeValue = settings.getString("collegeUser" + user.getUid(), null);
                    phoneValue = settings.getString("phoneUser" + user.getUid(), null);
                    if (nameValue != null) {
                        //Create a new User Object
                        authUser = new User();
                        authUser.setName(nameValue);
                        authUser.setEmail(user.getEmail());
                        authUser.setId(user.getUid());
                        authUser.setUniversity(collegeValue);
                        authUser.setPhoneNumber(phoneValue);
                        authUser.setPhotoURL(user.getPhotoUrl().toString());
                        try {
                            authUser.saveUser(); //Save the user details to the database
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // User is signed in
                        Log.d("onAuthStateChanged:", "signed_in:" + user.getUid()); //Log to the terminal that the object is saved sucessfully
                        Log.d("email", user.getEmail());
                        Log.d("name", user.getDisplayName());
                        finish(); //Finish the activity so that the user cannot return to it after opening.
                        Intent i = new Intent(LogInActivity.this, DashboardActivity.class);
                        i.putExtra("authUser", authUser);
                        startActivity(i);
                        LogInActivity.this.overridePendingTransition(0, 0); //Override the animation to get rid of the glitch effect
                    } else {
                        Toast.makeText(LogInActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                } else {


//                     User is signed out
                    Log.d("onAuthStateChanged:", "signed_out");
                }
                // ...
            }
        };

//        //Set up facebook authentication
//        callbackManager = CallbackManager.Factory.create();
//        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//
//
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d("callback", "Success");
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//                Toast.makeText(getApplicationContext(), "" + error.getMessage(), Toast.LENGTH_LONG).show();
//
//            }
//
//        });

//        ImageView facebookLogInImageView = (ImageView) findViewById(R.id.facebookLogInImageView);
//        facebookLogInImageView.setOnClickListener(this);

//
//        mCallbackManager = CallbackManager.Factory.create();
//        LoginButton loginButton = (LoginButton) findViewById(R.id.facebook_login_button);
//        loginButton.setReadPermissions("email", "public_profile");
//        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d(TAG, "facebook:onError", error);
//            }
//        });

//        FacebookSdk.sdkInitialize(getApplicationContext());

    }

    //
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "Connection failed"); //Simply logs if the connection is a failure
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.googleSignInImageView:
                signIn(); //This basically signs you in
                break;
//            case R.id.facebookLogInImageView:
//                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
//                break;
            default:

        }

    }


    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null)
            mAuth.removeAuthStateListener(mAuthListener);

        mGoogleApiClient.disconnect();
        if (pd.isShowing())
            pd.dismiss(); //To prevent view leaking


    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
        mAuth.addAuthStateListener(mAuthListener);

    }

    /**
     * Dispatch incoming result to the correct fragment.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);


            if (result.isSuccess()) { //if sign in is sucessful
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account); //Authenticate with firebase and complete firebase auth.
            } else
                Log.d("Sign in:", "Failed");
        }

    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        final AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        pd.show();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Task", "Succesful");
                        } else
                            Log.d("Task", "Failed");
                        pd.hide();
                        pd.dismiss();
                    }
                });
    }

    private void signIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient); //Creates the dialog for the user to sign in
        startActivityForResult(signInIntent, RC_SIGN_IN); //Starts the sign in activity and waits for result

    }


    //Code for handling Facebook sign in
//    private void handleFacebookAccessToken(AccessToken token) {
//        Log.d(TAG, "handleFacebookAccessToken:" + token);
//
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        pd.show();
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
//
//                        // If sign in fails, display a message to the user. If sign in succeeds
//                        // the auth state listener will be notified and logic to handle the
//                        // signed in user can be handled in the listener.
//                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithCredential", task.getException());
//                            Toast.makeText(LogInActivity.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                        pd.hide();
//                        // ...
//                    }
//                });
//    }
}
