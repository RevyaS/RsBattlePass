package io.github.RevyaS.factories;

import io.github.RevyaS.MainPlugin;
import io.github.RevyaS.data.GlobalData;
import io.github.RevyaS.data.containers.QuestData;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.mutable.DyeableData;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.yaml.snakeyaml.serializer.Serializer;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.xml.crypto.Data;
import java.net.SocketImpl;
import java.util.*;
import java.util.function.Consumer;

@Singleton
//Factory Class that generates Inventories
public class InventoryFactory {

    //Inventory Factories
    //func runs when event occurs
    public Inventory getMainInv()
    {
        //Prepare Custom Inventory
        InventoryTitle invT = new InventoryTitle(Text.of("BattlePass"));
        InventoryDimension invD = new InventoryDimension(9, 5);
        Inventory inv = Inventory.builder().
                property("inventorytitle", invT).   //Name
                property("inventorydimension", invD). //Dimension
                //Make everything in this inventory unclickable
                listener(ClickInventoryEvent.Primary.class, (ev) -> {
                    ev.setCancelled(true);
                }). //Event
                build(MainPlugin.getInstance());

        //Quest Button
        ItemStack btn = ItemStack.builder().
                itemType(ItemTypes.ENCHANTED_BOOK).
                quantity(1).build();
        Text btnName = TextSerializers.FORMATTING_CODE.deserialize("&bMissions&7 (Click)");
        //Modify name
        btn.offer(Keys.DISPLAY_NAME, btnName);
        List<Text> lores = new ArrayList<Text>();
        lores.add(TextSerializers.FORMATTING_CODE.deserialize("&7((Some description shit))"));
        btn.offer(Keys.ITEM_LORE, lores);
        Slot btnSlot = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(6,1)));
        btnSlot.set(btn);

        //Rewards Button
        btn = ItemStack.builder().
                itemType(ItemTypes.CHEST_MINECART).
                quantity(1).build();
        //Modify name
        btnName = TextSerializers.FORMATTING_CODE.deserialize("&bRewards&7 (Click)");
        btn.offer(Keys.DISPLAY_NAME, btnName);
        btn.offer(Keys.ITEM_LORE, lores);
        btnSlot = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(2, 1)));
        btnSlot.set(btn);

        //BP Status Button
        btn = ItemStack.builder().
                itemType(ItemTypes.ANVIL).
                quantity(1).build();
        //Modify name
        btnName = TextSerializers.FORMATTING_CODE.deserialize("&bSeason 1 Battle Pass");
        btn.offer(Keys.DISPLAY_NAME, btnName);
        lores = new ArrayList<Text>();
        lores.add(TextSerializers.FORMATTING_CODE.deserialize("&7Free Battle Pass")); //Battle Pass Type
        lores.add(TextSerializers.FORMATTING_CODE.deserialize("&7Tier " + globalData.getCurrTier()));
        int currTier = globalData.getCurrTier();
        List<Integer> nextTier = globalData.getMaxTiers();
        if(currTier >= nextTier.size())
            lores.add(TextSerializers.FORMATTING_CODE.deserialize("&7Max Tier Reached"));
        else
            lores.add(TextSerializers.FORMATTING_CODE.deserialize("&7Next Tier: " + globalData.getCurrPoints() + "/" + globalData.getMaxTiers().get(currTier)));
        btn.offer(Keys.ITEM_LORE, lores);
        btnSlot = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(4, 1)));
        btnSlot.set(btn);

        //About Button
        btn = ItemStack.builder().
                itemType(ItemTypes.BOOK).
                quantity(1).build();
        //Modify name
        btnName = TextSerializers.FORMATTING_CODE.deserialize("&bAbout");
        btn.offer(Keys.DISPLAY_NAME, btnName);
        btnSlot = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(4, 3)));
        btnSlot.set(btn);

        return inv;
    }

    //Mission Inventory
    public Inventory getMissionsInv()
    {

        InventoryTitle title = new InventoryTitle(Text.of("BattlePass Missions"));
        InventoryDimension dims = new InventoryDimension(9, 3);
        Inventory inv = Inventory.builder().
                property("inventorytitle", title).
                property("inventorydimension", dims).
                listener(ClickInventoryEvent.Primary.class, (ev) -> {
                    ev.setCancelled(true);
                }).
                build(MainPlugin.getInstance());

        //Create daily missions
        ItemStack item = ItemStack.builder().itemType(ItemTypes.BOOK).build();
        Text name = TextSerializers.FORMATTING_CODE.deserialize("&bDaily Missions &7(Click)");
        item.offer(Keys.DISPLAY_NAME, name);
        Slot itemSlot = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(2,1)));
        itemSlot.set(item);

        //Create weekly missions
        item = ItemStack.builder().itemType(ItemTypes.BOOK).build();
        name = TextSerializers.FORMATTING_CODE.deserialize("&bWeekly Missions &7(Click)");
        item.offer(Keys.DISPLAY_NAME, name);
        itemSlot = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(6, 1)));
        itemSlot.set(item);

        return inv;
    }

    //Daily Missions
    public Inventory getDailyMissions()
    {
        InventoryTitle title = new InventoryTitle(Text.of("Daily Missions"));
        InventoryDimension dims = new InventoryDimension(9, 5);
        Inventory inv = Inventory.builder().
                property("inventorytitle", title).
                property("inventorydimension", dims).build(MainPlugin.getInstance());
        //Create missions
        int x = 1, y = 1;
        for(QuestData qData: globalData.getDailyQuests().values())
        {
            ItemStack quest = ItemStack.builder().itemType(qData.getIcon()).build();
            quest.offer(Keys.DISPLAY_NAME, Text.of(qData.getDescription()));
            List<Text> prog = new ArrayList<Text>();
            prog.add(qData.getPoints().getFormattedText());
            prog.add(qData.getProgress().getFormattedText());
            quest.offer(Keys.ITEM_LORE, prog);
            Slot qPos = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(x, y)));
            qPos.set(quest);

            x++;
            if(x >8) {
                x = 1;
                y++;
            }
        }

