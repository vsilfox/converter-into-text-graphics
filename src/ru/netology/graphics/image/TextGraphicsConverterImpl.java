package ru.netology.graphics.image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class TextGraphicsConverterImpl implements TextGraphicsConverter {

    private int maxWidth;
    private int maxHeight;
    private double maxRatio;
    private TextColorSchema schema;

    public TextGraphicsConverterImpl(int maxWidth, int maxHeight, double maxRatio, TextColorSchema schema) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.maxRatio = maxRatio;
        this.schema = schema;
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        int width = img.getWidth();
        int height = img.getHeight();
        int ratio = width / height;
        if (ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }
        ratio = height / width;
        if (ratio > maxRatio) {
            throw new BadImageSizeException(ratio, maxRatio);
        }

        int newHeight;
        int newWidth;
        if (width > maxWidth || height > maxHeight) {
            int widthDivisor = width / maxWidth + 1;
            int heightDivisor = height / maxHeight + 1;
            if (widthDivisor > heightDivisor) {
                newWidth = width / widthDivisor;
                newHeight = height / widthDivisor;
            } else {
                newWidth = width / heightDivisor;
                newHeight = height / heightDivisor;
            }
        } else {
            newWidth = width;
            newHeight = height;
        }

        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();
        graphics.drawImage(scaledImage, 0, 0, null);
        WritableRaster bwRaster = bwImg.getRaster();

        char[][] charArray = new char[bwRaster.getHeight()][bwRaster.getWidth()];
        for (int h = 0; h < bwRaster.getHeight(); h++) {
            for (int w = 0; w < bwRaster.getWidth(); w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                charArray[h][w] = c;
            }
        }

        StringBuilder stringBuilder = new StringBuilder();
        for (char[] chars : charArray) {
            for (char aChar : chars) {
                stringBuilder.append(aChar);
                stringBuilder.append(aChar);
                stringBuilder.append(aChar);
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;

    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
