package id.ac.umn.shoebox;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.firebase.client.Firebase;
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

import static android.support.constraint.Constraints.TAG;

/**
 * Created by miqdude on 08/05/18.
 */

public class FirebaseDb{
    private static DatabaseReference mydb;
    private StorageReference storageReference;

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
        mywow.child(Utils.encodeEmail(order.getUserEmail())).child("orders").push().setValue(order.getOrderId());
        Log.d(TAG, "sendOrder: ljydgakjyfdkuyfadkuayfdka");
    }

    public void sendImage(Uri imageUri){
        StorageReference reference = storageReference.child("image_shoes/"+imageUri.getLastPathSegment());
        reference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                        Log.d(TAG, "onSuccess: "+downloadUrl);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: "+e);
                    }
                });
    }
}
