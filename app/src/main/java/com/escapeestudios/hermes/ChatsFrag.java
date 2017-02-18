package com.escapeestudios.hermes;


import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFrag extends Fragment {

    private SQLiteOpenHelper chatDatabaseHelper;
    private SQLiteDatabase dbChat;

    private Cursor cursorChats;

    private CursorAdapter adapter;
    private ListView listView;

    public ChatsFrag() {
        // Required empty public constructor
    }

    private void queryAgain()
    {

        cursorChats = dbChat.query(
                ChatDatabaseHelper.chatTable,
                new String[] {"_id",ChatDatabaseHelper.FRIENDNAME, ChatDatabaseHelper.FRIENDUID, ChatDatabaseHelper.LASTMESSAGE},
                null,
                null,
                null, null, ChatDatabaseHelper.LASTMESSAGETIME + " DESC"
        );
    }

    public void onResume()
    {
        super.onResume();
        queryAgain();
        adapter.changeCursor(cursorChats);
        adapter.notifyDataSetChanged();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_chats, container, false);

        listView = (ListView)view.findViewById(R.id.chat_history_list);
        try{
            chatDatabaseHelper = new ChatDatabaseHelper(getActivity().getApplicationContext());
            dbChat = chatDatabaseHelper.getReadableDatabase();

            queryAgain();
//            if(cursorMessages.moveToFirst())
//            {
            adapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_1, cursorChats,
                    new String[]{ChatDatabaseHelper.FRIENDNAME},new int[]{android.R.id.text1}, 0);
            listView.setAdapter(adapter);
//            }

        } catch (SQLiteException e)
        {
            Toast.makeText(getActivity(), "Database Unavailable", Toast.LENGTH_SHORT).show();
            Log.e("BLA",e.toString());
        }

        return view;
    }

}
