package com.example.springbootfileupload.controller;

import com.example.springbootfileupload.entity.Attachment;
import com.example.springbootfileupload.entity.Attachments;
import com.example.springbootfileupload.service.AttachmentService;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class AttachmentController {

   private AttachmentService attachmentService;

    public AttachmentController(AttachmentService attachmentService) {
        this.attachmentService = attachmentService;
    }

    //DOWNLOAD ZIP IN MULTIPLE FILE
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

    //DOWNLOAD SINGLE FILE WITH PARAMETER
//    @GetMapping("single-download/{fileName}")
//    ResponseEntity<Resource> downloadSingleFile(@PathVariable String fileName) {
//        Resource resource = attachmentService.downloadFileIts(fileName);
//        System.out.println(resource);
//        MediaType contentType = MediaType.IMAGE_JPEG;
//
//        return ResponseEntity.ok()
//                .contentType(contentType)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + resource.getFilename())
//                .body(resource);
//    }

//    @GetMapping("/api/download/jts")
    public Attachments downloadFileJts() {
        List<String> pathFiles = attachmentService.getAllPathJts();

        List<Attachment> attcs = new ArrayList<>();

            pathFiles
                    .stream()
                    .forEach(file -> {
                        Resource fullPath = attachmentService.getFullPathJts(file);
//                        System.out.println(fullPath);

                        byte[] byteFile;
                        String base64String;

                        try {
                            //CONVERT FILE INTO BYTES
                            byteFile = fullPath.getInputStream().readAllBytes();

                            //CONVERT INTO BASE64
                            base64String = Base64.getEncoder().encodeToString(byteFile);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        //CREATE NEW ATTACHMENT
                        Attachment attc = new Attachment(fullPath.getFilename(), base64String);

                        //PUSH NEW ATTACHMENT TO ATTACHMENTS LIST
                        attcs.add(attc);
                    });

        //START DELETE FILE AFTER HIT API
        pathFiles
                .stream()
                .forEach(file -> {
                    try {
                        attachmentService.deleteFileJts(file);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        //CREATE NEW ATTACHMENTS AND FILL WITH ATTCS
        Attachments result = new Attachments(attcs);

        return result;
    }

//    @GetMapping("/api/download/its")
    public Attachments downloadFileIts() throws IOException {
        List<String> pathFiles = attachmentService.getAllPathIts();

        List<Attachment> attcs = new ArrayList<>();

            pathFiles
                .stream()
                    .forEach(file -> {
                        Resource fullPath = null;
                        fullPath = attachmentService.getFullPathIts(file);

//                        System.out.println(fullPath);

                        byte[] byteFile;
                        String base64String;

                        try {
                            //CONVERT FILE INTO BYTES
                            byteFile = fullPath.getInputStream().readAllBytes();

                            //CONVERT INTO BASE64
                            base64String = Base64.getEncoder().encodeToString(byteFile);


                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        //CREATE NEW ATTACHMENT
                        Attachment attc = new Attachment(fullPath.getFilename(), base64String);

                        //PUSH NEW ATTACHMENT TO ATTACHMENTS LIST
                        attcs.add(attc);
                        System.out.println(fullPath);
                        String abc =null;
                        try {
                            abc = fullPath.getURL().toString();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            File fileToDelete = fullPath.getFile();
                            fileToDelete.setWritable(true);
                            fileToDelete.setExecutable(true);
                            RandomAccessFile raf = new RandomAccessFile(fileToDelete, "rw");
                            try (Stream<String> lines = Files.lines(Paths.get(abc.replace("file:", "")))) {

                                String content = lines.collect(Collectors.joining(System.lineSeparator()));

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            fullPath.getInputStream().close();
                            raf.close();
                            boolean dlt= fileToDelete.delete();

                            System.out.println(dlt);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
//                        System.out.println("abc "+ abc);
//                        String cct = abc.replace("file:", "");
//                        File fileToDelete = new File(cct);
//                        System.out.println(cct);

                    });

            //CREATE NEW ATTACHMENTS AND FILL WITH ATTCS
            Attachments result = new Attachments(attcs);

            return result;
        }

    @GetMapping("/api/delete/its")
    void deleteFileIts() throws IOException {
        List<String> pathFiles = attachmentService.getAllPathIts();
        System.out.println("Start deleting file");
        //START DELETE FILE AFTER HIT API
        pathFiles
                .stream()
                .forEach(file -> {
                    try {
                        attachmentService.deleteFileIts(file);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    @GetMapping("/api/download/{location}")
    public Attachments download(@PathVariable String location) throws IOException {
        if (Objects.equals(location, "its") || Objects.equals(location, "jts")) {
            return attachmentService.processAndDelete(location);
        }
        return null;
    }



}
