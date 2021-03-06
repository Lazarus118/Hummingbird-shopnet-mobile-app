package co.aulatech.e_portal_auth;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    private static final String PHOTOSERVICE_URL = "https://raw.githubusercontent.com/Lazarus118/Budgeat_Rand_Image_src/master/file.json";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TOOLBAR LOGIC
        // -----------------------------------------------------------------
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // DRAWER LOGIC
        // ------------------------------------------------------------------
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // TAB SCROLL LOGIC
        // ------------------------------------------------------------------
        TabHost host = (TabHost) findViewById(R.id.tabHost);
        host.setup();
        // Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Packages");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Packages");
        host.addTab(spec);
        // Tab 2
        spec = host.newTabSpec("Pre-alerts");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Pre-alerts");
        host.addTab(spec);
        // Tab 3
        spec = host.newTabSpec("Calculator");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Calculator");
        host.addTab(spec);
        // Tab 4
        spec = host.newTabSpec("More");
        spec.setContent(R.id.tab4);
        spec.setIndicator("More");
        host.addTab(spec);
        // TAB TITLE SIZE
        TabWidget tw = (TabWidget)host.findViewById(android.R.id.tabs);
        View tabView = tw.getChildTabViewAt(0);
        View tabView1 = tw.getChildTabViewAt(1);
        View tabView2 = tw.getChildTabViewAt(2);
        View tabView3 = tw.getChildTabViewAt(3);
        TextView tv = (TextView)tabView.findViewById(android.R.id.title);
        TextView tv1 = (TextView)tabView1.findViewById(android.R.id.title);
        TextView tv2 = (TextView)tabView2.findViewById(android.R.id.title);
        TextView tv3 = (TextView)tabView3.findViewById(android.R.id.title);
        tv.setTextSize(10);
        tv1.setTextSize(10);
        tv2.setTextSize(10);
        tv3.setTextSize(10);
        // EXPANDABLE LIST VIEW
        // ------------------------------------------------------------------
        expListView = (ExpandableListView) findViewById(R.id.lvExp); // get the listview
        prepareListData_1(); // preparing list data
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter); // setting list adapter
        // PRE_ALERTS
        // ------------------------------------------------------------------
        ListView listView = (ListView) findViewById(R.id.pre_alerts_list);
        // Array of strings...
        String[] values = new String[]
                { "Your 'Cell phone' is now ready",
                "Your 'Man's shirt' is now ready",
                "Your 'Leggings' is now ready"
                };
        ArrayAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, values);
        listView.setAdapter(adapter);
        // CALCULATOR
        // ------------------------------------------------------------------
        Spinner dropdown = (Spinner) findViewById(R.id.spinner);
        String[] items = new String[]
                {"Select item...", "Acoustic Guitar - (29.95%)", "Baby Pampers - (15.00%)"};
        ArrayAdapter<String> spinner_item = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        // dropdown.setBackgroundColor(Color.parseColor("#e9a825"));
        dropdown.setBackground(getResources().getDrawable(R.drawable.circular_background));
        dropdown.setAdapter(spinner_item);
        // ********************************************************* //
        Spinner dropdown2 = (Spinner) findViewById(R.id.spinner2);
        String[] items2 = new String[]
                {"Select item...", "Employee Account"};
        ArrayAdapter<String> spinner_item2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items2);
        // dropdown2.setBackgroundColor(Color.parseColor("#e9a825"));
        dropdown2.setBackground(getResources().getDrawable(R.drawable.circular_background));
        dropdown2.setAdapter(spinner_item2);
        // ********************************************************* //
        Button submit = (Button) findViewById(R.id.calculator_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText value = (EditText) findViewById(R.id.value);
                value.setText("This is just a demo " + "\uD83D\uDE0F");
            }
        });
        // IMAGE SLIDER
        // ------------------------------------------------------------------
        new LoadImage().execute(PHOTOSERVICE_URL);
        ImageButton next = (ImageButton)findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadImage().execute(PHOTOSERVICE_URL);
            }
        });
        ImageButton previous = (ImageButton)findViewById(R.id.previous);
        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LoadImage().execute(PHOTOSERVICE_URL);
            }
        });
    }

    /**********************************************************************************
     * Preparing the list data
     *********************************************************************************/
    private void prepareListData_1() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<>();

        listDataHeader.add("Instant Pot IP-DUO60 7-in-1 Multi-Functional Pressure Cooker"); // Adding child data
        listDataHeader.add("Farktop Women's Plus Size Casual Loose V Neck Chiffon T Shirt Blouse Tops");
        listDataHeader.add("(Pack of 2) Screen Protector For PS Vita 1000, Akwox Premium HD Clear 9H Tempered Glass Screen Protective Film For Sony PlayStation Vita PSV 1000-Max Clarity And Touch Accuracy Film");
        listDataHeader.add("Metal Wire Rack Stand Kitchen Cookware Steamer 5 5/8 Inch");
        listDataHeader.add("Blackhead Remover Mask");
        listDataHeader.add("Uncharted: Golden Abyss - PlayStation Vita");

        List<String> item1 = new ArrayList<>(); // Adding child data
        item1.add("Delivered: Yes");
        item1.add("Created Date: 2017-03-10 09:30:10");
        item1.add("Last Updated:  2017-03-10 09:30:10");
        item1.add("Courier: Post Office");
        item1.add("Tracking: 2454430001443431");
        item1.add("Sender: Amazon");
        item1.add("Charge: USD $99.99");

        List<String> item2 = new ArrayList<>(); // Adding child data
        item2.add("Delivered: Yes");
        item2.add("Created Date: 2017-03-10 09:30:10");
        item2.add("Last Updated:  2017-03-10 09:30:10");
        item2.add("Courier: Post Office");
        item2.add("Tracking: 2454430001443431");
        item2.add("Sender: Amazon");
        item2.add("Charge: USD $99.99");

        List<String> item3 = new ArrayList<>(); // Adding child data
        item3.add("Delivered: Yes");
        item3.add("Created Date: 2017-03-10 09:30:10");
        item3.add("Last Updated:  2017-03-10 09:30:10");
        item3.add("Courier: Post Office");
        item3.add("Tracking: 2454430001443431");
        item3.add("Sender: Amazon");
        item3.add("Charge: USD $99.99");

        List<String> item4 = new ArrayList<>(); // Adding child data
        item4.add("Delivered: Yes");
        item4.add("Created Date: 2017-03-10 09:30:10");
        item4.add("Last Updated:  2017-03-10 09:30:10");
        item4.add("Courier: Post Office");
        item4.add("Tracking: 2454430001443431");
        item4.add("Sender: Amazon");
        item4.add("Charge: USD $99.99");

        List<String> item5 = new ArrayList<>(); // Adding child data
        item5.add("Delivered: Yes");
        item5.add("Created Date: 2017-03-10 09:30:10");
        item5.add("Last Updated:  2017-03-10 09:30:10");
        item5.add("Courier: Post Office");
        item5.add("Tracking: 2454430001443431");
        item5.add("Sender: Amazon");
        item5.add("Charge: USD $99.99");

        List<String> item6 = new ArrayList<>(); // Adding child data
        item6.add("Delivered: Yes");
        item6.add("Created Date: 2017-03-10 09:30:10");
        item6.add("Last Updated:  2017-03-10 09:30:10");
        item6.add("Courier: Post Office");
        item6.add("Tracking: 2454430001443431");
        item6.add("Sender: Amazon");
        item6.add("Charge: USD $99.99");

        listDataChild.put(listDataHeader.get(0), item1); // Header, Child data
        listDataChild.put(listDataHeader.get(1), item2);
        listDataChild.put(listDataHeader.get(2), item3);
        listDataChild.put(listDataHeader.get(3), item4);
        listDataChild.put(listDataHeader.get(4), item5);
        listDataChild.put(listDataHeader.get(5), item6);
    }

    /**********************************************************************************
     * CONTACT US CLICK LOGIC
     *********************************************************************************/
    public void email_click(View view) {
        String to_email = "shopnet@hum-air.com";
        String subject = "inQuery from Customer: Monique";
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{to_email});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        // need this to prompts email client only
        // --------------------------------------
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
    public void whatsapp_click(View view) {
        int number = 2752371;
        Uri uri = Uri.parse("smsto:" + number);
        Intent i = new Intent(Intent.ACTION_SENDTO, uri);
        i.setPackage("com.whatsapp");
        startActivity(i);
    }
    public void call_click(View view) {
        String PhoneNum = "7674492102";
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:"+Uri.encode(PhoneNum.trim())));
        startActivity(callIntent);
    }

    /**********************************************************************************
     * BUTTON LOGIC
     *********************************************************************************/
    public void button_clickable() {
//        active_allowances_deductions = (Button) findViewById(R.id.allowances_deduction_btn);
//        leave_summary = (Button) findViewById(R.id.leave_summary_btn);
//        leave_details = (Button) findViewById(R.id.leave_details_btn);
//        vacation_leave_as_it = (Button) findViewById(R.id.vacation_leave_as_at_btn);
//        vacation_leave_forecast = (Button) findViewById(R.id.vacation_leave_forecast_btn);
//        submit_leave = (Button) findViewById(R.id.submit_leave_btn);
//        list_of_eadrs = (Button) findViewById(R.id.list_of_my_eadrs_btn);
//
//        active_allowances_deductions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent aad = new Intent(MainActivity.this, ListActivity.class);
//                String intent_to_list = "aad";
//                aad.putExtra("layout_info", intent_to_list);
//                MainActivity.this.startActivity(aad);
//            }
//        });
//        leave_summary.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent ls = new Intent(MainActivity.this, ListActivity.class);
//                String intent_to_list = "ls";
//                ls.putExtra("layout_info", intent_to_list);
//                MainActivity.this.startActivity(ls);
//            }
//        });
//        leave_details.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        vacation_leave_as_it.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        vacation_leave_forecast.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        submit_leave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        list_of_eadrs.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent eadrs = new Intent(MainActivity.this, ListActivity.class);
//                String intent_to_list = "eadrs";
//                eadrs.putExtra("layout_info", intent_to_list);
//                MainActivity.this.startActivity(eadrs);
//            }
//        });
    }

    /**********************************************************************************
     * ABOUT DIALOG
     *********************************************************************************/
    public void about_dialog() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.activity_about_dialog, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

        // set activity_prompt_name.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // get user input and set it to result
                                // edit text
                                // result.setText(userInput.getText());
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }

    /**********************************************************************************
     * DRAWER ITEMS
     *********************************************************************************/
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.settings) {
            // Handle the settings action
        } else if (id == R.id.about) {
            // Handle the about action
            about_dialog();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**********************************************************************************
     * IMAGE FETCH
     *********************************************************************************/
    private class LoadImage extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Fetching", Toast.LENGTH_LONG).show();
        }

        protected Bitmap doInBackground(String... args) {
            InputStream inputStream = null;
            String result = "";

            try {
                URL url = new URL(args[0]);
                HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
                if (httpCon.getResponseCode() != 200)
                    throw new Exception("Failed to connect");

                inputStream = httpCon.getInputStream();

                // convert inputstream to string
                if (inputStream != null)
                    result = convertInputStreamToString(inputStream);
                else
                    result = "Did not work!";

                if (result != null)
                    System.out.println(result);

                // convert String to JSONObject
                JSONObject json = new JSONObject(result);

                // get the array of photos
                JSONArray imageJSON = json.getJSONArray("Album");
                int index = imageJSON.length() - 1;
                Random rand = new Random();

                // lets get a randomly pic a picture to load
                int rindex = rand.nextInt((index) + 1);
                System.out.println(rindex);

                // return image;
                return BitmapFactory.decodeStream(new URL(imageJSON.getJSONObject(rindex).getString("url")).openStream());

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            return null;
        }


        protected void onPostExecute(Bitmap image) {
            if (image != null) {
                ImageView id_index_gallery_item_image = (ImageView) findViewById(R.id.id_index_gallery_item_image);
                id_index_gallery_item_image.setImageBitmap(image);

            } else {
                Toast.makeText(MainActivity.this, "Image Does Not exist or Network Error", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

}
