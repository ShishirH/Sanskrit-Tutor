package com.example.android.sanskrittutoring;

import android.content.Context;
import android.media.AudioManager;
import android.media.Image;
import android.media.MediaPlayer;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.path;
import static com.example.android.sanskrittutoring.R.raw.audio;

public class PhrasesActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_phrases);
        words.add(new Word("Hello", "namo namaḥ, namaskāraḥ (नमो नमः, नमस्कारः)", R.drawable.hello, R.raw.audio));
        words.add(new Word("Good-bye", "punardarśanāya (पुनर्दर्शनाय)", R.drawable.goodbye,R.raw.audio));
        words.add(new Word("Please", "kṛpayā (कृपया)", R.drawable.please,R.raw.audio));
        words.add(new Word("Thank you (for men)", "anugṛhito'smi (अनुगृहितोऽस्मि)", R.drawable.thankyoumen,R.raw.audio));
        words.add(new Word("Thank you (for women)", "anugṛhitāsmi (अनुगृहितास्मि)", R.drawable.thankyouwomen,R.raw.color_black));
        words.add(new Word("That one", "ayameva (अयमेव)", R.drawable.thatone,R.raw.color_black));
        words.add(new Word("How much?", "kiyat (कियत्)", R.drawable.howmuch,R.raw.color_black));
        words.add(new Word("Yes", "ām (आम्)", R.drawable.yes,R.raw.color_black));
        words.add(new Word("No", "na (न)", R.drawable.no,R.raw.color_black));
        words.add(new Word("Generic toast", "shubhamastu (शुभमस्तु)", R.drawable.toast,R.raw.color_black));

        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        WordAdapter wordAdapter = new WordAdapter(this, words);
        ListView listView = (ListView) findViewById(R.id.listphrase);
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
                    mediaPlayer = MediaPlayer.create(PhrasesActivity.this, words.get(i).getMsound());
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

    /*public void audioPlayer(String path)
    {
        MediaPlayer mp = new MediaPlayer();

        try
        {
            mp.setDataSource(path);
        }
        catch (IllegalArgumentException e)
        {
            e.printStackTrace();
        }

        catch (IllegalStateException e)
        {
            e.printStackTrace();
        }

        catch (IOException e)
        {
            e.printStackTrace();
        }

        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(IllegalStateException e)
        {
            e.printStackTrace();
        }
        mp.start();

    }
}*/