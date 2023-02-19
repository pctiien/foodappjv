package com.example.foodordermyson.Fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodordermyson.Activity.mainscreenActivity;
import com.example.foodordermyson.R;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodordermyson.Adapter.MoreInfo;
import com.example.foodordermyson.Adapter.MoreInfoAdapter;
import com.example.foodordermyson.Common.CurrentUser;
import com.example.foodordermyson.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link profileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class profileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public profileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment profileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static profileFragment newInstance(String param1, String param2) {
        profileFragment fragment = new profileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    static boolean isSignin = false;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("user");
    RecyclerView recyclerView;
    List<MoreInfo> moreInfoList;
    MoreInfoAdapter moreInfoAdapter;
    RoundedImageView profile_Avatar;
    ImageView profile_Back;
    TextView profile_ChangeProfile;
    TextView profile_Name;
    TextView profile_Email;
    TextView profile_PhoneNumber;
    TextView profile_Address;
    TextView profile_Warning;
    //Change Infomation Init
    AppCompatEditText profile_NameEdt;
    AppCompatEditText profile_EmailEdt;
    AppCompatEditText profile_AddressEdt;
    AppCompatEditText profile_PhoneNumberEdt;
    LinearLayoutCompat profile_PersonInf;
    LinearLayoutCompat profile_PersonChangeEdt;
    AppCompatButton profile_PersonChangeSave;
    TextView profile_PersonChangeCancel;
    ImageButton profile_AvatarChange;
    //Change avatar
    private final int REQUEST_CHANGE_PHOTO = 123;
    Uri avatarUri;
    String resultAvatarUri="";
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        avatarUri = result.getData().getData();
                        uploadImage();

                    }
                }
            }
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_profile,container,false);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initRecyclerView(view);
        initDrawable(view);
        setUpDrawable();
        setChangeProfile();
        setSaveChangeProfile();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_bot_nav,new cartFragment());
        return view;
    }
    private void uploadImage() {
        String imageName = UUID.randomUUID().toString();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference("avatar").child("img/" + imageName);
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        storageReference.putFile(avatarUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Can not upload your avatar now", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                //  Toast.makeText(profileActivity.this, "Can not upload your avatar", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                //Get download Uri from "Uploaded Uri"
                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Toast.makeText(getContext(), "Your avatar is ready to change now", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        resultAvatarUri = uri.toString();
                        Glide.with(getActivity()).load(uri.toString()).into(profile_Avatar);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double percent = 100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                progressDialog.setMessage("Uploaded " + percent + "%");
            }
        });
    }

    private void updateUserToFirebase(User user) {
        isSignin = true;
        databaseReference.child(user.getUid()).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
                ImageView img = new ImageView(getContext());
                img.setImageResource(R.drawable.completetick);
                alert.setMessage("Updated your identity document");
                alert.setView(img);
                alert.show();
            }
        });
    }

    //When click SaveChange Button to change info of Account --------
    private void setSaveChangeProfile() {

        //Save change click
        profile_PersonChangeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = String.valueOf(profile_NameEdt.getText());
                String newEmail = String.valueOf(profile_EmailEdt.getText());
                String newAddress = String.valueOf(profile_AddressEdt.getText());
                String newPhoneNumber = String.valueOf(profile_PhoneNumberEdt.getText());
                if (newName.trim().equals("") || newEmail.trim().equals("") || newAddress.trim().equals("") || newPhoneNumber.trim().equals("")) {
                    profile_Warning.setVisibility(View.VISIBLE);
                } else {
                    if (resultAvatarUri.toString().trim().equals("") == false && resultAvatarUri.toString().trim() != null && resultAvatarUri != null) {
                        CurrentUser.currentUser.setAvatar(resultAvatarUri);
                    }
                    profile_Warning.setVisibility(View.GONE);
                    User user = CurrentUser.currentUser;
                    user.setAddress(newAddress);
                    user.setEmail(newEmail);
                    user.setPhonenumber(newPhoneNumber);
                    user.setName(newName);
                    updateUserToFirebase(user);
                    CurrentUser.currentUser = user;
                    setUpDrawable();
                    profile_PersonChangeEdt.setVisibility(View.GONE);
                    profile_PersonInf.setVisibility(View.VISIBLE);
                    profile_AvatarChange.setVisibility(View.GONE);
                }
            }
        });


        //Cancel click


        profile_PersonChangeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resultAvatarUri = "";
                setUpDrawable();
                profile_Warning.setVisibility(View.GONE);
                profile_PersonChangeEdt.setVisibility(View.GONE);
                profile_PersonInf.setVisibility(View.VISIBLE);
                profile_AvatarChange.setVisibility(View.GONE);
            }
        });
        profile_AvatarChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                // intent.setPackage("com.android.gallery");
                activityResultLauncher.launch(Intent.createChooser(intent, "Select picture"));
                Log.d("Activity Request ", "OKAY");
            }
        });
    }

    //Set click change Profile
    private void setChangeProfile() {
        profile_ChangeProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                profile_AvatarChange.setVisibility(View.VISIBLE);
                profile_PersonInf.setVisibility(View.GONE);
                profile_PersonChangeEdt.setVisibility(View.VISIBLE);
                profile_AddressEdt.setText(profile_Address.getText());
                profile_PhoneNumberEdt.setText(profile_PhoneNumber.getText());
                profile_NameEdt.setText(profile_Name.getText());
                profile_EmailEdt.setText(profile_Email.getText());
            }
        });
    }


    private void initDrawable(View view) {
        profile_AvatarChange = (ImageButton) view.findViewById(R.id.profile_AvatarChange);
        profile_Warning = (TextView) view.findViewById(R.id.profile_Warning);
        profile_Back = (ImageView) view.findViewById(R.id.profile_Back);
        profile_NameEdt = (AppCompatEditText) view.findViewById(R.id.profile_NameEdt);
        profile_EmailEdt = (AppCompatEditText) view.findViewById(R.id.profile_EmailEdt);
        profile_AddressEdt = (AppCompatEditText) view.findViewById(R.id.profile_AddressEdt);
        profile_PhoneNumberEdt = (AppCompatEditText) view.findViewById(R.id.profile_PhoneNumberEdt);
        profile_ChangeProfile = (TextView) view.findViewById(R.id.profile_ChangeProfile);
        profile_Avatar = (RoundedImageView) view.findViewById(R.id.profile_Avatar);
        profile_Name = (TextView) view.findViewById(R.id.profile_Name);
        profile_Address = (TextView) view.findViewById(R.id.profile_Address);
        profile_Email = (TextView) view.findViewById(R.id.profile_Email);
        profile_PhoneNumber = (TextView) view.findViewById(R.id.profile_PhoneNumber);
        profile_PersonInf = (LinearLayoutCompat) view.findViewById(R.id.profile_PersonInf);
        profile_PersonChangeEdt = (LinearLayoutCompat) view.findViewById(R.id.profile_PersonChangeEdt);
        profile_PersonChangeSave = (AppCompatButton) view.findViewById(R.id.profile_PersonChangeSave);
        profile_PersonChangeCancel = (TextView) view.findViewById(R.id.profile_PersonChangeCancel);
    }

    private void setUpDrawable() {
        if (!CurrentUser.currentUser.getName().isEmpty() && CurrentUser.currentUser.getName() != null && !CurrentUser.currentUser.getName().trim().equals("")) {
            profile_Name.setText(CurrentUser.currentUser.getName().toString());

        }
        if (!CurrentUser.currentUser.getEmail().isEmpty() && CurrentUser.currentUser.getName() != null && !CurrentUser.currentUser.getEmail().trim().equals("")) {
            profile_Email.setText(CurrentUser.currentUser.getEmail().toString());
        }
        if (!CurrentUser.currentUser.getPhonenumber().isEmpty() && CurrentUser.currentUser.getPhonenumber() != null && !CurrentUser.currentUser.getPhonenumber().trim().equals("")) {
            profile_PhoneNumber.setText(CurrentUser.currentUser.getPhonenumber().toString());

        }
        if (!CurrentUser.currentUser.getAddress().isEmpty() && CurrentUser.currentUser.getAddress() != null && !CurrentUser.currentUser.getAddress().trim().equals("")) {
            profile_Address.setText(CurrentUser.currentUser.getAddress().toString());
        }
        if (!CurrentUser.currentUser.getAvatar().trim().isEmpty() && !CurrentUser.currentUser.getAvatar().trim().equals("")) {
            Glide.with(this).load(CurrentUser.currentUser.getAvatar()).into(profile_Avatar);
        } else {
            profile_Avatar.setImageResource(R.drawable.avatardefault);
        }
    }

    public void initRecyclerView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.profile_MoreInPerson);
        moreInfoList = new ArrayList<>();
        moreInfoAdapter = new MoreInfoAdapter(moreInfoList, getContext(), new MoreInfoAdapter.iItemClickListener() {
            @Override
            public void goToCartFragment() {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_bot_nav,new cartFragment());
                fragmentTransaction.commit();
                mainscreenActivity.bot_nav.getMenu().getItem(1).setChecked(true);
            }
        });
        recyclerView.setAdapter(moreInfoAdapter);
        moreInfoList.add(new MoreInfo("Cart"));
        moreInfoList.add(new MoreInfo("Purchase"));
        moreInfoList.add(new MoreInfo("Faq"));
        moreInfoList.add(new MoreInfo("Help"));
        moreInfoAdapter.notifyDataSetChanged();
    }
}