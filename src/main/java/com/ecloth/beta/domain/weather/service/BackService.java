package com.ecloth.beta.domain.weather.service;


import com.ecloth.beta.domain.weather.entity.BackImg;
import com.ecloth.beta.domain.weather.repository.BackRepository;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;
import java.util.Optional;

@Service
public class BackService {

    private final BackRepository backRepository;
    private String backgroundImgPath;

    public BackService(BackRepository backRepository) {
        this.backRepository = backRepository;
    }

    public List<BackImg> getAllBackImgs(){
        return backRepository.findAll();
    }

    public BackImg getBackImgById(Long id){
        Optional<BackImg> backImg = backRepository.findById(id);
        if (backImg.isPresent()) {
            return backImg.get();
        } else {
            throw new IllegalArgumentException("Invalid BackImg ID: " + id);
        }
    }

    public void saveBackImg(BackImg backImg){
        backRepository.save(backImg);
    }


    
    public void updateBackImg(BackImg backImg){
        Optional<BackImg> existingBackImg = backRepository.findById(backImg.getId());
        if (existingBackImg.isPresent()) {
            BackImg updatedBackImg = existingBackImg.get();
            updatedBackImg.backgroundImgPath(backImg.backgroundImgPath());
            updatedBackImg.setTitle(backImg.getTitle());
            backRepository.save(updatedBackImg);
        } else {
            throw new IllegalArgumentException("Invalid BackImg ID: " + backImg.getId());
        }
    }

    public void deleteBackImg(Long id){
        backRepository.deleteById(id);
    }

    public class FileTraversal {

        public void main(String[] args) {
            File folder = new File("Back");
            listFilesForFolder(folder);
        }

        public void listFilesForFolder(final File folder) {
            for (final File fileEntry : folder.listFiles()) {
                if (fileEntry.isDirectory()) {
                    listFilesForFolder(fileEntry);
                } else {
                    System.out.println(fileEntry.getName());
                }
            }
        }
    }

    public byte[] getImg() {
        byte[] imageBytes = new byte[0];
        try {
            File imageFile = new File("/Users/sonhanbyeol/Desktop/img");
            BufferedImage bufferedImage = ImageIO.read(imageFile);

            // 이미지를 byte[]로 변환
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
            imageBytes = byteArrayOutputStream.toByteArray();

            // byte[]로부터 이미지로 변환
            // ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageBytes);
            // BufferedImage newBufferedImage = ImageIO.read(byteArrayInputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return imageBytes;
    }

}
