package id.ac.umn.shoebox;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Map;

import static android.support.constraint.Constraints.TAG;

/**
 * Created by miqdude on 08/05/18.
 */

public class FirebaseDb{
    private static DatabaseReference mydb;
    private StorageReference storageReference;
    private String order_key;

    public void sendOrder(final Order order, final Context context){

        //ambil key terakir dari order_keys
        mydb = FirebaseDatabase.getInstance().getReference("order_keys");

        com.google.firebase.database.Query query = mydb.orderByKey()
                .equalTo(order.getCabang().replaceAll("\\s+","").toUpperCase());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String temp = null;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        //Map<String, String> order_keys = (Map<String, String>) snapshot.getValue();
                        temp = snapshot.getValue(String.class);
                        //String wow = snapshot.getKey();
                        //Toast.makeText(context,wow).show();
                        //Log.d(TAG, "onDataChange: 12313"+wow);
                    }
                order_key = temp;
                Log.d(TAG, "onDataChange: "+temp);

                sendOrder2(order,temp);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.d(TAG, order_key+" 123213");

//        //update order_keys/<nama_cabang>
//        mydb.setValue(Integer.toString(Integer.parseInt(order_key)+1));
//
//        //ubah order_id order menjadi yang format custom
//        order.setOrderId(order.getCabang().toLowerCase().charAt(0)+order_key);
//
//
//        //Untuk mengirim data ke orders
//        mydb = FirebaseDatabase.getInstance().getReference("orders");
//
//        //tukar kuncinya dengan kunci dari firebase
//        order.setOrderId(mydb.push().getKey());
//
//        //kirim data
//        mydb.child(order.getOrderId()).setValue(order);
//
//        //tambahkan order ke data usernya
//        mydb= FirebaseDatabase.getInstance().getReference("users");
//        mydb.child(Utils.encodeEmail(order.getUserEmail())).child("orders").push().setValue(order.getOrderId());
//        Log.d(TAG, "sendOrder: ljydgakjyfdkuyfadkuayfdka");
    }

    private void sendOrder2(Order order, String key){
        mydb = FirebaseDatabase.getInstance().getReference("order_keys/"+order.getCabang().toUpperCase()
        .replaceAll("\\s+",""));
//        Log.d("Cabang :", order.getCabang());

        //format ulang key
        key = String.format("%04d",Integer.parseInt(key)+1);

        //update order_keys/<nama_cabang>
        mydb.setValue(key);

        //ubah order_id order menjadi yang format custom
        order.setOrderId(order.getCabang().toLowerCase().charAt(0)+key);

        //kirim data ke orders
        mydb = FirebaseDatabase.getInstance().getReference("orders");

        //kirim data
        mydb.child(order.getOrderId()).setValue(order);

        //tambahkan order ke data usernya
        mydb= FirebaseDatabase.getInstance().getReference("users");
        mydb.child(Utils.encodeEmail(order.getUserEmail())).child("orders").push().setValue(order.getOrderId());
        Log.d(TAG, "sendOrder: ljydgakjyfdkuyfadkuayfdka");
    }
}
