package com.example.android.sanskrittutoring;

/**
 * Created by ohlordino on 29/9/17.
 */

public class Word {
    private String mdefaultTranslation, msanskritTranslation;
    private int mimage;
    private int msound;

    public int getMsound() {
        return msound;
    }

    public Word(String mdefaultTranslation, String msanskritTranslation, int image, int sound) {
        this.mdefaultTranslation = mdefaultTranslation;
        this.msanskritTranslation = msanskritTranslation;
        this.mimage = image;
        this.msound=sound;
    }

    public String getMdefaultTranslation() {
        return mdefaultTranslation;
    }

    public String getMsanskritTranslation() {
        return msanskritTranslation;
    }

    public int getImage() {
        return mimage;
    }
}
