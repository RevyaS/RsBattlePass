package io.github.RevyaS.data.containers;

//import io.github.RevyaS.DAGTest.*;
import io.github.RevyaS.DAGTest.Car;
import io.github.RevyaS.DAGTest.DaggerCarComponent;
import io.github.RevyaS.data.GlobalData;
import io.github.RevyaS.data.types.QuestType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.easymock.PowerMock;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.FormattingCodeTextSerializer;
import org.spongepowered.api.text.serializer.TextSerializers;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.powermock.api.easymock.PowerMock.mockStatic;
import static org.powermock.api.easymock.PowerMock.verify;

//Statics used
//@RunWith(PowerMockRunner.class)
//@PrepareForTest({FormattingCodeTextSerializer.class,
//        TextSerializers.class})
class QuestDataTest {
    @Mock
    GlobalData globalData;
    AutoCloseable autoCloseable;
    private QuestData underTest;

    @BeforeEach
    public void setup() {
        autoCloseable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    public void teardown() throws Exception{
        autoCloseable.close();
    }

    //QuestData.getDescription()
    @Test
    void isDescriptionCorrectWhenQuestTypeIsWalkingBlocks() {
        //given
        underTest = new QuestData(QuestType.WALK_BLOCK, 20, 10, globalData);
        //when
        TextableString desc = underTest.getDescription();
        //then
        assertThat(desc.toString()).startsWith("Walk").endsWith("blocks");
    }

    @Test
    void isDescriptionCorrectWhenQuestTypeIsNone() {
        //given
        underTest = new QuestData(QuestType.NONE, 20, 10, globalData);
        //when
        TextableString desc = underTest.getDescription();
        //then
        assertThat(desc.toString()).isEqualTo("EMPTY QUEST");
    }

    @Test
    void isDescriptionPresentingCorrectTotalProgressWhenQuestTypeIsWalkingBlocks() {
        //given
        underTest = new QuestData(QuestType.WALK_BLOCK, 20, 10, globalData);
        //when
        TextableString actual = underTest.getDescription();
        //then
        TextableString expected = TextableString.of("Walk 20 blocks");
        assertThat(actual.toString()).isEqualTo(expected.toString());
    }

    //QuestData.getProgress()
    @Test
    void isProgressUpdatedButIncomplete() {
        //given
        underTest = new QuestData(QuestType.WALK_BLOCK, 20, 20, globalData);
        underTest.updateProgress(10);
        //when
        TextableString actual = underTest.getProgress();
        //then
        TextableString expected = TextableString.of("&710/20 Travelled");
        assertThat(actual.toString()).isEqualTo(expected.toString());
    }

    @Test
    void isProgressUpdatedButCompleted() {
        //given
        underTest = new QuestData(QuestType.WALK_BLOCK, 20, 20, globalData);
        underTest.updateProgress(20);
        //when
        TextableString actual = underTest.getProgress();
        //then
        TextableString expected = TextableString.of("&aCompleted");
        assertThat(actual.toString()).isEqualTo(expected.toString());
    }

    @Test
    void isProgressUpdatedButExcessive() {
        //given
        underTest = new QuestData(QuestType.WALK_BLOCK, 20, 20, globalData);
        underTest.updateProgress(30);
        //when
        TextableString actual = underTest.getProgress();
        //then
        TextableString expected = TextableString.of("&aCompleted");
        assertThat(actual.toString()).isEqualTo(expected.toString());
    }

    //QuestData.getPoints()
    @Test
    void isPointsTextCorrect() {
        //given
        underTest = new QuestData(QuestType.WALK_BLOCK, 20, 30, globalData);
        //when
        TextableString actual = underTest.getPoints();
        //then
        TextableString expected = TextableString.of("&730 Points");
        assertThat(actual.toString()).isEqualTo(expected.toString());
    }

    //QuestData.getIcon()
    @Test
    void isIconNullWhenTypeIsNone() {
        //given
        underTest = new QuestData(QuestType.NONE, 5, 10, globalData);
        //when
        ItemType actual = underTest.getIcon();
        //then
        assertThat(actual).isNull();
    }

    @Test
    void isIconNullWhenTypeIsWalkingBlocks() {
        //given
        underTest = new QuestData(QuestType.WALK_BLOCK, 5, 10, globalData);
        //when
        ItemType actual = underTest.getIcon();
        //then
        assertThat(actual).isEqualTo(ItemTypes.GRASS);
    }

    //QuestData.updateProgress()
    @Test
    void isUpdateProgressAccumulative() {
        //given
        underTest = new QuestData(QuestType.WALK_BLOCK, 50, 30, globalData);
        underTest.updateProgress(20);
        underTest.updateProgress(5);
        //when
        int actual = underTest.getCurrProg();
        //then
        assertThat(actual).isEqualTo(25);
    }

    //QuestData.isCompleted()
    @Test
    void isNotCompletedWhenProgressIsInsufficient() {
        //given
        underTest = new QuestData(QuestType.WALK_BLOCK, 50, 20, globalData);
        underTest.updateProgress(30);
        //when
        boolean actual = underTest.isCompleted();
        //then
        assertThat(actual).isFalse();
    }

    @Test
    void isCompletedWhenProgressIsSufficient() {
        //given
        underTest = new QuestData(QuestType.WALK_BLOCK, 50, 20, globalData);
        underTest.updateProgress(50);
        //when
        boolean actual = underTest.isCompleted();
        //then
        assertThat(actual).isTrue();
    }

    @Test
    void isCompletedWhenProgressIsExcessive() {
        //given
        underTest = new QuestData(QuestType.WALK_BLOCK, 50, 20, globalData);
        underTest.updateProgress(80);
        //when
        boolean actual = underTest.isCompleted();
        //then
        assertThat(actual).isTrue();
    }

    //QuestData.toString()
    @Test
    void isStringRepresentationCorrectWhenTypeIsNull() {
        //given
        underTest = new QuestData(QuestType.NONE, 50, 30, globalData);
        //when
        String actual = underTest.toString();
        //then
        assertThat(actual).isEqualTo("NONE:EMPTY QUEST");
    }

    @Test
    void isStringRepresentationCorrectWhenTypeIsWalkingBlocks() {
        //given
        underTest = new QuestData(QuestType.WALK_BLOCK, 50, 30, globalData);
        //when
        String actual = underTest.toString();
        //then
        assertThat(actual).isEqualTo("WALK_BLOCK:Walk 50 blocks");
    }
}