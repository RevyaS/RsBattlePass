package io.github.RevyaS.observers;

import dagger.Component;
import io.github.RevyaS.MainPlugin;
import io.github.RevyaS.data.GlobalData;
import io.github.RevyaS.factories.InventoryFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.item.inventory.InteractInventoryEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.*;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import javax.inject.Inject;
import javax.swing.*;
import java.util.*;
import java.util.function.Supplier;

//Input listener Singleton
public class InventoryObserver {
    //When Slots are clicked in Inventory
    @Listener
    public void clickedItem(ClickInventoryEvent.Primary ev)
    {
        //Get Item Display Name
        ItemStackSnapshot iss = ev.getCursorTransaction().getDefault();
        if(iss.getType().equals(ItemTypes.NONE)) return;
        Optional<Player> pCause = ev.getCause().first(Player.class);
        Player currP = pCause.get();

        ItemStack item = iss.createStack();
        Optional<Text> dnT = item.get(Keys.DISPLAY_NAME);
        if(!dnT.isPresent()) return;
        String itemName = TextSerializers.FORMATTING_CODE.serialize(dnT.get());

        //Check if itemName is registered into the Navigation List
        if(globalData.getNavList().contains(itemName))
        {
//            tSwitchInv = new Thread(this::clickedNav);
            //Setup variables to communicate
            Sponge.getServer().getBroadcastChannel().send(Text.of("Navigation Item"));
            clickedNav(itemName, currP);
            return;
        }

        //If itemName is a reward
        if(itemName.contains("Tier") && itemName.contains("Rewards") && item.getType().equals(ItemTypes.CHEST_MINECART))
        {
            Sponge.getServer().getBroadcastChannel().send(Text.of("Reward Item"));
            Slot s = ev.getSlot().get();
            //Get Tier
            itemName = itemName.replace("&aTier ", "");
            itemName = itemName.replace(" Rewards", "");
            int tier = Integer.parseInt(itemName);
//            Sponge.getServer().getBroadcastChannel().send(Text.of("Tier Clicked: " + tier));
            clickedReward(tier, s, currP);
            return;
        }

        Sponge.getServer().getBroadcastChannel().send(Text.of("No Classification"));

    }


    public void clickedReward(int tier, Slot itemSlot, Player clicker)
    {
        Task.builder().execute( () ->
        {

            Sponge.getServer().getBroadcastChannel().send(Text.of("Attempt offering item"));
            clicker.sendMessage(Text.of("Maybe it only sends via player inside tasks"));
            //Get data container
            DataContainer[] rewardsData = globalData.getRewardsMapFree().get(tier);

            //Go through the list
            for(DataContainer itemData : rewardsData)
            {
                //Create items
                ItemStack item = ItemStack.builder().fromContainer(itemData).build();
                //Give to Player
                clicker.getInventory().offer(item);
            }


            //ChangeIcon
            ItemStack newIcon = ItemStack.builder().itemType(ItemTypes.MINECART).build();
            Text iconName = TextSerializers.FORMATTING_CODE.deserialize("&aTier " + tier + " Rewards");
            newIcon.offer(Keys.DISPLAY_NAME, iconName);
            itemSlot.set(newIcon);
            Sponge.getServer().getBroadcastChannel().send(Text.of("Done offering item"));
        }).submit(MainPlugin.getInstance());
    }


    //Only Handles Clicking on buttons to switch
    public void clickedNav(String code, Player clicker)
    {
        Task.builder().execute( () ->
        {
            clicker.closeInventory();
            //Get reference
            List<String> codeList = globalData.getNavList();
            Inventory inv;
            //Obligatory if else statements because I'm using Lists
            //Mission Button

            inv = (code.equals(codeList.get(0))) ? inventoryFactory.getMissionsInv() : //Missions Button
                (code.equals(codeList.get(1))) ? inventoryFactory.getRewardsInv(currPage) : //RewardsButton
                (code.equals(codeList.get(2))) ? inventoryFactory.getRewardsInv(++currPage) : //Next Page
                (code.equals(codeList.get(3))) ? inventoryFactory.getRewardsInv(--currPage) : //Prev Page
                (code.equals(codeList.get(4))) ? inventoryFactory.getDailyMissions() : //Daily Missions
                null;
            clicker.openInventory(Objects.requireNonNull(inv));
        }).submit(MainPlugin.getInstance());
    }

    public InventoryFactory getInventoryFactory() {return inventoryFactory;}

    public GlobalData getGlobalData() {
        return globalData;
    }

    @Inject
    public InventoryObserver(InventoryFactory inventoryFactory, GlobalData globalData)
    {
        this.inventoryFactory = inventoryFactory;
        this.globalData = globalData;
    }

    //References
    private GlobalData globalData;
    private final InventoryFactory inventoryFactory;

    //Values
    private int currPage = 1;

//    Player currP; //The player performing the action
}
