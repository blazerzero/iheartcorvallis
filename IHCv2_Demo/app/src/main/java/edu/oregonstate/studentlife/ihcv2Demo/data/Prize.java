package edu.oregonstate.studentlife.ihcv2Demo.data;

/**
 * Created by Omeed on 2/12/18.
 */

public class Prize {
    private String name;
    private String level;

    public Prize(String name, String level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
