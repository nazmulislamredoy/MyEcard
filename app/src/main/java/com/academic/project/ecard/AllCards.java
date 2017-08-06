package com.academic.project.ecard;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.auction.dto.response.SignInResponse;
import com.auction.util.ACTION;
import com.auction.util.REQUEST_TYPE;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.auction.udp.BackgroundWork;
import org.json.JSONObject;

public class AllCards extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        private LinearLayout business_card_layout_1;
        private TextView tvLC1FullName, tvLC1JobTitle, tvLC1Cell, tvLC1Email, tvLC1Website, tvLC1Address;
        SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_cards);
        Toolbar toolbar = (Toolbar) findViewById(R.id.all_cards_toolbar);
        setSupportActionBar(toolbar);

        // Session Manager
        session = new SessionManager(getApplicationContext());

        tvLC1FullName = (TextView)findViewById(R.id.tv_lc1_full_name);
        tvLC1JobTitle = (TextView)findViewById(R.id.tv_lc1_job_title);
        tvLC1Cell = (TextView)findViewById(R.id.tv_lc1_cell);
        tvLC1Email = (TextView)findViewById(R.id.tv_lc1_full_email);
        tvLC1Website = (TextView)findViewById(R.id.tv_lc1_website);
        tvLC1Address = (TextView)findViewById(R.id.tv_lc1_address);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.all_cards_nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        onClickBusinessCard1ButtonListener();

        this.initTemplate();

    }

    public void initTemplate()
    {
        String sessionId = session.getSessionId();
        org.bdlions.transport.packet.PacketHeaderImpl packetHeader = new org.bdlions.transport.packet.PacketHeaderImpl();
        packetHeader.setAction(ACTION.FETCH_LOCATION_LIST);
        packetHeader.setRequestType(REQUEST_TYPE.REQUEST);
        packetHeader.setSessionId(sessionId);
        new BackgroundWork().execute(packetHeader, "{}", new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg != null)
                {
                    String stringSignInResponse = (String)msg.obj;
                    if(stringSignInResponse != null)
                    {
                        System.out.println(stringSignInResponse);
                        Gson gson = new Gson();
                        SignInResponse signInResponse = gson.fromJson(stringSignInResponse, SignInResponse.class);
                        if(signInResponse.isSuccess())
                        {
                            //set profile info into card template
                            try
                            {
                                //set profile info
                                JSONObject jsonProfileInfo  = new JSONObject(stringSignInResponse);
                                JSONObject jsonUserInfo  = new JSONObject(jsonProfileInfo.get("user").toString());
                                JSONObject jsonCompanyInfo  = new JSONObject(jsonProfileInfo.get("company").toString());
                                tvLC1FullName.setText(jsonUserInfo.get("firstName").toString()+" "+jsonUserInfo.get("lastName").toString());
                                tvLC1JobTitle.setText(jsonProfileInfo.get("designation").toString());
                                tvLC1Cell.setText(jsonUserInfo.get("cell").toString());
                                tvLC1Email.setText(jsonUserInfo.get("email").toString());
                                tvLC1Website.setText(jsonCompanyInfo.get("website").toString());
                                tvLC1Address.setText(jsonCompanyInfo.get("address").toString());
                            }
                            catch (Exception ex)
                            {

                            }
                        }
                        else
                        {
                            AlertDialog.Builder  sign_in_builder = new AlertDialog.Builder(AllCards.this);
                            sign_in_builder.setMessage(signInResponse.getMessage())
                                    .setCancelable(false)
                                    .setPositiveButton("", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });

                            AlertDialog sign_in_alert = sign_in_builder.create();
                            sign_in_alert.setTitle("Alert!!!");
                            sign_in_alert.show();

                        }
                    }
                    else
                    {
                        //go to mail page
                    }
                }
                else
                {
                    //go to mail page
                }

            }
        });
    }

    public void onClickBusinessCard1ButtonListener(){
        business_card_layout_1 = (LinearLayout)findViewById(R.id.business_card_1);
        business_card_layout_1.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent single_card_intent = new Intent(AllCards.this, SingleContact.class);
                        startActivity(single_card_intent);
                    }
                }
        );
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_cards, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent nav_profile_intent = new Intent(AllCards.this, UserProfile.class);
            startActivity(nav_profile_intent);
        } else if (id == R.id.nav_all_cards) {
            Intent nav_all_cards_intent = new Intent(AllCards.this, AllCards.class);
            startActivity(nav_all_cards_intent);
        } else if (id == R.id.nav_settings) {
            Intent nav_setting_intent = new Intent(AllCards.this, Settings.class);
            startActivity(nav_setting_intent);
        } else if (id == R.id.nav_logout) {
            //clear session
            session.logoutUser();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
