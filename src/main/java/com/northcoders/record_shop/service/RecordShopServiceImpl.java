package com.northcoders.record_shop.service;

import com.northcoders.record_shop.model.Album;
import com.northcoders.record_shop.repository.RecordShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecordShopServiceImpl implements RecordShopService {

    @Autowired
    RecordShopRepository recordShopRepository;

    @Override
    public List<Album> getAllAlbums() {
        List<Album> albums = new ArrayList<>();
        recordShopRepository.findAll().forEach(albums::add);
        return albums;
    }

    @Override
    public Optional<Album> getAlbumById(Long id) {
        return recordShopRepository.findById(id);
    }

    @Override
    public Album postAlbum(Album album){
      return recordShopRepository.save(album);
    }
}
