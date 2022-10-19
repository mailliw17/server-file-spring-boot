package com.example.springbootfileupload.service;

import com.example.springbootfileupload.entity.Attachment;
import com.example.springbootfileupload.entity.Attachments;
import org.springframework.core.io.Resource;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public interface AttachmentService {

    Resource downloadFileIts(String fileName);

    Path pathFileJts(String fileName);

    Resource fileNameJts(String fileName);

    ArrayList<String> getAllPathIts();

    ArrayList<String> getAllPathJts();

    void deleteFileIts(String pathDelete) throws IOException;

    void deleteFileJts(String pathDelete) throws IOException;

//    void unzipFile() throws IOException;

//    List<Attachments> attachmentsJson();
}
