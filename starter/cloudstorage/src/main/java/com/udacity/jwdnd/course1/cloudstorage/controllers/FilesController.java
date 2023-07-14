package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.models.UserFile;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

@Controller
public class FilesController {

    private final FileService fileService;
    private final UserService userService;

    public FilesController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @GetMapping("/files/{fileId}")
    public ResponseEntity<ByteArrayResource> displayFile(@RequestParam boolean download, @PathVariable Integer fileId, Authentication authentication) throws IOException {
        User user = userService.getUser(authentication.getName());
        System.out.println("fileId in displayFile(): " + fileId);
        // Load the file from the file system or database
        UserFile userFile = fileService.getFileByIdAndUserId(fileId, user.getUserId());
        long fileSize = Long.parseLong(userFile.getFileSize());
        System.out.println("fileSize in displayFile(): " + fileSize);
        ByteArrayResource resource = new ByteArrayResource(userFile.getFileData());
        if(download) {
            System.out.println("download is true");
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(userFile.getContentType()))
                    .contentLength(fileSize)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userFile.getFileName() + "\"")
                    .body(resource);
        } else {
            System.out.println("download is false");
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(userFile.getContentType()))
                    .contentLength(fileSize)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename*=\"" + userFile.getFileName() + "\"")
                    .body(resource);
        }


    }

    @PostMapping("/file-upload")
    public String fileUpload(@RequestParam("fileUpload") MultipartFile fileUpload, Authentication authentication, Model model) throws IOException {
        try {
            User user = userService.getUser(authentication.getName());
            Integer userId = user.getUserId();

            long fileSize = fileUpload.getSize();

//            handle user not logged in
            if(userId == null) {
                model.addAttribute("error", true);
                model.addAttribute("message", "There was an error uploading the file.  Please log in and try again.");
                return "result";
            }

//           handle empty file
            if(!(fileSize > 0)) {
                model.addAttribute("error", true);
                model.addAttribute("message", "There was an error uploading the file. The file was empty.");
                return "result";
            }

//            handle file size limit (10MB)
            if(fileSize > 10000000) {
                model.addAttribute("error", true);
                model.addAttribute("message", "There was an error uploading the file. The file size limit is 100MB");
                return "result";
            }

            String fileSizeString = String.valueOf(fileUpload.getSize());
            String fileName = StringUtils.cleanPath(fileUpload.getOriginalFilename());
            String contentType = fileUpload.getContentType();

            System.out.println("file size: " + fileSizeString);;
            System.out.println("file name: " + fileName);
            System.out.println("content type: " + contentType);

//            handle duplicate file name
            if(!fileService.isFileNameAvailable(fileName, userId)) {
                model.addAttribute("error", true);
                model.addAttribute("message", "There was an error uploading the file. A file with that name already exists.");
                return "result";
            }

//            handle invalid file type
            if(!fileService.isValidFileType(contentType)) {
                model.addAttribute("error", true);
                model.addAttribute("message", "There was an error uploading the file. The file type is not allowed.");
                return "result";
            }

//            handle empty file name
            if(fileName == "") {
                model.addAttribute("error", true);
                model.addAttribute("message", "There was an error uploading the file. The file name cannot be empty.");
                return "result";
            }

//            handle empty content type
            if(contentType == "") {
                model.addAttribute("error", true);
                model.addAttribute("message", "There was an error uploading the file. The file type cannot be empty.");
                return "result";
            }

//            create file object
            UserFile file = new UserFile(
                    fileName,
                    contentType,
                    fileSizeString,
                    userId,
                    fileUpload.getBytes()
            );

//            add file to database
            int added = fileService.uploadFile(file);

//            handle error adding file to database
            if(!(added > 0)) {
                model.addAttribute("error", true);
                model.addAttribute("message", "There was an error uploading the file. Please try again.");
                return "result";
            }

            model.addAttribute("success", true);
            model.addAttribute("activeTab", "files");
            UserFile[] files = fileService.getAllFilesForUser(userId);
            Arrays.stream(files).toList().forEach(f -> System.out.println("user file: " + f.getFileName()));
            model.addAttribute("allFiles", files);
            return "result";


        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", true);
            model.addAttribute("message", "There was an error uploading the file. Please try again.");
            return "result";
        }


    }

    @GetMapping("/deleteFile")
    public String deleteFile(@RequestParam("id") Integer fileId, Authentication authentication, Model model ) {
        System.out.println("attempting to delete file with id: " + fileId);
        try {
            User user = userService.getUser(authentication.getName());
            Integer userId = user.getUserId();

            System.out.println("fileId in deleteFile(): " + fileId);
            System.out.println("userId in deleteFile(): " + userId);

            fileService.deleteFile(fileId, userId);
            model.addAttribute("success", true);
            return "result";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", true);
            model.addAttribute("message", "There was an error deleting the file. Please try again.");
            return "result";
        }
    }


}
