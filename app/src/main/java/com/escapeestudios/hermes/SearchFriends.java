package com.escapeestudios.hermes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchFriends extends AppCompatActivity {
    ArrayAdapter adapter;

    public static final String USERS = "users";
    public static final String NAME = "name";
    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    ArrayList<User> arrayList= new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friends);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child(USERS);

//        arrayList.add("January");
//        arrayList.add("February");
//        arrayList.add("March");
//        arrayList.add("April");
//        arrayList.add("May");
//        arrayList.add("June");
//        arrayList.add("July");
//        arrayList.add("August");
//        arrayList.add("September");
//        arrayList.add("October");
//        arrayList.add("November");
//        arrayList.add("December");

        adapter= new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);
        ListView listView = (ListView)findViewById(R.id.list_user);
        listView.setAdapter(adapter);

        SearchView searchView = (SearchView)findViewById(R.id.friends_search_bar);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });


        mDatabaseReference.orderByChild(NAME)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren())
                        {
                            User user = snapshot.getValue(User.class);
                            arrayList.add(user);
//                            Toast.makeText(SearchFriends.this, "added",Toast.LENGTH_SHORT).show();
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchFriends.this, Profile.class);
                intent.putExtra(Profile.UID, arrayList.get(position).getUid());

                startActivity(intent);
            }
        });
    }
}
