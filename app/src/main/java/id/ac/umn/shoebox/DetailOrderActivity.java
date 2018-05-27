package id.ac.umn.shoebox;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.gms.common.SignInButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DetailOrderActivity extends AppCompatActivity {
    Button ganti_status;

    TextView stat, order, lemari, nama, ser, sub_ser, merek, tgl_order, tgl_deadline, gembok;
    DatabaseReference databaseReference;
    ImageView Psepatu;
    ProgressDialog progressDialog;

    String order_id;

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
        tgl_order = findViewById(R.id.tgl_masuk);
        tgl_deadline=findViewById(R.id.deadline);
        gembok = findViewById(R.id.kunci);



        Intent a = getIntent();
        order_id = a.getStringExtra("OrderID");
//        Toast.makeText(DetailOrderActivity.this, order_id, Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order.setText(order_id.toUpperCase());
//                Psepatu.setImageURI(dataSnapshot.child("orders").child(order_id).child("image").getValue());
                try{
                    if(dataSnapshot.child("orders").child(order_id).child("noLaci").getValue().toString().equals(""))
                    {
                        lemari.setText("Nomor Laci belum ditemukan");
                    }
                    else lemari.setText(dataSnapshot.child("orders").child(order_id).child("noLaci").getValue().toString());
                    stat.setText(dataSnapshot.child("orders").child(order_id).child("status_service").getValue().toString());
                    String email = dataSnapshot.child("orders").child(order_id).child("userEmail").getValue().toString();
                    String namaLengkap = dataSnapshot.child("users").child(email.replace(".", ",")).child("fullName").getValue().toString();
                    nama.setText(namaLengkap);
                    ser.setText(dataSnapshot.child("orders").child(order_id).child("service").getValue().toString());
                    sub_ser.setText(dataSnapshot.child("orders").child(order_id).child("subService").getValue().toString());
                    merek.setText(dataSnapshot.child("orders").child(order_id).child("merkSepatu").getValue().toString());
                    tgl_order.setText(dataSnapshot.child("orders").child(order_id).child("tanggal_masuk").getValue().toString());

                    try{
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy", Locale.ROOT);
                        Date  firstDate = sdf.parse(dataSnapshot.child("orders").child(order_id).child("tanggal_masuk").getValue().toString());
                        Calendar now = Calendar.getInstance();
                        now.setTime(firstDate);
                        now.add(Calendar.DAY_OF_MONTH, 3);
                        String selsai = sdf.format(now.getTime());

                        tgl_deadline.setText(selsai);

                    }catch (Exception e){e.printStackTrace();}

//                    gembok.setText();
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
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(DetailOrderActivity.this, ganti_status);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.status, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        final String tmp = item.getTitle().toString();
                        alert_dialog(order_id, tmp);
//                        Toast.makeText(DetailOrderActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method

        Button back_button = findViewById(R.id.back_btn);
        back_button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailOrderActivity.this, ListOrderActivity.class));
            }
        });
    }
    private void updateData(String orderID, final String isi){
        databaseReference = FirebaseDatabase.getInstance().getReference();

        databaseReference.child("orders").child(orderID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataSnapshot.getRef().child("status_service").setValue(isi);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("User", databaseError.getMessage());
            }
        });

    }

    public void alert_dialog(final String orderID, String title){
        AlertDialog.Builder alert = new AlertDialog.Builder(DetailOrderActivity.this);
        alert.setTitle("Alert");
        alert.setCancelable(true);
        alert.setMessage("Are you sure to change the status ?");

        final String tmp = title;

        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                progressDialog = new ProgressDialog(DetailOrderActivity.this);
                progressDialog.setMessage("Please Wait");
                progressDialog.show();
                progressDialog.setCancelable(false);
                updateData(orderID, tmp);
                progressDialog.dismiss();
                stat.setText(tmp);

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

}