//        BlockState blk = BlockState.builder().blockType(BlockTypes.GRASS).build();
//        ItemStack blk = ItemStack.builder().itemType(ItemTypes.GRASS).build();
//        blk.offer(Keys.DISPLAY_NAME, Text.of("Walk Quest"));
//        Slot blkSlot = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(1,1)));
//        blkSlot.set(blk);

        return inv;
    }

    public Inventory getRewardsInv(int page)
    {
        int bpMax = globalData.getMaxBp(), length = 9,
            pageCount = bpMax / length + 1;
        boolean isPremium = true;

        //Create rewards
        int bpLevel = globalData.getCurrTier(),
            //Compute for page:
            toShow = (length * page > bpMax) ? bpMax % length : length,
            start = (page - 1) * length + 1,
            end = start + toShow - 1;

        InventoryTitle title = new InventoryTitle(Text.of("BattlePass " + start + "-" + end));
        InventoryDimension dims = new InventoryDimension(length, 4);
        Inventory inv = Inventory.builder().
                property("inventorytitle", title).
                property("inventorydimension", dims).
                listener(ClickInventoryEvent.Primary.class, (ev) -> {
                    ev.setCancelled(true);
                }).
                build(MainPlugin.getInstance());

        //Generate List
        for(int i = 0; i < toShow; i++)
        {
            //Generate Glass Pane
            ItemStack gp = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).build();
            //Color it
            int currLevel = (page - 1) * length + i + 1;//Compute currLevel added considering the page
            //Prepare color and name
            boolean included = (currLevel <= bpLevel); //If the current tile is included in the current bpLevel
            gp.offer(Keys.DYE_COLOR, included ? DyeColors.GREEN : DyeColors.GRAY);
            Text dName = TextSerializers.FORMATTING_CODE.deserialize(included ? ("&aTier " + currLevel + " unlocked") : "&4Tier " + currLevel + " locked");
            gp.offer(Keys.DISPLAY_NAME, dName);
            //Add to slot
            Slot panelSlot = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(i, 1)));
            panelSlot.set(gp);

            //Free Reward for this Tier
            if(!globalData.getRewardsMapFree().containsKey(currLevel)) continue;
            ItemStack reward = generateRewardItem(currLevel, included, false);
            Slot rewardPos = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(i, 0)));
            rewardPos.set(reward);

            Sponge.getServer().getBroadcastChannel().send(Text.of("Checking Premium"));
            //Premium Reward for this Tier
            if(!globalData.getRewardsMapPremium().containsKey(currLevel)) continue;
            Sponge.getServer().getBroadcastChannel().send(Text.of("Load Premium " + currLevel));
            included &= isPremium;
            reward = generateRewardItem(currLevel, included, true);
            rewardPos = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(i, 2)));
            rewardPos.set(reward);

        }

        //Generate navigation buttons
        if(page != pageCount)
        {
            ItemStack next = ItemStack.builder().itemType(ItemTypes.ARROW).build();
            Text nextName = TextSerializers.FORMATTING_CODE.deserialize("&bNext Page &7(Click)");
            next.offer(Keys.DISPLAY_NAME, nextName);
            Slot nextSlot = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(5, 3)));
            nextSlot.set(next);
        }
        if(page != 1)
        {
            ItemStack prev = ItemStack.builder().itemType(ItemTypes.ARROW).build();
            Text prevName = TextSerializers.FORMATTING_CODE.deserialize("&bPrevious Page &7(Click)");
            prev.offer(Keys.DISPLAY_NAME, prevName);
            Slot prevSlot = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(3, 3)));
            prevSlot.set(prev);
        }

        //TEST ITEM
