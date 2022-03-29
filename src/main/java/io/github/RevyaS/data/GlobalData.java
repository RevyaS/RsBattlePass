package io.github.RevyaS.data;

import io.github.RevyaS.data.containers.QuestData;
import io.github.RevyaS.data.types.QuestType;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.item.ItemType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalData {

    //Singleton Configurations
    private GlobalData() {
        initNavList();
        initRewardsMap();
        initQuestList();
        initTierScale();
    }

    //Init Functions
    private void initNavList()
    {
        navList = new ArrayList<String>();
        navList.add("&bMissions&7 (Click)");
        navList.add("&bRewards&7 (Click)");
        navList.add("&bNext Page &7(Click)");
        navList.add("&bPrevious Page &7(Click)");
        navList.add("&bDaily Missions &7(Click)");
    }

    private void initTierScale()
    {
        tierMax = new ArrayList<Integer>();
        tierMax.add(20);
        tierMax.add(40);
    }

    private void initRewardsMap()
    {
        rewardsMapFree = new HashMap<Integer, DataContainer[]>();
        rewardsMapPremium = new HashMap<Integer, DataContainer[]>();

        //Tier1 Rewards
        DataContainer[] dcArr = new DataContainer[2];
        //Reward 1
        DataContainer dc = DataContainer.createNew();
        dc.set(DataQuery.of("ItemType"), "minecraft:arrow");
        dc.set(DataQuery.of("Count"), 5);
        dc.set(DataQuery.of("UnsafeDamage"), 0); //Damage Taken by Item
        dcArr[0] = dc;
        //Reward 2
        dc = DataContainer.createNew();
        dc.set(DataQuery.of("ItemType"), "minecraft:diamond_shovel");
        dc.set(DataQuery.of("Count"), 1);
        dc.set(DataQuery.of("UnsafeDamage"), 1);
        dcArr[1] = dc;
        rewardsMapFree.put(1, dcArr);

        //Tier2 Rewards
        dcArr = new DataContainer[1];
        //Reward 1
        dc = DataContainer.createNew();
        dc.set(DataQuery.of("ItemType"), "minecraft:diamond_chestplate");
        dc.set(DataQuery.of("Count"), 1);
        dc.set(DataQuery.of("UnsafeDamage"), 0);
        dcArr[0] = dc;
        rewardsMapFree.put(2, dcArr);
        rewardsMapFree.put(3, dcArr);

        //Premium
        //Tier 1
        dcArr = new DataContainer[1];
        //Reward 1
        dc = DataContainer.createNew();
        dc.set(DataQuery.of("ItemType"), "minecraft:flint_and_steel");
        dc.set(DataQuery.of("Count"), 2);
        dc.set(DataQuery.of("UnsafeDamage"), 0);
        dcArr[0] = dc;
        rewardsMapPremium.put(1, dcArr);
        //Tier 3
        dcArr = new DataContainer[2];
        //Reward 1
        dc = DataContainer.createNew();
        dc.set(DataQuery.of("ItemType"), "minecraft:beef");
        dc.set(DataQuery.of("Count"), 10);
        dc.set(DataQuery.of("UnsafeDamage"), 0);
        dcArr[0] = dc;
        //Reward 2
        dc = DataContainer.createNew();
        dc.set(DataQuery.of("ItemType"), "minecraft:coal");
        dc.set(DataQuery.of("Count"), 30);
        dc.set(DataQuery.of("UnsafeDamage"), 0);
        dcArr[1] = dc;
        rewardsMapPremium.put(3, dcArr);
    }


    private void initQuestList()
    {
        dailyQuestList = new HashMap<QuestType, QuestData>();
        QuestData qd = new QuestData(QuestType.WALK_BLOCK, 10, 30);
        dailyQuestList.put(QuestType.WALK_BLOCK, qd);
    }

    public static GlobalData getInstance() {
        if(instance != null) return instance;
        instance = new GlobalData();
        return instance;
    }


    //Get Nav Map
    public static List<String> getNavList()  {
        return getInstance().navList;
    }

    //Get Max Bp
    public static  int getMaxBp() {return  getInstance().bpMax;}

    public static Map<Integer, DataContainer[]> getRewardsMapFree() {return getInstance().rewardsMapFree;}
    public static Map<Integer, DataContainer[]> getRewardsMapPremium() {return getInstance().rewardsMapPremium;}

    //Quests
    public static Map<QuestType, QuestData> getDailyQuests() {return getInstance().dailyQuestList;}

    //Tiers
    public static List<Integer> getMaxTiers() {return getInstance().tierMax;}

    //Data
    public static int getCurrTier() {return getInstance().currTier;}

    public static int getCurrPoints() {return getInstance().currPoints;}

    //Setters
    public static void updateCurrPoints(int addedPoints) {
        GlobalData instance = getInstance();
        instance.currPoints += addedPoints;
//        if(instance.currTier >= instance.tierMax.size()) return;
        while(instance.currTier < instance.tierMax.size() && instance.currPoints >= instance.tierMax.get(instance.currTier))
        {
            instance.currPoints %= instance.tierMax.get(instance.currTier);
            instance.currTier++;
        }
    }

    static GlobalData instance;
    //Global Variables
    int bpMax = 20, //Max level of BP determines the amount of pages
        currTier = 0, currPoints = 0; //Current Tier Level

    //List of Display Names that's meant for navigation
    List<String> navList;
    List<Integer> tierMax;

    Map<Integer, DataContainer[]> rewardsMapFree;
    Map<Integer, DataContainer[]> rewardsMapPremium;

    Map<QuestType, QuestData> dailyQuestList;

}

