package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.UserFile;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public Boolean isFileNameAvailable(String fileName, Integer userId) {
        return fileMapper.getFileByNameAndUserId(fileName, userId) == null;
    }

//    only allow files with the following extensions: jpg, png, gif, txt, and pdf
    public Boolean isValidFileType(String contentType) {
        return contentType.equals("image/jpeg")
                || contentType.equals("image/png")
                || contentType.equals("image/gif")
                || contentType.equals("text/plain")
                || contentType.equals("application/pdf");
    }

    public UserFile getFileByName(String fileName, Integer userId) {
        return fileMapper.getFileByNameAndUserId(fileName, userId);
    }
    public UserFile getFileByIdAndUserId(Integer fileId, Integer userId) {
        return fileMapper.getFileByIdAndUserId(fileId, userId);
    }

    public UserFile[] getAllFilesForUser(Integer userId) {
        return fileMapper.getFilesByUserId(userId);
    }

    public int uploadFile(UserFile file) {
        return fileMapper.insertFile(file);
    }

    public void deleteFile(Integer fileId, Integer userId) {
        fileMapper.delete(fileId, userId);
    }

}
