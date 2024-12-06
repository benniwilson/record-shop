package com.northcoders.record_shop.service;

import com.northcoders.record_shop.model.Album;

import java.util.List;
import java.util.Optional;

public interface RecordShopService {
    public List<Album> getAllAlbums();
    public Album getAlbumById(Long id);
    public Album postAlbum(Album album);
    public Album putAlbum(Album album, long id);
}
