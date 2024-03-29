package id.ac.umn.shoebox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import id.ac.umn.shoebox.Constants;
import id.ac.umn.shoebox.SharedPrefManager;
import id.ac.umn.shoebox.Utils;
import id.ac.umn.shoebox.User;
import com.firebase.client.Firebase;
import com.google.firebase.database.ValueEventListener;

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
    public SharedPrefManager sharedPrefManager;
    private final Context mContext = this;

    private String name, email;
    private String photo;
    private Uri photoUri;
    private SignInButton mSignInButton;
    private String pNumber, pPriviledge;
    private String address;
    private String cabangAdmin;

    private FirebaseDatabase mDatabase;
    private String privilege_flag;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);
        progressDialog = new ProgressDialog(this);
        mSignInButton = findViewById(R.id.sign_in_button);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        mSignInButton.setOnClickListener(this); //waktu di click supaya user pilih akun
        progressDialog.setMessage("Tes");
        progressDialog.show();
        progressDialog.setCancelable(false);
        configureSignIn();
        progressDialog.dismiss();

        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();

        /** Proses Autentikasi */
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
    }

    /**
    //Method untuk membuat user baru pada firebase database setelah authentication berhasil
    //Menyimpan info user pada sharedpreferences
    //Utils : class java untuk menangani encodemail karena firebase tidak dapat menerima . karena itu . di replace */
    private void createUserInFireBaseHelper(){
        final String encodeEmail = Utils.encodeEmail(email.toLowerCase());
        final Firebase userlocation = new Firebase (Constants.FIREBASE_URL_USERS).child(encodeEmail);
        final String pNumber ="null";
        final String privilege = "user";
        final String address = "null";

        /** add listener ke lokasi diatas */
        userlocation.addListenerForSingleValueEvent(new com.firebase.client.ValueEventListener(){
            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null){
                    /** ambil timestamp dari server masukin ke hashmap */
                    HashMap<String,Object> timestampJoined = new HashMap<>();
                    timestampJoined.put(Constants.FIREBASE_PROPERTY_TIMESTAMP,ServerValue.TIMESTAMP);

                    /** Insert ke firebase database */
                    User newUser = new User(name,photo,encodeEmail,pNumber,address,
                            privilege,timestampJoined);
                    userlocation.setValue(newUser);
                    Toast.makeText(LoginActivity.this,"Account created", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                if (firebaseError.getCode() == FirebaseError.EMAIL_TAKEN){
                }
                else {
                    Toast.makeText(LoginActivity.this,firebaseError.getDetails(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void configureSignIn(){
    /** Konfigurasi sign in untuk mendapatkan email dan nama */
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(LoginActivity.this.getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        /** Build a GoogleApiClient with access to GoogleSignIn.API and the options above. */
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();
        mGoogleApiClient.connect();
    }

    /** user pilih account google untuk signin */
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /** Method yang handle saat button sign in di click dan masukin ke sharedpreference untuk selanjutnya */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**  Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...); */
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            Log.d(TAG, "handleSignInResult:"  + result.getStatus().toString() + result.isSuccess());
            if (result.isSuccess()) {
                /**  Google Sign In was successful, save Token and a state then authenticate with Firebase 8*/
                GoogleSignInAccount account = result.getSignInAccount();
                idToken = account.getIdToken();
                name = account.getDisplayName();
                email = account.getEmail();
                photoUri = account.getPhotoUrl();
                photo = photoUri.toString();

                /** Save Data to SharedPreference */
                sharedPrefManager = new SharedPrefManager(mContext);
                sharedPrefManager.saveIsLoggedIn(mContext, true);
                sharedPrefManager.saveEmail(mContext, email);
                sharedPrefManager.saveName(mContext, name);
                sharedPrefManager.savePhoto(mContext, photo);
                sharedPrefManager.saveToken(mContext, idToken);
                AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
                firebaseAuthWithGoogle(credential);
            }
            else {
                /** Google Sign In failed, update UI appropriately */
                Log.e(TAG, "Login Unsuccessful. ");
                Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }



    /** Keamanan layer 2 : firebase auntheticate */
    private void firebaseAuthWithGoogle(AuthCredential credential){
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential" + task.getException().getMessage());
                            task.getException().printStackTrace();
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            progressDialog.setMessage("Create Account...");
                            progressDialog.show();
                            progressDialog.setCancelable(false);
                            createUserInFireBaseHelper();
                            progressDialog.dismiss();
                            progressDialog.setMessage("Login...");
                            progressDialog.show();
                            final String encodeEmail = Utils.encodeEmail(email.toLowerCase());

                            mDatabase = FirebaseDatabase.getInstance();
                                final DatabaseReference reference = mDatabase.getReference("users").child(encodeEmail);
                                reference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                        if (dataSnapshot.exists()) {
                                            Log.d("Tes Loop","Data snapshot already exist");
                                            User getuserdata = dataSnapshot.getValue(User.class);
                                            privilege_flag = getuserdata.getPrivilege();
                                            pNumber = getuserdata.getpNumber();
                                            cabangAdmin = getuserdata.getCabangAdmin();
                                            sharedPrefManager.savepNumber(mContext, pNumber);
                                            if (privilege_flag.toString().equals("admin")) {
                                                sharedPrefManager.savepPrivilege(mContext, privilege_flag);
                                                sharedPrefManager.saveCabangAdmin(mContext, cabangAdmin);
                                                Intent intent = new Intent(LoginActivity.this, ListOrderActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            } else if (privilege_flag.toString().equals("user") && pNumber.toString().equals("null")) {
                                                sharedPrefManager.savepPrivilege(mContext, privilege_flag);
                                                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                sharedPrefManager.savepPrivilege(mContext, "user");
                                                Intent intent = new Intent(LoginActivity.this, UtamaActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
                                            }
                                            progressDialog.dismiss();
                                            Toast.makeText(LoginActivity.this, "Login successful",
                                                    Toast.LENGTH_SHORT).show();
                                        } else {
                                            Log.d("Tes Loop","Data snapshot still does not exist");
                                            progressDialog.dismiss();
                                            progressDialog.setMessage("Login...");
                                            progressDialog.show();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuthListener != null){
            FirebaseAuth.getInstance().signOut();
        }
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    public void onClick(View view) {
        Utils utils = new Utils(this);
        int id = view.getId();

        if (id == R.id.sign_in_button){
            if (utils.isNetworkAvailable()){
                signIn();
            }else {
                Toast.makeText(LoginActivity.this, "Oops! no internet connection!", Toast.LENGTH_SHORT).show();
            }
        }
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
