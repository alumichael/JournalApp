package com.example.mike4christ.journalapp;


import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mike4christ.journalapp.data.DiaryContract;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private FloatingActionButton mAddDiaryButton;
    private Toolbar mToolbar;
    DiaryCursorAdapter mCursorAdapter;

    ListView diaryListView;
    ProgressDialog prgDialog;
    TextView diaryText;
    String diaryTitle="";
    private RelativeLayout activity_main;


    private static final int DIARY_LOADER = 0;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        mToolbar.setTitle(R.string.app_name);


        diaryListView = (ListView) findViewById(R.id.list);
        diaryText = (TextView) findViewById(R.id.diaryText);

        activity_main = (RelativeLayout)findViewById(R.id.activity_my_main);

        //Init Firebase
        auth = FirebaseAuth.getInstance();


        View emptyView = findViewById(R.id.empty_view);
        diaryListView.setEmptyView(emptyView);

        mCursorAdapter = new DiaryCursorAdapter(this, null);
        diaryListView.setAdapter(mCursorAdapter);

        diaryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(MainActivity.this, AddDiaryActivity.class);

                Uri currentDiaryUri = ContentUris.withAppendedId(DiaryContract.DiaryEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentDiaryUri);

                startActivity(intent);

            }
        });


        mAddDiaryButton = (FloatingActionButton) findViewById(R.id.fab);



        mAddDiaryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(v.getContext(), AddDiaryActivity.class);
                //startActivity(intent);
                addDiaryTitle();
            }
        });

        getSupportLoaderManager().initLoader(DIARY_LOADER, null, this);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                DiaryContract.DiaryEntry._ID,
                DiaryContract.DiaryEntry.KEY_TITLE,
                DiaryContract.DiaryEntry.KEY_DESCRIPTION,


        };

        return new CursorLoader(this,   // Parent activity context
                DiaryContract.DiaryEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
       /* if (cursor.getCount() > 0) {

            diaryText.setVisibility(View.VISIBLE);
        } else {

            diaryText.setVisibility(View.INVISIBLE);

        }*/

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);

    }

    public void addDiaryTitle() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Diary Title");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (input.getText().toString().isEmpty()) {
                    return;
                }

                diaryTitle = input.getText().toString();
                ContentValues values = new ContentValues();

                values.put(DiaryContract.DiaryEntry.KEY_TITLE, diaryTitle);


                Uri newUri = getContentResolver().insert(DiaryContract.DiaryEntry.CONTENT_URI, values);

                restartLoader();


                if (newUri == null) {
                    Toast.makeText(getApplicationContext(), "Setting Diary Title failed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Title set successfully", Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void restartLoader() {
        getSupportLoaderManager().restartLoader(DIARY_LOADER, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_password) {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Change Password");

            final EditText new_pass = new EditText(this);
            new_pass.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(new_pass);

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (new_pass.getText().toString().isEmpty()) {
                        return;
                    }
                    changePassword(new_pass.getText().toString());


                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            return true;
        }

        if (id == R.id.log_out) {
            logoutUser();
            return true;
        }


    

        return super.onOptionsItemSelected(item);

    }
    private void changePassword(String newPassword) {
        FirebaseUser user = auth.getCurrentUser();
        user.updatePassword(newPassword).addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Snackbar snackBar = Snackbar.make(activity_main,"Password changed",Snackbar.LENGTH_LONG);
                    snackBar.show();
                }
            }
        });
    }
    private void logoutUser() {
        auth.signOut();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
        }
    }

}

