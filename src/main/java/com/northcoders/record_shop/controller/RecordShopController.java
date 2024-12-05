package com.northcoders.record_shop.controller;


import com.northcoders.record_shop.model.Album;
import com.northcoders.record_shop.service.RecordShopService;
import com.northcoders.record_shop.service.RecordShopServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<Optional<Album>> getAlbumById(@PathVariable(name = "id") Long id){
        Optional<Album> album = recordShopService.getAlbumById(id);
        if (album.isEmpty()){
            return new ResponseEntity<>(album, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(album, HttpStatus.OK);
    }
}
