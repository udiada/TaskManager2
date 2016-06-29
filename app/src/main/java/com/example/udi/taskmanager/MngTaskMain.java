package com.example.udi.taskmanager;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.udi.taskmanager.data.MyAdapterTask;
import com.example.udi.taskmanager.data.MyTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MngTaskMain extends AppCompatActivity {

    EditText etFastAdd;
    Button btnFastAdd;
    //5. (have ti build MytaskAdapter before)
    private MyAdapterTask adapterTask;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mng_task_activity);
        //6.
        listView = (ListView)findViewById(R.id.listView);
        adapterTask = new MyAdapterTask(this,R.layout.item_my_task);
        //7.
        listView.setAdapter(adapterTask); // link between the listview and the adapter
        //1. get the key by email and replace the "."  with "_"
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","_");

        //2. connect to databasa at the email
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference(email);
             // create listner for every changr in that key
        reference.child("MyTasks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {  // dataSnapshot = picture of akll the data
                //8.
                adapterTask.clear(); // in every circle in the for we clear the task
                                     // and building from clean
                // for each child in that object:
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    MyTask maMyTask=ds.getValue(MyTask.class); // to use MyTask
                    maMyTask.setTaskKey(ds.getKey());
                    //9.  add mtTask to adapter
                    adapterTask.add(maMyTask);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        }) ;

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                Intent intent = new Intent(MngTaskMain.this,AddTaskActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
}
