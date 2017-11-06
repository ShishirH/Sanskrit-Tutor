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
public class PhrasesFragment extends Fragment {
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

    public PhrasesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_phrases,container,false);
        if(check==1) {
            words.add(new Word("Hello", "namo namaḥ, namaskāraḥ (नमो नमः, नमस्कारः)", R.drawable.hello, R.raw.audio));
            words.add(new Word("Good-bye", "punardarśanāya (पुनर्दर्शनाय)", R.drawable.goodbye, R.raw.audio));
            words.add(new Word("Please", "kṛpayā (कृपया)", R.drawable.please, R.raw.audio));
            words.add(new Word("Thank you (for men)", "anugṛhito'smi (अनुगृहितोऽस्मि)", R.drawable.thankyoumen, R.raw.audio));
            words.add(new Word("Thank you (for women)", "anugṛhitāsmi (अनुगृहितास्मि)", R.drawable.thankyouwomen, R.raw.color_black));
            words.add(new Word("That one", "ayameva (अयमेव)", R.drawable.thatone, R.raw.color_black));
            words.add(new Word("How much?", "kiyat (कियत्)", R.drawable.howmuch, R.raw.color_black));
            words.add(new Word("Yes", "ām (आम्)", R.drawable.yes, R.raw.color_black));
            words.add(new Word("No", "na (न)", R.drawable.no, R.raw.color_black));
            words.add(new Word("Generic toast", "shubhamastu (शुभमस्तु)", R.drawable.toast, R.raw.color_black));
            check=0;
        }

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        WordAdapter wordAdapter = new WordAdapter(getActivity(), words);
        ListView listView = (ListView) rootView.findViewById(R.id.listphrase);
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
