package id.ac.umn.shoebox;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.support.design.widget.NavigationView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;


public class UtamaActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, HomeFragment.OnFragmentInteractionListener, OrderFragment.OnFragmentInteractionListener, HelpFragment.OnFragmentInteractionListener{

    Context mContext = this;

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.navigation_logout){
            signOut();
            Toast.makeText(getApplicationContext(),"Logout Berhasil",Toast.LENGTH_SHORT).show();
            finish();
        }
        else if(id==R.id.navigation_edit_profile){
            Intent intent = new Intent(UtamaActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_utama);

        VerifyPermission();
        }

    /*@Override
    protected void onStart() {
        super.onStart();
        AlertDialog.Builder phoneNumberDialog = new AlertDialog.Builder(UtamaActivity.this);
    }*/
    public void Menu(){
        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.navigate);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.layout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_reorder_black_24dp));
        //tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_notifications_black_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_help_black_24dp));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final id.ac.umn.shoebox.PagerAdapter pagerAdapter = new id.ac.umn.shoebox.PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        configureSignIn();

    }
    public void configureSignIn(){
// Configure sign-in to request the user's basic profile like name and email
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();
        mGoogleApiClient.connect();
    }
    private static final int REQUEST_CODE = 1;

    public boolean VerifyPermission(){
        Log.d("MyTag", "Verify Permission");
       // int camera = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();


        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_CODE);
            return false;
        }
        return true;
    }

    public static List<String> needPermissions(Context context, String... permissions) {
        List<String> neededPermission = new ArrayList<>();
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    neededPermission.add(permission);
                }
            }
        }
        return neededPermission;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Menu();
                } else {
                    Menu();
                    Toast.makeText(getApplicationContext(), "TES", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        //VerifyPermission();
    }

    private void signOut(){
        new SharedPrefManager(mContext).clear();
        mAuth.signOut();

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Intent intent = new Intent(UtamaActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }


    }

