package io.github.RevyaS.di;

import dagger.Component;
import io.github.RevyaS.commands.BattlePass;
import io.github.RevyaS.data.containers.QuestData;
import io.github.RevyaS.observers.BlockObserver;
import io.github.RevyaS.observers.InventoryObserver;

import javax.inject.Singleton;

@Singleton
@Component
public interface SingletonComponent {
    BattlePass getBattlePass();
    InventoryObserver getInventoryObserver();
    BlockObserver getBlockObserver();

    void inject(QuestData questData);
}
