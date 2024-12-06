package com.northcoders.record_shop.service;

import com.northcoders.record_shop.exception.AlbumNotFoundException;
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
    public Album getAlbumById(Long id) {
        Optional<Album> album = recordShopRepository.findById(id);
        if (album.isPresent()){
            return album.get();
        }else{
            throw new AlbumNotFoundException(String.format("No Album with id: %s, was found in the system", id));
        }
    }

    @Override
    public Album postAlbum(Album album){
      return recordShopRepository.save(album);
    }

    @Override
    public Album putAlbum(Album album, long id) {
        Optional<Album> optionalAlbum = recordShopRepository.findById(id);
        if (optionalAlbum.isPresent()){
            album.setId(id);
            return recordShopRepository.save(album);
        }else{
            throw new AlbumNotFoundException(String.format("No Album with id: %s, was found in the system",id));
        }
    }

    @Override
    public Album deleteAlbum(long id) {
        Optional<Album> album = recordShopRepository.findById(id);
        Album deletedAlbum;
        if (album.isPresent()){
            deletedAlbum = album.get();
            recordShopRepository.deleteById(id);
            return deletedAlbum;
        }else{
            throw new AlbumNotFoundException(String.format("No Album with id: %s, was found in the system",id));
        }
    }
}
