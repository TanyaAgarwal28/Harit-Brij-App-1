package com.haritbrij.haritBrij;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.haritbrij.haritBrij.models.Tree;
import com.haritbrij.haritBrij.utils.ImageHelper;
import com.haritbrij.haritBrij.utils.SharedPrefConstants;
import com.haritbrij.haritBrij.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TreeProfileFragment extends Fragment {
    UserMainViewModel viewModel;
    ImageView treeProfileImageView,UploadImageView1,UploadImageView2,UploadImageView3;
    TextView utIdTextView;
    TextView districtTextView;
    TextView blockTextView;
    TextView villageTextView;
    MapView mapView;
    GoogleMap mGoogleMap;
    Bitmap img1,img2,img3;


    Tree tree;

    public TreeProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tree_profile_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(UserMainViewModel.class);


        int position = viewModel.getPosition();
        tree = viewModel.getTreeList().get(position);




        utIdTextView = view.findViewById(R.id.utidTextView);
        districtTextView = view.findViewById(R.id.districtTextView);
        blockTextView = view.findViewById(R.id.blockTextView);
        villageTextView  = view.findViewById(R.id.villageTextView);
        treeProfileImageView = view.findViewById(R.id.treeProfileImageView);
        UploadImageView1=view.findViewById(R.id.UploadImageView1);
        UploadImageView2=view.findViewById(R.id.UploadImageView2);
        UploadImageView3=view.findViewById(R.id.UploadImageView3);
        mapView = view.findViewById(R.id.treeProfileMapView);
        mapView.onCreate(savedInstanceState);

        utIdTextView.setText(tree.id);
        districtTextView.setText(tree.district);
        blockTextView.setText(tree.block);
        villageTextView.setText(tree.village);
        UploadImageView2.setImageAlpha(50);
        UploadImageView3.setImageAlpha(50);


//            String baseUrl = VolleySingleton.getBaseUrl();
//            String myUrl = baseUrl + "getalltree.php";
//
//
//            StringRequest myRequest = new StringRequest(Request.Method.GET, myUrl,
//                    response -> {
//                        try{
//                            //Create a JSON object containing information from the API.
//                            JSONObject myJsonObject = new JSONObject(response);
//                            JSONArray jsonArray = myJsonObject.getJSONArray("body");
//
//                            //save the from response in new tree object
//                            for(int jsonArrayIndex = 0; jsonArrayIndex < jsonArray.length(); jsonArrayIndex++) {
//
//                                JSONObject indexedTree = jsonArray.getJSONObject(jsonArrayIndex);
//                                if(indexedTree.get("strutid").equals(tree.id)){
//                                    System.out.println(tree.id);
//                                    if(indexedTree.getString("img2") != null) {
//                                    String display = indexedTree.getString("img2");
//                                    Toast.makeText(getActivity(), display, Toast.LENGTH_SHORT).show();
//                                        Bitmap image = ImageHelper.decodeImage(display);
//                                        UploadImageView1.setImageBitmap(image);
//                                        UploadImageView2.setImageAlpha(255);
//                                    }
//                                    else{
//                                        UploadImageView1.setImageAlpha(255);
//                                        UploadImageView2.setImageAlpha(50);
//                                    }
//                                }
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    },
//                    volleyError -> Log.e(getClass().getSimpleName(), volleyError.toString())
//            );
//
//            VolleySingleton.getInstance(getContext()).addToRequestQueue(myRequest);
//




        byte[] decodedString = Base64.decode(tree.image1, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        treeProfileImageView.setImageBitmap(decodedByte);

        //setting up the mapView
        mapView.getMapAsync(new OnMapReadyCallback() {
            @SuppressLint("MissingPermission")
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                mGoogleMap = googleMap;
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.setMyLocationEnabled(true);
                // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
                try {
                    MapsInitializer.initialize(requireActivity());
                    LatLng sydney = new LatLng(27.60522281732449, 77.59289534421812);
                    googleMap.addMarker(new MarkerOptions()
                            .position(sydney));
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(13.0f));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });






        UploadImageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, 2);
          } catch (ActivityNotFoundException e) {
                    // display error state to the user
                    Toast.makeText(getActivity(), "Sorry Camera not found", Toast.LENGTH_LONG).show();
                }
            }
        });
        UploadImageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, 3);
             } catch (ActivityNotFoundException e) {
                    // display error state to the user
                    Toast.makeText(getActivity(), "Sorry Camera not found", Toast.LENGTH_LONG).show();
                }
            }
        });
        UploadImageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, 4);

               } catch (ActivityNotFoundException e) {
                    // display error state to the user
                    Toast.makeText(getActivity(), "Sorry Camera not found", Toast.LENGTH_LONG).show();
                }
            }
        });










    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == RESULT_OK) {
            assert data != null;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            treeRegisterBitmap = imageBitmap;
            Bitmap resizedImage = Bitmap.createScaledBitmap(imageBitmap, 500, 500, true);
            img1 = resizedImage;
            UploadImageView1.setImageBitmap(resizedImage);
            UploadImageView2.setImageAlpha(255);
            JSONObject object=new JSONObject();
            try {
                object.put("strutid",tree.id);
                object.put("status",1);
                object.put("img",ImageHelper.encodeImage(img1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String baseUrl = VolleySingleton.getBaseUrl();
            String myUrl = baseUrl + "anotherimgupload.php";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, myUrl, object,
                    response -> {
                        Toast.makeText(getActivity(), "Data uploaded", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        Toast.makeText(getActivity(), "Data not uploaded", Toast.LENGTH_SHORT).show();
                    }
            );

            VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("Clicked",true);    //name is the key so may use a username or whatever you want
            editor.commit();
        }
        if (requestCode == 3 && resultCode == RESULT_OK) {
            assert data != null;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            treeRegisterBitmap = imageBitmap;
            Bitmap resizedImage = Bitmap.createScaledBitmap(imageBitmap, 500, 500, true);
            img2=resizedImage;
            UploadImageView2.setImageBitmap(resizedImage);
            UploadImageView3.setImageAlpha(255);
            JSONObject object=new JSONObject();
            try {
                object.put("strutid",tree.id);
                object.put("status",1);
                object.put("img",ImageHelper.encodeImage(img2));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String baseUrl = VolleySingleton.getBaseUrl();
            String myUrl = baseUrl + "anotherimgupload.php";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, myUrl, object,
                    response -> {
                        Toast.makeText(getActivity(), "Data uploaded", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        Toast.makeText(getActivity(), "Data not uploaded", Toast.LENGTH_SHORT).show();
                    }
            );

            VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
        }
        if (requestCode == 4 && resultCode == RESULT_OK) {
            assert data != null;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            treeRegisterBitmap = imageBitmap;
            Bitmap resizedImage = Bitmap.createScaledBitmap(imageBitmap, 500, 500, true);
            img3=resizedImage;
            UploadImageView3.setImageBitmap(resizedImage);
            JSONObject object=new JSONObject();
            try {
                object.put("strutid",tree.id);
                object.put("status",1);
                object.put("img",ImageHelper.encodeImage(img3));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String baseUrl = VolleySingleton.getBaseUrl();
            String myUrl = baseUrl + "anotherimgupload.php";
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, myUrl, object,
                    response -> {
                        Toast.makeText(getActivity(), "Data uploaded", Toast.LENGTH_SHORT).show();
                    },
                    error -> {
                        Toast.makeText(getActivity(), "Data not uploaded", Toast.LENGTH_SHORT).show();
                    }
            );

            VolleySingleton.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
