package io.github.RevyaS.observers;

import io.github.RevyaS.MainPlugin;
import io.github.RevyaS.data.GlobalData;
import io.github.RevyaS.data.types.QuestType;
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

import java.util.Optional;

//Singleton that handles block events
public class BlockObserver {
//    @Listener
//    public void blockCollision(CollideBlockEvent ev)
//    {
//        Optional<Player> pO = ev.getCause().first(Player.class);
//        if(!pO.isPresent()) return;
//        Player pp = pO.get();
//        Location<World> tLocation = ev.getTargetLocation();
//        Sponge.getServer().getBroadcastChannel().
//            send(Text.of("Collide(Player): " + (pp != null) + " @ " + tLocation.getBlockPosition()));
////            Sponge.getServer().getBroadcastChannel().send(Text.of("Collided with Block at " + tLocation));
//    }

    @Listener
    public  void changedStats(ChangeStatisticEvent.TargetPlayer ev)
    {
        if(ev.getStatistic().equals(Statistics.WALK_ONE_CM))
        {
            if(! GlobalData.getDailyQuests().containsKey(QuestType.WALK_BLOCK)) return;
            if(GlobalData.getDailyQuests().get(QuestType.WALK_BLOCK).isCompleted()) return;

            if(cmTravelled >= blockSizeInCm) {
                GlobalData.getDailyQuests().get(QuestType.WALK_BLOCK).updateProgress(1);
                blockTravelled++;
            }
            cmTravelled %= blockSizeInCm;
//            Sponge.getServer().getBroadcastChannel().
//                send(Text.of("Player Walked " + ++cmTravelled + " Blocks: " + blockTravelled));

//            //Reset if char isn't travelling
//            Task.builder().intervalTicks(20).execute( () -> {
//                Sponge.getServer().getBroadcastChannel().
//                    send(Text.of("Followup 1sec " + v + "->" + cmTrav));
//            }).submit(MainPlugin.getInstance());
        }

    }

    int blockSizeInCm = 5;
    int cmTravelled = 0;
    int blockTravelled = 0;
    int prevTrav = 0;
    boolean travelling = false;

    public static BlockObserver getInstance() {
        if(instance != null) return instance;
        instance = new BlockObserver();
        return  instance;
    }

    private BlockObserver() {}

    static BlockObserver instance;
}
