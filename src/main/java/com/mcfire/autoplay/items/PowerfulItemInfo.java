package com.mcfire.autoplay.items;

public class PowerfulItemInfo {
    // umm... it can be an item
    private int level;
    private int stage;
    private final int quality;//0 is common, 1 is rare, 2 is epic, 3 is legendary
    private final String name;

    public int getLevel(){
        return level;
    }

    public String getName(){
        return name;
    }

    public int getStage() {
        return stage;
    }

    public int getQuality() {
        return quality;
    }

    public PowerfulItemInfo(int level, int stage, int quality, String name) {
        this.level = level;
        this.stage=stage;
        this.quality=quality;
        this.name = name;
    }
    public boolean levelUp(){
        return levelUp(1);
    }
    public boolean levelUp(int amt){ // amt means amount
        if(level<100){
            level+=amt;
            return true;
        }else{
            return false;
        }
    }
    public void resetLevel(){
        level=0;
    }
    public boolean stageUp(){
        if(stage<10){
            stage++;
            return true;
        }else {
            return false;
        }
    }
}
