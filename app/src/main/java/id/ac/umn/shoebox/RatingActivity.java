package id.ac.umn.shoebox;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import android.widget.Toast;


public class RatingActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView txtRatingValue;
    private Button btnSubmit;
    ImageView pSepatu;
    String order_id, cabang;
    ProgressDialog waiting;
    DatabaseReference databaseReference;
    TextView order;
    FirebaseDb firebaseDb = new FirebaseDb();
    private String noLaci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        addListenerOnRatingBar();
        addListenerOnButton();
        pSepatu = findViewById(R.id.sepatu);
        Intent a = getIntent();
        order = (TextView) findViewById(R.id.idorder);
        order_id = a.getStringExtra("OrderID");
        cabang = a.getStringExtra("CABANG");
        waiting = new ProgressDialog(this);

        databaseReference  = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order.setText(order_id.toUpperCase());
                try{
                    if(dataSnapshot.child(cabang+"/orders").child(order_id).child("image").getValue().toString().equals("")){
                        pSepatu.setImageResource(R.drawable.shoes);
                        Toast.makeText(RatingActivity.this, "Image Load Failed", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        waiting.setMessage("Load Data..");
                        waiting.show();
                        waiting.setCancelable(false);
                        String gambar = dataSnapshot.child(cabang+"/orders").child(order_id).child("image").getValue().toString();
                        //Toast.makeText(DetailOrderActivity.this, gambar, Toast.LENGTH_SHORT).show();
                        noLaci = dataSnapshot.child(cabang+"/orders").child(order_id).child("noLaci").getValue().toString();                        retrieveGambar(gambar);
                        waiting.dismiss();
                    }
                }catch (Exception e){e.printStackTrace();}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void retrieveGambar(String gambar){

        StorageReference storage = FirebaseStorage.getInstance().getReference();
        StorageReference tujuan = storage.child("image_shoes/").child(gambar);

        tujuan.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(RatingActivity.this).load(uri.toString()).into(pSepatu);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Failed
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);

        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {

                txtRatingValue.setText(String.valueOf(rating));

            }
        });
    }

    public void addListenerOnButton() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

               /* Toast.makeText(RatingActivity.this,
                        String.valueOf(ratingBar.getRating()),
                        Toast.LENGTH_SHORT).show();*/
               float rating = ratingBar.getRating();
               int ratingSend = Math.round(rating);

               firebaseDb.kirimRating(cabang, order_id, ratingSend);
               firebaseDb.setFree(cabang,noLaci);
               startActivity(new Intent(RatingActivity.this, UtamaActivity.class));
            }

        });

    }

}
