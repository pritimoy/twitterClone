package com.example.twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayList<String> tUsers;
    private ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseInstallation.getCurrentInstallation().saveInBackground();

        listView = findViewById(R.id.listViewUserList);
        tUsers = new ArrayList<>();
        adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_checked, tUsers);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(this);

        try {
            ParseQuery<ParseUser> query = ParseUser.getQuery();
            query.whereNotEqualTo("username", ParseUser.getCurrentUser().getUsername());
            query.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {
                    if (objects.size() > 0 && e == null){
                        for (ParseUser twitterUser : objects){
                            tUsers.add(twitterUser.getUsername());
                        }
                        listView.setAdapter(adapter);
                        for (String twitterUser : tUsers){
                            if (ParseUser.getCurrentUser().getList("myfollower") != null){
                                if (ParseUser.getCurrentUser().getList("myfollower").contains(twitterUser)){
                                    listView.setItemChecked(tUsers.indexOf(twitterUser), true);
                                }
                            }

                        }
                    }
                }
            });
        }
        catch (Exception e){
            e.getMessage();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.logout_item:
                FancyToast.makeText(this, " Successfully Logged out "
                        + ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_LONG,
                        FancyToast.SUCCESS, false).show();

                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {

                        Intent intent = new Intent(MainActivity.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                });
            case R.id.sendTweetItem:
                startActivity(new Intent(MainActivity.this, SendTweet.class));
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView = (CheckedTextView) view;
        if (checkedTextView.isChecked()){
            FancyToast.makeText(MainActivity.this, tUsers.get(position) + " is now followed", FancyToast.INFO, Toast.LENGTH_SHORT, false).show();
            ParseUser.getCurrentUser().add("myfollower", tUsers.get(position));
        }else {
            FancyToast.makeText(MainActivity.this, tUsers.get(position) + " is now unfollowed", FancyToast.INFO, Toast.LENGTH_SHORT, false).show();

            ParseUser.getCurrentUser().getList("myfollower").remove(tUsers.get(position));
            List currentMyFollowerList = ParseUser.getCurrentUser().getList("myfollower");
            ParseUser.getCurrentUser().remove("myfollower");
            ParseUser.getCurrentUser().put("myfollower", currentMyFollowerList);
        }
        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    FancyToast.makeText(MainActivity.this, " Saved Changes", FancyToast.INFO, Toast.LENGTH_SHORT, false).show();
                }else FancyToast.makeText(MainActivity.this,  e.getMessage(), FancyToast.INFO, Toast.LENGTH_SHORT, false).show();
            }
        });
    }
}
