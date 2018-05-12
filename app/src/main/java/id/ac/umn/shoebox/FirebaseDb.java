package id.ac.umn.shoebox;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by miqdude on 08/05/18.
 */

public class FirebaseDb{
    private static DatabaseReference mydb;

    public void sendOrder(Order order){

        mydb = FirebaseDatabase.getInstance().getReference("orders");

        //Untuk mengirim data ke orders
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference mywow = db.getReference("orders");

        //tukar kuncinya dengan kunci dari firebase
        order.setOrderId(mywow.push().getKey());

        //kirim data
        mywow.child(order.getOrderId()).setValue(order);

        //tambahkan order ke data usernya
        mywow = db.getReference("users");
        mywow.child(order.getUserEmail()).child("orders").push().setValue(order.getOrderId());
    }
}
