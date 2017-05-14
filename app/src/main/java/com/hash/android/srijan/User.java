package com.hash.android.srijan;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Spandita Ghosh on 1/31/2017.
 */

public class User implements Parcelable {
    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
    private String id;
    private String name;
    private String phoneNumber;
    private String email;
    private String university;
    private String photoURL;

    public User() {
    }

    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        phoneNumber = in.readString();
        email = in.readString();
        university = in.readString();
        photoURL = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.phoneNumber);
        parcel.writeString(this.email);
        parcel.writeString(this.university);
        parcel.writeString(this.photoURL);

    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {

        this.university = university;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void saveUser() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            //user signed in
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("users");
            myRef.child(getName()).child(this.getId()).setValue(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    setId(null);
                }
            });

        } else
            Log.d("saveUser:", "User not authenticated");
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
