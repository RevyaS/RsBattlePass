package io.github.RevyaS.data.containers;

import io.github.RevyaS.MainPlugin;
import io.github.RevyaS.data.GlobalData;
import io.github.RevyaS.data.types.QuestType;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKey;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import javax.inject.Inject;
import java.awt.*;

import static java.util.Objects.isNull;

public class QuestData {
    GlobalData globalData;

    public QuestData(QuestType type, int totalProg, int points, GlobalData globalData)
    {
        this.totalProg = totalProg;
        this.type = type;
        this.points = points;
        this.globalData = globalData;
        icon = type.getType();
    }

    //GETTERS
    //Get Texts
    public TextableString getDescription()
    {
        if(isNull(type.getType()))
            return TextableString.of(type.getPrefix());

        return TextableString.of(type.getPrefix() + " " +
            totalProg + " " + type.getSuffix());
    }

    public TextableString getProgress()
    {
        String prefix = " " + type.getProgressText();
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
            //Fire Event
            globalData.updateCurrPoints(points);
        }
    }

    public GlobalData getGlobalData() {
        return globalData;
    }

    //    public static QuestDataBuilder builder() {return new QuestDataBuilder();}

    private final QuestType type;
    private int currProg = 0;
    private final int totalProg, points;
    private final ItemType icon; //What the icon looks like
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
