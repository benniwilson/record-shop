package com.northcoders.record_shop.service;

import com.northcoders.record_shop.model.Album;
import com.northcoders.record_shop.model.Genre;
import com.northcoders.record_shop.repository.AlbumRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class AlbumServiceTest {

    @Mock
    AlbumRepository albumRepository;

    @InjectMocks
    AlbumServiceImpl albumService;

    @Test
    @DisplayName("getAllAlbumsreturns an empty list with an empty repo")
    public void test_getAllAlbumsEmptyRepo(){
        List<Album> albumList = new ArrayList<>();
        when(albumRepository.findAll()).thenReturn(albumList);
        List<Album> expected = new ArrayList<>();
        List<Album> result = albumService.getAllAlbums();
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("getAllAlbums returns a list of albums")
    public void test_getAllAlbums(){
        List<Album> albumList = new ArrayList();
        Album album1 = Album.builder()
                .id(1L)
                .name("Beerbongs and Bentleys")
                .genre(Genre.Pop)
                .price(8.99)
                .stock(5)
                .artist("Post Malone")
                .dateReleased(LocalDate.of(2018,4,27))
                .build();

        Album album2 = Album.builder()
                .id(2L)
                .name("ASTROWORLD")
                .stock(8)
                .genre(Genre.Rap)
                .price(10.99)
                .artist("Travis Scott")
                .dateReleased(LocalDate.of(2018, 8,3))
                .build();

        albumList.add(album1);
        albumList.add(album2);

        when(albumRepository.findAll()).thenReturn(albumList);

        List<Album> result = albumService.getAllAlbums();

        assertThat(result).isEqualTo(albumList);
        assertThat(result).hasSize(2);
    }

}