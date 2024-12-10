package com.northcoders.record_shop.service;

import com.northcoders.record_shop.exception.AlbumNotFoundException;
import com.northcoders.record_shop.exception.NullAttributeException;
import com.northcoders.record_shop.model.Album;
import com.northcoders.record_shop.repository.RecordShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;


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
        if (album.isPresent()) {
            return album.get();
        } else {
            throw new AlbumNotFoundException(String.format("No Album with id: %s, was found in the system", id));
        }
    }

    @Override
    public Album postAlbum(Album album) {
        Map<String, Boolean> nullCheckMap = nullAttributeCatcher(album);
        List<String> nullKeys = new ArrayList<>();
        if (nullCheckMap.containsValue(true)) {
            for (var e : nullCheckMap.entrySet()) {
                if (e.getValue().equals(true)) {
                    nullKeys.add(e.getKey());
                }
            }
            throw new NullAttributeException("The following attributes are invalid: " + nullKeys.toString());
        }
        return recordShopRepository.save(album);
    }

    @Override
    public Album putAlbum(Album album, long id) {
        Map<String, Boolean> nullCheckMap = nullAttributeCatcher(album);
        List<String> nullKeys = new ArrayList<>();
        if (nullCheckMap.containsValue(true)) {
            for (var e : nullCheckMap.entrySet()) {
                if (e.getValue().equals(true)) {
                    nullKeys.add(e.getKey());
                }
            }
            throw new NullAttributeException("The following attributes are invalid: " + nullKeys.toString());
        }
        Optional<Album> optionalAlbum = recordShopRepository.findById(id);
        if (optionalAlbum.isPresent()) {
            album.setId(id);
            return recordShopRepository.save(album);
        } else {
            throw new AlbumNotFoundException(String.format("No Album with id: %s, was found in the system", id));
        }
    }

    @Override
    public Album deleteAlbum(long id) {
        Optional<Album> album = recordShopRepository.findById(id);
        Album deletedAlbum;
        if (album.isPresent()) {
            deletedAlbum = album.get();
            recordShopRepository.deleteById(id);
            return deletedAlbum;
        } else {
            throw new AlbumNotFoundException(String.format("No Album with id: %s, was found in the system", id));
        }
    }

    @Override
    public Map<String, Boolean> nullAttributeCatcher(Album album) {
        Map<String, Boolean> mapAlbum = new HashMap<>();
        if (album.getId() < 0) {
            mapAlbum.put("id", true);
        } else {
            mapAlbum.put("id", false);
        }
        if (album.getName() == null || album.getName().isEmpty()) {
            mapAlbum.put("name", true);
        } else {
            mapAlbum.put("name", false);
        }
        if (album.getArtist() == null || album.getArtist().isEmpty()) {
            mapAlbum.put("artist", true);
        } else {
            mapAlbum.put("artist", false);
        }
        if (album.getDateReleased() == null) {
            mapAlbum.put("dateReleased", true);
        } else {
            mapAlbum.put("dateReleased", false);
        }
        if (album.getGenre() == null) {
            mapAlbum.put("genre", true);
        } else {
            mapAlbum.put("genre", false);
        }
        if (album.getStock() < 0) {
            mapAlbum.put("stock", true);
        } else {
            mapAlbum.put("stock", false);
        }
        if (album.getPrice() <= 0.0) {
            mapAlbum.put("price", true);
        } else {
            mapAlbum.put("price", false);
        }
        return mapAlbum;
    }
}
