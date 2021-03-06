package com.example.android.sanskrittutoring;


import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FamilyFragment extends Fragment {
    MediaPlayer mediaPlayer=null;
    int check=1;
    ArrayList<Word> words = new ArrayList<Word>();
    AudioManager audioManager;
    AudioManager.OnAudioFocusChangeListener onAudioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener()
    {
        public void onAudioFocusChange(int focusChange)
        {
            if(focusChange == AudioManager.AUDIOFOCUS_GAIN)
            {
                if(mediaPlayer!=null)
                {
                    mediaPlayer.setVolume(1.0f,1.0f);
                    mediaPlayer.start();
                }
            }

            else if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT)
            {
                if(mediaPlayer!=null)
                {
                    mediaPlayer.pause();
                }
            }

            else if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK)
            {
                mediaPlayer.setVolume(0.09f,0.09f);
            }

            else if(focusChange == AudioManager.AUDIOFOCUS_LOSS)
            {
                if(mediaPlayer!=null)
                {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer=null;
                    audioManager.abandonAudioFocus(onAudioFocusChangeListener);
                }
            }

        }
    };

    MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if(mediaPlayer!=null)
            {
                mediaPlayer.release();
                audioManager.abandonAudioFocus(onAudioFocusChangeListener);
            }
        }
    };
    public FamilyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_family,container,false);
        if(check==1) {
            words.add(new Word("Mother", "Mātā (माता)", R.drawable.mother, R.raw.audio));
            words.add(new Word("Father", "Pitā (पिता)", R.drawable.father, R.raw.audio));
            words.add(new Word("Maternal Grandfather", "Mātāmahaḥ (मातामहः)", R.drawable.maternalgrandfather, R.raw.audio));
            words.add(new Word("Maternal Grandmother", "Mātāmahī (मातामही)", R.drawable.maternalgrandmother, R.raw.audio));
            words.add(new Word("Paternal Grandfather", "Pitāmahaḥ (पितामहः)", R.drawable.paternalgrandfather, R.raw.audio));
            words.add(new Word("Paternal Grandmother", "Pitāmahī (पितामही)", R.drawable.paternalgrandmother, R.raw.audio));
            words.add(new Word("Husband", "Patiḥ (पतिः)", R.drawable.husband, R.raw.audio));
            words.add(new Word("Wife", "Patnī (पत्नी)", R.drawable.wife, R.raw.audio));
            words.add(new Word("Daughter", "Putrī (पुत्री)", R.drawable.daughters, R.raw.audio));
            words.add(new Word("Son", "Putraḥ (पुत्रः)", R.drawable.son, R.raw.audio));
            words.add(new Word("Elder Brother", "Jyeṣṭhabhrātā (ज्येष्ठभ्राता)", R.drawable.elderbrother, R.raw.audio));
            words.add(new Word("Younger Brother", "Kaniṣṭhabhrātā (कनिष्ठभ्राता)", R.drawable.youngerbrother, R.raw.audio));
            words.add(new Word("Elder Sister", "Jyeṣṭhabhaginī (ज्येष्ठभगिनी)", R.drawable.eldersister, R.raw.audio));
            words.add(new Word("Younger Sister", "Kaniṣṭhabhaginī (कनिष्ठभगिनी)", R.drawable.youngersister, R.raw.audio));
            words.add(new Word("Father-in-Law", "Śvaśuraḥ (श्वशुरः)", R.drawable.fatherinlaw, R.raw.audio));
            words.add(new Word("Mother-in-Law", "Śvaśrūḥ (श्वश्रूः)", R.drawable.motherinlaw, R.raw.audio));
            words.add(new Word("Grandson", "Pautraḥ (पौत्रः)", R.drawable.grandson, R.raw.audio));
            words.add(new Word("Granddaughter", "पौत्री (Pautrī)", R.drawable.granddaughter, R.raw.audio));
            words.add(new Word("Friend", "सखा (Sakhā)", R.drawable.friend, R.raw.audio));
            check=0;
        }

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        WordAdapter wordAdapter = new WordAdapter(getActivity(), words);
        ListView listView = (ListView) rootView.findViewById(R.id.listfamily);
        listView.setAdapter(wordAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mediaPlayer != null && mediaPlayer.isPlaying())
                {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer=null;
                }
                int result = audioManager.requestAudioFocus(onAudioFocusChangeListener,
                        +                        AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                {
                    mediaPlayer = MediaPlayer.create(getActivity(), words.get(i).getMsound());
                    try {
                        mediaPlayer.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }
            }
        });
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);
        }
    }
}
