package id.ac.umn.shoebox;

import android.*;
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

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

    public OrderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderFragment.
     */
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

    }
    ImageView sepatu;
    private static final int PICK_IMAGE = 100;
    private static final int TAKE_PHOTOS = 50;
    Uri imageUri, cameraUri;
    private StorageReference IStorage;
    private ProgressDialog progressDialog;
    String filename, CurrentPath;
    private File CreateFile() throws IOException{
        //String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFilename = "JPEG_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        Toast.makeText(getContext(), storageDir.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), "GA DAPET IJIN", Toast.LENGTH_SHORT).show();
        }
        File image = File.createTempFile(imageFilename,".jpg");
        CurrentPath = "file:"+image.getAbsolutePath();
        return image;
    }
    private void galleryAddPic(){
        Intent mediascan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediascan.setData(cameraUri);
        getActivity().sendBroadcast(mediascan);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        IStorage = FirebaseStorage.getInstance().getReference();

        Button camera = (Button) view.findViewById(R.id.camerabutton);
        sepatu = view.findViewById(R.id.pict_sepatu);
        progressDialog = new ProgressDialog(getActivity());
        camera.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Intent camera = new Intent();
              camera.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//              if(camera.resolveActivity(getActivity().getPackageManager())!=null){
//                  File photofile = null;
//                  try{
//                      photofile = CreateFile();
//                      Toast.makeText(getContext(), CurrentPath.toString(),Toast.LENGTH_SHORT).show();
//                  }catch (IOException e){
//                      Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                  }
//                  if(photofile!=null){
//                      cameraUri = FileProvider.getUriForFile(getContext(),"id.ac.umn.shoebox.provider", photofile);
//                      camera.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
//                  }else{
//                      Log.d("BODO AMAT", "onClick: NULL JIR");
//                      Toast.makeText(getContext(), "NULL JIR", Toast.LENGTH_SHORT).show();
//                  }
//                  startActivityForResult(camera, TAKE_PHOTOS);
//              }
              startActivityForResult(camera, TAKE_PHOTOS);
          }
      });
        Button galery = (Button) view.findViewById(R.id.gallerybutton);
        galery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });


        Button order_but = (Button) view.findViewById(R.id.order_btn);
        order_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),DetailActivity.class));
            }
        });
        final EditText merek = view.findViewById(R.id.merek_edit);
        final EditText comment = view.findViewById(R.id.keterangan_edit);

        Button cancelBut = view.findViewById(R.id.back_btn);
        cancelBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                merek.setText("");
                comment.setText("");
                sepatu.setImageResource(R.drawable.ic_image_black_24dp);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK){
            progressDialog.setMessage("Uploading ....");
            progressDialog.show();

            imageUri=data.getData();
            filename = imageUri.getPath();
            StorageReference storageReference = IStorage.child("image_shoes/"+filename);
            storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Uri download = taskSnapshot.getDownloadUrl();
                    Toast.makeText(getContext(),"WOI UPLOAD", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"Gagal :(", Toast.LENGTH_SHORT).show();
                }
            });
            sepatu.setImageURI(imageUri);
        }
        if(requestCode==TAKE_PHOTOS && resultCode==RESULT_OK){
            //galleryAddPic();
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            sepatu.setImageBitmap(bitmap);
        }
            /*progressDialog.setMessage("Uploading ....");
            progressDialog.show();

            //cameraUri= data.getData();

            progressDialog.dismiss();
            /*StorageReference storageReference = IStorage.child("image_shoes/"+cameraUri);
            storageReference.putFile(cameraUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Uri download = taskSnapshot.getDownloadUrl();
                    Toast.makeText(getContext(),"WOI UPLOAD", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(),"Gagal :(", Toast.LENGTH_SHORT).show();
                }
            });
        }*/
    }


    @Override
    public void onStart() {
        super.onStart();

        TextView tv = (TextView) getActivity().findViewById(R.id.text_view);
        tv.setText(" Keterangan :\n Untuk pemesanan service Repaint ataupun \n Repair akan mendapatkan free service Reclean.");

        Spinner spinner = getActivity().findViewById(R.id.cabang);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.listcabang,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        Spinner spinner2 = getActivity().findViewById(R.id.service);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(getContext(), R.array.listservice,
                android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter2);

        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Spinner spinner3 = getActivity().findViewById(R.id.subservice);
                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.listreclean,
                            android.R.layout.simple_spinner_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(adapter3);
                } else if (position == 1) {
                    Spinner spinner3 = getActivity().findViewById(R.id.subservice);
                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.listrepaint,
                            android.R.layout.simple_spinner_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(adapter3);
                } else if (position == 2) {
                    Spinner spinner3 = getActivity().findViewById(R.id.subservice);
                    ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getContext(), R.array.listrepair,
                            android.R.layout.simple_spinner_item);
                    adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner3.setAdapter(adapter3);
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
