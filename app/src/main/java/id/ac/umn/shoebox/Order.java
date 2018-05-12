package id.ac.umn.shoebox;

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

    public Order(){}

    public Order(String orderid, String userEmail, String cabang, String service,
                 String subService,String merkSepatu,String image, String comment){
        this.orderId = orderid;
        this.userEmail = userEmail;
        this.cabang = service;
        this.merkSepatu = merkSepatu;
        this.image = image;
        this.comment = comment;
        this.subService = subService;
    }

    public String getOrderId(){return orderId;}
    public String getUserEmail(){return userEmail;}
    public String getCabang(){return cabang;}
    public String getService(){return service;}
    public String getSubService(){return subService;}
    public String getMerkSepatu(){return merkSepatu;}
    public String getImage(){return image;}
    public String getComment(){return comment;}

    public void setOrderId(String id){this.orderId = id;}
}
