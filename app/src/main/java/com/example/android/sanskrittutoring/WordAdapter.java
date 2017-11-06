package com.example.android.sanskrittutoring;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ohlordino on 29/9/17.
 */

public class WordAdapter extends ArrayAdapter<Word> {


//    public WordAdapter(Activity context, ArrayList<Word> word)
//    {
//        super(context,0,word);
//    }

    public WordAdapter(@NonNull Activity context, ArrayList<Word> word) {
        super(context, 0, word);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if(listItemView==null)
        {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Word currentWord = getItem(position);

        TextView englishWord = (TextView) listItemView.findViewById(R.id.english);
        englishWord.setText(currentWord.getMdefaultTranslation());

        TextView sanskritWord = (TextView) listItemView.findViewById(R.id.sanskrit);
        sanskritWord.setText(currentWord.getMsanskritTranslation());

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.tag);
        int imagetag = currentWord.getImage();
        if (imagetag==1)
        {
            imageView.setVisibility(View.GONE);
        }
        else
            imageView.setImageResource(imagetag);

        return listItemView;
    }
}
