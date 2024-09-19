package com.spotify11.demo.services;

import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.User;

import com.spotify11.demo.exception.FileStorageException;
import com.spotify11.demo.exception.SongException;

import com.spotify11.demo.property.FileStorageProperties;
import com.spotify11.demo.repo.SongRepo;
import com.spotify11.demo.repo.UserRepository;
import com.spotify11.demo.response.UploadFileResponse;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;


@Service
public class SongImpl implements SongService {

    private static final Logger log = LoggerFactory.getLogger(SongImpl.class);


    private final UserRepository userRepo;

    private final Path fileStorageLocation;

    private final SongRepo songRepo;




    public SongImpl(UserRepository userRepo, SongRepo songRepo, FileStorageProperties fileStorageProperties) {
        this.userRepo = userRepo;
        this.songRepo = songRepo;
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try{
            Files.createDirectories(this.fileStorageLocation);
        }catch(Exception e){
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", e);
        }

    }

    @Transactional
    @Override
    public UploadFileResponse createSong(String title, String artist, MultipartFile file, String email) throws Exception, FileNotFoundException, FileStorageException {
        User user = userRepo.findByEmail(email).get();
        if (file != null) {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            try {
                if (fileName.contains("..")) {
                    throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
                }
                Path targetLocation = this.fileStorageLocation.resolve(fileName);
                Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/downloadFile/")
                        .path(fileName)
                        .toUriString();
                UploadFileResponse xyz3 = new UploadFileResponse((int) songRepo.count() + 1, fileName, fileDownloadUri, file.getContentType(), file.getSize());


                Song song123 = new Song(title, artist, targetLocation.toUri(), fileName);

                user.getLibrary().addSong(song123);
                songRepo.saveAndFlush(song123);

                userRepo.save(user);
                return xyz3;
            } catch (IOException ex) {
                throw new FileStorageException("Could not store file " + fileName + ".Please try again!", ex);
            }
        } else {
            throw new FileNotFoundException("File not found.");
        }


    }
    @Transactional
    @Override
    public Resource loadFileAsResource(String fileName) throws FileNotFoundException {
        try{
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()){
                return resource;
            }else{
                throw new FileNotFoundException("File not found " + fileName);
            }
        }catch(MalformedURLException ex){
            throw new FileNotFoundException("File not found" + fileName);
        }
    }


    @Transactional
    @Override
    public Song updateSong(String title, String artist, MultipartFile file, Integer song_id, String email) throws SongException, FileStorageException, IOException {
        if (userRepo.findByEmail(email).isPresent()) {
            User user = userRepo.findByEmail(email).get();
            List<Song> xyz = user.getLibrary().getSongs();
            for (Song song : xyz) {
                if (song.getTitle().equals(title)) {
                    song.setFilename(file.getOriginalFilename());
                    song.setFileDownloadUri(file.getResource().getURI());
                    song.setArtist(artist);
                    song.setTitle(title);
                    songRepo.save(song);
                    return song;
                }
            }
        }

        return null;

    }


    @Transactional
    public Song deleteSong(int song_id, String email) throws SongException {
            User user = userRepo.findByEmail(email).get();
            List<Song> xyz = user.getLibrary().getSongs();
            song_id = song_id - 1;
            Song song = xyz.get(song_id);
            if(xyz.contains(song)){
                user.getLibrary().getSongs().remove(song_id);
                userRepo.save(user);
                return song;
            }else{
                throw new SongException("Song not found in library or is null");
            }

    }


    public Song getSong(int song_id, String email) throws SongException {
        User user = userRepo.findByEmail(email).get();

        List<Song> xyz = user.getLibrary().getSongs();
        for(Song song : xyz){
            if (song.getId() == song_id){
                return song;
            }
        }
        throw new SongException("Song id:" + song_id + " has not been found");

    }

    public Song getSong(String title, String email) throws  SongException {
        User user = userRepo.findByEmail(email).get();

        List<Song> xyz = user.getLibrary().getSongs();
        for (Song song : xyz) {
            if (song.getTitle().equals(title)) {
                return song;
            }
        }

        throw new SongException("Song title: " + title + " could not be found");

    }


    public List<Song> getAllSongs(String email) throws SongException {
        User user = userRepo.findByEmail(email).get();
        List<Song> xyz = user.getLibrary().getSongs();
        return xyz;
    }
}