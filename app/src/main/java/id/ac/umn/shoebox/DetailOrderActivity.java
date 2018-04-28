package id.ac.umn.shoebox;


        import android.content.Intent;
        import android.os.Bundle;
        import android.app.Activity;
        import android.support.v7.app.AppCompatActivity;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.PopupMenu;
        import android.widget.Toast;

public class DetailOrderActivity extends AppCompatActivity {
    Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_order);
        button1 = (Button) findViewById(R.id.status_btn);
        button1.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(DetailOrderActivity.this, button1);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.status, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
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
        inflater.inflate(R.menu.admin,menu);
        return super.onCreateOptionsMenu(menu);
    }


}
