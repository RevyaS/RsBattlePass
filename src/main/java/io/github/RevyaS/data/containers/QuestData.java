package io.github.RevyaS.data.containers;

import io.github.RevyaS.data.GlobalData;
import io.github.RevyaS.data.types.QuestType;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class QuestData {
//    public QuestData(QuestDataBuilder builderData)
//    {
//        type = builderData.type;
//        description = builderData.description;
//    }

    public QuestData(QuestType type, int totalProg, int points)
    {
        this.totalProg = totalProg;
        this.type = type;
        this.points = points;
        description = getDescription();
        //Set Icon
        switch (type)
        {
            case WALK_BLOCK:
                icon = ItemTypes.GRASS;
                break;
        }
    }

    //GETTERS
    //Get Texts
    public TextableString getDescription()
    {
        switch (type)
        {
            case WALK_BLOCK:
                description = TextableString.of("Walk " + totalProg + " blocks");
                break;
            default:
                description = TextableString.of("EMPTY QUEST");
        }
        return description;
    }

    public TextableString getProgress()
    {
        String prefix = "";
        switch (type)
        {
            case WALK_BLOCK:
                prefix = " Travelled";
        }
        TextableString t = completed ? TextableString.of("&aCompleted") :
                TextableString.of("&7" + currProg + "/" + totalProg + prefix);
        return t;
    }

    public TextableString getPoints()
    {
        return TextableString.of("&7" + points + " Points");
    }

    //Get Data
    public ItemType getIcon()
    {
        return icon;
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public int getCurrProg() {return currProg;}

    @Override
    public String toString()
    {
        return type.toString() + ":" + getDescription().toString();
    }

    //SETTERS
    public void updateProgress(int addedProgress)
    {
        currProg += addedProgress;
        if(currProg >= totalProg) {
            completed = true;
            GlobalData.updateCurrPoints(points);
        }
    }



//    public static QuestDataBuilder builder() {return new QuestDataBuilder();}

    private QuestType type;
    private TextableString description;
    private int currProg = 0, totalProg, points;
    private ItemType icon; //What the icon looks like
    private boolean completed = false;

    //Builder
//    public static class QuestDataBuilder
//    {
//        QuestType type; //Required
//        String description;
//        int totalProgress;
//
//        public QuestDataBuilder(QuestType type, int totalProgress) {}
//
//        public QuestData build()
//        {
//            if(type == null) type = QuestType.NONE;
//            if(description == null) description = "";
//            return new QuestData(this);
//        }
//    }
}
