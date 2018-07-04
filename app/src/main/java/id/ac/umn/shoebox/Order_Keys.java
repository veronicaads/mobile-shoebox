package id.ac.umn.shoebox;

/**
 * Constructor Order Keys
 */

public class Order_Keys {
    private String umn;
    private String mercubuana;

    public Order_Keys(String umn,String mercubuana){
        this.mercubuana = mercubuana;
        this.umn = umn;
    }

    public Order_Keys(){}

    public String getUmn(){return umn;}
    public String getMercubuana(){return mercubuana;}
}
