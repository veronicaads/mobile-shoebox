package id.ac.umn.shoebox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

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
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import id.ac.umn.shoebox.SharedPrefManager;

import static android.support.constraint.Constraints.TAG;

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
    private TextView mFullNameTextView, mEmailTextView,mPhoneNumberView;
    private String mUsername, mEmail,mPhoneNumber;
    //private CircleImageView mProfileImageView;

    private OnFragmentInteractionListener mListener;

    SharedPrefManager sharedPrefManager;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;


    private DatabaseReference databaseReference;
    private ListView listViewOrders;
    private List<String> orderList;

    public HomeFragment() {
        // Required empty public constructor
    }

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

        orderList = new ArrayList<>();
    }

    @Override
    public void onStart() {
        super.onStart();

        mFullNameTextView = getView().findViewById(R.id.nama_user);
        mEmailTextView = getView().findViewById(R.id.email_user);
        mPhoneNumberView = getView().findViewById(R.id.notlp_user);
        final com.github.siyamed.shapeimageview.CircularImageView photo = (com.github.siyamed.shapeimageview.CircularImageView) getView().findViewById(R.id.photo);
        //mProfileImageView = (CircleImageView) getView().findViewById(R.id.photo);
        sharedPrefManager = new SharedPrefManager(getContext());
        mEmail = sharedPrefManager.getUserEmail();
        mUsername = sharedPrefManager.getName();
        mPhoneNumber = sharedPrefManager.getpNumber();
        mFullNameTextView.setText(mUsername);
        //mEmailTextView.setText(mEmail);
        //mPhoneNumberView.setText(mPhoneNumber);
        String uri = sharedPrefManager.getPhoto();
        Uri mPhotoUri = Uri.parse(uri);

       Picasso.with(getContext())
               .load(mPhotoUri)
                .placeholder(android.R.drawable.sym_def_app_icon)
               .error(android.R.drawable.sym_def_app_icon)
               .into(photo);

        databaseReference = FirebaseDatabase.getInstance().getReference("users")
                .child(Utils.encodeEmail(mEmail)).child("orders");

        //
        //ambil list order dari fragment_home
        //
        listViewOrders = (ListView) getView().findViewById(R.id.list_order);

        //
        //async listener task untuk update setiap order
        //baru ditambahkan
        //
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                orderList.clear();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String od = ds.getValue(String.class);
                    orderList.add(od);
                    Log.d("ds",od);
                }

                try {
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1,orderList);
                    listViewOrders.setAdapter(adapter);
                }
                catch (Exception io){
                    io.getStackTrace();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        listViewOrders.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getActivity(),DetailActivity.class));
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home,container,false);
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.navigate);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
//        String[] array = new String[] {"U001","U002"};
//        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1,array);
//        listView.setAdapter(arrayAdapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent();
//                intent.setClass(getActivity(), DetailActivity.class);
//                //intent.putExtra("position", i);
//                //intent.putExtra("id", id);
//                startActivity(intent);
//            }
//        });
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
