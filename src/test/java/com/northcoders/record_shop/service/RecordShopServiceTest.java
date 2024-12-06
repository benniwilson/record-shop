package com.northcoders.record_shop.service;

import com.northcoders.record_shop.exception.AlbumNotFoundException;
import com.northcoders.record_shop.model.Album;
import com.northcoders.record_shop.model.Genre;
import com.northcoders.record_shop.repository.RecordShopRepository;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class RecordShopServiceTest {

    @Mock
    RecordShopRepository albumRepository;

    @InjectMocks
    RecordShopServiceImpl albumService;

    @Test
    @DisplayName("getAllAlbums returns an empty list with an empty repo")
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

    @Test
    @DisplayName("Method returns optional empty if id doesn't correspond to an album")
    public void test_getAlbumByIdWrongId(){
        long id = 1;
        Throwable exception = assertThrows(AlbumNotFoundException.class, () -> albumService.getAlbumById(id));
        assertEquals("No Album with id: 1, was found in the system", exception.getMessage());
    }

    @Test
    @DisplayName("Method returns album that the id corresponds to")
    public void test_getAlbumById(){
        long id = 27;
        Optional<Album> album = Optional.of(Album.builder()
                .id(id)
                .name("ASTROWORLD")
                .stock(8)
                .genre(Genre.Rap)
                .price(10.99)
                .artist("Travis Scott")
                .dateReleased(LocalDate.of(2018, 8, 3))
                .build());
        when(albumRepository.findById(id)).thenReturn(album);
        Album result = albumService.getAlbumById(id);
        assertThat(album.get()).isEqualTo(result);
    }

    @Test
    @DisplayName("Post album returns the posted album")
    public void test_postAlbum(){
        Album album1 = Album.builder()
                .id(1L)
                .name("Beerbongs and Bentleys")
                .genre(Genre.Pop)
                .price(8.99)
                .stock(5)
                .artist("Post Malone")
                .dateReleased(LocalDate.of(2018,4,27))
                .build();

        when(albumRepository.save(album1)).thenReturn(album1);
        Album result = albumService.postAlbum(album1);
        assertThat(result).isEqualTo(album1);
    }

    @Test
    @DisplayName("Post album returns null if the album is null")
    public void test_postAlbumNull(){
        Album album1 = null;
        when(albumRepository.save(album1)).thenReturn(album1);
        Album result = albumService.postAlbum(album1);
        assertThat(result).isEqualTo(album1);
    }

    @Test
    @DisplayName("Throws exception if passed a invalid id")
    public void test_putAlbumWrongId(){
        long id = 4;
        Album album1 = Album.builder()
                .id(1L)
                .name("Beerbongs and Bentleys")
                .genre(Genre.Pop)
                .price(8.99)
                .stock(5)
                .artist("Post Malone")
                .dateReleased(LocalDate.of(2018,4,27))
                .build();
        Throwable exception = assertThrows(AlbumNotFoundException.class, () -> albumService.putAlbum(album1,id));
        assertEquals("No Album with id: 4, was found in the system", exception.getMessage());
    }

    @Test
    @DisplayName("Updates the values when passed a valid id")
    public void test_putAlbum(){
        long id = 3;
        Album album1 = Album.builder()
                .id(id)
                .name("ASTROWORLD")
                .stock(8)
                .genre(Genre.Rap)
                .price(10.99)
                .artist("Travis Scott")
                .dateReleased(LocalDate.of(2018, 8, 3))
                .build();

       Album album2 = Album.builder()
                .name("Beerbongs and Bentleys")
                .genre(Genre.Pop)
                .price(8.99)
                .stock(5)
                .artist("Post Malone")
                .dateReleased(LocalDate.of(2018,4,27))
                .build();

        when(albumRepository.findById(id)).thenReturn(Optional.of(album1));
        when(albumRepository.save(album2)).thenReturn(album2);
        Album result = albumService.putAlbum(album2, id);
        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getName()).isEqualTo("Beerbongs and Bentleys");
    }

    @Test
    @DisplayName("Throws an exception when passed an invalid id")
    public void test_deleteAlbumWrongId(){
        long id = 4;
        Throwable exception = assertThrows(AlbumNotFoundException.class, () -> albumService.deleteAlbum(id));
        assertEquals("No Album with id: 4, was found in the system", exception.getMessage());
    }

    @Test
    @DisplayName("Returns the album that was deleted when passed a valid id")
    public void test_deleteAlbum(){
        long id = 1L;
        Album album2 = Album.builder()
                .id(1L)
                .name("Beerbongs and Bentleys")
                .genre(Genre.Pop)
                .price(8.99)
                .stock(5)
                .artist("Post Malone")
                .dateReleased(LocalDate.of(2018,4,27))
                .build();

        when(albumRepository.findById(id)).thenReturn(Optional.of(album2));
        Album result = albumService.deleteAlbum(id);
        assertThat(result).isEqualTo(album2);
    }

    @Test
    @DisplayName("Method returns the correct Map of which attributes are null/empty")
    public void test_nullAttributeCatcher(){
        Album album1 = Album.builder()
                .id(3L)
                .dateReleased(LocalDate.of(2023, 12, 4))
                .name("Post Malone")
                .build();

        Album album2 = Album.builder()
                .stock(-5)
                .price(-13)
                .build();

        Album album3 = Album.builder()
                .id(13L)
                .price(3.99)
                .stock(12)
                .genre(Genre.Country)
                .dateReleased(LocalDate.of(2024,12,6))
                .artist("Travis Scott")
                .name("ASTROWORLD")
                .build();

        Map<String, Boolean> expected1 = Map.of("id", false, "name", false, "artist", true
        ,"dateReleased",false,"genre",true,"stock",false, "price", true);

        Map<String, Boolean> expected2 = Map.of("id", true, "name", true, "artist", true
                ,"dateReleased",true,"genre",true,"stock",true, "price", true);

        Map<String, Boolean> expected3 = Map.of("id", false, "name", false, "artist", false
                ,"dateReleased",false,"genre",false,"stock",false, "price", false);

        Map<String, Boolean> result1 = albumService.nullAttributeCatcher(album1);
        Map<String, Boolean> result2 = albumService.nullAttributeCatcher(album2);
        Map<String, Boolean> result3 = albumService.nullAttributeCatcher(album3);

        System.out.println(result3.toString());
        assertTrue(result1.equals(expected1));
        assertTrue(result2.equals(expected2));
        assertTrue(result3.equals(expected3));
    }
}