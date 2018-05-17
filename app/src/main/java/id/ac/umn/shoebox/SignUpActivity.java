package id.ac.umn.shoebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;
import java.util.Map;

import id.ac.umn.shoebox.SharedPrefManager;

public class SignUpActivity extends AppCompatActivity {

    private EditText mFullNameEditText, mAddressEditText,mPhoneNumberEditText;
    private String mUsername,mEmail,mAddress,mPhoneNumber;

    DatabaseReference mDatabase;

    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mFullNameEditText = (EditText) findViewById(R.id.nama);
        mAddressEditText = findViewById(R.id.address);
        mPhoneNumberEditText = findViewById(R.id.pNumber);
        sharedPrefManager = new SharedPrefManager(SignUpActivity.this);
        mUsername = sharedPrefManager.getName();
        mEmail = sharedPrefManager.getUserEmail();
        mFullNameEditText.setText(mUsername);
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        final String encodeEmail = Utils.encodeEmail(mEmail.toLowerCase());



        Button profileButton = findViewById(R.id.edit_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAddressEditText.getText().toString().equals("") || mPhoneNumberEditText.getText().toString().equals("")){
                    Toast.makeText(SignUpActivity.this,"Field Alamat & Nomor Telefon harus di isi !",Toast.LENGTH_SHORT).show();
                }
                else {
                    mAddress = mAddressEditText.getText().toString();
                    mPhoneNumber = mPhoneNumberEditText.getText().toString();
                    mDatabase.child(encodeEmail).child("address").setValue(mAddress);
                    mDatabase.child(encodeEmail).child("pNumber").setValue(mPhoneNumber);
                    startActivity(new Intent(SignUpActivity.this, UtamaActivity.class));
                    Toast.makeText(getBaseContext(),"Updated", Toast.LENGTH_SHORT).show();
                }

            }
        });


        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, UtamaActivity.class));
                Toast.makeText(getBaseContext(),"Not Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();

    }
}

