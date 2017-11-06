package com.example.android.sanskrittutoring;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class ColorsActivity extends AppCompatActivity {
    @Override
    public String toString() {
        return "ColorsActivity{" +
                "mediaPlayer=" + mediaPlayer +
                ", words=" + words +
                ", audioManager=" + audioManager +
                ", onAudioFocusChangeListener=" + onAudioFocusChangeListener +
                ", onCompletionListener=" + onCompletionListener +
                '}';
    }

    MediaPlayer mediaPlayer=null;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_colors);
        words.add(new Word("Red", "लोहितः (Lohitaḥ)", R.drawable.red,R.raw.audio));
        words.add(new Word("Green", "Haritaḥ (हरितः)", R.drawable.green,R.raw.audio));
        words.add(new Word("Blue", "Nīlaḥ (नीलः)", R.drawable.blue,R.raw.audio));
        words.add(new Word("Black", "Śyāmaḥ (श्यामः)", R.drawable.blacks,R.raw.audio));
        words.add(new Word("White", "Śuklaḥ (शुक्लः)", R.drawable.white,R.raw.audio));
        words.add(new Word("Grey", "Dhūsaraḥ (धूसरः)", R.drawable.gray,R.raw.audio));
        words.add(new Word("Brown", "Śyāvaḥ (श्यावः)", R.drawable.brown,R.raw.audio));
        words.add(new Word("Pink", "Pāṭalaḥ (पाटलः)", R.drawable.pink,R.raw.audio));
        words.add(new Word("Yellow", "Pītaḥ (पीतः)", R.drawable.yellow,R.raw.audio));
        words.add(new Word("Orange", "Kausumbhaḥ (कौसुम्भः)", R.drawable.orange,R.raw.audio));
        //words.add(new Word("Crimson", "Śoṇaḥ (शोणः)", R.drawable.colors));
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        WordAdapter wordAdapter = new WordAdapter(this, words);
        ListView listView = (ListView) findViewById(R.id.listcolors);
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

                int result = 0;
                try {
                    result = audioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
                } catch (Exception e) {
                    Log.d("status", toString());
                }
                if(result==AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                {
                    mediaPlayer = MediaPlayer.create(ColorsActivity.this, words.get(i).getMsound());
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
            audioManager.abandonAudioFocus(onAudioFocusChangeListener);

        }
    }
}