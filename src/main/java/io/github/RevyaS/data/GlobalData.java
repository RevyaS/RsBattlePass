package io.github.RevyaS.data;

import com.google.common.base.Supplier;
import io.github.RevyaS.factories.InventoryFactory;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlobalData {

    //Singleton Configurations
    private GlobalData() {
        initNavList();
        initRewardsMap();
    }

    //Init Functions
    private void initNavList()
    {
        navList = new ArrayList<String>();
        navList.add("&bMissions&7 (Click)");
        navList.add("&bRewards&7 (Click)");
        navList.add("&bNext Page &7(Click)");
        navList.add("&bPrevious Page &7(Click)");
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

    static GlobalData instance;
    //Global Variables
    int bpMax = 20; //Max level of BP determines the amount of pages


    //List of Display Names that's meant for navigation
    List<String> navList;

    Map<Integer, DataContainer[]> rewardsMapFree;
    Map<Integer, DataContainer[]> rewardsMapPremium;
}

