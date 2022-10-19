package com.example.springbootfileupload.controller;

import com.example.springbootfileupload.dto.AttachmentDto;
import com.example.springbootfileupload.dto.AttachmentsDto;
import com.example.springbootfileupload.entity.Attachment;
import com.example.springbootfileupload.entity.Attachments;
import com.example.springbootfileupload.service.AttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

@RestController
public class AttachmentController {

    private AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

//    @GetMapping("/download/jts")
//    String downloadSingleFileJts(HttpServletResponse response) throws IOException {
//        ArrayList<String> pathFiles = attachmentService.getAllPathJts();
//
//        synchronized (this) {
//            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
//
//            pathFiles
//                    .stream()
//                    .forEach(file -> {
//                        Resource resource = attachmentService.downloadFileJts(file);
//                        System.out.println(resource);
//                        System.out.println(resource.toString());
//                        System.out.println(resource.toString().getBytes());
//
////                        ENCRYPT FILE PATH
//                        MessageDigest md = null;
//                        try {
//                            md = MessageDigest.getInstance("MD5");
//                        } catch (NoSuchAlgorithmException e) {
//                            throw new RuntimeException(e);
//                        }
//                        md.update(resource.toString().getBytes());
//                        byte[] digest = md.digest();
//                        String myHash = DatatypeConverter
//                                .printHexBinary(digest);
//
//                        System.out.println(myHash);
//                        ZipEntry zipEntry = new ZipEntry(resource.getFilename());
//
//
//
//                        try {
//                            zipEntry.setSize(resource.contentLength());
//                            zos.putNextEntry(zipEntry);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    });
//            zos.finish();
//            zos.close();
//
//            return null;
////             TO DELETE AFTER ZIPPING FILE
////            pathFiles
////                    .stream()
////                    .forEach(file -> {
////                        try {
////                            attachmentService.deleteFileIts(file);
////
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
////                    });
//        }
//    }


//    @GetMapping("/download/its")
//    void downloadSingleFileIts(HttpServletResponse response) throws IOException {
//        ArrayList<String> pathFiles = attachmentService.getAllPathIts();
//
//        synchronized (this) {
//            ZipOutputStream zos = new ZipOutputStream(response.getOutputStream());
//
//            pathFiles
//                    .stream()
//                    .forEach(file -> {
//                        Resource resource = attachmentService.downloadFileIts(file);
//
//                        ZipEntry zipEntry = new ZipEntry(resource.getFilename());
//
//                        try {
//                            zipEntry.setSize(resource.contentLength());
//                            zos.putNextEntry(zipEntry);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    });
//            zos.finish();
//            zos.close();
//
//            // TO DELETE AFTER ZIPPING FILE
//            pathFiles
//                    .stream()
//                    .forEach(file -> {
//                        try {
//                            attachmentService.deleteFileIts(file);
//
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    });
//        }
//    }

    @GetMapping("single-download/{fileName}")
    ResponseEntity<Resource> downloadSingleFile(@PathVariable String fileName) {
        Resource resource = attachmentService.downloadFileIts(fileName);
        System.out.println(resource);
        MediaType contentType = MediaType.IMAGE_JPEG;

        return ResponseEntity.ok()
                .contentType(contentType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + resource.getFilename())
                .body(resource);
    }

    @GetMapping("/api/download/jts")
    public Attachments downloadFileJts() {
        List<String> pathFiles = attachmentService.getAllPathJts();

        List<Attachment> attcs = new ArrayList<>();

            pathFiles
                    .stream()
                    .forEach(file -> {
                        Path fileName = attachmentService.pathFileJts(file);
                        Resource resource = attachmentService.fileNameJts(file);
                        // System.out.println(resource);

                        // START ENCRYPT
                        MessageDigest md = null;
                        try {
                            md = MessageDigest.getInstance("MD5");
                        } catch (NoSuchAlgorithmException e) {
                            throw new RuntimeException(e);
                        }
                        md.update(resource.toString().getBytes());
                        byte[] digest = md.digest();
                        String myHash = DatatypeConverter
                                .printHexBinary(digest);
                        //EOF ENCRYPT

                        //CREATE NEW ATTACHMENT
                        Attachment attc = new Attachment(resource.getFilename(), fileName);

                        //PUSH NEW ATTACHMENT TO ATTACHMENTS LIST
                        attcs.add(attc);
                    });

        //START DELETE FILE AFTER HIT API
//        pathFiles
//                .stream()
//                .forEach(file -> {
//                    try {
//                        attachmentService.deleteFileJts(file);
//
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
        //EOF DELETE FILE AFTER HIT API

        //CREATE NEW ATTACHMENTS AND FILL WITH ATTCS
        Attachments result = new Attachments(attcs);

        return result;
    }

    @GetMapping("/api/download/its")
    public Attachments downloadFileIts() {
        List<String> pathFiles = attachmentService.getAllPathIts();
        List<Attachment> attcs = new ArrayList<>();

        pathFiles
                .stream()
                .forEach(file -> {
                    Resource resource = attachmentService.downloadFileIts(file);
                    // System.out.println(resource);

                    //START ENCRYPT
                    MessageDigest md = null;
                    try {
                        md = MessageDigest.getInstance("MD5");
                    }catch (NoSuchAlgorithmException e) {
                        throw new RuntimeException(e);
                    }
                    md.update(resource.toString().getBytes());
                    byte[] digest = md.digest();
                    String myHash = DatatypeConverter.printHexBinary(digest);
                    //EOF ENCRYPT

                    //CREATE NEW ATTACHMENT
//                    Attachment attc = new Attachment(resource.getFilename(), resource.toString());

                    //PUSH NEW ATTACHMENT TO ATTACHMENT LIST
//                    attcs.add(attc);
                });

        //START DELETE FILE AFTER HIT API
//        pathFiles
//                .stream()
//                .forEach(file -> {
//                    try {
//                        attachmentService.deleteFileIts(file);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
        //EOF DELETE FILE AFTER HIT API

        //CREATE NEW ATTACHMENTS AND FILL WITH ATTCS
        Attachments result = new Attachments(attcs);

        return result;
    }
}
