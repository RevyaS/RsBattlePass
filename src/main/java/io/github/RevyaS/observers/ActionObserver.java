package io.github.RevyaS.observers;

import io.github.RevyaS.MainPlugin;
import io.github.RevyaS.factories.InventoryFactory;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryProperty;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import javax.swing.*;
import java.util.Optional;

//Input listener Singleton
public class ActionObserver {
    //When Slots are clicked in Inventory
    @Listener
    public void clickedInv(ClickInventoryEvent.Primary ev)
    {
        //Get Inv Name
        String invName = ev.getTargetInventory().getName().get();
        //If inv is not from BattlePass

        if(!clickedBPInv(invName))
        {
            Sponge.getServer().getBroadcastChannel().send(Text.of("Not a BP Inventory"));
            return;
        }
        //Get Player
        Optional<Player> p = ev.getCause().first(Player.class);
        currP = p.get();
        currP.sendMessage(Text.of("Clicked Inv"));

//        Sponge.getServer().getBroadcastChannel().
//            send(Text.of("Clicked by " + pS.getName() +
//                " : " + pS.getUniqueId() +
//                " : " + t));

        ItemStackSnapshot iss = ev.getCursorTransaction().getDefault();

        //Get ItemName clicked
        String code = "";
        Text t = Text.of(code);
        if(!iss.getType().equals(ItemTypes.NONE))
        {
            ItemStack item = iss.createStack();
            Optional<Text> ot = item.get(Keys.DISPLAY_NAME);
            t = ot.get();
            code = TextSerializers.FORMATTING_CODE.serialize(t);
        }
        currP.sendMessage(t);
        //Run switch inventory code separately as the inventory is still in use as long as
        //this listener method is still running
        tSwitchInv = new Thread(this::switchInv);
        invCode = code;
        tSwitchInv.start();
    }

    //Switches inventory via Task to run separately from the Listener method clickedInv()
    public void switchInv()
    {
        Task.builder().execute( () ->
            {
                Inventory inv;
                switch (invCode)
                {
                    case "&bMissions&7 (Click)":
                        inv = InventoryFactory.getMissionsInv();
                        currP.closeInventory();
                        currP.openInventory(inv);
                        break;
                    case "&bRewards&7 (Click)":
                        inv = InventoryFactory.getRewardsInv(currPage);
                        currP.closeInventory();
                        currP.openInventory(inv);
                        break;
                    case "&bNext Page &7(Click)":
                        inv = InventoryFactory.getRewardsInv(++currPage);
                        currP.closeInventory();
                        currP.openInventory(inv);
                        break;
                    case "&bPrevious Page &7(Click)":
                        inv = InventoryFactory.getRewardsInv(--currPage);
                        currP.closeInventory();
                        currP.openInventory(inv);
                        break;
                    case "":
                        break;
                    default:
                        currP.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(invCode));

                }
            }
        ).submit(MainPlugin.getInstance());
    }


    boolean clickedBPInv(String name) {
        //Names:
        return name.contains("BattlePass");

    }


    //Private constructors makes sure no other instance of this Singleton is made
    private ActionObserver()
    {}

    //To instantiate itself in case no instance already exists
    //And for all class to access this Singleton
    public static ActionObserver getInstance()
    {
        if(instance != null) return instance;
        instance = new ActionObserver();
        return instance;
    }

    //Keep an instance of itself
    static ActionObserver instance;

    //Values
    int currPage = 1;
    //Threads
    Thread tSwitchInv; //Used to run switch inventory code due to avoid inUse() issues
    String invCode; //Used for determining the new inventory
    Player currP; //The player performing the action
}
