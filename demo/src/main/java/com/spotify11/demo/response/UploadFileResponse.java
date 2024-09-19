package com.spotify11.demo.response;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

import java.net.URI;

@Data
public class UploadFileResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fileName;
    private URI fileDownloadUri;
    private String fileType;
    private long size;


    public UploadFileResponse(Integer song_id2, String fileName, String fileDownloadUri, String fileType, long size) {
        this.fileName = fileName;
        this.id = song_id2;
        this.fileDownloadUri = URI.create(fileDownloadUri);
        this.fileType = fileType;
        this.size = size;

    }


}
