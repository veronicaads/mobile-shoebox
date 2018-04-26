package id.ac.umn.shoebox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.FirebaseError;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.ServerValue;
import id.ac.umn.shoebox.Constants;
//import com.tutorial.authentication.utils.SharedPrefManager;
import id.ac.umn.shoebox.Utils;
//import com.tutorial.authentication.model.User;
import com.firebase.client.Firebase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener{

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final int RC_SIGN_IN = 9001;
    private static final String TAG = "LoginActivity";
    private String idToken;
    //public SharedPrefManager sharedPrefManager;
    private final Context mContext = this;

    private String name, email;
    private String photo;
    private Uri photoUri;
    private SignInButton mSignInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mSignInButton.setOnClickListener(this); //waktu di click supaya user pilih akun
        //configureSignIn();

        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();

        //proses authentication
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //mendapatkan id signin user
                FirebaseUser user = firebaseAuth.getCurrentUser();

                //jika user sign in , maka panggil method untuk save detail user ke Firebase
                if (user != null){
                    createUserInFireBaseHelper();
                    Log.d(TAG,"onAuthStateChange:signed_in" + user.getUid());
                }
                else {
                    Log.d(TAG,"onAuthStateChange:signed_out");
                }
            }
        };

       // com.google.android.gms.common.SignInButton signInBut = findViewById(R.id.sign_in_button);
       // signInBut.setOnClickListener(new View.OnClickListener() {
       //     @Override
       //     public void onClick(View view) {
      //          startActivity(new Intent(LoginActivity.this, UtamaActivity.class));
      //      }
      //  });

    }

    //Method untuk membuat user baru pada firebase database setelah authentication berhasil
    //Menyimpan info user pada sharedpreferences
    //Utils : class java untuk menangani encodemail karena firebase tidak dapat menerima . karena itu . di replace
    private void createUserInFireBaseHelper(){
        final String encodeEmail = Utils.encodeEmail(email.toLowerCase());
        final Firebase userlocation = new Firebase (Constants.FIREBASE_URL_USERS).child(encodeEmail);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
