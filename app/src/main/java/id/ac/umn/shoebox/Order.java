package id.ac.umn.shoebox;

import com.google.firebase.database.Exclude;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by miqdude on 06/05/18.
 */

public class Order {
    private String orderId;
    private String userEmail;
    private String cabang;
    private String service;
    private String subService;
    private String merkSepatu;
    private String image;
    private String comment;
    private HashMap<String,String> tanggal_pesan;
    private String tanggal_ambil;
    private String status_service;
    private String status_pembayaran;
    private Integer biaya;

    public Order(){}

    public Order(String orderid, String userEmail, String cabang, String service,
                 String subService,String merkSepatu,String image, String comment,
                 HashMap tanggal_pesan, String tanggal_ambil, String status_service,
                 String status_pembayaran, Integer biaya){
        this.orderId = orderid;
        this.userEmail = userEmail;
        this.cabang = cabang;
        this.merkSepatu = merkSepatu;
        this.image = image;
        this.comment = comment;
        this.subService = subService;
        this.service = service;
        //this.tanggal_pesan = tanggal_pesan;
        this.tanggal_ambil = tanggal_ambil;
        this.status_service = status_service;
        this.status_pembayaran = status_pembayaran;
        this.biaya = biaya;
    }

    public String getOrderId(){return orderId;}
    public String getUserEmail(){return userEmail;}
    public String getCabang(){return cabang;}
    public String getService(){return service;}
    public String getSubService(){return subService;}
    public String getMerkSepatu(){return merkSepatu;}
    public String getImage(){return image;}
    public String getComment(){return comment;}
    //public HashMap<String, String> getTanggal_pesan(){return tanggal_pesan;}
    public String getTanggal_ambil(){return tanggal_ambil;}
    public String getStatus_service(){return status_service;}
    public String getStatus_pembayaran(){return status_pembayaran;}
    public Integer getBiaya(){return biaya;}

    public void setOrderId(String id){this.orderId = id;}
}
