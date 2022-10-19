package com.example.springbootfileupload.entity;

import com.example.springbootfileupload.dto.AttachmentDto;
import com.example.springbootfileupload.dto.AttachmentsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;
import org.springframework.core.io.Resource;

import javax.persistence.*;
import java.nio.file.Path;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class Attachment {
    private String fileName;
    private Path md5;

    public AttachmentDto convertToDto() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, AttachmentDto.class);
    }
}
