package id.ac.umn.shoebox;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import android.support.v4.view.ViewPager;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private TextView mFullNameTextView;
    private String mUsername, mEmail,mPhoneNumber;
    //private CircleImageView mProfileImageView;

    Runnable runnable = new Runnable() {
        public void run() {
            if (myCustomPagerAdapter.getCount() == page) {
                page = 0;
            } else {
                page++;
            }
            viewPager.setCurrentItem(page, true);
            handler.postDelayed(this, delay);
        }
    };

    private OnFragmentInteractionListener mListener;

    SharedPrefManager sharedPrefManager;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;

    private DatabaseReference databaseReference_a,databaseReference_u,databaseReference_p,databaseReference_m;
    private ListView listViewOrders;
    private static  List orderList;
    private static List cabangList;
    private static List serviceList;
    private static List inDateList;
    private static List outDateList;
    private static List statuslist;

    int images[] = {R.drawable.slider1, R.drawable.slider2, R.drawable.slider3};
    CustomPagerAdapter myCustomPagerAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    private Handler handler;
    private int delay = 5000;
    private int page = 0;
    ViewPager viewPager;
    private OrderAdapter orderAdapter;
    private ArrayList<OrderModel> models;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
       cabangList = new ArrayList<>();
        orderList = new ArrayList<>();
        serviceList = new ArrayList();
        inDateList = new ArrayList();
        outDateList = new ArrayList();
        statuslist = new ArrayList();
    }

    public static String StringToDate(String date,String Service){
        String date1 = date;
        String service1 = Service;
        String replacestring = date1.replace('-','/');
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        Date estDate, esDateadd = null;
        try {
            estDate = df.parse(replacestring);
            Calendar cal = Calendar.getInstance();
            cal.setTime(estDate);
            if (service1.equals("Repaint")){
                cal.add(Calendar.DATE,14);
                esDateadd = cal.getTime();
            }
            else if (service1.equals("Repair")){
                cal.add(Calendar.DATE,5);
                esDateadd = cal.getTime();
            }
            else if (service1.equals("Reclean")){
                cal.add(Calendar.DATE,5);
                esDateadd = cal.getTime();
            }
            else {
                cal.add(Calendar.DATE,14);
                esDateadd = cal.getTime();
            }

        }
        catch (ParseException e){
            e.printStackTrace();
        }
        Format format = new SimpleDateFormat("dd-MM-yyyy");
        String estdate_final = format.format(esDateadd);
        return estdate_final;
    }

    public static ArrayList<OrderModel> getList(){
        ArrayList<OrderModel> listorder = new ArrayList<>();
        System.out.println(orderList.size());
        for (int i = 0; i < orderList.size();i++){
            String od = (String) orderList.get(i);
            String order = od.substring(1,5);
            String cabang = (String) cabangList.get(i);
            String service = (String) serviceList.get(i);
            String status = (String) statuslist.get(i);
            String tglmasuk = (String) inDateList.get(i);
            String estdate = StringToDate(tglmasuk,service);
            if (status.equals("pending")){
                listorder.add(new OrderModel(R.drawable.pending,service.toUpperCase(),cabang.toUpperCase(),order,tglmasuk,estdate,"PENDING - Kami akan segera melayani permintaan anda"));
            }
            else if (status.equals("progress")){
                listorder.add(new OrderModel(R.drawable.progress,service.toUpperCase(),cabang.toUpperCase(),order,tglmasuk,estdate,"PROSES - Kami sedang mempersiapkan yang terbaik"));
            }
            else if (status.equals("done")){
                listorder.add(new OrderModel(R.drawable.done,service.toUpperCase(),cabang.toUpperCase(),order,tglmasuk,estdate,"SELESAI - Sepatu kesayanganmu sudah dapat diambil"));
            }
        }
        return listorder;
    }

    @Override
    public void onStart() {
        super.onStart();
        orderList.clear();
        cabangList.clear();
        serviceList.clear();
        inDateList.clear();
        outDateList.clear();

        final com.github.siyamed.shapeimageview.CircularImageView photo = getView().findViewById(R.id.photo);
        mFullNameTextView = getView().findViewById(R.id.nama_user);
        sharedPrefManager = new SharedPrefManager(getContext());
        mEmail = sharedPrefManager.getUserEmail();
        mUsername = sharedPrefManager.getName();
        mPhoneNumber = sharedPrefManager.getpNumber();
        mFullNameTextView.setText(mUsername);
        String uri = sharedPrefManager.getPhoto();
        Uri mPhotoUri = Uri.parse(uri);

        Picasso.with(getContext())
               .load(mPhotoUri)
                .placeholder(android.R.drawable.sym_def_app_icon)
               .error(android.R.drawable.sym_def_app_icon)
               .into(photo);

        //slider handler & timer
        handler = new Handler();
        viewPager = getView().findViewById(R.id.viewPager);
        myCustomPagerAdapter = new CustomPagerAdapter(getActivity(),images);
        viewPager.setAdapter(myCustomPagerAdapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                page = position;
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        /** Ambil list order dari fragment_home*/
        listViewOrders = (ListView) getView().findViewById(R.id.list_order);

        /** Query List Order User cabang mercubuana*/
        databaseReference_m = FirebaseDatabase.getInstance().getReference("mercubuana/orders");
        Query query = databaseReference_m.orderByChild("userEmail").equalTo(sharedPrefManager.getUserEmail().toString());
        query.addChildEventListener(new ChildEventListener() {
            String checker = "";
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Order order = dataSnapshot.getValue(Order.class);
                    String od = order.getOrderId();
                    if (!checker.equals(od)){
                        orderList.add(od);
                        String cabang = order.getCabang();
                        cabangList.add(cabang);
                        String servis = order.getService();
                        serviceList.add(servis);
                        String status_Servis = order.getStatus_service();
                        statuslist.add(status_Servis);
                        String tglmasuk = order.getTanggal_masuk();
                        inDateList.add(tglmasuk);
                        checker = od;
                    }
                }
                try
                {
                    models = getList();
                    orderAdapter = new OrderAdapter(getContext(), models);
                    listViewOrders.setAdapter(orderAdapter);
                }
                catch(Exception error)
                {
                    error.printStackTrace();
                }
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
        });

        databaseReference_p = FirebaseDatabase.getInstance().getReference("pertamina/orders");
        Query query_p = databaseReference_p.orderByChild("userEmail").equalTo(sharedPrefManager.getUserEmail().toString());
        query_p.addChildEventListener(new ChildEventListener() {
            String checker = "";
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Order order = dataSnapshot.getValue(Order.class);
                    String od = order.getOrderId();
                    if (!checker.equals(od)){
                        orderList.add(od);
                        String cabang = order.getCabang();
                        cabangList.add(cabang);
                        String servis = order.getService();
                        serviceList.add(servis);
                        String status_Servis = order.getStatus_service();
                        statuslist.add(status_Servis);
                        String tglmasuk = order.getTanggal_masuk();
                        inDateList.add(tglmasuk);
                        checker = od;
                    }
                }
                try
                {
                    models = getList();
                    orderAdapter = new OrderAdapter(getContext(), models);
                    listViewOrders.setAdapter(orderAdapter);
                }
                catch(Exception error)
                {
                    error.printStackTrace();
                }
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
        });

        databaseReference_u = FirebaseDatabase.getInstance().getReference("umn/orders");
        Query query2 = databaseReference_u.orderByChild("userEmail").equalTo(sharedPrefManager.getUserEmail().toString());
        query2.addChildEventListener(new ChildEventListener() {
            String checker = "";
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Order order = dataSnapshot.getValue(Order.class);
                    String od = order.getOrderId();
                    if (!checker.equals(od)){
                        orderList.add(od);
                        String cabang = order.getCabang();
                        cabangList.add(cabang);
                        String servis = order.getService();
                        serviceList.add(servis);
                        String status_Servis = order.getStatus_service();
                        statuslist.add(status_Servis);
                        String tglmasuk = order.getTanggal_masuk();
                        inDateList.add(tglmasuk);
                        checker = od;
                    }
                }
                try
                {
                    models = getList();
                    orderAdapter = new OrderAdapter(getContext(), models);
                    listViewOrders.setAdapter(orderAdapter);
                }
                catch(Exception error)
                {
                    error.printStackTrace();
                }
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
        });

        databaseReference_a = FirebaseDatabase.getInstance().getReference("atmajaya/orders");
        Query query_a = databaseReference_a.orderByChild("userEmail").equalTo(sharedPrefManager.getUserEmail().toString());
        query_a.addChildEventListener(new ChildEventListener() {
            String checker = "";
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Order order = dataSnapshot.getValue(Order.class);
                    String od = order.getOrderId();
                    if (!checker.equals(od)){
                        orderList.add(od);
                        String cabang = order.getCabang();
                        cabangList.add(cabang);
                        String servis = order.getService();
                        serviceList.add(servis);
                        String status_Servis = order.getStatus_service();
                        statuslist.add(status_Servis);
                        String tglmasuk = order.getTanggal_masuk();
                        inDateList.add(tglmasuk);
                        checker = od;
                    }
                }
                try
                {
                    models = getList();
                    orderAdapter = new OrderAdapter(getContext(), models);
                    listViewOrders.setAdapter(orderAdapter);
                }
                catch(Exception error)
                {
                    error.printStackTrace();
                }
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
        });

        listViewOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String orderId = (String) orderList.get(position);
                String cabang = (String) cabangList.get(position);
                Intent i = new Intent(getActivity(),DetailActivity.class);
                i.putExtra("ORDERID",orderId);
                i.putExtra("CABANG",cabang);
                startActivity(i);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.navigate);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        handler.postDelayed(runnable,delay);
    }

    @Override
    public void onPause() {
        super.onPause();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
