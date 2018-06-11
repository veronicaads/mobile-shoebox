package id.ac.umn.shoebox;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class ListOrderActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private String cabangAdmin;

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return orderID.size();
        }

        @Override
        public Object getItem(int i) {
            return orderID.get(i);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View view, ViewGroup viewGroup) {
            TextView txt1, txt2, txt3, txt4;
            ImageView imv;
            View viewHolder =getLayoutInflater().inflate(R.layout.listview_admin_layout,null);

            txt1=viewHolder.findViewById(R.id.order_id);
            txt2=viewHolder.findViewById(R.id.Deadline);
            txt3=viewHolder.findViewById(R.id.status);
            txt4=viewHolder.findViewById(R.id.levelpriority);
            imv=viewHolder.findViewById(R.id.images);

            //imv.setImageResource(gambar[position]);
            txt1.setText(orderID.get(position));
            txt2.setText(Deadline.get(position));
            txt3.setText(Status.get(position));
//            txt4.setText(Level.get(position));
            //imv.setImageResource(gambar.get(position));
            return viewHolder;
        }
    }



    ListView listView ;

//    String[] Level = new String[] {"Level Priority : 3","Level Priority : 2","Level Priority : 1","Level Priority : 2"};
//    String[] Status = new String[] {"Status : Pending","Status : On Progress","Status : Finished","Status : Finished"};
//    String[] Deadline = new String[] {"Deadline : 25 April 2018","Deadline : 25 April 2018","Deadline : 25 April 2018","Deadline : 25 April 2018"};
//    String[] orderID = new String[] {"Order ID : U0003","Order ID : U0002","Order ID : U0001","Order ID : U0005"};
//    Integer[] gambar = new Integer[]{R.drawable.icons8_high_priority_48, R.drawable.icons8_warning_shield_48, R.drawable.icons8_error_40};

    ArrayList<String> Status   = new ArrayList<String>();
    ArrayList<String> Deadline = new ArrayList<String>();
    ArrayList<String> orderID  = new ArrayList<String>();
    ArrayList<String> Level    = new ArrayList<String>();
    ArrayList<Integer> gambar  = new ArrayList<Integer>();

    private DatabaseReference databaseReference;
    private List<String> orderList;

    @Override
    protected void onStart() {

        super.onStart();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);
        listView= (ListView) findViewById(R.id.listorder);

        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();

        TextView isi = findViewById(R.id.tanggal);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ROOT);
        isi.setText(String.format("Tanggal Hari ini : %s",sdf.format(new Date())));
        Toolbar toolbar = (Toolbar) findViewById(R.id.navigate);
        setSupportActionBar(toolbar);


        //ambil cabang admin
        cabangAdmin = new SharedPrefManager(this).getCabangAdmin();
        Log.d("cabang", "onCreate: "+cabangAdmin);
        DatabaseReference popUp = FirebaseDatabase.getInstance().getReference(cabangAdmin);
        popUp.child("inbox").addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        for(DataSnapshot ds : dataSnapshot.getChildren()){
//                            PushMessage pm = ds.getValue(PushMessage.class);
//                            Log.d("Push Message", pm.getMessage() + " " + pm.getStatus());
//                        }
                        PushMessage pm = dataSnapshot.getValue(PushMessage.class);
                        Log.d("Push Message", pm.getMessage() + " " + pm.getStatus()
                            + " " + s);
                        pushNotification(pm.getMessage());
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,array);
        final CustomAdapter customAdminList = new CustomAdapter();
        /*COBA*/

        databaseReference = FirebaseDatabase.getInstance().getReference("orders");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    orderID.add(ds.child("orderId").getValue().toString());
                    Status.add(String.format("Status : %s",ds.child("status_service").getValue().toString()));
                    if(ds.child("tanggal_masuk").getValue().toString().equals("")){
                        Deadline.add("Kosong");
                    }
                    else {
                        try{
                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy", Locale.ROOT);
                            Date  firstDate = sdf.parse(ds.child("tanggal_masuk").getValue().toString());
                            Calendar now = Calendar.getInstance();
                            now.setTime(firstDate);
                            if(ds.child("service").getValue().toString().equals("Reclean") || ds.child("service").getValue().toString().equals("Repair"))
                                now.add(Calendar.DAY_OF_MONTH, 5);
                            else if(ds.child("service").getValue().toString().equals("Repaint"))
                                now.add(Calendar.DAY_OF_MONTH, 14);
                            String selsai = sdf.format(now.getTime());

                            Deadline.add(String.format("Deadline: %s", selsai));
                        }catch (Exception e){e.printStackTrace();}

                    }
                    try{
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy", Locale.ROOT);
                        Date now = new Date();
                        Date firstDate = sdf.parse(ds.child("tanggal_masuk").getValue().toString());
                        //Date secondDate = sdf.parse(ds.child("tanggal_keluar").getValue().toString());

                        /*Tambahan Hari Keluar*/
                        Calendar saat = Calendar.getInstance();
                        saat.setTime(firstDate);
                        if(ds.child("service").getValue().toString().equals("Reclean") || ds.child("service").getValue().toString().equals("Repair"))
                            saat.add(Calendar.DAY_OF_MONTH, 5);
                        else if(ds.child("service").getValue().toString().equals("Repaint"))
                            saat.add(Calendar.DAY_OF_MONTH, 14);
                        Date kelar = saat.getTime();

                        if(ds.child("status_service").getValue().toString().equals("Done")){
                            gambar.add(R.drawable.shoes);
                        }
                        else{
                            long diffInMillies = Math.abs(kelar.getTime() - now.getTime());
                            long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                            long level = 30-diff;
                            //long level=3;
                            if(level<0)level=0;
                            Level.add(String.format("Level: %s", Long.toString(level)));
                            if(level >= 3)gambar.add(R.drawable.icons8_high_priority_48);
                            else if(level == 2)gambar.add(R.drawable.icons8_warning_shield_48);
                            else gambar.add(R.drawable.icons8_error_40);
                        }

                    } catch (Exception e) {e.printStackTrace();}
                }
                try{
                    listView.setAdapter(customAdminList);
                }catch (Exception e){
                    e.getStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        /*BATAS AKHIR*/


        configureSignIn();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(ListOrderActivity.this, DetailOrderActivity.class);
                intent.putExtra("OrderID", customAdminList.getItem(i).toString() );
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.navigation_edit_profile){
            Intent intent=new Intent(ListOrderActivity.this, SignUpActivity.class);
            startActivity(intent);
            finish();
        }
        else if(id==R.id.navigation_logout){
            signOut();
            Toast.makeText(getApplicationContext(),"Logout Berhasil",Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
    Context mContext = this;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    private void signOut(){
        new SharedPrefManager(mContext).clear();
        mAuth.signOut();

        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        Intent intent = new Intent(ListOrderActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }



    public void configureSignIn(){
// Configure sign-in to request the user's basic profile like name and email
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleApiClient with access to GoogleSignIn.API and the options above.
        mGoogleApiClient = new GoogleApiClient.Builder(mContext)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, options)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    long doubleBack;
    final int TIME_DELAY = 1500;

    @Override
    public void onBackPressed() {
        if (doubleBack + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        doubleBack = System.currentTimeMillis();
    }


    private String getCabangAdmin(Context mContext){
        final String[] adminEmail = {new SharedPrefManager(mContext).getUserEmail()};
        final String[] cabang = {""};

        DatabaseReference mydb = FirebaseDatabase.getInstance().getReference("users");
        mydb.child(Utils.encodeEmail(adminEmail[0])).child("cabang").addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String adminemail = dataSnapshot.getValue(String.class);
                        Log.d("adminEmail", "onDataChange: "+adminemail);
                        cabang[0] = adminemail;
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        return cabang[0];
    }

    private
    void pushNotification(String text){
        Intent resultIntent = new Intent(this, ListOrderActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this
                ,0, resultIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("ShoeBox")
                .setContentText(text)
                .setSmallIcon(R.drawable.icon)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        NotificationManager notificationManager = (NotificationManager)
                getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,notification);
        Log.d("push", "pushNotification: ");
    }
}