//        ItemStack sampleCollection = ItemStack.builder().
//                itemType(ItemTypes.CHEST_MINECART).quantity(1).build();
//        Text iName = TextSerializers.FORMATTING_CODE.deserialize(("&aTier 1 Rewards"));
//        sampleCollection.offer(Keys.DISPLAY_NAME, iName);
//        List<Text> lores = new ArrayList<Text>();
//        lores.add(TextSerializers.FORMATTING_CODE.deserialize("&73x Arrows"));
//        lores.add(TextSerializers.FORMATTING_CODE.deserialize("&7Diamond Body Armor"));
//        sampleCollection.offer(Keys.ITEM_LORE, lores);
//        //Insert to Slot
//        Slot saSlot = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(0,0)));
//        saSlot.set(sampleCollection);
//        rewards = inv;
        return inv;
    }

    //Returns Item that represents rewards
    //available - if item is available
    private ItemStack generateRewardItem(int tier, boolean available, boolean isPremium)
    {
        //Generate Free rewards
        ItemStack reward = ItemStack.builder().itemType( available ? ItemTypes.CHEST_MINECART : ItemTypes.MINECART).build();
        reward.offer(Keys.DISPLAY_NAME, TextSerializers.FORMATTING_CODE.
                deserialize((available ? "&a" : "&c") + "Tier " + tier + " Rewards"));
        //Generate list
        List<Text> itemList = new ArrayList<Text>();
        DataContainer[] dc = isPremium ? globalData.getRewardsMapPremium().get(tier) : globalData.getRewardsMapFree().get(tier);

        for(DataContainer itemData : dc)
        {
            //Get Item name
            String itemName = String.valueOf(itemData.get(DataQuery.of("ItemType")).get());
            //query will result in minecraft:[Item_Name] eg: minecraft:chest_minecart
            itemName = itemName.replace("minecraft:", ""); //Remove first part
            itemName = itemName.replace("_", " "); //Replace _ with spaces
            String[] subs = itemName.split(" "); //Separate spaces
            itemName =  "";
            for(String sub : subs)
            {
                itemName += sub.substring(0,1).toUpperCase() + sub.substring(1); //Capitalize first letter
                itemName += " ";
            }

            Text itemInf = TextSerializers.FORMATTING_CODE.deserialize("&7" + itemData.get(DataQuery.of("Count")).get() + "x " + itemName);
            itemList.add(itemInf);
        }
        reward.offer(Keys.ITEM_LORE, itemList);
        return reward;
    }

    public GlobalData getGlobalData() {
        return globalData;
    }

    @Inject
    public InventoryFactory(GlobalData globalData) {
        this.globalData = globalData;
    }

    private final GlobalData globalData;
}
