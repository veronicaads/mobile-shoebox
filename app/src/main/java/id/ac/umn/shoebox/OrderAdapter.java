package id.ac.umn.shoebox;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by veronica on 5/25/18.
 */

public class OrderAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<OrderModel> models;

    public OrderAdapter(Context context, ArrayList<OrderModel> models) {
        this.context = context;
        this.models = models;
    }

    @Override
    public int getCount() {
        return models.size();
    }

    @Override
    public Object getItem(int i) {
        return models.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null){
            view=View.inflate(context, R.layout.list_order, null);
        }
        ImageView images = (ImageView) view.findViewById(R.id.img);
        TextView orderID = view.findViewById(R.id.order_id);
        TextView cabang = view.findViewById(R.id.cabang_list);
        TextView service = view.findViewById(R.id.servicelist);
       TextView inDate = view.findViewById(R.id.tanggalmasuk);
       TextView estDate = view.findViewById(R.id.tanggalestimasi);
       TextView status = view.findViewById(R.id.status);
        OrderModel model = models.get(i);

        images.setImageResource(model.getImageService());
        orderID.setText(model.getOrderID());
        cabang.setText(model.getCabang());
        service.setText(model.getService());
        inDate.setText(model.getInDate());
        estDate.setText(model.getEstDate());
        status.setText(model.getStatus());
        return view;
    }
}
