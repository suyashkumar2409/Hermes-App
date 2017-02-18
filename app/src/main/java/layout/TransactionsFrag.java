package layout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.escapeestudios.hermes.FriendsFrag;
import com.escapeestudios.hermes.MainActivity;
import com.escapeestudios.hermes.R;
import com.escapeestudios.hermes.UserExtra;
//import com.escapeestudios.hermes.toAdd;
import com.escapeestudios.hermes.Transaction;
//import com.escapeestudios.hermes.toAdd;
import com.escapeestudios.hermes.toAdd_2;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFrag extends Fragment {

    public static ArrayList<Transaction> arr = new ArrayList<Transaction>();

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference ref1;
    private DatabaseReference ref2;
    private View view;
    ArrayAdapter<Transaction> listAdapter;
    ValueEventListener listener_creator;
    ValueEventListener listener_acceptor;

    public TransactionsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_transactions, container, false);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child(toAdd_2.TRANSACTION);

        Button button = (Button)view.findViewById(R.id.trans_frag_transaction_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTransaction(view);
            }
        });

        if (view != null) {

            listener_creator = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Transaction trans = snap.getValue(Transaction.class);
                        arr.add(trans);
                    }
                    if (listAdapter != null)
                        listAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

            listener_acceptor = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        Transaction trans = snap.getValue(Transaction.class);
                        arr.add(trans);
                    }
                    if (listAdapter != null)
                        listAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

        }

        return view;
    }

    public void onResume()
    {
        super.onResume();
        arr.clear();

        Log.e("RESUME","work");

        ref1 = mDatabaseReference.orderByChild("creatorUID").equalTo(MainActivity.currentUser.getUid()).getRef();
//        ref2 = mDatabaseReference.orderByChild("acceptorUID").equalTo(MainActivity.currentUser.getUid()).getRef();

        ref1.addValueEventListener(listener_creator);
//        ref2.addValueEventListener(listener_acceptor);
        listAdapter = new ArrayAdapter<Transaction>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                arr);
        ListView l = (ListView)view.findViewById(R.id.trans_frag_list);
        l.setAdapter(listAdapter);
        listAdapter.notifyDataSetChanged();
    }

    public void onPause()
    {
        super.onPause();
        ref1.removeEventListener(listener_creator);
//        ref2.removeEventListener(listener_acceptor);

    }

    public void onStart() {
        super.onStart();
        view = getView();

    }

    public void addTransaction(View view) {
        Intent intent = new Intent(getActivity(), toAdd_2.class);
        startActivity(intent);
    }

}
