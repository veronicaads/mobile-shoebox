package id.ac.umn.shoebox;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.logging.Level;

public class ListOrderActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    class CustomAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return orderID.length;
        }

        @Override
        public Object getItem(int i) {
            return null;
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
            txt1.setText(orderID[position]);
            txt2.setText(Deadline[position]);
            txt3.setText(Status[position]);
            txt4.setText(Level[position]);
            if(Level[position].equals("Level Priority : 3")){imv.setImageResource(gambar[0]);}
            else if(Level[position].equals("Level Priority : 2")){imv.setImageResource(gambar[1]);}
            else if(Level[position].equals("Level Priority : 1")){imv.setImageResource(gambar[2]);}
            return viewHolder;
        }
    }
    ListView listView ;

    String[] Level = new String[] {"Level Priority : 3","Level Priority : 2","Level Priority : 1","Level Priority : 2"};
    String[] Status = new String[] {"Status : Pending","Status : On Progress","Status : Finished","Status : Finished"};
    String[] Deadline = new String[] {"Deadline : 25 April 2018","Deadline : 25 April 2018","Deadline : 25 April 2018","Deadline : 25 April 2018"};
    String[] orderID = new String[] {"Order ID : U0003","Order ID : U0002","Order ID : U0001","Order ID : U0005"};
    Integer[] gambar = new Integer[]{R.drawable.icons8_high_priority_48, R.drawable.icons8_warning_shield_48, R.drawable.icons8_error_40};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);
        listView= (ListView) findViewById(R.id.listorder);

        mAuth = com.google.firebase.auth.FirebaseAuth.getInstance();

        Toolbar toolbar = (Toolbar) findViewById(R.id.navigate);
        setSupportActionBar(toolbar);

        //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,array);
        CustomAdapter customAdminList = new CustomAdapter();
        listView.setAdapter(customAdminList);
        configureSignIn();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                startActivity(new Intent(ListOrderActivity.this, DetailOrderActivity.class));
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
}
