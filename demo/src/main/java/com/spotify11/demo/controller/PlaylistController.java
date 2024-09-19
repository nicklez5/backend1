package com.spotify11.demo.controller;

import com.spotify11.demo.entity.Playlist;
import com.spotify11.demo.entity.Song;

import com.spotify11.demo.exception.PlaylistException;

import com.spotify11.demo.exception.UserException;

import com.spotify11.demo.services.PlaylistService;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin
@RestController
@RequestMapping("/playlist")
public class PlaylistController {

    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    // ADD SONG
    @Transactional
    @PostMapping("/addSong/{song_id}")
    public ResponseEntity<String> addSongForPlaylist(@PathVariable("song_id") int song_id, @RequestParam("email") String email) throws Exception {
        try{
            String playlist1 = playlistService.addSong(song_id, email);
            return ResponseEntity.ok(playlist1);
        } catch (Exception e) {

            throw new Exception("Song ID: " + song_id + "could not be found");
        }

    }
    @Transactional
    @DeleteMapping("/removeSong/{song_id}")
    public ResponseEntity<String> removeSongFromPlaylist(@PathVariable("song_id") int song_id, @RequestParam("email") String email) throws Exception {
        try{
            String playlist1 = playlistService.removeSong(song_id, email);
            return ResponseEntity.ok(playlist1);
        } catch (Exception e) {
            throw new Exception("Song name: " + song_id + "could not be found");
        }

    }
    @Transactional
    @GetMapping(value = "/getSongs")
    public ResponseEntity<List<Song>> getSongs(@RequestParam("email") String email) throws Exception {
        try{
            List<Song> str1 = playlistService.getSongs(email);
            return ResponseEntity.ok(str1);
        } catch (Exception e) {
            throw new Exception("Could not find user with email: " + email);
        }

    }



    // READ
    @GetMapping("/read")
    public ResponseEntity<String> readPlaylist(@RequestParam("email") String email) throws Exception {
        String pt2 = playlistService.readPlaylist(email);
        return ResponseEntity.ok(pt2);
    }
    @GetMapping("/read/songs")
    public ResponseEntity<String> readSongs(@RequestParam("email") String email) throws Exception {
        String pt2 = playlistService.readPlaylistSongs(email);
        return ResponseEntity.ok(pt2);
    }
    //RENAME
    @Transactional
    @PutMapping("/rename")
    public ResponseEntity<String> renamePlaylist(@RequestParam("email") String email, @RequestParam("playlist_name") String playlist_name) throws UserException, PlaylistException {
        String str3 = playlistService.renamePlaylist(email,playlist_name);
        return ResponseEntity.ok(str3);
    }
    // CLEAR
    @Transactional
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearPlaylist(@RequestParam("email") String email) throws UserException {
        String str3 = playlistService.clearPlaylist(email);
        return ResponseEntity.ok(str3);
    }

    // GET A PLAYLIST
    @Transactional
    @GetMapping("/getPlaylistName")
    public ResponseEntity<String> getPlaylistName(@RequestParam("email") String email) throws UserException, PlaylistException {
        String str1 = playlistService.getPlaylistName(email);
        return ResponseEntity.ok(str1);
    }
    @Transactional
    @PutMapping("/setPlaylistName")
    public ResponseEntity<String> setPlaylistName(@RequestParam("email") String email, @RequestParam("name") String name) throws Exception {
        String str1 = playlistService.namePlaylist(email, name);
        return ResponseEntity.ok(str1);
    }




}
