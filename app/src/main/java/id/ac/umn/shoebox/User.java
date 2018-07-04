package id.ac.umn.shoebox;

/**
 * Constructor User
 */


import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class User {

    private String fullName;
    private String photo;
    private String email;
    private String pNumber;
    private String privilege;
    private String address;
    private String cabangAdmin;
    private HashMap<String,Object> timestampJoined;

    public User() {
    }

    /**
     * Use this constructor to create new User.
     * Takes user name, email and timestampJoined as params
     *
     * @param timestampJoined
     */
    public User(String mFullName, String mPhoneNo, String mEmail,
                String mPNumber,String mAddress,String mPrivilege
                ,HashMap<String, Object> timestampJoined) {
        this.fullName = mFullName;
        this.photo = mPhoneNo;
        this.email = mEmail;
        this.pNumber = mPNumber;
        this.address = mAddress;
        this.timestampJoined = timestampJoined;
        this.privilege = mPrivilege;
    }




    public void UpdateUser(String mAddress,String mPNumber){
        this.address = mAddress;
        this.pNumber = mPNumber;
    }


    public String getPrivilege(){
        return privilege;
    }

    public String getpNumber(){
        return pNumber;
    }

    public String getAddress(){return address;}

    public String getFullName() {
        return fullName;
    }

    public String getPhoto() {
        return photo;
    }

    public String getEmail() {
        return email;
    }

    public HashMap<String, Object> getTimestampJoined() {
        return timestampJoined;
    }

    public String getCabangAdmin(){return cabangAdmin;}

}