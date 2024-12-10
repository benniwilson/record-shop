package com.northcoders.record_shop.controller;


import com.northcoders.record_shop.model.Album;
import com.northcoders.record_shop.service.RecordShopService;
import com.northcoders.record_shop.service.RecordShopServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/recordshop")
public class RecordShopController{

    @Autowired
    private RecordShopServiceImpl recordShopService;

    @GetMapping
    public ResponseEntity<List<Album>> getAllAlbums(){
        return new ResponseEntity<>(recordShopService.getAllAlbums(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Album> getAlbumById(@PathVariable(name = "id") Long id){
        Album album = recordShopService.getAlbumById(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Album> postAlbum(@RequestBody Album album){
        return new ResponseEntity<>(recordShopService.postAlbum(album), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Album> putAlbum(@RequestBody Album album, @PathVariable(name = "id") long id){
        return new ResponseEntity<>(recordShopService.putAlbum(album,id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Album> deleteAlbum(@PathVariable(name = "id") long id){
        Album album = recordShopService.deleteAlbum(id);
        return new ResponseEntity<>(album, HttpStatus.OK);
    }

}
