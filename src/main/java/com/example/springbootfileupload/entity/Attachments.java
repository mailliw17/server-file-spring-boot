package com.example.springbootfileupload.entity;

import com.example.springbootfileupload.dto.AttachmentDto;
import com.example.springbootfileupload.dto.AttachmentsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.modelmapper.ModelMapper;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Attachments {
    @OneToMany
    private List<Attachment> attachments;


    public AttachmentsDto convertToDto() {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(this, AttachmentsDto.class);
    }
}
