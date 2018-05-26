package id.ac.umn.shoebox;

import android.app.AlertDialog;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailOrderActivity extends AppCompatActivity {
    Button ganti_status;

    TextView stat, order, lemari, nama, ser, sub_ser, merek;
    DatabaseReference databaseReference;
    ImageView Psepatu;



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



        Intent a = getIntent();
        final String order_id = a.getStringExtra("OrderID");
        Toast.makeText(DetailOrderActivity.this, order_id, Toast.LENGTH_SHORT).show();

        databaseReference = FirebaseDatabase.getInstance().getReference();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order.setText(order_id);
//                Psepatu.setImageURI(dataSnapshot.child("orders").child(order_id).child("image").getValue());
                lemari.setText(dataSnapshot.child("orders").child(order_id).child("noLaci").getValue().toString());
                stat.setText(dataSnapshot.child("orders").child(order_id).child("status_service").getValue().toString());
                String email = dataSnapshot.child("orders").child(order_id).child("userEmail").getValue().toString();
                String namaLengkap = dataSnapshot.child("users").child(email.replace(".", ",")).child("fullName").getValue().toString();
                nama.setText(namaLengkap);
                ser.setText(dataSnapshot.child("orders").child(order_id).child("service").getValue().toString());
                sub_ser.setText(dataSnapshot.child("orders").child(order_id).child("subService").getValue().toString());
                merek.setText(dataSnapshot.child("orders").child(order_id).child("merkSepatu").getValue().toString());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        ganti_status.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(DetailOrderActivity.this, ganti_status);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.status, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(DetailOrderActivity.this);
                        alert.setTitle("Alert");
                        alert.setMessage("Are you sure to change the status ?");

                        final EditText input = new EditText(DetailOrderActivity.this);
                        alert.setView(input);

                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                String value = input.getText().toString();
                                Log.d("", "Pin Value : "+value);
                                return;
                            }
                        });

                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                return;
                            }
                        });


                         if(item.getTitle().equals("Lunas Telah diambil dari Lemari")){
                            stat.setText(item.getTitle());
                        }
                        else if(item.getTitle().equals("Sepatu berada di showroom")){
                            stat.setText(item.getTitle());
                        }
                        else if(item.getTitle().equals("Sedang di Cuci")){
                            stat.setText(item.getTitle());
                        }
                        else if(item.getTitle().equals("Selesai")){
                            stat.setText(item.getTitle());
                        }
                        Toast.makeText(DetailOrderActivity.this,"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }


}
