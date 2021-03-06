package com.wadhavekar.healthme.WorkoutPackage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.DialogRedirect;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.wadhavekar.healthme.CheckBoxCheckedListener;
import com.wadhavekar.healthme.DatabaseManager;
import com.wadhavekar.healthme.R;

import java.util.ArrayList;

public class Abs extends AppCompatActivity implements WorkoutDialogBoxListener, CheckBoxCheckedListener {
    TextView absWorkout, chestW;
    EditText sets, reps;
    ArrayList<String> absList;
    ListView absListView;
    DatabaseManager databaseManager;
    ArrayList<WorkoutCheckedList> theCheckedList = new ArrayList<>();
    ArrayList<WorkoutCheckedList> myList;
    CheckBox chkbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_abs);
        absWorkout = (TextView) findViewById(R.id.chest_workout_name);
        sets = (EditText) findViewById(R.id.et_sets);
        reps = (EditText) findViewById(R.id.et_reps);
        chkbox = (CheckBox) findViewById(R.id.checkbox1);

        databaseManager = new DatabaseManager(this);

        absList = new ArrayList<>();
        absList.add("Crunches");
        absList.add("Reverse Crunches");
        absList.add("Russian Twist");
        absList.add("Tuck and Crunch");
        absList.add("Leg Raises");
        absList.add("Bicycle Crunches");

        WorkoutListAdapter adapter = new WorkoutListAdapter(this, R.layout.workout_list_adapter, absList);
        absListView = (ListView) findViewById(R.id.TheListView1);
        absListView.setAdapter(adapter);
        adapter.setCheckedListener((CheckBoxCheckedListener) this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Abs.this);
                View mview = getLayoutInflater().inflate(R.layout.activity_workout_dialog_box, null);


                final Spinner spinner = (Spinner) mview.findViewById(R.id.spinner1);
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Abs.this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.SpinnerItems));
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);

                builder.setNegativeButton("Cancel", new DialogRedirect() {
                    @Override
                    protected void redirect() {


                    }
                });


                builder.setPositiveButton("Add Workout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int count=0;
                        if (!spinner.getSelectedItem().toString().equalsIgnoreCase("Select day")) {





                            String dayWorkout = spinner.getSelectedItem().toString();
                            for (int i = 0; i < theCheckedList.size(); i++) {

                                String name = theCheckedList.get(i).getWorkoutName();
                                String set = theCheckedList.get(i).getWorkoutSets();
                                String rep = theCheckedList.get(i).getWorkoutReps();
                                Toast.makeText(Abs.this, "Hello" + theCheckedList.size(), Toast.LENGTH_LONG).show();


                                //Toast.makeText(Chest.this, name + " " + set+ " "+ rep, Toast.LENGTH_SHORT).show();


                                boolean insert = databaseManager.addWorkoutData(name,set,rep,dayWorkout);
                                if (insert) {
                                    count++;
                                }
                                else {

                                }
                            }

                            if (count == theCheckedList.size()){
                                Toast.makeText(Abs.this, "Successfully added to "+ spinner.getSelectedItem().toString() + "'s workout", Toast.LENGTH_LONG).show();
                            }
                            else{
                                Toast.makeText(Abs.this, "Something went wrong", Toast.LENGTH_LONG).show();
                            }
                        }



                    }
                });
                builder.setView(mview);
                AlertDialog dialog = builder.create();
                dialog.show();


            }
        });


    }


    @Override
    public void applyText(String dayName) {

    }


    @Override
    public void getCheckBoxCheckedListener(int position) {
        ListView l = (ListView) findViewById(R.id.TheListView1);
        EditText e_s;
        EditText e_r;
        CheckBox c;

        TextView chestWo = l.getChildAt(position).findViewById(R.id.chest_workout_name);
        e_s = l.getChildAt(position).findViewById(R.id.et_sets);
        e_r = l.getChildAt(position).findViewById(R.id.et_reps);
        c = l.getChildAt(position).findViewById(R.id.checkbox1);

        if (c.isChecked() == true) {
            if (e_r.getText().toString().equals("") || e_s.getText().toString().equals("")) {
                c.setChecked(false);
                Toast.makeText(this, "Please enter the number of sets and reps", Toast.LENGTH_LONG).show();


            } else {
                int val = -1;
                c.setEnabled(true);
                String name = chestWo.getText().toString();
                String set = e_s.getText().toString();
                String rep = e_r.getText().toString();

                WorkoutCheckedList w = new WorkoutCheckedList(name, set, rep);

                for (int j = 0; j < theCheckedList.size(); j++) {

                    if (name.equals(theCheckedList.get(j).getWorkoutName()) && set.equals(theCheckedList.get(j).getWorkoutSets()) && rep.equals(theCheckedList.get(j).getWorkoutReps()) ) {
                        val = j;
                        break;
                    }
                }

                if (val == -1) {
                    theCheckedList.add(w);
                    //Toast.makeText(this, "Added to list successfully " + e_r.getText().toString() + " 999", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "Already added", Toast.LENGTH_LONG).show();
                }
            }
        } else if (c.isChecked() == false) {


            int value = -1;

            String name = chestWo.getText().toString();
            String set = e_s.getText().toString();
            String rep = e_r.getText().toString();


            WorkoutCheckedList w = new WorkoutCheckedList(name, set, rep);
            for (int j = 0; j < theCheckedList.size(); j++) {

                if (name.equals(theCheckedList.get(j).getWorkoutName()) && set.equals(theCheckedList.get(j).getWorkoutSets()) && rep.equals(theCheckedList.get(j).getWorkoutReps()) ) {
                    value = j;
                    break;
                }



            }



            if (value != -1) {
                theCheckedList.remove(theCheckedList.get(value));
                //Toast.makeText(this, "Removed from list successfully ", Toast.LENGTH_SHORT).show();

            }




        }
    }
}




