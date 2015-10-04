package mdplayer.darvin.midhun.mdplayer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

    ViewPager viewPager;                                  //Declaring ViewPager
    FragmentPagerAdapter adapterViewPager;                //PagerAdapter for switching fragments in the main view

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);                  //Setting toolbar for the activity
        setSupportActionBar(toolbar);



        viewPager = (ViewPager) findViewById(R.id.viewPager);                  //Assigning ViewPager object to the xml
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());    //Creating new object of MyPageAdapter class
        viewPager.setAdapter(adapterViewPager);                                //Setting the adapter of viewPager




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
                View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());


                if (child != null && mGestureDetector.onTouchEvent(motionEvent)) {
                    Drawer.closeDrawers();
                    int itemClicked = recyclerView.getChildLayoutPosition(child);
                    Toast.makeText(MainActivity.this, "The Item Clicked is: " + itemClicked, Toast.LENGTH_SHORT).show();

                    switch (itemClicked) {
                        case 1:{
                            viewPager.setCurrentItem(itemClicked-1);
                            break;
                        }

                        case 2:{
                            viewPager.setCurrentItem(itemClicked-1);
                            break;
                        }

                        case 3:{
                            viewPager.setCurrentItem(itemClicked-1);
                        }

                        default:break;

                    }

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


    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new HomeFragment();
                case 1:
                    return new ProfileFragment();
                case 2:
                    return new MessageFragment();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }
}
