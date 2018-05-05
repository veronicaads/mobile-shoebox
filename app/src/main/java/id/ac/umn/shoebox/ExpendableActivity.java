package id.ac.umn.shoebox;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpendableActivity extends AppCompatActivity {

    ExpandableListView expend_list;
    private ExpendableListAdapter adapter;
    private List<String> list;
    private HashMap<String, List<String>> listHashMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expendable);

        expend_list = (ExpandableListView) findViewById(R.id.submenu_expandablelistview);
        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

        if (position == 0){
            TextView judul = (TextView) findViewById(R.id.judul);
            judul.setText("RECLEAN");
            init_reclean();
            adapter= new ExpendableListAdapter(this, list, listHashMap);
            expend_list.setAdapter(adapter);
        }
        if (position == 1){
            TextView judul = (TextView) findViewById(R.id.judul);
            judul.setText("REPAINT");
        }
        if (position == 2){
            TextView judul = (TextView) findViewById(R.id.judul);
            judul.setText("REPAIR");
        }
        if (position == 3){
            TextView judul = (TextView) findViewById(R.id.judul);
            judul.setText("HOW TO ORDER");
        }
        if (position == 4){
            TextView judul = (TextView) findViewById(R.id.judul);
            judul.setText("ABOUT US");
        }

    }

    private void init_reclean(){
        list = new ArrayList<>();
        list.add("Fast Clean");
        list.add("Deep Clean");
        list.add("Unyellowing");

        listHashMap = new HashMap<>();

        List<String> fast = new ArrayList<>();
        fast.add("Membersihkan sepatu Anda dengan cepat dan Membersihkan sepatu hanya bagian luar tanpa meninggalkan pembersihan secara teliti agar sepatu Anda terlihat segar kembali");

        List<String> deep = new ArrayList<>();
        deep.add("Membersihkan sepatu bagian luar dan dalam. Membersihkan sepatu Anda secara detail hingga menghilangkan bakteri jamur yang menyebabkan bau pada sepatu kesayangan Anda.");

        List<String> unyel = new ArrayList<>();
        unyel.add("Membersihkan sepatu Anda secara detail hingga menghilangkan bakteri jamur yang menyebabkan bau pada sepatu kesayangan Anda.");

        listHashMap.put(list.get(0),fast);
        listHashMap.put(list.get(1),deep);
        listHashMap.put(list.get(2),unyel);
    }
}
