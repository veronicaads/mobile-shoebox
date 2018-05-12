package id.ac.umn.shoebox;

/**
 * Created by miqdude on 06/05/18.
 */

public class Payment {
    private String image;
    private String comment;
    private String userEmail;
    private String orderId;
    private String paymentId;

    public Payment(){}

    public Payment(String paymentId, String userEmail, String orderId, String image, String comment){
        this.paymentId = paymentId;
        this.userEmail = userEmail;
        this.orderId = orderId;
        this.image = image;
        this.comment = comment;
    }
}
