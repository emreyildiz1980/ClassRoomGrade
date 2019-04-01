package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static SQLiteDatabase database;
    Button addclassbtn;
    EditText newclassroomname;
    ListView listViewClassRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newclassroomname = findViewById(R.id.newclassname);
        listViewClassRoom = findViewById(R.id.listViewClassrooms);

        final ArrayList<String> className =new ArrayList<String>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,className);
        listViewClassRoom.setAdapter(arrayAdapter);

        try {
            database=this.openOrCreateDatabase("classroom",MODE_PRIVATE,null);
            database.execSQL("CREATE TABLE IF NOT EXISTS classrooms (name VARCHAR)");

            Cursor cursor =database.rawQuery("SELECT * FROM classrooms",null);

            int nameIx = cursor.getColumnIndex("name");

            cursor.moveToFirst();

            while (cursor != null){
                className.add(cursor.getString(nameIx));
                cursor.moveToNext();
                arrayAdapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            e.printStackTrace();}

        addclassbtn=findViewById(R.id.addclassbtn);


        addclassbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newClassRoom = newclassroomname.getText().toString();
                if (!newClassRoom.equalsIgnoreCase("")) {
                    try {
                        database = openOrCreateDatabase("classroom",MODE_PRIVATE,null);
                        database.execSQL("CREATE TABLE IF NOT EXISTS classrooms (name VARCHAR)");

                        String sqlString = "INSERT INTO classrooms (name) VALUES (?)";
                        SQLiteStatement statement = database.compileStatement(sqlString);
                        statement.bindString(1,newClassRoom);
                        statement.execute();

                        newclassroomname.setText("");
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }




               // Intent intent=new Intent(getApplicationContext(),StudentListActivity.class);
                //startActivity(intent);
            }
        });

       listViewClassRoom.setOnItemClickListener(new AdapterView.OnItemClickListener() {

           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               try {
                   final String name1 = parent.getItemAtPosition(position).toString();
                   try {

                       database = openOrCreateDatabase("classroom",MODE_PRIVATE,null);
                       database.execSQL("CREATE TABLE IF NOT EXISTS"+"'"+name1+"'"+"VALUES(name VARCHAR, score INTEGER)");
                       //database.execSQL("INSERT INTO"+"'"+name1+"'"+" (name,score) VALUES (name1,2100)");

                   }catch (Exception e){
                       e.printStackTrace();
                   }

                   newclassroomname.setText(name1);
                  Intent intent = new Intent(getApplicationContext(),StudentListActivity.class);
                  startActivity(intent);
               } catch (Exception e){
                   e.printStackTrace();
               }

           }


       });

        listViewClassRoom.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final String name2 = parent.getItemAtPosition(position).toString();
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete ClassRoom")
                        .setMessage("Are you sure you want to delete this ClassRoom?")

                        // Specifying a listener allows you to take an action before dismissing the dialog.
                        // The dialog is automatically dismissed when a dialog button is clicked.
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    //database.execSQL("DROP TABLE "+name2);
                                    String sqlString = "DELETE FROM classrooms WHERE name=(?)";
                                    SQLiteStatement statement = database.compileStatement(sqlString);
                                    statement.bindString(1,name2);
                                    statement.execute();

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        })

                        // A null listener allows the button to dismiss the dialog and take no further action.
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


                return true;
            }
        });





    }

    public void showalert(View v){

    }
}
