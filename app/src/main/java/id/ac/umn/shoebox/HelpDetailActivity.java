package id.ac.umn.shoebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class HelpDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_detail);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

        if (position == 0){
            TextView textHeader = (TextView) findViewById(R.id.header_txt);
            textHeader.setText(R.string.header_Reclean);
            TextView myTextView = (TextView) findViewById(R.id.detail_txt);
            myTextView.setText(R.string.Reclean);
        }
        if (position == 1){
            TextView textHeader = (TextView) findViewById(R.id.header_txt);
            textHeader.setText(R.string.header_Repaint);
            TextView myTextView = (TextView) findViewById(R.id.detail_txt);
            myTextView.setText(R.string.Repaint);
        }
        if (position == 2){
            TextView textHeader = (TextView) findViewById(R.id.header_txt);
            textHeader.setText(R.string.header_Repair);
            TextView myTextView = (TextView) findViewById(R.id.detail_txt);
            myTextView.setText(R.string.Repair);
        }
        if (position == 4){
            TextView myTextView = (TextView) findViewById(R.id.detail_txt);
            myTextView.setText(R.string.Products);
        }
    }
}
