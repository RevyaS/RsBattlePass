package io.github.RevyaS;

import io.github.RevyaS.commands.BattlePass;
import io.github.RevyaS.data.GlobalData;
import io.github.RevyaS.data.containers.QuestData;
import io.github.RevyaS.data.types.QuestType;
import io.github.RevyaS.di.DaggerSingletonComponent;
import io.github.RevyaS.di.SingletonComponent;
import io.github.RevyaS.factories.InventoryFactory;
import io.github.RevyaS.observers.BlockObserver;
import io.github.RevyaS.observers.InventoryObserver;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MainPluginTest {

    @Test
    void isInventoryFactorySingletonForDifferentComponents() {
        //Given
        SingletonComponent comp = DaggerSingletonComponent.builder().build();
        //When
        InventoryFactory actual = comp.getInventoryObserver().getInventoryFactory();
        //Then
        InventoryFactory expected = comp.getBattlePass().getInventoryFactory();
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void isGlobalDataSingletonForDifferentComponents() {
        //Given
        SingletonComponent comp = DaggerSingletonComponent.create();
//        When
        GlobalData actual1 = comp.getInventoryObserver().getGlobalData();
        GlobalData actual2 = comp.getBattlePass().getInventoryFactory().getGlobalData();
        //Then
        GlobalData expected = comp.getBlockObserver().getGlobalData();
        assertThat(actual1).isEqualTo(expected);
        assertThat(actual2).isEqualTo(expected);
    }
}