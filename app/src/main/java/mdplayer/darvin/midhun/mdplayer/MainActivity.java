package mdplayer.darvin.midhun.mdplayer;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    String NAME = "Midhun Darvin";
    String EMAIL = "midhunadarvin@gmail.com";
    int PROFILE = R.drawable.default_profile_dp;

    private Toolbar toolbar;
    String TITLES[] = {"Home","Profile","Messages","Music","Settings"};
    int ICONS[] = {R.drawable.ic_home,R.drawable.ic_profile,R.drawable.ic_message,R.drawable.ic_note1,R.drawable.ic_settings};

    RecyclerView mNavigationView;                           // Declaring RecyclerView
    RecyclerView.Adapter mNavigationAdapter;                        // Declaring Adapter For Recycler View
    RecyclerView.LayoutManager mLayoutManager;            // Declaring Layout Manager as a linear layout manager
    DrawerLayout Drawer;

    ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        mNavigationView = (RecyclerView) findViewById(R.id.RecyclerView); // Assigning the RecyclerView Object to the xml View

        mNavigationView.setHasFixedSize(true);                            // Letting the system know that the list objects are of fixed size

        mNavigationAdapter = new NavAdapter(TITLES,ICONS,NAME,EMAIL,PROFILE);       // Creating the Adapter of MyAdapter class
                                                                                    // And passing the titles,icons,header view name, header view email,
                                                                                    // and header view profile picture
        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mNavigationView.setLayoutManager(mLayoutManager);               // Setting Layout Manager to Navigation View
        mNavigationView.setAdapter(mNavigationAdapter);                 // Setting our adapter to Navigation View


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view
        mDrawerToggle = new ActionBarDrawerToggle(this,Drawer,toolbar,R.string.nav_open,R.string.nav_close){

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }

        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();               // Finally we set the drawer toggle sync State


        final GestureDetector mGestureDetector = new GestureDetector(MainActivity.this, new GestureDetector.SimpleOnGestureListener() {
                                                                    // Gesture detector to check for single taps,swipes,long taps in recycler view
            @Override public boolean onSingleTapUp(MotionEvent e) {
                return true;                                        // To get single tap events
            }

        });

        // Set an onItemTouchListener to the navigation drawer

        mNavigationView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
                View child = recyclerView.findChildViewUnder(motionEvent.getX(),motionEvent.getY());



                if(child!=null && mGestureDetector.onTouchEvent(motionEvent)){
                    //Drawer.closeDrawers();
                    Toast.makeText(MainActivity.this,"The Item Clicked is: "+recyclerView.getChildLayoutPosition(child), Toast.LENGTH_SHORT).show();
                    return true;

                }


                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });
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
}
