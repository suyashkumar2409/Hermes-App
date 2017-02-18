package layout;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.escapeestudios.hermes.R;
import com.escapeestudios.hermes.toAdd;
import com.escapeestudios.hermes.Transaction;
import com.escapeestudios.hermes.toAdd;
import com.escapeestudios.hermes.toAdd_2;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransactionsFrag extends Fragment {

    public static ArrayList<Transaction> arr = new ArrayList<Transaction>();

    public TransactionsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_transactions, container, false);

        Button button = (Button)view.findViewById(R.id.trans_frag_transaction_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTransaction(view);
            }
        });
        return view;
    }

    public void onStart() {
        super.onStart();
        View view = getView();

        if (view != null) {
            ArrayAdapter<Transaction> listAdapter = new ArrayAdapter<Transaction>(
                    getActivity(),
                    android.R.layout.simple_list_item_1,
                    arr);
            ListView l = (ListView)view.findViewById(R.id.trans_frag_list);
            l.setAdapter(listAdapter);
        }
    }

    public void addTransaction(View view) {
        Intent intent = new Intent(getActivity(), toAdd_2.class);
        startActivity(intent);
    }

}
