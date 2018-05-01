package id.ac.umn.shoebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button profileButton = findViewById(R.id.edit_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, UtamaActivity.class));
                Toast.makeText(getBaseContext(),"TerUpdated", Toast.LENGTH_SHORT).show();
            }
        });

        Button backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this, UtamaActivity.class));
                Toast.makeText(getBaseContext(),"Not Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
