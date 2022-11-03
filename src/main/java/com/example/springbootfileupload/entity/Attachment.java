package com.example.springbootfileupload.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class Attachment {
    private String fileName;
    private String fileBytes;
}
