package io.github.RevyaS;

import com.google.inject.Inject;
import io.github.RevyaS.commands.BattlePass;
import io.github.RevyaS.data.containers.QuestData;
import io.github.RevyaS.factories.InventoryFactory;
import io.github.RevyaS.observers.BlockObserver;
import io.github.RevyaS.observers.InventoryObserver;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.event.world.LoadWorldEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.storage.WorldProperties;

import javax.swing.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Plugin(id = "rsbattlepass", name = "Rs Battlepass", version = "0.0.1", description = "Battlepass System")
public class MainPlugin {
    @Inject
    private Logger logger;

    @Inject
    public Game game;

    //BP Inventory Command Handler
    private BattlePass bpComm;

    private static MainPlugin mp;

    @Listener
    public void preInit(GamePreInitializationEvent ev)
    {

    }

    @Listener
    public void init(GameInitializationEvent ev)
    {
        initComm();
        //Command Specifications
        CommandSpec bpAccess = CommandSpec.builder().
                description(Text.of("Access Battlepass")).
                executor(bpComm).build();

        //Register Command Area
        game.getCommandManager().register(this, bpAccess, "battlepass", "bp");
        mp = this;

        //Register EventListeners
        game.getEventManager().registerListeners(this, InventoryObserver.getInstance());
        Sponge.getServer().getBroadcastChannel().send(Text.of("Registering BlockObserver"));
        game.getEventManager().registerListeners(this, BlockObserver.getInstance());

        //TEST AREA
//        QuestData qd = QuestData.builder().build();
//        Sponge.getServer().getBroadcastChannel().send(Text.of(qd.toString()));
    }


    @Listener
    public void postInit(GamePostInitializationEvent ev)
    {

    }

//    @Listener
//    public void invClicked(ClickInventoryEvent ev)
//    {
//        //p.sendMessage(Text.of("Clicked Inventory"));
//        logger.info("Clicked Inv Inside");
//    }

    @Listener
    public void serverStarts(GameStartedServerEvent ev)
    {
        logger.info("Rs Battlepass has started");
    }

    @Listener
    public void onServerStop(GameStoppedServerEvent ev)
    {
        logger.info("Rs Battlpass has stopped");
    }

    //Init Commands
    private void initComm()
    {
        bpComm = new BattlePass();
    }



    //Stupid workaround for Singletons
    public static MainPlugin getInstance() {
        return mp;
    }

}
