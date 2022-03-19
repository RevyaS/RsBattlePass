package io.github.RevyaS.factories;

import io.github.RevyaS.MainPlugin;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.DyeableData;
import org.spongepowered.api.data.type.DyeColor;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

//Factory Class that generates Inventories
public class InventoryFactory {

    //Inventory Factories
    //func runs when event occurs
    public static Inventory getMainInv()
    {
        if(main != null) return main;

        //Prepare Custom Inventory
        InventoryTitle invT = new InventoryTitle(Text.of("BattlePass"));
        Inventory inv = Inventory.builder().
                property("inventorytitle", invT).   //Name
                property("inventorydimension", new InventoryDimension(9, 2)). //Dimension
                //Make everything in this inventory unclickable
                listener(ClickInventoryEvent.Primary.class, (ev) -> {
                    ev.setCancelled(true);
                }). //Event
                build(MainPlugin.getInstance());

//        ItemStack custom = ItemStack.builder().itemData().build()
        ItemStack btn = ItemStack.builder().
                itemType(ItemTypes.ANVIL).
                quantity(1).build();
        //Modify name
        btn.offer(Keys.DISPLAY_NAME,
                TextSerializers.FORMATTING_CODE.deserialize("&bMissions&7 (Click)"));
//        ListValue<Text> t = new ArrayList<Text>();
        List<Text> lores = new ArrayList<Text>();
        lores.add(TextSerializers.FORMATTING_CODE.deserialize("&7((Some description shit))"));
        btn.offer(Keys.ITEM_LORE, lores);

        ItemStack btn2 = ItemStack.builder().
                itemType(ItemTypes.ANVIL).
                quantity(1).build();
        //Modify name
        Text formatted = TextSerializers.FORMATTING_CODE.deserialize("&bRewards&7 (Click)");
        btn2.offer(Keys.DISPLAY_NAME,formatted);
        btn2.offer(Keys.ITEM_LORE, lores);
//        Sponge.getServer().getBroadcastChannel().send(formatted);

        //Test  Item
        ItemStack gTile = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE).build();
        //Stupidly convoluted method of coloring items such as GLASS PANES
//        DyeableData greenDye = Sponge.getDataManager().
//                getManipulatorBuilder(DyeableData.class).
//                get().create();
//        greenDye.type().set(DyeColors.GREEN);
        gTile.offer(Keys.DYE_COLOR, DyeColors.GREEN);

        //Give name
        Slot btnSlot = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(2,1)));
        Slot btnSlot2 = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(6,1)));
        Slot btnSlot3 = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(1,1)));

        btnSlot.set(btn);
        btnSlot2.set(btn2);
        btnSlot3.set(gTile);

        main = inv;
        return inv;
    }

    //Mission Inventory
    public static Inventory getMissionsInv()
    {
        if(missions != null) return missions;

        InventoryTitle title = new InventoryTitle(Text.of("BattlePass Missions"));
        InventoryDimension dims = new InventoryDimension(9, 4);
        Inventory inv = Inventory.builder().
                property("inventorytitle", title).
                property("inventorydimension", dims).
                build(MainPlugin.getInstance());

        missions = inv;
        return inv;
    }

    public static Inventory getRewardsInv(int page)
    {
        if(rewards != null)
            if(rewards[page - 1] != null)
                return rewards[page - 1];

//        if(rewards != null) return rewards;

        int length = 9, pageCount = bpMax / length + 1;
        if(rewards == null)
            rewards = new Inventory[pageCount];

        //Create rewards
        int bpLevel = 1,
            //Compute for page:
            toShow = (length * page > bpMax) ? bpMax % length : length,
            start = (page - 1) * length + 1,
            end = start + toShow - 1;

        InventoryTitle title = new InventoryTitle(Text.of("BattlePass " + start + "-" + end));
        InventoryDimension dims = new InventoryDimension(length, 5);
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
        }

        //Generate navigation buttons
        if(page != pageCount)
        {
            ItemStack next = ItemStack.builder().itemType(ItemTypes.ARROW).build();
            Text nextName = TextSerializers.FORMATTING_CODE.deserialize("&bNext Page &7(Click)");
            next.offer(Keys.DISPLAY_NAME, nextName);
            Slot nextSlot = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(5, 4)));
            nextSlot.set(next);
        }


        if(page != 1)
        {
            ItemStack prev = ItemStack.builder().itemType(ItemTypes.ARROW).build();
            Text prevName = TextSerializers.FORMATTING_CODE.deserialize("&bPrevious Page &7(Click)");
            prev.offer(Keys.DISPLAY_NAME, prevName);
            Slot prevSlot = inv.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotPos.of(3, 4)));
            prevSlot.set(prev);
        }

        rewards[page - 1] = inv;
//        rewards = inv;
        return inv;
    }

    //Max level of BP determines the amount of pages
    static int bpMax = 20;

    //Keeps generated inventory due to issues of inventories not opening
    //Might be caused by the time it takes to generate a new one
    //So I'll just sacrifice memory for performance
    static Inventory main, missions;
//    static  Inventory rewards;
    static Inventory[] rewards;
}
