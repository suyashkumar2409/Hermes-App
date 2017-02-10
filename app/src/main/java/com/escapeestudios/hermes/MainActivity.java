package com.escapeestudios.hermes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

//  ******************  startActivityForResult Constants **************************
    private static final int RC_SIGN_IN = 123;

//    ****************  Firebase Objects *****************************************
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

//    ****************  Activity Objects  ****************************************
    private Context ctx;
    private String mUserName;

//    ****************  Activity Constants ***************************************
    private static final String ANONYMOUS = "ANONYMOUS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ************** Write Main Code here *********************

        ctx = this.getApplicationContext();

//        ************** All Firebase initialisations ***************
        mFirebaseAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();

                if(currentUser != null)
                {
                    onSignedInInitialise(currentUser.getDisplayName());
                }
                else
                {
                    onSignedOutCleanUp();
                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setIsSmartLockEnabled(false)
                                    .setProviders(AuthUI.GOOGLE_PROVIDER,
                                                AuthUI.FACEBOOK_PROVIDER)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };




//        ************** Main Code Ends****************************

//        Required for facebook sdk init
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onResume(){
        super.onResume();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onPause(){
        super.onPause();
        mFirebaseAuth.removeAuthStateListener(mAuthStateListener);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN)
        {
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(ctx, "Signed in",Toast.LENGTH_SHORT).show();
            }
            else if(resultCode == RESULT_CANCELED)
            {
                finish();
            }
        }
    }
    private void onSignedInInitialise(String userName)
    {
        mUserName = userName;

    }

    private void onSignedOutCleanUp()
    {
        mUserName = ANONYMOUS;
    }
}

