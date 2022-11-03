package com.example.springbootfileupload.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class Attachments {
    @OneToMany
    private List<Attachment> attachments;
}
