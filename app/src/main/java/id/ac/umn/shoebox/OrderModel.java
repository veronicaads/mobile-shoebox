package id.ac.umn.shoebox;

import android.util.Log;

/**
 * Created by veronica on 5/23/18.
 */

public class OrderModel {
    private int ImageService;
    private String service,cabang,inDate,estDate, status;
    private String orderID;

    public OrderModel(int imageService, String Service, String Cabang, String OrderID, String InDate, String EstDate, String Status) {
        ImageService = imageService;
        service = Service;
        cabang = Cabang;
        orderID = OrderID;
        inDate = InDate;
        estDate = EstDate;
        status = Status;
    }

    public int getImageService() {
        return ImageService;
    }

    public void setImageService(int imageService) {
        ImageService = imageService;
    }

    public String getService() {
        return service;
    }

    public void setService(String Service) {
        Service = Service;
    }

    public String getCabang() {
        return cabang;
    }

    public void setCabang(String Cabang) {
        Cabang = Cabang;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String OrderID) {
        OrderID = OrderID;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        inDate = inDate;
    }

    public String getEstDate() {
        return estDate;
    }

    public void setEstDate(String estDate) {
        estDate = estDate;
    }
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        status = status;
    }
}
