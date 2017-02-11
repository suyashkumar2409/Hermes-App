package com.escapeestudios.hermes;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

//  ******************  startActivityForResult Constants **************************
    private static final int RC_SIGN_IN = 123;

//    ****************  Firebase Objects *****************************************
    private FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private FirebaseUser currentUser;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mUserDatabase;

//    ****************  Activity Objects  ****************************************
    private Context ctx;
    private String mUserName;
    ActionBar actionBar;

//    ****************  View Objects **********************************************
    private ViewPager mViewPager;
    private AppPagerAdapter mAppPagerAdapter;

//    ****************  Activity Constants ***************************************
    private static final String ANONYMOUS = "ANONYMOUS";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        ************** Write Main Code here *********************
        ctx = this.getApplicationContext();

//        ************** Pager Code *******************************
        actionBar = getSupportActionBar();
        mAppPagerAdapter =
                new AppPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppPagerAdapter);
        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        // When swiping between pages, select the
                        // corresponding tab.
                        actionBar.setSelectedNavigationItem(position);
                    }
                });

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // show the given tab
                mViewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        addTab("Friends", tabListener);
        addTab("Chats", tabListener);
        addTab("Activity", tabListener);


//        ************** All Firebase initialisations ***************
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();

        mUserDatabase = mFirebaseDatabase.getReference("users");

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.sign_out:
                AuthUI.getInstance().signOut(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void onSignedInInitialise(String userName)
    {
        mUserName = userName;
        Toast.makeText(ctx, userName, Toast.LENGTH_SHORT).show();
        checkUserInDatabase();
    }

    private void onSignedOutCleanUp()
    {
        mUserName = ANONYMOUS;
    }

    private void checkUserInDatabase()
    {
        final User user = new User(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getEmail());
        mUserDatabase.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    mUserDatabase.child(user.getUid()).setValue(user);
                    Toast.makeText(ctx, "New User Added",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(ctx, "User Exists",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addTab(String str, ActionBar.TabListener tabListener)
    {
        actionBar.addTab(
                actionBar.newTab()
                        .setText(str)
                        .setTabListener(tabListener));

    }
}

