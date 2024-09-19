package com.spotify11.demo.services;

import com.spotify11.demo.entity.Song;

import com.spotify11.demo.exception.FileStorageException;
import com.spotify11.demo.exception.SongException;

import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.response.UploadFileResponse;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public interface SongService {



    UploadFileResponse createSong(String title, String artist, MultipartFile file, String email) throws Exception, FileNotFoundException, FileStorageException;
    Song updateSong(String title, String artist, MultipartFile file, Integer song_id, String email) throws UserException, SongException, IOException, FileStorageException;
    Resource loadFileAsResource (String filename) throws FileNotFoundException;

    Song deleteSong(int song_id, String email) throws UserException, SongException;
    Song getSong(int id, String email) throws  UserException,SongException;
    Song getSong(String title, String email) throws  UserException,SongException;
    List<Song> getAllSongs(String email) throws UserException, SongException;
}
