package com.example.mike4christ.journalapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.example.mike4christ.journalapp.data.DiaryContract;


/**
 * Created by Michael Alu on 26/06/18.
 */

public class DiaryCursorAdapter extends CursorAdapter {

    private TextView mTitleText,mDescriptionText ;
    private ImageView  mThumbnailImage;
    private ColorGenerator mColorGenerator = ColorGenerator.DEFAULT;
    private TextDrawable mDrawableBuilder;

    public DiaryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.diary_item, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        mTitleText = (TextView) view.findViewById(R.id.recycle_title);
        mDescriptionText=(TextView)view.findViewById(R.id.recycle_description);
        
        mThumbnailImage = (ImageView) view.findViewById(R.id.thumbnail_image);

        int titleColumnIndex = cursor.getColumnIndex(DiaryContract.DiaryEntry.KEY_TITLE);
        int descriptionColumnIndex = cursor.getColumnIndex(DiaryContract.DiaryEntry.KEY_DESCRIPTION);
        

        String title = cursor.getString(titleColumnIndex);
        String description = cursor.getString(descriptionColumnIndex);
       




        setDiaryTitle(title);


        if (description != null){
            String descriptn = description;
            mDescriptionText.setText(descriptn);

        }else{
            mDescriptionText.setText("No Description yet !");
        }
        
        


    }

    // Set reminder title view
    public void setDiaryTitle(String title) {
        mTitleText.setText(title);
        String letter = "A";

        if(title != null && !title.isEmpty()) {
            letter = title.substring(0, 1);
        }

        int color = mColorGenerator.getRandomColor();

        // Create a circular icon consisting of  a random background colour and first letter of title
        mDrawableBuilder = TextDrawable.builder()
                .buildRound(letter, color);
        mThumbnailImage.setImageDrawable(mDrawableBuilder);
    }
   
    
}
