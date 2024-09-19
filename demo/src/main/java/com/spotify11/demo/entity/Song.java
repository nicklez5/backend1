package com.spotify11.demo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.net.URI;

@Setter
@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(unique = true, nullable = false)
    private int id;

    private String title;
    private String artist;
    private URI fileDownloadUri;
    private String filename;



    public Song(String title, String artist, URI fileDownloadUri, String filename) {
        this.title = title;
        this.artist = artist;
        this.fileDownloadUri = fileDownloadUri;
        this.filename = filename;
    }


    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}