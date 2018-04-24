package id.ac.umn.shoebox;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

public class NotificationActivity extends AppCompatActivity {

    ListView list;
    String[] web = {
            " (U0001) Sepatu siap diambil! ",
            " (U0001) Sepatu dalam tahap pencucian",
            " (U0001) Sepatu telah sampai di dapur pencucian",
            " (U0001) Sepatu telah diambil dari lemari"
    } ;
    Integer[] imageId = {
            R.drawable.ic_notifications_black_24dp,
            R.drawable.ic_notifications_black_24dp,
            R.drawable.ic_notifications_black_24dp,
            R.drawable.ic_notifications_black_24dp,
    };


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_reorder:
                    return true;
                case R.id.navigation_notifications:
                    return true;
                case R.id.navigation_help:
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        CustomListNotif adapter = new
                CustomListNotif(NotificationActivity.this, web, imageId);
        list=(ListView)findViewById(R.id.listnotif);
        list.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

}
