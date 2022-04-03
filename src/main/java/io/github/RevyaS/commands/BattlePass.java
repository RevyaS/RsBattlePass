package io.github.RevyaS.commands;

import io.github.RevyaS.MainPlugin;
import io.github.RevyaS.factories.InventoryFactory;
import jdk.jfr.internal.tool.Main;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.*;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.InventoryTitle;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;
import java.util.function.Consumer;

//Command Executors are classes that performs actions once a command is entered
public class BattlePass implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        //Check if entered by player
        if(src instanceof Player)
        {
            Player p = (Player)src;
            Text t = LiteralText.builder("You entered the command").toText();
            p.sendMessage(t); //Send message to player

            Inventory currInv = inventoryFactory.getMainInv();

            //Show inventory to player
            p.openInventory(currInv);
            return CommandResult.success();
        }

        return CommandResult.empty();
    }

    @Inject
    public BattlePass(InventoryFactory inventoryFactory) {
        this.inventoryFactory = inventoryFactory;
    }

    public InventoryFactory getInventoryFactory() {return inventoryFactory;}

    //Main Inv, Mission Inv, Reward Inv
    InventoryFactory inventoryFactory;
}
