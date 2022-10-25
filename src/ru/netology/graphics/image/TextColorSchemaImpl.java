package ru.netology.graphics.image;

public class TextColorSchemaImpl implements TextColorSchema {

    @Override
    public char convert(int color) {
        char[] chars = {'#', '$', '@', '%', '*', '+', '-', '\''};

        for (int i = 0; i < chars.length; i++) {
            if (color / 32 == i) {
                return chars[i];
            }
        }
        return '0';
    }
}
