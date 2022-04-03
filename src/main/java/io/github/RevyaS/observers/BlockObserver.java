package io.github.RevyaS.observers;

import io.github.RevyaS.MainPlugin;
import io.github.RevyaS.data.GlobalData;
import io.github.RevyaS.data.types.QuestType;
import io.github.RevyaS.factories.InventoryFactory;
import jdk.nashorn.internal.ir.Block;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.CollideBlockEvent;
import org.spongepowered.api.event.statistic.ChangeStatisticEvent;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.statistic.Statistics;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import javax.inject.Inject;
import java.util.Optional;

//Singleton that handles block events
public class BlockObserver {

    @Listener
    public  void changedStats(ChangeStatisticEvent.TargetPlayer ev)
    {
        if(ev.getStatistic().equals(Statistics.WALK_ONE_CM))
        {
            if(! globalData.getDailyQuests().containsKey(QuestType.WALK_BLOCK)) return;
            if(globalData.getDailyQuests().get(QuestType.WALK_BLOCK).isCompleted()) return;

            if(cmTravelled >= blockSizeInCm) {
//                Sponge.getServer().getBroadcastChannel().send(Text.of("Updating"));
                globalData.getDailyQuests().get(QuestType.WALK_BLOCK).updateProgress(1);
                blockTravelled++;
            }
            cmTravelled %= blockSizeInCm;
            cmTravelled++;
            Sponge.getServer().getBroadcastChannel().send(Text.of("Detected Movement " + cmTravelled + "/" + blockSizeInCm + ":" + blockTravelled));
//            Sponge.getServer().getBroadcastChannel().
//                send(Text.of("Player Walked " + ++cmTravelled + " Blocks: " + blockTravelled));

//            //Reset if char isn't travelling
//            Task.builder().intervalTicks(20).execute( () -> {
//                Sponge.getServer().getBroadcastChannel().
//                    send(Text.of("Followup 1sec " + v + "->" + cmTrav));
//            }).submit(MainPlugin.getInstance());
        }

    }

    public GlobalData getGlobalData() {return globalData;}

    @Inject
    public BlockObserver(GlobalData globalData) {
        this.globalData = globalData;
    }

    int blockSizeInCm = 5;
    int cmTravelled = 0;
    int blockTravelled = 0;
    int prevTrav = 0;
    boolean travelling = false;
    private GlobalData globalData;

}
