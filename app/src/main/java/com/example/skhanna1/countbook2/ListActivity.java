package com.example.skhanna1.countbook2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;


public class ListActivity extends AppCompatActivity {

    //Declaring final variables that are used to send values to the counterWidActivity.class
    public static final String FILENAME = "file.sav";
    public static final String NAME = "nameOfCounter";
    public static final String IVal = "InitialVal";
    public static final String CVal = "CurrentVal";
    public static final String CCOM = "CounterComment";
    public static String DateOfPos = "position";
    public static final String i = "pos";

    //Declaring variables to be used inside this class
    Intent intent;
    EditText counterName;
    Button addCounterButton;
    ListView list;
    //Creating an Array of counter class
    ArrayList<Counter> counterObjArray;
    //creating an adapter used to put the array in the list
    ArrayAdapter<Counter> counterAdapter;
    Counter newCounter;
    EditText counterComment;
    EditText counterInit;
    String nameOfCounter;
    String InitVal;
    String CComment;
    Button clearButton;
    Integer ipos;
    String strEditText;
    String strEditText2;
    String strEditText3;
    TextView setName;
    TextView setCVal;
    TextView numC;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        //Get the name that the user enters in the main Screen; Doing the same for comment and
        // Initial Value user declares
        counterName = (EditText) findViewById(R.id.cname);
        counterComment = (EditText) findViewById(R.id.editText);
        counterInit = (EditText) findViewById(R.id.editText4);

        //Creating buttons that the user can use on the main screen
        addCounterButton = (Button) findViewById(R.id.button);
        clearButton = (Button)findViewById((R.id.clear));
        numC = (TextView)findViewById(R.id.total);

        //Declaring array where classes are to be stored
        counterObjArray = new ArrayList<Counter>();

        //CouterButton given function
        addCounterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting the variables that the user will pass to create the list item
                nameOfCounter = counterName.getText().toString();
                InitVal = counterInit.getText().toString();
                CComment = counterComment.getText().toString();
                //Calling create list method that will create a new counter and put it in a list
                createList(nameOfCounter,InitVal,CComment);

            }
        });
        //If you want to delete all the counters deleted button
        clearButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                //clearing the adapter values it holds
                counterAdapter.clear();
                //updating the adapter
                counterAdapter.notifyDataSetChanged();
            }
        });
    }

    public void createList(String name, String initialValue, String Comment) {
        //creating new counter
        newCounter = new Counter(name, initialValue, Comment);
        //adding the counter created to the list of counterObjarray
        counterObjArray.add(newCounter);
        //saving the values we got in file
        saveInFile();


        //Get the list ID
        list = (ListView) findViewById(R.id.counterList);

        //get the number of counters created
        numC.setText(String.valueOf(counterAdapter.getCount()));
        //allocating the counteradapter to the new custom adapter created that performs
        //allocates the value from the items of counter array and display on the screen
        counterAdapter = new CustomAdapter(ListActivity.this, R.layout.custom_list, counterObjArray);

        //put the adapter to the list
        list.setAdapter(counterAdapter);
        counterAdapter.notifyDataSetChanged();

        //perform function when user clicks on the app
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                intent = new Intent(ListActivity.this, counterWidActivity.class);
                //sending values to the counterWidActivty to use
                intent.putExtra(NAME, counterObjArray.get(position).getNameOfCounter());
                intent.putExtra(CVal, counterObjArray.get(position).getCurrentValue());
                intent.putExtra(IVal, counterObjArray.get(position).getInitValue());
                intent.putExtra(CCOM, counterObjArray.get(position).getComment());
                intent.putExtra(i, position);

                //declared so that can access class from outside the class
                ipos = position;

                //converting files into Json so that can be saved and loaded
                Gson gson = new Gson();
                String obj = gson.toJson(counterObjArray.get(position));
                intent.putExtra(DateOfPos, obj);

                //using for result to get back data also
                startActivityForResult(intent, 1);


            }
        });


        //used for deleting the counters by clicking on the date with a long press
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {
                //getting the item
                Counter item = counterAdapter.getItem(index);
                //removing the item
                counterAdapter.remove(item);
                counterAdapter.notifyDataSetChanged();
                return false;
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        //loading list onStart
        loadFile();
        //assigning the array adapter with values
        counterAdapter = new ArrayAdapter<Counter>(this, R.layout.custom_list, counterObjArray);
        counterAdapter.notifyDataSetChanged();
    }

    //saving values in the file
    public void saveInFile() {

        try {
            //opening the file
            FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            //writing on the file
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(fos));
            Gson gson = new Gson();
            gson.toJson(counterObjArray, out);
            out.flush();
            fos.close();

        }
        //catching exceptions
        catch (FileNotFoundException e) {
            throw new RuntimeException();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }


    //loading the file
    public void loadFile() {
        try {
            //reading from the file
            FileInputStream fis = openFileInput(FILENAME);
            BufferedReader in = new BufferedReader(new InputStreamReader(fis));

            Gson gson = new Gson();
            //Used for specific adapter
            Type listType = new TypeToken<ArrayList<Counter>>(){}.getType();
            //conversion
            counterObjArray = gson.fromJson(in, listType);
        } catch (FileNotFoundException e) {
            counterObjArray = new ArrayList<Counter>();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    //getting back the values from counterWidActrivity -> updating the counters
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                //Getting the values from counterWidActivity the user changed
                strEditText = data.getStringExtra("name".toString());
                //updating the counter specific values
                counterObjArray.get(ipos).setNameOfCounter(strEditText.toString());
                //displaying the change
                setName = (TextView) findViewById(R.id.Name);
                setName.setText(strEditText);

                strEditText2 = data.getStringExtra("CVal".toString());
                counterObjArray.get(ipos).setCurrentValue(strEditText2.toString());
                setCVal = (TextView) findViewById(R.id.currentVal);
                setCVal.setText(strEditText2);

                strEditText3 = data.getStringExtra("CCom".toString());
                counterObjArray.get(ipos).setComment(strEditText3.toString());

                //saving the values in file
                saveInFile();
                //updating the file
                counterAdapter.notifyDataSetChanged();
                //loading the file
                loadFile();
            }
        }
    }
}
