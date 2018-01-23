package edu.oregonstate.studentlife.ihcv2;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Omeed on 1/22/18.
 */

public class Constants extends AppCompatActivity {
    private int goldThreshold = 15;
    private int silverThreshold = 10;
    private int bronzeThreshold = 7;
    private int goldColor = getResources().getColor(R.color.eventGold);
    private int silverColor = getResources().getColor(R.color.eventSilver);
    private int bronzeColor = getResources().getColor(R.color.eventBronze);

    public int getGoldThreshold() {
        return goldThreshold;
    }

    public void setGoldThreshold(int goldThreshold) {
        this.goldThreshold = goldThreshold;
    }

    public int getSilverThreshold() {
        return silverThreshold;
    }

    public void setSilverThreshold(int silverThreshold) {
        this.silverThreshold = silverThreshold;
    }

    public int getBronzeThreshold() {
        return bronzeThreshold;
    }

    public void setBronzeThreshold(int bronzeThreshold) {
        this.bronzeThreshold = bronzeThreshold;
    }

    public int getGoldColor() {
        return goldColor;
    }

    public void setGoldColor(int goldColor) {
        this.goldColor = goldColor;
    }

    public int getSilverColor() {
        return silverColor;
    }

    public void setSilverColor(int silverColor) {
        this.silverColor = silverColor;
    }

    public int getBronzeColor() {
        return bronzeColor;
    }

    public void setBronzeColor(int bronzeColor) {
        this.bronzeColor = bronzeColor;
    }
}
