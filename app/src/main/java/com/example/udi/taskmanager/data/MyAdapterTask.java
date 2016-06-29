package com.example.udi.taskmanager.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.udi.taskmanager.R;
import com.example.udi.taskmanager.data.MyTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by ort on 27/06/2016.
 */
public class MyAdapterTask extends ArrayAdapter <MyTask> {
    private final DatabaseReference reference;

    public MyAdapterTask(Context context, int resource) {
        super(context, resource); // resource = link to to XML file that holds the view we waont to use (R.layout....)
        String email= FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".","_");

        //2. connect to databasa at the email
         reference= FirebaseDatabase.getInstance().getReference(email+"/MyTasks");
        // create listner for every changr in that key

    }
    //4.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // posion = seqoantial number of the item (Task)
        // convertView = build one item from the list

       // if (convertView==null)
      //  {
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.item_my_task,parent,false); // the infaltor built java view from XML
     //   }

        // row ui
        // 5. get referance for each ui object at thr R.layout.Item_My_Task
        CheckBox chbIsCompleted=(CheckBox) convertView.findViewById(R.id.chbxIsComplated);
        TextView tvText = (TextView) convertView.findViewById(R.id.tvText);
        TextView tvDate = (TextView) convertView.findViewById(R.id.tvDate);
        ImageButton btnCall = (ImageButton) convertView.findViewById(R.id.btnCall);
        ImageButton btnLocation = (ImageButton) convertView.findViewById(R.id.btnLocation);

        //6. Data source
        final MyTask myTask=getItem(position);
        //7. set the MyTask Data to the ui (connection between the data dource and the ui
        chbIsCompleted.setChecked(myTask.isComplated());
        tvText.setText(myTask.getText());
        tvDate.setText(myTask.getCreatedAt().toString());
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // make call
            }
        });
        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //go to map
            }
        });


        chbIsCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                myTask.setComplated(isChecked);
                reference.child(myTask.getTaskKey()).setValue(myTask, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if(databaseError==null)
                        {
                            Toast.makeText(getContext(),"UpDate successful",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(getContext(),"UdDate Failed"+databaseError.getMessage(),
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }
        });


        return convertView;
    }
}
