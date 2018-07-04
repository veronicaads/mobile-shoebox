package id.ac.umn.shoebox;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Adapter untuk model
 */

public class ModelAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Model> models;

    public ModelAdapter(Context context, ArrayList<Model> models) {
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
            view=View.inflate(context, R.layout.list_help, null);
        }
        ImageView images = (ImageView) view.findViewById(R.id.img);
        TextView title = view.findViewById(R.id.txt);
        Model model = models.get(i);

        images.setImageResource(model.getImageService());
        title.setText(model.getTitle());
        return view;
    }
}
