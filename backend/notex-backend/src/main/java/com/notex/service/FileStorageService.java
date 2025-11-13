// package com.notex.service;

// import org.springframework.stereotype.Service;
// import org.springframework.core.io.Resource;
// import org.springframework.core.io.UrlResource;
// import org.springframework.web.multipart.MultipartFile;

// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.nio.file.StandardCopyOption;

// @Service
// public class FileStorageService {

//     private final Path rootLocation = Paths.get(System.getProperty("user.home"), "notex-uploads");

//     public String storeFile(MultipartFile file, String folder) {
//         try {
//             Path dir = rootLocation.resolve(folder == null ? "" : folder);
//             Files.createDirectories(dir);
//             Path target = dir.resolve(file.getOriginalFilename());
//             Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
//             return target.toString();
//         } catch (Exception ex) {
//             throw new RuntimeException("Failed to store file", ex);
//         }
//     }

//     public Resource loadFileAsResource(String filename, String folder) {
//         try {
//             Path file = rootLocation.resolve(folder == null ? "" : folder).resolve(filename);
//             Resource resource = new UrlResource(file.toUri());
//             if (resource.exists() || resource.isReadable()) {
//                 return resource;
//             }
//             throw new RuntimeException("File not found: " + filename);
//         } catch (Exception ex) {
//             throw new RuntimeException("Failed to load file", ex);
//         }
//     }

//     public void deleteFile(String filename, String folder) {
//         try {
//             Path file = rootLocation.resolve(folder == null ? "" : folder).resolve(filename);
//             Files.deleteIfExists(file);
//         } catch (Exception ex) {
//             throw new RuntimeException("Failed to delete file", ex);
//         }
//     }
// }

//New Code -
package com.notex.service;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import com.notex.model.dto.response.FileUploadResponse;
import com.notex.exception.BadRequestException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {
    
    @Value("${file.upload-dir}")
    private String uploadDir;
    
    public String storeFile(MultipartFile file) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        
        try {
            if (fileName.contains("..")) {
                throw new BadRequestException("Invalid file path: " + fileName);
            }
            
            String fileExtension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0) {
                fileExtension = fileName.substring(dotIndex);
            }
            
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
            
            String fileType = getFileType(file.getContentType());
            Path targetLocation = Paths.get(uploadDir, fileType, uniqueFileName);
            
            Files.createDirectories(targetLocation.getParent());
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            
            return "/uploads/" + fileType + "/" + uniqueFileName;
            
        } catch (IOException ex) {
            throw new BadRequestException("Could not store file " + fileName + ". Please try again!");
        }
    }
    
    public FileUploadResponse uploadFile(MultipartFile file) {
        String fileUrl = storeFile(file);
        
        FileUploadResponse response = new FileUploadResponse();
        response.setFileName(file.getOriginalFilename());
        response.setFileUrl(fileUrl);
        response.setFileType(file.getContentType());
        response.setFileSize(file.getSize());
        
        return response;
    }
    
    public void deleteFile(String fileUrl) {
        try {
            Path filePath = Paths.get(uploadDir).resolve(fileUrl.replace("/uploads/", "")).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException ex) {
            throw new BadRequestException("Could not delete file: " + fileUrl);
        }
    }
    
    private String getFileType(String contentType) {
        if (contentType == null) {
            return "documents";
        }
        
        if (contentType.startsWith("image/")) {
            return "images";
        } else if (contentType.startsWith("audio/")) {
            return "audio";
        } else {
            return "documents";
        }
    }
}