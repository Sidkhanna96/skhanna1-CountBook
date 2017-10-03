package com.example.skhanna1.countbook2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class counterWidActivity extends AppCompatActivity {

    //Declaring the variables to be used in the program
    Button buttonAdd;
    Button buttonSub;
    EditText Name;
    EditText counter2;
    EditText comment;
    TextView CDate;
    Button buttonReset;
    Counter counter3;
    Button save;
    Intent intent2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter_wid);

        //declaring the buttons the user will use.
        buttonAdd = (Button) findViewById(R.id.button4);
        buttonSub = (Button) findViewById(R.id.button3);
        buttonReset = (Button) findViewById(R.id.button5);

        //getting the id places where the values need to be displayed on
        Name = (EditText) findViewById(R.id.naming);
        counter2 = (EditText) findViewById(R.id.editText2);
        comment = (EditText) findViewById(R.id.editText6);
        CDate = (TextView) findViewById(R.id.textView);

        //getting intent
        intent2 = getIntent();

        //converting the values back to normal form from Json
        Gson gson = new Gson();
        String object = intent2.getStringExtra(ListActivity.DateOfPos);
        Type listType = new TypeToken<Counter>() {
        }.getType();

        //Getting the object counter which the user clicked
        counter3 = gson.fromJson(object, listType);

        //setting the date
        CDate.setText(counter3.getDate());

        //getting the name the user entered; same with initial value and the comment
        String Cname = intent2.getStringExtra(ListActivity.NAME);
        final String IVal = intent2.getStringExtra(ListActivity.IVal);
        String CVal = intent2.getStringExtra(ListActivity.CVal);
        String CComment = intent2.getStringExtra(ListActivity.CCOM);

        //Setting the name to the value the user entered to be displayed same with counter and comment
        Name.setText(Cname);
        counter2.setText(CVal);
        comment.setText(CComment);

        //Incrementing the initial value
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //gets the value at the position
                String starVal = counter2.getText().toString();
                //changes to integer
                int intStarVal = Integer.parseInt(starVal);
                //changes its value
                intStarVal++;
                //setting the value
                counter2.setText(String.valueOf(intStarVal));
            }
        });

        //similar as the addbutton
        buttonSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String starVal2 = counter2.getText().toString();
                int intStarVal2 = Integer.parseInt(starVal2);
                if (intStarVal2 > 0) {
                    intStarVal2--;
                    counter2.setText(String.valueOf(intStarVal2));
                }
            }
        });

        //Resetting the value to original
        buttonReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                //getting the inital value entered bu the user
                counter2.setText(String.valueOf(IVal));
                counter3.setCurrentValue(String.valueOf(IVal));

            }
        });

        //For person to go back and save the value to be updated
        save = (Button)findViewById(R.id.button2);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent2 = new Intent();
                //updating the specific counter the user entered and updating it
                counter3.setNameOfCounter(String.valueOf(Name.getText()));
                counter3.setCurrentValue(String.valueOf(counter2.getText()));
                counter3.setComment(String.valueOf(comment.getText()));

                //sending back the data to listActivity
                intent2.putExtra("name", counter3.getNameOfCounter().toString());
                intent2.putExtra("CVal", counter3.getCurrentValue().toString());
                intent2.putExtra("CCom", counter3.getComment().toString());
                setResult(RESULT_OK, intent2);
                //finishing activity
                finish();
            }
        });
    }
}