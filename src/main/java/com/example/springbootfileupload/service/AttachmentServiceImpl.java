package com.example.springbootfileupload.service;

//import com.example.springbootfileupload.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class AttachmentServiceImpl implements AttachmentService{
    private Path fileStoragePathIts;
    private  String fileStorageLocationIts;
    private Path fileStoragePathJts;
    private  String fileStorageLocationJts;

    private final ResourceLoader resourceLoader;

    public AttachmentServiceImpl(@Value("${file.storage.locationIts}") String fileStorageLocationIts, @Value("${file.storage.locationJts}") String fileStorageLocationJts, ResourceLoader resourceLoader) {
        fileStoragePathIts = Paths.get(fileStorageLocationIts).toAbsolutePath().normalize();
        this.fileStorageLocationIts = fileStorageLocationIts;

        fileStoragePathJts = Paths.get(fileStorageLocationJts).toAbsolutePath().normalize();
        this.fileStorageLocationJts = fileStorageLocationJts;

        this.resourceLoader = resourceLoader;
    }

    @Override
    //    FUNCTION FOR COLLECT ALL TITLE IN ITS FOLDER
    public ArrayList<String> getAllPathIts() {
        ArrayList<String> pathFiles = new ArrayList<String>();
        File folder = new File(this.fileStorageLocationIts);
        File[] listOfFiles = folder.listFiles();

        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                pathFiles.add(listOfFiles[i].getName());
            }
        }

        return pathFiles;
    }

    @Override
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

    @Override
    public void deleteFileJts(String pathDelete) throws IOException {
        Path fileToDeletePath = Paths.get(this.fileStorageLocationJts + "/" + pathDelete);
        Files.delete(fileToDeletePath);
    }

    @Override
    public void deleteFileIts(String pathDelete) throws IOException {
        Path fileToDeletePath = Paths.get(this.fileStorageLocationIts + "/" + pathDelete);
        Files.delete(fileToDeletePath);
    }

    @Override
    public Resource downloadFileIts(String fileName) {
        Path path = Paths.get(fileStorageLocationIts).toAbsolutePath().resolve(fileName);
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


    @Override
    public Path pathFileJts(String fileName) {
        Path path = Paths.get(fileStorageLocationJts).toAbsolutePath().resolve(fileName);

        return path;
    }

    public Resource fileNameJts(String fileName) {
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

//    public void unzipFile() throws IOException {
//
//        System.out.println("void unzipYa");
////        String fileZip = "coba/jts.zip";
//        Path path = Paths.get(fileStoragePathUnzip.toUri()).toAbsolutePath();
//        System.out.println(path);
//        ZipInputStream zis = new ZipInputStream(new FileInputStream(path.toFile()));
//        ZipEntry zipEntry = zis.getNextEntry();
//
//        zis.closeEntry();
//        zis.close();
//    }
}
