package com.spotify11.demo.services;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;
import com.spotify11.demo.entity.User;

import com.spotify11.demo.exception.PlaylistException;
import com.spotify11.demo.exception.SongException;
import com.spotify11.demo.exception.UserException;
import com.spotify11.demo.repo.PlaylistRepo;
import com.spotify11.demo.repo.SongRepo;

import com.spotify11.demo.repo.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PlaylistImpl implements PlaylistService {



    private final UserRepository userRepo;
    private final PlaylistRepo playlistRepo;
    private final SongRepo songRepo;

    public PlaylistImpl(UserRepository userRepo, PlaylistRepo playlistRepo, SongRepo songRepo) {
        this.userRepo = userRepo;
        this.playlistRepo = playlistRepo;
        this.songRepo = songRepo;
    }


    @Override
    public String getPlaylistName(String email){
        if(userRepo.findByEmail(email).isPresent()){
            User user = userRepo.findByEmail(email).get();
            Playlist playlist1 = user.getPlaylist();
            return playlist1.getPlaylistName();
        }else{
            return null;
        }
    }
    @Transactional
    @Override
    public String addSong(Integer song_id,String email) throws Exception {
            if(userRepo.findByEmail(email).isPresent()) {
                User user = userRepo.findByEmail(email).get();
                if(songRepo.findById(song_id).isPresent()){
                    Song song = songRepo.findById(song_id).get();
                    user.getPlaylist().getSongs().add(song);
                    return "Song title:" + song.getTitle() + " added to playlist";
                }else{
                    throw new SongException("Song does not exist");
                }
            }else{
                throw new Exception("User is not present");
            }



    }

    @Transactional
    @Override
    public String removeSong(Integer song_id, String email) throws SongException, UserException {
        if(userRepo.findByEmail(email).isPresent()) {
            User user1 = userRepo.findByEmail(email).get();
            if(songRepo.findById(song_id).isPresent()){
                Song song = songRepo.findById(song_id).get();
                user1.getPlaylist().getSongs().remove(song);
                userRepo.save(user1);
                return "Song title:" + song.getTitle() + " removed from playlist";
            }else{
                throw new SongException("Song does not exist");
            }

        }else{
            throw new UserException("User does not exist");
        }

    }
    @Transactional
    @Override
    public String readPlaylist(String email) throws Exception {
            if(userRepo.findByEmail(email).isPresent()) {
                User user = userRepo.findByEmail(email).get();
                Playlist playlist1 = user.getPlaylist();
                List<Song> xyz3 = playlist1.getSongs();
                String xyz = "";
                for(Song song : xyz3 ){
                    xyz += song.getTitle() + " " + song.getArtist() + "\n";
                }
                return xyz;
            }else{
                throw new UserException("User is not present");
            }


    }
    @Override
    public String readPlaylistSongs(String email) throws Exception {
        if(userRepo.findByEmail(email).isPresent()) {
            User user = userRepo.findByEmail(email).get();
            Playlist playlist1 = user.getPlaylist();
            List<Song> xyz3 = playlist1.getSongs();
            String xyz = "";
            for(Song song : xyz3 ){
                xyz += song.getTitle() + " " + song.getArtist() + "\n";
            }
            return xyz;
        }else{
            throw new UserException("User is not present");
        }
    }



    @Transactional
    public String renamePlaylist(String email, String playlist_name) throws UserException {
            if(userRepo.findByEmail(email).isPresent()) {
                User user = userRepo.findByEmail(email).get();
                final String ply2 = user.getPlaylist().getPlaylistName();
                user.getPlaylist().setPlaylistName(playlist_name);
                userRepo.save(user);
                return "Your playlist have been renamed to " + ply2;
            }

        return null;
    }

    @Transactional
    @Override
    public String clearPlaylist(String email) throws UserException {
            if(userRepo.findByEmail(email).isPresent()) {
                User user = userRepo.findByEmail(email).get();
                Playlist ply1 = user.getPlaylist();
                user.getPlaylist().getSongs().clear();
                userRepo.save(user);
                return "Playlist name:" + ply1.getPlaylistName() + " has been cleared";
            }else{
                throw new UserException("User is not present");
            }


    }

    @Override
    public List<Song> getSongs(String email) throws UserException {
        if(userRepo.findByEmail(email).isPresent()){
            User user = userRepo.findByEmail(email).get();
            Playlist playlist1 = user.getPlaylist();
            return playlist1.getSongs();
        }else{
            throw new UserException("User does not exist");
        }


    }

    @Override
    public String namePlaylist(String email, String name) throws Exception {
        if(userRepo.findByEmail(email).isPresent()){
            User user = userRepo.findByEmail(email).get();
            Playlist playlist1 = user.getPlaylist();
            playlist1.setPlaylistName(name);
            playlistRepo.save(playlist1);
            return "Your playlist has been renamed to: " + name;
        }else{
            throw new Exception("User cannot be found");
        }
    }


}
