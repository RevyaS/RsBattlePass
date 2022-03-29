package io.github.RevyaS.data.containers;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

public class TextableString {
    public TextableString(String string)
    {
        this.string = string;
    }

    public Text getFormattedText() { return TextSerializers.FORMATTING_CODE.deserialize(toString());}

    @Override
    public String toString()
    {
        return string;
    }

    public static TextableString of(String string) { return new TextableString(string);}

    private String string;
}
