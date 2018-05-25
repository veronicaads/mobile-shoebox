package id.ac.umn.shoebox;

/**
 * Created by veronica on 5/23/18.
 */

public class OrderModel {
    private int ImageService;
    private String Service,Cabang,OrderID,inDate,estDate, status;

    public OrderModel(int imageService, String Service, String Cabang, String OrderID, String inDate, String estDate, String status) {
        ImageService = imageService;
        Service = Service;
        Cabang = Cabang;
        OrderID = OrderID;
        inDate = inDate;
        estDate = estDate;
        status = status;
    }

    public int getImageService() {
        return ImageService;
    }

    public void setImageService(int imageService) {
        ImageService = imageService;
    }

    public String getService() {
        return Service;
    }

    public void setService(String Service) {
        Service = Service;
    }

    public String getCabang() {
        return Cabang;
    }

    public void setCabang(String Cabang) {
        Cabang = Cabang;
    }

    public String getOrderID() {
        return OrderID;
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
