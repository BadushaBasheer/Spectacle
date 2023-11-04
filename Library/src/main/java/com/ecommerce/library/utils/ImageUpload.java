package com.ecommerce.library.utils;

import com.ecommerce.library.service.ProductService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Component
public class ImageUpload {

//    String UPLOAD_FOLDER =         "D:\\23092023\\E-Commerce_badusha_23\\Admin\\src\\main\\resources\\static\\ProductImage";
    String UPLOAD_FOLDER =         "/home/ubuntu/Spectacle/Admin/src/main/resources/static/ProductImage";

//    String UPLOAD_FOLDER_CUSTOMER = "D:\\23092023\\spectacle\\Customer\\src\\main\\resources\\static\\ProductImage";
    String UPLOAD_FOLDER_CUSTOMER = "/home/ubuntu/Spectacle/Customer/src/main/resources/static/ProductImage";


    public List<String> uploadImages(List<MultipartFile> imageFiles){
        File uploadDir = new File(UPLOAD_FOLDER);
        File uploadDirCus = new File(UPLOAD_FOLDER_CUSTOMER);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        if (!uploadDirCus.exists()) {
            uploadDir.mkdirs();
        }
        List<String> uniqueFileNames = new ArrayList<>();

        try {
            for (MultipartFile imageFile : imageFiles) {
                String originalFileName = imageFile.getOriginalFilename();
                String fileExtension = originalFileName.substring(originalFileName.lastIndexOf('.'));
                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

                Path destinationPath = Path.of(UPLOAD_FOLDER, uniqueFileName);
                Path destinationPathCus = Path.of(UPLOAD_FOLDER_CUSTOMER, uniqueFileName);
                while (Files.exists(destinationPath)) {
                    uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                    destinationPath = Path.of(UPLOAD_FOLDER, uniqueFileName);
                }
                while (Files.exists(destinationPathCus)) {
                    uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                    destinationPathCus = Path.of(UPLOAD_FOLDER_CUSTOMER, uniqueFileName);
                }

                Files.copy(imageFile.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
                Files.copy(imageFile.getInputStream(), destinationPathCus, StandardCopyOption.REPLACE_EXISTING);

                uniqueFileNames.add(uniqueFileName);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return uniqueFileNames;
    }


    public boolean checkExist(MultipartFile imageProduct) {
        boolean isExisted = false;
        try {
            File file = new File(UPLOAD_FOLDER + "\\" + imageProduct.getOriginalFilename());
            isExisted = file.exists();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isExisted;
    }


}

