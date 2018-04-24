package id.ac.umn.shoebox;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListOrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order);

        ListView listView = (ListView) findViewById(R.id.listorder);
        String[] array = new String[] {"Order ID : U0003 \n Deadline : 25 April 2018 \n Status : Pending \n Level Priority : 3","Order ID : U0002 \n Deadline : 25 April 2018 \n Status : On Progress \n Level Priority : 2","Order ID : U0001 \n Deadline : 25 April 2018 \n Status : Finished \n Level Priority : 1"};
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,array);
        listView.setAdapter(arrayAdapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.admin,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
