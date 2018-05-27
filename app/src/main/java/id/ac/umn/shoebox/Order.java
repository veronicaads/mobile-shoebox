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
    private String tanggal_masuk;
    private String tanggal_keluar;
    private String status_service;
    private String status_pembayaran;
    private Integer biaya;
    private String noLaci;
    private String buktiPembayaran;

    public Order(){}

    public Order(String orderid, String userEmail, String cabang, String service,
                 String subService,String merkSepatu,String image, String comment,
                 String tanggal_masuk, String tanggal_keluar, String status_service,
                 String status_pembayaran, Integer biaya, String noLaci, String buktiPembayaran){
        this.orderId = orderid;
        this.userEmail = userEmail;
        this.cabang = cabang;
        this.merkSepatu = merkSepatu;
        this.image = image;
        this.comment = comment;
        this.subService = subService;
        this.service = service;
        this.tanggal_masuk = tanggal_masuk;
        this.tanggal_keluar = tanggal_keluar;
        this.status_service = status_service;
        this.status_pembayaran = status_pembayaran;
        this.biaya = biaya;
        this.noLaci = noLaci;
        this.buktiPembayaran = buktiPembayaran;
    }

    public String getOrderId(){return orderId;}
    public String getUserEmail(){return userEmail;}
    public String getCabang(){return cabang;}
    public String getService(){return service;}
    public String getSubService(){return subService;}
    public String getMerkSepatu(){return merkSepatu;}
    public String getImage(){return image;}
    public String getComment(){return comment;}
    public String getTanggal_masuk(){return tanggal_masuk;}
    public String getTanggal_keluar(){return tanggal_keluar;}
    public String getStatus_service(){return status_service;}
    public String getStatus_pembayaran(){return status_pembayaran;}
    public Integer getBiaya(){return biaya;}
    public String getNoLaci(){return noLaci;}
    public String getBuktiPembayaran(){return buktiPembayaran;}

    public void setOrderId(String id){this.orderId = id;}
    public void setNoLaci(String no){noLaci = no;}
}
