package id.ac.umn.shoebox;


import com.firebase.client.Firebase;
import com.firebase.client.ServerValue;

import java.util.Date;

/**
 * Created by miqdude on 29/05/18.
 */

public class PushMessage {
    private String message; //isi pesannya
    private String status;  //buat check udh di read atau belum

    public PushMessage(){}

    public PushMessage(String message){
        this.message = message;
        this.status = "sent";
    }

    public String getMessage(){return message;}
    public String getStatus(){return status;}
}
