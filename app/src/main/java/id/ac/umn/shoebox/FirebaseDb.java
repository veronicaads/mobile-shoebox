package id.ac.umn.shoebox;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Message;
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

        final String cabang = order.getCabang().replaceAll("\\s+","").toLowerCase();

        //ambil key terakir dari cabangnya
        mydb = FirebaseDatabase.getInstance().getReference(cabang);

        mydb.orderByKey().equalTo("key").limitToFirst(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = "";
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                        key = snapshot.getValue(String.class);
                    }

                Log.d(TAG, "onDataChange: "+key);

                getLaci(order,key, cabang);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    //ambil nomor laci
    private void getLaci(final Order order, final String key, final String cabang){
        mydb = FirebaseDatabase.getInstance().getReference(cabang);

        mydb.child("laci").orderByValue().equalTo("free").limitToFirst(1).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        String nmor_laci = "";
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            nmor_laci = ds.getKey();
                        }

                        Log.d(TAG, "nomor laci"+nmor_laci);

                        mydb.child("laci").child(nmor_laci).setValue("occupied");

                        sendOrder2(order,key,nmor_laci,cabang);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );
    }

    private void sendOrder2(Order order, String key, String laci, String cabang){
        mydb = FirebaseDatabase.getInstance().getReference(cabang);

        //format ulang key
        key = String.format("%04d",Integer.parseInt(key)+1);

        Log.d(TAG, "sendOrder2: "+key);

        //update order_keys/<nama_cabang>
        mydb.child("key").setValue(key);

        //ubah order_id order menjadi yang format custom
        order.setOrderId(order.getCabang().toLowerCase().charAt(0)+key);

        //tambahkan dengan nomor laci baru
        order.setNoLaci(laci);

        //kirim data ke <nama_cabang/orders>
        mydb = FirebaseDatabase.getInstance().getReference(cabang);

        //kirim data
        mydb.child("orders").child(order.getOrderId()).setValue(order);

        //tambahkan order ke data usernya
        mydb= FirebaseDatabase.getInstance().getReference("users");
        mydb.child(Utils.encodeEmail(order.getUserEmail())).child("orders").push().setValue(order.getOrderId());
        Log.d(TAG, "sendOrder: ljydgakjyfdkuyfadkuayfdka");


        //send message to cabang
        mydb = FirebaseDatabase.getInstance().getReference(cabang);
        mydb.child("inbox").push().setValue(new PushMessage(
                order.getService()+ " " + order.getSubService()
                        + " " + order.getStatus_service()));
    }

    public void kirimBukti(String cabang, String order_id, String imagePath){
        mydb = FirebaseDatabase.getInstance().getReference(cabang);
        mydb.child("orders").child(order_id).setValue(imagePath);
        //mydb.child(order_id).child("buktiPembayaran").setValue(imagePath);
    }

    /*public void kirimRating(String cabang, String order_id, int rating){
        mydb = FirebaseDatabase.getInstance().getReference(cabang);
        mydb.child("orders").child(order_id).setValue(rating);
        //mydb.child(order_id).child("buktiPembayaran").setValue(imagePath);
    }*/

}
