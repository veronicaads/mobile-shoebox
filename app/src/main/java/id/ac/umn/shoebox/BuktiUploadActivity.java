package id.ac.umn.shoebox;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class BuktiUploadActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 100;
    private static final int TAKE_PHOTOS = 50;
    Uri imageUri;
    ImageView bukti ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bukti_upload);
        bukti = (ImageView) findViewById(R.id.buktibayar);

        Button camera = (Button) findViewById(R.id.btnTakeImage);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, TAKE_PHOTOS);
            }
        });
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
                startActivity(new Intent(BuktiUploadActivity.this, UtamaActivity.class));
                Toast.makeText(getApplicationContext(), "Upload Selesai", Toast.LENGTH_SHORT).show();
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            imageUri=data.getData();
            bukti.setImageURI(imageUri);
        }
        if(requestCode==TAKE_PHOTOS && resultCode==RESULT_OK){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            bukti.setImageBitmap(bitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

}
