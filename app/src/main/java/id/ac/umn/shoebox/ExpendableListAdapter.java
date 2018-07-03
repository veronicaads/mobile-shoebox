package id.ac.umn.shoebox;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/** Class Adapter untuk Expandable List View*/
public class ExpendableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listAdapter;
    private HashMap<String, List<String>> listHashMap;

    public ExpendableListAdapter(Context context, List<String> listAdapter, HashMap<String, List<String>> listHashMap) {
        this.context = context;
        this.listAdapter = listAdapter;
        this.listHashMap = listHashMap;
    }

    @Override
    public int getGroupCount() {
        return listAdapter.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return listHashMap.get(listAdapter.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return listAdapter.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return listHashMap.get(listAdapter.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String headerTitle = (String)getGroup(i);
        if(view==null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_reclean,null);
        }
        TextView header = (TextView)view.findViewById(R.id.textView);
        header.setTypeface(null, Typeface.BOLD);
        header.setText(headerTitle);
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final String childTxt = (String)getChild(i,i1);
        if(view==null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_isireclean,null);
        }
        TextView txtListChild = (TextView)view.findViewById(R.id.isi_reclean);
        txtListChild.setText(childTxt);
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
