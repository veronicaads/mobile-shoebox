package id.ac.umn.shoebox;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailOrderActivity extends AppCompatActivity {
    Button ganti_status;

    TextView stat, order, lemari, nama, ser, sub_ser, merek, tgl_order, tgl_deadline, gembok;
    DatabaseReference databaseReference;
    ImageView Psepatu, Pbukti;
    ProgressDialog progressDialog, waiting;

    String order_id, cabs;
    String userEmail;
    String noLaci;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        ganti_status = (Button) findViewById(R.id.status_btn);
        stat = findViewById(R.id.status);
        order = findViewById(R.id.no_order);
        lemari = findViewById(R.id.nolemari);
        nama = findViewById(R.id.user_name);
        ser = findViewById(R.id.service);
        sub_ser = findViewById(R.id.subservice);
        merek = findViewById(R.id.merk);
        Psepatu = findViewById(R.id.sepatu);
        Pbukti = findViewById(R.id.bukti);
        tgl_order = findViewById(R.id.tgl_masuk);
        tgl_deadline=findViewById(R.id.deadline);
        gembok = findViewById(R.id.kunci);

        waiting = new ProgressDialog(this);

        Intent a = getIntent();
        order_id = a.getStringExtra("OrderID");
        cabs = a.getStringExtra("CABANG");
        noLaci = a.getStringExtra("LACI");

        databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order.setText(order_id.toUpperCase());
                /** Ambil gambar sepatu dari storage*/
                try{
                    if(dataSnapshot.child(cabs+"/orders").child(order_id).child("image").getValue().toString().equals("")){
                       Psepatu.setImageResource(R.drawable.shoes);
                        Toast.makeText(DetailOrderActivity.this, "Sepatu Image Load Failed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        waiting.setMessage("Load Data..");
                        waiting.show();
                        waiting.setCancelable(false);
                        String gambar = dataSnapshot.child(cabs+"/orders").child(order_id).child("image").getValue().toString();
                        retrieveGambar(gambar);
                        waiting.dismiss();
                    }
                }catch (Exception e){e.printStackTrace();}

                /** Ambil gambar bukti transfer dari storage*/
                try{
                    if(dataSnapshot.child(cabs+"/orders").child(order_id).child("buktiPembayaran").getValue().toString().equals("")){
                        Pbukti.setImageResource(R.drawable.shoes);
                        Toast.makeText(DetailOrderActivity.this, "Bukti Image Load Failed", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        waiting.setMessage("Load Data..");
                        waiting.show();
                        waiting.setCancelable(false);
                        String bukti = dataSnapshot.child(cabs+"/orders").child(order_id).child("buktiPembayaran").getValue().toString();
                        //Toast.makeText(DetailOrderActivity.this, gambar, Toast.LENGTH_SHORT).show();
                        retrieveBukti(bukti);
                        waiting.dismiss();
                    }
                }catch (Exception e){e.printStackTrace();}

                /** Query Nomor Laci dari database*/
                try{
                    if(dataSnapshot.child(cabs+"/orders").child(order_id).child("noLaci").getValue().toString().equals(""))
                    {
                        lemari.setText("Nomor Laci belum ditemukan");
                    }
                    else lemari.setText(dataSnapshot.child(cabs+"/orders").child(order_id).child("noLaci").getValue().toString());

                    /** Query data dari database dan set text ke text view */
                    stat.setText(dataSnapshot.child(cabs+"/orders").child(order_id).child("status_service").getValue().toString());
                    String email = dataSnapshot.child(cabs+"/orders").child(order_id).child("userEmail").getValue().toString();
                    String namaLengkap = dataSnapshot.child("users").child(email.replace(".", ",")).child("fullName").getValue().toString();
                    nama.setText(namaLengkap);
                    userEmail = email;
                    ser.setText(dataSnapshot.child(cabs+"/orders").child(order_id).child("service").getValue().toString());
                    sub_ser.setText(dataSnapshot.child(cabs+"/orders").child(order_id).child("subService").getValue().toString());
                    merek.setText(dataSnapshot.child(cabs+"/orders").child(order_id).child("merkSepatu").getValue().toString());
                    tgl_order.setText(dataSnapshot.child(cabs+"/orders").child(order_id).child("tanggal_masuk").getValue().toString());
                    gembok.setText(dataSnapshot.child(cabs+"/orders").child(order_id).child("kunciGembok").getValue().toString());
                    /** Menentukan tanggal deadline servis*/
                    try{
                        SimpleDateFormat sdf = new SimpleDateFormat("d-M-yy", Locale.ROOT);
                        Date  firstDate = sdf.parse(dataSnapshot.child(cabs+"/orders").child(order_id).child("tanggal_masuk").getValue().toString());
                        Calendar now = Calendar.getInstance();

                        now.setTime(firstDate);
                        if(ser.getText().toString().equals("Reclean")||ser.getText().toString().equals("Repair")){
                            now.add(Calendar.DAY_OF_MONTH, 5);
                        }
                        else if(ser.getText().toString().equals("Repaint"))
                            now.add(Calendar.DAY_OF_MONTH, 14);
                        String selsai = sdf.format(now.getTime());
                        tgl_deadline.setText(selsai);

                    }catch (Exception e){e.printStackTrace();}

                }catch (Exception e){e.printStackTrace();}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ganti_status.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String tmp;
                /** Creating the instance of PopupMenu */
                PopupMenu popup = new PopupMenu(DetailOrderActivity.this, ganti_status);
                /** Inflating the Popup using xml file */
                popup.getMenuInflater().inflate(R.menu.status, popup.getMenu());

                /** registering popup with OnMenuItemClickListener */
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        final String tmp = item.getTitle().toString();
                        alert_dialog(order_id, cabs,tmp, userEmail);
                        return true;
                    }
                });

                popup.show();
            }
        });

        Button back_button = findViewById(R.id.back_btn);
        back_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailOrderActivity.this, ListOrderActivity.class));
            }
        });
    }

    /** Function Retrieve Gambar Sepatu untuk Firebase Storage*/
    private void retrieveGambar(String gambar){

        StorageReference storage = FirebaseStorage.getInstance().getReference();
        StorageReference tujuan = storage.child("image_shoes/").child(gambar);

        tujuan.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(DetailOrderActivity.this).load(uri.toString()).into(Psepatu);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Failed
            }
        });
    }

    /** Function Retrieve Gambar Bukti Transfer untuk Firebase Storage*/
    private void retrieveBukti(String buktiGambar){

        StorageReference storage = FirebaseStorage.getInstance().getReference();
        StorageReference tujuan = storage.child("bukti_pembayaran/").child(buktiGambar);

        tujuan.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(DetailOrderActivity.this).load(uri.toString()).into(Pbukti);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Failed
            }
        });
    }

    /** Function Update Status ke Database jika Change Status di tekan*/
    private void updateData(final String orderID, final String cabang, final String isi, final String userEmail){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Log.d("detailorder", "updateData: ");
        databaseReference.child(cabang).child("orders").child(orderID).child("status_service").setValue(isi);
    }

    public void alert_dialog(final String orderID, final String cabang, String title, final String userEmail){
        AlertDialog.Builder alert = new AlertDialog.Builder(DetailOrderActivity.this);
        alert.setTitle("Alert");
        alert.setCancelable(true);
        alert.setMessage("Are you sure to change the status ?");

        final String tmp = title;

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                /** Notifikasi jika bukti pembayaran tidak valid untuk user*/
                if(tmp.equals("Bukti Pembayaran Non Valid")){
                    progressDialog = new ProgressDialog(DetailOrderActivity.this);
                    progressDialog.setMessage("Please Wait");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    databaseReference = FirebaseDatabase.getInstance().getReference();
                    databaseReference.child(cabang).child("orders").child(orderID).child("status_service").setValue(tmp);
                    databaseReference.child(cabang).child("orders").child(orderID).child("buktiPembayaran").setValue("");
                    progressDialog.dismiss();
                    stat.setText(tmp);
                    Glide.with(DetailOrderActivity.this).load(R.drawable.shoes).into(Pbukti);
                }
                /** Notifikasi user sesuai status yang diganti*/
                else{
                    progressDialog = new ProgressDialog(DetailOrderActivity.this);
                    progressDialog.setMessage("Please Wait");
                    progressDialog.show();
                    progressDialog.setCancelable(false);
                    updateData(orderID, cabang, tmp, userEmail);
                    notifyUser(userEmail, "Order ID "+ orderID + " " + tmp);
                    progressDialog.dismiss();
                    stat.setText(tmp);
                    Log.d("alert", "onClick: wowowowo");
                }
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog ale = alert.create();
        ale.setCanceledOnTouchOutside(true);
        ale.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, ListOrderActivity.class));
    }

    /** Push Notification Function sesuai message tmp*/
    private void notifyUser(String userEmail, String tmp){
        DatabaseReference df = FirebaseDatabase.getInstance().getReference("inbox");
        df.child(Utils.encodeEmail(userEmail)).push().setValue(new PushMessage(tmp));
        Log.d("notifyuser", "notifyUser: "+userEmail+ " " +tmp);
    }

    /** Function untuk memfreekan laci jika sebuah order telah selesai*/
    private void setToDone(int laci, String cabang){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference(cabang);
        databaseReference.child("laci").child(String.valueOf(laci)).setValue("free");
    }
}
