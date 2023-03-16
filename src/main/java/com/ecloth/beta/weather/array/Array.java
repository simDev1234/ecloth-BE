package com.ecloth.beta.weather.array;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

public class Array {

    public static void main(String[] args) {
        try {
            File imageFile = new File("/Users/sonhanbyeol/Desktop/img");
            BufferedImage bufferedImage = ImageIO.read(imageFile);

            // 이미지를 byte[]로 변환
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            // byte[]로부터 이미지로 변환
            // ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            // BufferedImage newBufferedImage = ImageIO.read(byteArrayInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
