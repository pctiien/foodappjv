package com.example.foodordermyson.Activity;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodordermyson.Adapter.MoreInfo;
import com.example.foodordermyson.Adapter.MoreInfoAdapter;
import com.example.foodordermyson.Common.CurrentUser;
import com.example.foodordermyson.Fragment.cartFragment;
import com.example.foodordermyson.Model.User;
import com.example.foodordermyson.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
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

public class profileActivity extends AppCompatActivity {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initRecyclerView();
        initDrawable();
        setUpDrawable();
        setChangeProfile();
        setSaveChangeProfile();
        setBackButton();
    }

    private void uploadImage() {
        String imageName = UUID.randomUUID().toString();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference("avatar").child("img/" + imageName);
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();
        storageReference.putFile(avatarUri).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profileActivity.this, "Can not upload your avatar now", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(profileActivity.this, "Your avatar is ready to change now", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        resultAvatarUri = uri.toString();
                        Glide.with(profileActivity.this).load(uri.toString()).into(profile_Avatar);
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

    private void setBackButton() {
        profile_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void updateUserToFirebase(User user) {
        isSignin = true;
        FirebaseApp.initializeApp(profileActivity.this);
        databaseReference.child(user.getUid()).setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                AlertDialog.Builder alert = new AlertDialog.Builder(profileActivity.this);
                ImageView img = new ImageView(getBaseContext());
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


    private void initDrawable() {
        profile_AvatarChange = (ImageButton) findViewById(R.id.profile_AvatarChange);
        profile_Warning = (TextView) findViewById(R.id.profile_Warning);
        profile_Back = (ImageView) findViewById(R.id.profile_Back);
        profile_NameEdt = (AppCompatEditText) findViewById(R.id.profile_NameEdt);
        profile_EmailEdt = (AppCompatEditText) findViewById(R.id.profile_EmailEdt);
        profile_AddressEdt = (AppCompatEditText) findViewById(R.id.profile_AddressEdt);
        profile_PhoneNumberEdt = (AppCompatEditText) findViewById(R.id.profile_PhoneNumberEdt);
        profile_ChangeProfile = (TextView) findViewById(R.id.profile_ChangeProfile);
        profile_Avatar = (RoundedImageView) findViewById(R.id.profile_Avatar);
        profile_Name = (TextView) findViewById(R.id.profile_Name);
        profile_Address = (TextView) findViewById(R.id.profile_Address);
        profile_Email = (TextView) findViewById(R.id.profile_Email);
        profile_PhoneNumber = (TextView) findViewById(R.id.profile_PhoneNumber);
        profile_PersonInf = (LinearLayoutCompat) findViewById(R.id.profile_PersonInf);
        profile_PersonChangeEdt = (LinearLayoutCompat) findViewById(R.id.profile_PersonChangeEdt);
        profile_PersonChangeSave = (AppCompatButton) findViewById(R.id.profile_PersonChangeSave);
        profile_PersonChangeCancel = (TextView) findViewById(R.id.profile_PersonChangeCancel);
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

    public void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.profile_MoreInPerson);
        moreInfoList = new ArrayList<>();
        moreInfoAdapter = new MoreInfoAdapter(moreInfoList, profileActivity.this, new MoreInfoAdapter.iItemClickListener() {
            @Override
            public void goToCartFragment() {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction().replace(R.id.container_bot_nav,new cartFragment());
                fragmentTransaction.commit();
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