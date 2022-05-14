package io.github.RevyaS.data.types;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;

public enum QuestType {
    WALK_BLOCK (ItemTypes.GRASS, "Walk", "Blocks", "Travelled"),
    BLOCK_DESTROY (ItemTypes.DIRT, "Destroy", "Blocks", "Destroyed"),
    NONE (null, "EMPTY QUEST", "", "");

    final private ItemType type;
    final private String prefix;
    final private String suffix;

    public String getProgressText() {
        return progressText;
    }

    final private String progressText;

    public ItemType getType() {
        return type;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    QuestType(ItemType type, String prefix, String suffix, String progressText) {
        this.type = type;
        this.prefix = prefix;
        this.suffix = suffix;
        this.progressText = progressText;
    }
}
