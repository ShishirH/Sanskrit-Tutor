package com.example.android.sanskrittutoring;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class NumbersActivity extends AppCompatActivity {
    ArrayList<Word> words = new ArrayList<Word>();
    AudioManager audioManager;
    MediaPlayer mediaPlayer=null;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);
        words.add(new Word("zero", "śūnya (शून्य)", R.drawable.zero,R.raw.audio));
        words.add(new Word("one", "eka (एक)", R.drawable.one,R.raw.audio));
        words.add(new Word("two", "dvi (द्वि)", R.drawable.two,R.raw.audio));
        words.add(new Word("three", "tri (त्रि)", R.drawable.three,R.raw.audio));
        words.add(new Word("four", "catur (चतुर्)", R.drawable.four,R.raw.audio));
        words.add(new Word("five", "pañca (पञ्चन्)", R.drawable.five,R.raw.audio));
        words.add(new Word("six", "ṣaṭh (षट्)", R.drawable.six,R.raw.audio));
        words.add(new Word("seven", "sapta (सप्त)", R.drawable.seven,R.raw.audio));
        words.add(new Word("eight", "aṣṭa (अष्ट)", R.drawable.eight,R.raw.audio));
        words.add(new Word("nine", "nava (नव)", R.drawable.nine,R.raw.audio));
        words.add(new Word("ten", "daśa (दश)", R.drawable.ten,R.raw.audio));

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        WordAdapter wordAdapter = new WordAdapter(this, words);
        ListView listView = (ListView) findViewById(R.id.list);
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
                    mediaPlayer = MediaPlayer.create(NumbersActivity.this, words.get(i).getMsound());
                    try {
                        mediaPlayer.start();
                    } catch (IllegalStateException e) {
                        e.printStackTrace();
                    }
                    mediaPlayer.setOnCompletionListener(onCompletionListener);
                }

            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        audioManager.abandonAudioFocus(onAudioFocusChangeListener);
    }



}