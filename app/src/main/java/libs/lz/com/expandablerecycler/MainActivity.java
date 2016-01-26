package libs.lz.com.expandablerecycler;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import libs.lz.com.expandablerecyclerviewlibs.BaseTreeAdapter;

public class MainActivity extends AppCompatActivity implements BaseTreeAdapter.OnItemClickListener<TestNode> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        initRecyclerView();

    }

    private void initRecyclerView() {
        RecyclerView recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        TestAdapter adapter=new TestAdapter();

        List<TestNode> list = new ArrayList<>();

        list.add(new TestNode(1,1,true,1));
        list.add(new TestNode(11,1,false,2));
        list.add(new TestNode(12,1,false,2));
        list.add(new TestNode(13,1,true,2));
        list.add(new TestNode(131,13,false,3));
        list.add(new TestNode(132,13,false,3));
        list.add(new TestNode(133,13,false,3));
        list.add(new TestNode(2,2,false,1));
        list.add(new TestNode(3,3,true,1));
        list.add(new TestNode(31,3,false,2));
        list.add(new TestNode(32,3,false,2));
        list.add(new TestNode(33,3,true,2));
        list.add(new TestNode(331,33,false,3));
        list.add(new TestNode(4,4,false,1));
        list.add(new TestNode(5,5,false,1));
        list.add(new TestNode(6,6,false,1));


        adapter.setListener(this);




//        list.add(new TestNode(333,333,true));
//        list.add(new TestNode(8,333,false));
//        list.add(new TestNode(9,333,false));
//        list.add(new TestNode(10,333,false));
//
//
//        list.add(new TestNode(444,444,true));
//        list.add(new TestNode(11,444,true));
//        list.add(new TestNode(12,11,false));
//
//        list.add(new TestNode(13,444,true));
//        list.add(new TestNode(14,13,false));
//        list.add(new TestNode(15,13,false));
        adapter.setItems(list);
        adapter.setVisibleItems(list);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemExpand(TestNode item, int adapterPosition) {
        Snackbar.make(findViewById(R.id.recyclerView),"item : "+ item.getId()+" expand",Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onItemCollapsed(TestNode item, int adapterPosition) {
        Snackbar.make(findViewById(R.id.recyclerView),"item : "+ item.getId()+" collapsed",Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onLastTreeNodeItemClick(TestNode item, int adapterPosition) {
        Snackbar.make(findViewById(R.id.recyclerView),"item : "+ item.getId()+" clicked",Snackbar.LENGTH_SHORT).show();
    }
}
