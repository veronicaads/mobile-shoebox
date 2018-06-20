package id.ac.umn.shoebox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    TextView stat_tv, order_tv, lemari_tv, nama_tv, ser_tv, sub_ser_tv, merk_tv, tgl_order, tgl_deadline, gembok;
    DatabaseReference databaseReference;
    ImageView Psepatu;
    ProgressDialog progressDialog,waiting;
    String order_id,cabang, tmp;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent a = getIntent();
        order_id = a.getStringExtra("ORDERID");
        tmp=order_id;
        cabang =  a.getStringExtra("CABANG");
        stat_tv = findViewById(R.id.status);
        order_tv = findViewById(R.id.no_order);
        lemari_tv = findViewById(R.id.nolemari);
        nama_tv = findViewById(R.id.user_name);
        ser_tv = findViewById(R.id.service);
        sub_ser_tv = findViewById(R.id.subservice);
        merk_tv = findViewById(R.id.merrk);
        Psepatu = findViewById(R.id.sepatu);

        gembok = findViewById(R.id.gembok);

        sharedPrefManager = new SharedPrefManager(getBaseContext());
        waiting = new ProgressDialog(this);

        if (cabang.equals("Pertamina")){
            databaseReference = FirebaseDatabase.getInstance().getReference("pertamina/orders");
        }
        else if (cabang.equals("Mercubuana")){
            databaseReference = FirebaseDatabase.getInstance().getReference("mercubuana/orders");
        }
        else if (cabang.equals("Atma Jaya")){
            databaseReference = FirebaseDatabase.getInstance().getReference("atmajaya/orders");
        }
        else if (cabang.equals("UMN")){
            databaseReference = FirebaseDatabase.getInstance().getReference("umn/orders");
        }

        /** Query detail order */
        Query query = databaseReference.orderByChild("orderId").equalTo(order_id);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Order order = dataSnapshot.getValue(Order.class);
                waiting.setMessage("Load Data..");
                waiting.show();
                waiting.setCancelable(false);
                order_tv.setText(order.getOrderId().toUpperCase());
                lemari_tv.setText(order.getNoLaci());
                stat_tv.setText(order.getStatus_service());
                nama_tv.setText(sharedPrefManager.getName());
                ser_tv.setText(order.getService());
                sub_ser_tv.setText(order.getSubService());
                merk_tv.setText(order.getMerkSepatu());

                //----------------------------------
                //gembok.setText(order_id.getGembok());
                //----------------------------------

                try{
                    if(order.getImage().toString().equals("")){
                        Psepatu.setImageResource(R.drawable.shoes);
                        Toast.makeText(DetailActivity.this, "Image Load Failed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        String gambar = order.getImage().toString();
                        Toast.makeText(DetailActivity.this, gambar, Toast.LENGTH_SHORT).show();
                        retrieveGambar(gambar);
                    }
                }catch (Exception e){e.printStackTrace();}
                waiting.dismiss();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Button pembayaran = findViewById(R.id.bayar_btn);
        pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(DetailActivity.this, BuktiUploadActivity.class);
                i.putExtra("ORDERID", tmp);
                startActivity(i);
            }
        });

        Button back= findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailActivity.this, UtamaActivity.class));
            }
        });
    }

    /** Function untuk mengambil download url image pada database & menampilkannya pada image view*/
    private void retrieveGambar(String gambar){
        StorageReference storage = FirebaseStorage.getInstance().getReference();
        StorageReference tujuan = storage.child("image_shoes/").child(gambar);

        tujuan.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(DetailActivity.this).load(uri.toString()).fitCenter().override(500,500).into(Psepatu);
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
}
