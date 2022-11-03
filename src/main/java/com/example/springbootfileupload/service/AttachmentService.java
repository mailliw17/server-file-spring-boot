package com.example.springbootfileupload.service;

import com.example.springbootfileupload.entity.Attachment;
import com.example.springbootfileupload.entity.Attachments;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Service
public class AttachmentService {

    private Path fileStoragePathIts;
    private  String fileStorageLocationIts;
    private Path fileStoragePathJts;
    private  String fileStorageLocationJts;

    private final ResourceLoader resourceLoader;

    public AttachmentService(@Value("${file.storage.locationIts}") String fileStorageLocationIts, @Value("${file.storage.locationJts}") String fileStorageLocationJts, ResourceLoader resourceLoader) {
        fileStoragePathIts = Paths.get(fileStorageLocationIts).toAbsolutePath().normalize();
        this.fileStorageLocationIts = fileStorageLocationIts;

        fileStoragePathJts = Paths.get(fileStorageLocationJts).toAbsolutePath().normalize();
        this.fileStorageLocationJts = fileStorageLocationJts;

        this.resourceLoader = resourceLoader;
    }

    //    FUNCTION FOR COLLECT ALL TITLE IN ITS FOLDER
    public ArrayList<String> getAllPathIts() throws IOException {

        ArrayList<String> pathFiles = new ArrayList<String>();
        File folder = new File(this.fileStorageLocationIts);
        File[] listOfFiles = folder.listFiles();

//        System.out.println("lif : " + listOfFiles);

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                pathFiles.add(listOfFiles[i].getName());
            }
        }

        return pathFiles;
    }


    //    FUNCTION FOR COLLECT ALL TITLE IN JTS FOLDER
    public ArrayList<String> getAllPathJts() {
        ArrayList<String> pathFiles = new ArrayList<String>();
        File folder = new File(this.fileStorageLocationJts);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                pathFiles.add(listOfFiles[i].getName());

            }
        }

        return pathFiles;
    }

    public void deleteFileJts(String pathDelete) throws IOException {
        Path fileToDeletePath = Paths.get(this.fileStorageLocationJts + "/" + pathDelete);
        Files.delete(fileToDeletePath);
    }

    public void deleteFileIts(String pathDelete) throws IOException {
        System.out.println(pathDelete);
        Path fileToDeletePath = Paths.get(this.fileStorageLocationIts + "/" + pathDelete).toAbsolutePath();

        String abc = fileToDeletePath.toUri().toString();
        File file = new File(abc.replace("file:", ""));
        try (Stream<String> lines = Files.lines(Paths.get(abc.replace("file:", "")))) {

            String content = lines.collect(Collectors.joining(System.lineSeparator()));

        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("halo1");
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        raf.close();
        boolean res = file.delete();
        System.out.println(res);
    }

//    @Override
//    public Resource downloadFileIts(String fileName) {
//        Path path = Paths.get(fileStorageLocationIts).toAbsolutePath().resolve(fileName);
//        RandomAccessFile raf;
//        Resource resource;
//        try {
//            resource = new UrlResource(path.toUri());
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("Issue in reading file", e);
//        }
//
//        if(resource.exists() && resource.isReadable()) {
//            return resource;
//        } else {
//            throw new RuntimeException("The file does not exist or not readable");
//        }
//    }

    public Resource getFullPathJts(String fileName) {
        Path path = Paths.get(fileStorageLocationJts).toAbsolutePath().resolve(fileName);
        Resource resource;
        try {
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Issue in reading file", e);
        }

        if(resource.exists() && resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("The file does not exist or not readable");
        }
    }

    public Resource getFullPathIts(String fileName) {
        Path path = Paths.get(fileStorageLocationIts).resolve(fileName);
//        System.out.println(path.toUri());

        String abc = path.toUri().toString();

        Resource resource;
        try {
            resource = new UrlResource(path.toUri());

//            File file = new File(abc.replace("file:", ""));
//            System.out.println("halo1");
//            RandomAccessFile raf = new RandomAccessFile(file, "r");
//            raf.close();
//            file.delete();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return resource;

//        if(resource.exists() && resource.isReadable()) {
//            return resource;
//        } else {
//            throw new RuntimeException("The file does not exist or not readable");
//        }
    }

    public List<Path> getListOfPath(String location) {

        String fileLocation = null;
        List<Path> listOfPath = new ArrayList<>();

        if(Objects.equals(location, "its")) {
             fileLocation = this.fileStorageLocationIts;
        } else if (Objects.equals(location, "jts")) {
            fileLocation = this.fileStorageLocationJts;
        }

        try (Stream<Path> paths = Files.walk(Paths.get(fileLocation))) {
            paths
                    .forEach(path -> {
                        listOfPath.add(path);
                    });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return listOfPath;
    }

    public Attachments processAndDelete(String location) throws IOException {
        List<Path> listOfPath2 = new ArrayList<>();
        List<Attachment> listOfAttachments = new ArrayList<>();

        if(Objects.equals(location, "its")) {
            listOfPath2 = this.getListOfPath("its");
        } else if (Objects.equals(location, "jts")) {
            listOfPath2 = this.getListOfPath("jts");
        }

        for(int i = 1; i < listOfPath2.size(); i++) {

            byte[] byteFile = Files.readAllBytes(listOfPath2.get(i));
            String base64 = Base64.getEncoder().encodeToString(byteFile);
            String stringByte = String.valueOf(listOfPath2.get(i));
            String fileName = String.valueOf(listOfPath2.get(i).getFileName());

            Attachment attachment = new Attachment(fileName, base64);
            listOfAttachments.add(attachment);

            File file = new File(stringByte);
            file.delete();
        }

        Attachments result = new Attachments(listOfAttachments);

        return result;
    }

//    public List<Path> getListOfPathIts() {
//        List<Path> listOfPath = new ArrayList<>();
//        try (Stream<Path> paths = Files.walk(Paths.get(fileStorageLocationIts))) {
//            paths
//                    .forEach(path -> {
//                        listOfPath.add(path);
//                    });
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return listOfPath;
//    }

//    public List<Path> getListOfPathJts() {
//        List<Path> listOfPath = new ArrayList<>();
//        try (Stream<Path> paths = Files.walk(Paths.get(fileStorageLocationJts))) {
//            paths
//                    .forEach(path -> {
//                        listOfPath.add(path);
//                    });
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return listOfPath;
//    }

//    public Attachments processAndDeleteIts() throws IOException {
//        List<Path> kumpulanByte2 = this.getListOfPathIts();
//        List<Attachment> listOfAttachments = new ArrayList<>();
//
//        for(int i = 1; i < kumpulanByte2.size(); i++) {
//
//            byte[] byteFile = Files.readAllBytes(kumpulanByte2.get(i));
//            String base64 = Base64.getEncoder().encodeToString(byteFile);
//            String stringByte = String.valueOf(kumpulanByte2.get(i));
//            String fileName = String.valueOf(kumpulanByte2.get(i).getFileName());
//
//            Attachment attachment = new Attachment(fileName, base64);
//            listOfAttachments.add(attachment);
//
//            File file = new File(stringByte);
//            file.delete();
//        }
//
//        Attachments result = new Attachments(listOfAttachments);
//
//        return result;
//    }

//    public Attachments processAndDeleteJts() throws IOException {
//        List<Path> kumpulanByte2 = this.getListOfPathJts();
//        List<Attachment> listOfAttachments = new ArrayList<>();
//
//        for(int i = 1; i < kumpulanByte2.size(); i++) {
//
//            byte[] byteFile = Files.readAllBytes(kumpulanByte2.get(i));
//            String base64 = Base64.getEncoder().encodeToString(byteFile);
//            String stringByte = String.valueOf(kumpulanByte2.get(i));
//            String fileName = String.valueOf(kumpulanByte2.get(i).getFileName());
//
//            Attachment attachment = new Attachment(fileName, base64);
//            listOfAttachments.add(attachment);
//
//            File file = new File(stringByte);
//            file.delete();
//        }
//
//        Attachments result = new Attachments(listOfAttachments);
//
//        return result;
//    }
}
