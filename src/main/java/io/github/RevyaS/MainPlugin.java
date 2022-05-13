package io.github.RevyaS;

import com.google.inject.Inject;
import io.github.RevyaS.commands.BattlePass;
import io.github.RevyaS.di.DaggerSingletonComponent;
import io.github.RevyaS.di.SingletonComponent;
import io.github.RevyaS.observers.BlockObserver;
import io.github.RevyaS.observers.InventoryObserver;
import jdk.nashorn.internal.ir.Block;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.*;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;


@Plugin(id = "rsbattlepass", name = "Rs Battlepass", version = "0.0.1", description = "Battlepass System")
public class MainPlugin {
    @Inject
    public Game game;

    //BP Inventory Command Handler
//    private BattlePass bpComm;
    SingletonComponent singletonComponent;

    private static MainPlugin mp;

    @Listener
    public void preInit(GamePreInitializationEvent ev)
    {

    }

//    public String testInject() {
//        BattlePassSingleton comp = DaggerBattlePassSingleton.create();
//        comp.inject(this);
//        return comp1 + " created with " + comp2;
//    }

    @Listener
    public void init(GameInitializationEvent ev)
    {
        initDI();
        BattlePass bpComm = singletonComponent.getBattlePass();

        //Command Specifications
        CommandSpec bpAccess = CommandSpec.builder().
                description(Text.of("Access Battlepass")).
                executor(bpComm).build();

        //Register Command Area
        game.getCommandManager().register(this, bpAccess, "battlepass", "bp");
        mp = this;

        InventoryObserver invObs = singletonComponent.getInventoryObserver();
        //Register EventListeners
        game.getEventManager().registerListeners(this, invObs);
        Sponge.getServer().getBroadcastChannel().send(Text.of("Registering BlockObserver"));
        BlockObserver blkObs = singletonComponent.getBlockObserver();
        game.getEventManager().registerListeners(this, blkObs);

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

//    @Listener
//    public void serverStarts(GameStartedServerEvent ev)
//    {
//        logger.info("Rs Battlepass has started");
//    }
//
//    @Listener
//    public void onServerStop(GameStoppedServerEvent ev)
//    {
//        logger.info("Rs Battlpass has stopped");
//    }

    //Init Commands
    private void initDI()
    {
        singletonComponent = DaggerSingletonComponent.create();

    }



    //Stupid workaround for Singletons
    public static MainPlugin getInstance() {
        return mp;
    }

}
