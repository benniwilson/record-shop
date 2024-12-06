package com.northcoders.record_shop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.northcoders.record_shop.exception.AlbumNotFoundException;
import com.northcoders.record_shop.model.Album;
import com.northcoders.record_shop.model.Genre;
import com.northcoders.record_shop.service.RecordShopServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
class RecordShopControllerTest {

    @Mock
    RecordShopServiceImpl mockRecordShopService;

    @InjectMocks
    RecordShopController recordShopController;

    @Autowired
    MockMvc mockMvcController;
    ObjectMapper mapper;

    @BeforeEach
    public void setup(){
        mockMvcController = MockMvcBuilders.standaloneSetup(recordShopController).build();
        mapper = new ObjectMapper();
    }

    @Test
    @DisplayName("Returns an empty list and HttpStatus OK when there is no albums in the database")
    public void test_getAllAlbumsEmptyRepo() throws Exception {
        List<Album> albums = new ArrayList<>();
        when(mockRecordShopService.getAllAlbums()).thenReturn(albums);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/api/recordshop"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Returns an album list and HttpStatus OK when there is albums in the database")
    public void test_getAllAlbums() throws Exception {
        List<Album> albums = new ArrayList<>();
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

        albums.add(album1);
        albums.add(album2);

        when(this.mockRecordShopService.getAllAlbums()).thenReturn(albums);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/api/recordshop").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Beerbongs and Bentleys"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value("ASTROWORLD"))

        ;
    }

    @Test
    @DisplayName("An incorrect ID returns a Not found status code")
    public void test_getAlbumByIdWrongId() throws Exception {
        when(mockRecordShopService.getAlbumById(1L)).thenThrow(AlbumNotFoundException.class);
        this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/api/recordshop/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("A valid ID returns an Ok status code and an album object")
    public void test_getAlbumById() throws Exception {
        long id = 9L;
        Album album = Album.builder()
                .id(id)
                .name("Beerbongs and Bentleys")
                .genre(Genre.Pop)
                .price(8.99)
                .stock(5)
                .artist("Post Malone")
                .dateReleased(LocalDate.of(2018,4,27))
                .build();
        when(mockRecordShopService.getAlbumById(id)).thenReturn(album);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/api/recordshop/" + id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Beerbongs and Bentleys"));

    }

    @Test
    @DisplayName("Returns a bad request status code if the request body album is null")
    public void test_postAlbumNull() throws Exception {
        Album album = null;
        when(mockRecordShopService.postAlbum(album)).thenReturn(album);
        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/recordshop"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @DisplayName("Returns an OK status code if the album is posted successfully")
    public void test_postAlbum() throws Exception {
        Album album = Album.builder()
                .id(224L)
                .name("Beerbongs and Bentleys")
                .genre(Genre.Pop)
                .price(8.99)
                .stock(5)
                .artist("Post Malone")
                .dateReleased(LocalDate.of(2018,4,27))
                .build();

        when(mockRecordShopService.postAlbum(Mockito.any(Album.class))).thenReturn(album);

        mapper.registerModule(new JavaTimeModule());
        String requestBody = mapper.writeValueAsString(album);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/recordshop").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(224))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Beerbongs and Bentleys"));
    }

    @Test
    @DisplayName("Posting multiple albums works as expected")
    public void test_postAlbumMultiplePosts() throws Exception {
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

        when(mockRecordShopService.postAlbum(Mockito.any(Album.class))).thenReturn(album1);

        mapper.registerModule(new JavaTimeModule());
        String requestBody = mapper.writeValueAsString(album1);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/recordshop").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Beerbongs and Bentleys"));


        when(mockRecordShopService.postAlbum(Mockito.any(Album.class))).thenReturn(album2);

        mapper.registerModule(new JavaTimeModule());
        String requestBody1 = mapper.writeValueAsString(album2);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/recordshop").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody1).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("ASTROWORLD"));
    }

    @Test
    @DisplayName("An incorrect id returns a not found status code")
    public void test_putAlbumIncorrectId() throws Exception {
        long id = 1L;
        Album album = new Album();

        String requestBody = mapper.writeValueAsString(album);

        when(mockRecordShopService.putAlbum(album, id)).thenThrow(AlbumNotFoundException.class);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.put("/api/recordshop/1").content(requestBody).accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Updates the album object at the id given")
    public void test_putAlbum() throws Exception {
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
                .id(3L)
                .name("Beerbongs and Bentleys")
                .genre(Genre.Pop)
                .price(8.99)
                .stock(5)
                .artist("Post Malone")
                .dateReleased(LocalDate.of(2018,4,27))
                .build();

        when(mockRecordShopService.putAlbum(album2, id)).thenReturn(album2);
        mapper.registerModule(new JavaTimeModule());
        String requestBody1 = mapper.writeValueAsString(album2);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.put("/api/recordshop/3").contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody1).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Beerbongs and Bentleys"));
    }

    @Test
    @DisplayName("An invalid id returns a not found status code")
    public void test_deleteIdWrongId() throws Exception {
        long id = 1L;
        when(mockRecordShopService.deleteAlbum(id)).thenThrow(AlbumNotFoundException.class);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.delete("/api/recordshop/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("A valid id returns the deleted album")
    public void test_deleteId() throws Exception {
        long id  = 12L;
        Album album1 = Album.builder()
                .id(id)
                .name("ASTROWORLD")
                .stock(8)
                .genre(Genre.Rap)
                .price(10.99)
                .artist("Travis Scott")
                .dateReleased(LocalDate.of(2018, 8, 3))
                .build();

        when(mockRecordShopService.deleteAlbum(id)).thenReturn(album1);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.delete("/api/recordshop/"+id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(12))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("ASTROWORLD"));
    }
}