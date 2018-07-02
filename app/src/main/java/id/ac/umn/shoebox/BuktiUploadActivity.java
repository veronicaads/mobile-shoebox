package id.ac.umn.shoebox;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.sql.Timestamp;



public class BuktiUploadActivity extends AppCompatActivity {

    FirebaseStorage storage;
    private StorageReference IStorage;
    StorageReference storageReference;
    String order_id;
    String cabang;
    TextView orders;
    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("order_id");
    //DatabaseReference usersRef = ref.child("orders");

    private static final int PICK_IMAGE = 100;
//    private static final int TAKE_PHOTOS = 50;
    Uri imageUri;
    ImageView bukti ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //storage = FirebaseStorage.getInstance();
        //storageReference = storage.getReference();

        setContentView(R.layout.activity_bukti_upload);
        bukti = (ImageView) findViewById(R.id.buktibayar);
        orders= findViewById(R.id.no_order);

        Intent a = getIntent();
        order_id = a.getStringExtra("ORDERID");
        cabang = a.getStringExtra("CABANG");
        IStorage = FirebaseStorage.getInstance().getReference();

        progressDialog = new ProgressDialog(BuktiUploadActivity.this);
        orders.setText(order_id.toUpperCase());

        //Toast.makeText(getApplicationContext(), order_id, Toast.LENGTH_SHORT).show();
//        Button camera = (Button) findViewById(R.id.btnTakeImage);
//        camera.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivityForResult(intent, TAKE_PHOTOS);
//            }
//        });
        Button galery = (Button) findViewById(R.id.btnGalerryImage);
        galery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });



        Button submit = (Button) findViewById(R.id.btnSubmit);
        submit.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              progressDialog.setMessage("Uploading ....");
              progressDialog.show();
              progressDialog.setCancelable(false);
              filename = imageUri.getPath();

              final FirebaseDb firebaseDb = new FirebaseDb();

              Timestamp timestamp_buktiBayar = new Timestamp(System.currentTimeMillis());
              final Long Path_BuktiBayar = timestamp_buktiBayar.getTime();
              final String imagePath = "bukti_pembayaran/" + Path_BuktiBayar;

              storageReference = IStorage.child(imagePath);
              storageReference.putFile(imageUri)
                      .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                          @Override
                          public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                              firebaseDb.kirimBukti(cabang, order_id, Path_BuktiBayar.toString());
                              startActivity(new Intent(BuktiUploadActivity.this, UtamaActivity.class));
                          }
                      }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Toast.makeText(getApplicationContext(), "Gagal :(", Toast.LENGTH_SHORT).show();

                      //firebaseDb.kirimBukti(cabang, order_id, Path_BuktiBayar.toString());

                      startActivity(new Intent(BuktiUploadActivity.this, UtamaActivity.class));
                      Toast.makeText(getApplicationContext(), "Upload Selesai", Toast.LENGTH_SHORT).show();
                  }
              });
          }
      });

        Button back = (Button) findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(BuktiUploadActivity.this, UtamaActivity.class));
                Toast.makeText(getApplicationContext(), "Order Dibatalkan", Toast.LENGTH_SHORT).show();
            }
        });
    }

    String filename, currentPath;
    //private StorageReference IStorage;
    ProgressDialog progressDialog;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            imageUri=data.getData();
            bukti.setImageURI(imageUri);
        }
//        if(requestCode==TAKE_PHOTOS && resultCode==RESULT_OK){
//            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
//            bukti.setImageBitmap(bitmap);
//        }
    }
//    private File createImageFile() throws IOException {
//        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFilename = "JPEG_"+timestamp;
//        Log.d("Camera", "Bisa Create");
//        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM+"/Camera");
//        Log.d("Camera", "Bisa dapet file");
//        File image = File.createTempFile(imageFilename,".jpg");
//        Log.d("Camera", image.toString());
//        Toast.makeText(getApplicationContext(), storageDir.getAbsolutePath(), Toast.LENGTH_SHORT).show();
//        Log.d("Camera", storageDir.getAbsolutePath());
//        currentPath = "file:"+image.getAbsolutePath();
//        return image;
//    }
//    private void dispatchTakePicture() {
//        Intent takePict = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
//        Log.d("Camera", "Bisa foto");
//        if (takePict.resolveActivity(this.getPackageManager()) != null) {
//            File photo = null;
//            try {
//                photo = createImageFile();
//                //Toast.makeText(getContext(), currentPath.toString(), Toast.LENGTH_SHORT).show();
//            } catch (IOException ec) {
//                //Toast.makeText(getContext(), ec.getMessage(), Toast.LENGTH_SHORT);
//            }
//            if (photo != null) {
//                takePict.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
//                startActivityForResult(takePict, TAKE_PHOTOS);
//            }
//        }
//    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

}
