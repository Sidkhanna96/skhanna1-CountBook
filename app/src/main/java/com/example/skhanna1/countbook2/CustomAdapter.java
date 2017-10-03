package com.example.skhanna1.countbook2;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by skhanna1 on 9/28/17.
 */

//creating a custom adapter
public class CustomAdapter extends ArrayAdapter<Counter> {

    public CustomAdapter(@NonNull Context context, int resource, ArrayList<Counter> item) {
        //Telling the adapter to put values into the custom layout
        super(context, R.layout.custom_list, item);
    }

    public CustomAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    @Override
    //View class
    public View getView(int position, View customView, ViewGroup parent) {

        if (customView == null) {
            LayoutInflater inflating = LayoutInflater.from(this.getContext());
            customView = inflating.inflate(R.layout.custom_list, null);
        }
        //getting the position of the list item that the user created
        Counter counter = getItem(position);

        //getting the ID for the elements to be displayed in textview
        TextView textName = (TextView) customView.findViewById(R.id.Name);
        TextView textDate = (TextView) customView.findViewById(R.id.Date);
        TextView textVal = (TextView) customView.findViewById(R.id.currentVal);

        //Placing the value of the counters into the xml
        textName.setText(counter.getNameOfCounter().toString());
        textDate.setText(counter.getDate().toString());
        textVal.setText(counter.getCurrentValue().toString());

        return customView;
    }
}
