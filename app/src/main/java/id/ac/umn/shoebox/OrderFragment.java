package id.ac.umn.shoebox;

import android.*;
import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import static android.app.Activity.RESULT_OK;
import static android.support.constraint.Constraints.TAG;
import static android.view.View.generateViewId;

public class OrderFragment extends Fragment {
    private int PICK_IMAGE_REQUEST = 1;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private Spinner cabang_spinner,service_spinner,subservice_spinner;
    SharedPrefManager sharedPrefManager;

    public OrderFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static OrderFragment newInstance(String param1, String param2) {
        OrderFragment fragment = new OrderFragment();
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
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        getActivity().getWindow().setBackgroundDrawableResource(R.drawable.background);
    }

    ImageView sepatu;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    private StorageReference IStorage;
    private ProgressDialog progressDialog;
    private DatabaseReference database;
    String filename, currentPath;
    String isi="";
    Uri donlod;
    Integer flag_order=0;
    StorageReference storageReference;

    /** validasi input user*/
    public boolean validate(){
        if(merek_edit.getText().toString().length()<2 || merek_edit.getText().toString().isEmpty()){
            return false;
        }
        else if(nomor_gembok.getText().toString().isEmpty() || nomor_gembok.getText().toString().length()<4){
            return false;
        }
        else return true;
    }

    EditText merek_edit;
    EditText comment_edit;
    EditText nomor_gembok;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        IStorage = FirebaseStorage.getInstance().getReference();

        sepatu = view.findViewById(R.id.pict_sepatu);
        progressDialog = new ProgressDialog(getActivity());

        Button galery = (Button) view.findViewById(R.id.gallerybutton);
        galery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

       merek_edit = (EditText) view.findViewById(R.id.merek_edit2);
       comment_edit = (EditText) view.findViewById(R.id.keterangan_edit);
       nomor_gembok = (EditText) view.findViewById(R.id.no_gembok_edit);

        final String userEmail = new SharedPrefManager(getContext()).getUserEmail();

        Button order_but = (Button) view.findViewById(R.id.order_btn);
        order_but.setOnClickListener(new View.OnClickListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                if(validate()){
                    AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                    alert.setTitle("Alert");
                    alert.setCancelable(true);
                    alert.setMessage("Are you sure to make an order ?");

                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Log.d("alert", "onClick: wowowowo");
                            progressDialog.setMessage("Please Wait ....");
                            progressDialog.show();
                            progressDialog.setCancelable(false);
                            filename = imageUri.getLastPathSegment();

                            final String merek = merek_edit.getText().toString();
                            final String comment = comment_edit.getText().toString();
                            final String gembok = nomor_gembok.getText().toString();

                            //
                            //kirim gambar
                            //
                            Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                            final Long Path = timestamp.getTime();
                            final String imagePath = "image_shoes/"+Path;

                            FirebaseDb firebaseDb = new FirebaseDb();

                            /** Upload gambar pada storage*/
                            storageReference = IStorage.child(imagePath);
                            storageReference.putFile(imageUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Toast.makeText(getContext(),"File Updated !", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(),"Gagal :(", Toast.LENGTH_SHORT).show();
                                }
                            });

                            final String cabang = cabang_spinner.getSelectedItem().toString();
                            final String service = service_spinner.getSelectedItem().toString();
                            final String subservice = subservice_spinner.getSelectedItem().toString();

                            String price = subservice.replaceAll("[()]","");

                            Log.d(TAG, "onClick: price "+price);

                            Scanner s = new Scanner(price).useDelimiter("[^0-9]+");
                            int harga = s.nextInt();

                            Log.d(TAG, "onClick: scanner "+Integer.toString(harga));

                            Log.d(TAG, "onClick: ordered");

                            // ambil tanggal masuk
                            Date d = new Date();
                            String tgl_pesan = String.format("%d-%d-%d",d.getDate(),d.getMonth(),d.getYear()-100);

                            //
                            //kirim order baru
                            //belum cek jika gambar belom berisi maka akan gagal
                            Order od = new Order("0000",userEmail,cabang,service,subservice,merek,
                                    Path.toString(),comment, tgl_pesan,"",
                                    "pending","belum lunas",harga
                                    ,"","",gembok,0);
                            firebaseDb.sendOrder(od,getContext());

                            Log.d("cabang",cabang);
                            Log.d("Merek",merek);
                            progressDialog.dismiss();
                            startActivity(new Intent(getActivity(),UtamaActivity.class));
                            flag_order=0;
                        }
                    });

                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });
                    AlertDialog ale = alert.create();
                    ale.setCanceledOnTouchOutside(true);
                    ale.show();
                }
                else startActivity(new Intent(getActivity(), UtamaActivity.class));
            }
        });

        Button cancelBut = view.findViewById(R.id.back_btn);
        cancelBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                merek_edit.setText("");
                comment_edit.setText("");
                nomor_gembok.setText("");
                sepatu.setImageResource(R.drawable.ic_image_black_24dp);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            imageUri=data.getData();
            sepatu.setImageURI(imageUri);
        }
    }


    @Override
    public void onStart() {
        super.onStart();

        TextView tv = (TextView) getActivity().findViewById(R.id.text_view);
        tv.setText(" Keterangan :\n Untuk pemesanan service Repaint ataupun \n Repair akan mendapatkan free service Reclean.");

        cabang_spinner = getActivity().findViewById(R.id.cabang);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.listcabang,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cabang_spinner.setAdapter(adapter);

        service_spinner = getActivity().findViewById(R.id.service);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.listservice,
                android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        service_spinner.setAdapter(adapter2);

        service_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    subservice_spinner = getActivity().findViewById(R.id.subservice);
                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.listreclean,
                            android.R.layout.simple_spinner_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subservice_spinner.setAdapter(adapter3);
                } else if (position == 1) {
                    subservice_spinner = getActivity().findViewById(R.id.subservice);
                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.listrepaint,
                            android.R.layout.simple_spinner_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subservice_spinner.setAdapter(adapter3);
                } else if (position == 2) {
                    subservice_spinner = getActivity().findViewById(R.id.subservice);
                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.listrepair,
                            android.R.layout.simple_spinner_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    subservice_spinner.setAdapter(adapter3);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
