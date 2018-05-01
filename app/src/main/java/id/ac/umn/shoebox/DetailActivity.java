package id.ac.umn.shoebox;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tv= (TextView)findViewById(R.id.text_view);
       // tv.setText(" Silahkan lakukan pembayaran melalui  \n bank CBA ke nomor rekening xxxxxxxx \n Setelah itu, silahkan upload bukti \n transfer  di menu yang telah tersedia");
        Button pembayaran = findViewById(R.id.bayar_btn);
        pembayaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DetailActivity.this, BuktiUploadActivity.class));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

}
