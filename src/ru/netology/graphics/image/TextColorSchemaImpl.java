package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {

    @Override
    public char convert(int color) {
        char[] chars = {'#', '$', '@', '%', '*', '+', '-', '\''};
        return chars[color / (256 / chars.length)];
    }
}
