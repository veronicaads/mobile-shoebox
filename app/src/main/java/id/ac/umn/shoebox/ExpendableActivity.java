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
            init_repaint();
            adapter= new ExpendableListAdapter(this, list, listHashMap);
            expend_list.setAdapter(adapter);
        }
        if (position == 2){
            TextView judul = (TextView) findViewById(R.id.judul);
            judul.setText("REPAIR");
            init_repair();
            adapter= new ExpendableListAdapter(this, list, listHashMap);
            expend_list.setAdapter(adapter);
        }
        if (position == 3){
            TextView judul = (TextView) findViewById(R.id.judul);
            judul.setText("HOW TO ORDER");
            init_order();
            adapter= new ExpendableListAdapter(this, list, listHashMap);
            expend_list.setAdapter(adapter);
        }
        if (position == 4){
            TextView judul = (TextView) findViewById(R.id.judul);
            judul.setText("ABOUT US");
            init_about();
            adapter= new ExpendableListAdapter(this, list, listHashMap);
            expend_list.setAdapter(adapter);
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

    private void init_order(){
        list = new ArrayList<>();
        list.add("Set your Order");


        listHashMap = new HashMap<>();

        List<String> set = new ArrayList<>();
        set.add("1. Pilih cabang tempat anda akan menggunakan jasa ShoeBox\n");
        set.add("2. Pilih Service yang diinginkan\n");
        set.add("3. Pilih -SubService- yang anda inginkan\n");
        set.add("4. Masukkan foto dari sepatu yang ingin anda Treatment (Untuk keperluan memberikan ekspektasi Hasil)\n");
        set.add("5. Berikan pesan kepada ShoeBox tentang sepatu anda (opsional)\n");

        listHashMap.put(list.get(0),set);

    }

    private void init_repaint(){
        list = new ArrayList<>();
        list.add("Dress Shoes");
        list.add("Casual Shoes");
        list.add("Workshoes");
        list.add("Dance Shoes");
        list.add("Boots");
        list.add("Sandals");
        list.add("Slippers");
        list.add("Running Shoes");
        list.add("Basketball Shoes");

        listHashMap = new HashMap<>();

        List<String> dress = new ArrayList<>();
        dress.add("Sepatu kantor atau sepatu yang biasa dipakai untuk acara resmi");

        List<String> casual = new ArrayList<>();
        casual.add("Sepatu yang dipakai untuk berpergian santai tanpa meninggakan kesan santai");

        List<String> work = new ArrayList<>();
        work.add("Sepatu yang dipakai untuk pekerjaan lapangan");

        List<String> dance = new ArrayList<>();
        dance.add("Sepatu yang digunakan untuk berdansa atau menari");

        List<String> boots = new ArrayList<>();
        boots.add("Sepatu dengan ukuran tinggi");

        List<String> sandals = new ArrayList<>();
        sandals.add("Alas kaki jepit santai");

        List<String> slippers = new ArrayList<>();
        slippers.add("Sepatu pipih yang digunakan untuk santai");

        List<String> running = new ArrayList<>();
        running.add("Sepatu yang digunakan untuk olahraga lari");

        List<String> basket = new ArrayList<>();
        basket.add("Sepatu yang digunakan untuk bermain basket");

        listHashMap.put(list.get(0),dress);
        listHashMap.put(list.get(1),casual);
        listHashMap.put(list.get(2),work);
        listHashMap.put(list.get(3),dance);
        listHashMap.put(list.get(4),boots);
        listHashMap.put(list.get(5),sandals);
        listHashMap.put(list.get(6),slippers);
        listHashMap.put(list.get(7),running);
        listHashMap.put(list.get(8),basket);
    }

    private void init_repair(){
        list = new ArrayList<>();
        list.add("Ordinary Sizing");
        list.add("Full Sizing");
        list.add("Ordinary Sewing");
        list.add("Full Sewing");
        list.add("Remove Outer Soll");
        list.add("Remove Inner Soll");
        list.add("Leather Patch");
        list.add("Leather Pressing");


        listHashMap = new HashMap<>();

        List<String> ordinary = new ArrayList<>();
        ordinary.add("Untuk Anda yang ingin mengelem sepatu dengan wilayah lem kecil");

        List<String> full = new ArrayList<>();
        full.add("Untuk Anda yang ingin mengelem sepatu secara  keseluruhan");

        List<String> ordSew = new ArrayList<>();
        ordSew.add("Untuk Anda yang Ingin menjahit sepatu dengan wilayah jahit kecil");

        List<String> fullSew = new ArrayList<>();
        fullSew.add("Untuk Anda yang ingin menjahit sepatu secara keseluruhan");

        List<String> remOut = new ArrayList<>();
        remOut.add("Untuk Anda yang ingin mengganti soll luar sepatu");

        List<String> remIn = new ArrayList<>();
        remIn.add("Untuk Anda yang ingin mengganti soll dalam sepatu");

        List<String> leatherPat = new ArrayList<>();
        leatherPat.add("Untuk Anda yang ingin menambal kulit pada sepatu kulit");

        List<String> leatherPres = new ArrayList<>();
        leatherPres.add("Untuk Anda yang ingin merapikan dan melekatkan bagian dalam/luar pada sepatu kulit.");

        listHashMap.put(list.get(0),ordinary);
        listHashMap.put(list.get(1),full);
        listHashMap.put(list.get(2),ordSew);
        listHashMap.put(list.get(3),fullSew);
        listHashMap.put(list.get(4),remOut);
        listHashMap.put(list.get(5),remIn);
        listHashMap.put(list.get(6),leatherPat);
        listHashMap.put(list.get(7),leatherPres);
    }

    private void init_about(){
        list = new ArrayList<>();
        list.add("ShoeBox");
        list.add("Tim Kita");


        listHashMap = new HashMap<>();

        List<String> ShoeBox = new ArrayList<>();
        ShoeBox.add("" +
                "Executive Director   : Odillio Erlys\n" +
                "Creative Marketing : Benita Amellia\n" +
                "IT Support             : Thomas Nugroho\n" +
                "Finansial               : Vira Mutiara Sukma W.\n" +
                "Designer               : Chintya Oktaviani\n" +
                "                           : Lina Olivia Ayuningtyas\n");



        List<String> kita = new ArrayList<>();
        kita.add("Project Leader           : Stefanus Kurniawan\n" +
                "Frontend Developer    : Veronica Dian Sari\n" +
                "Backend Developer     : Miqdad Abdurrahman\n" +
                "Support                    : Fakhri Naufal Zuhdi\n");


        listHashMap.put(list.get(0),ShoeBox);
        listHashMap.put(list.get(1),kita);
    }
}
