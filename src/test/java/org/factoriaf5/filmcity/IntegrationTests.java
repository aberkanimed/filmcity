package org.factoriaf5.filmcity;

import org.factoriaf5.filmcity.domain.Movie;
import org.factoriaf5.filmcity.repositories.MovieRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class IntegrationTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    MovieRepository movieRepository;


    @BeforeEach
    void setUp() {
        movieRepository.deleteAll();
    }

    @Test
    void returnsTheExistingMovies() throws Exception {

        addSampleMovies();

        mockMvc.perform(get("/movies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]", hasSize(2)))
                .andExpect(jsonPath("$[0].title", equalTo("Jurassic Park")))
                .andExpect(jsonPath("$[0].coverImage", equalTo("https://www.themoviedb.org/t/p/w600_and_h900_bestv2/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg")))
                .andExpect(jsonPath("$[0].director", equalTo("Steven Spielberg")))
                .andExpect(jsonPath("$[0].year", equalTo(1993)))
                .andExpect(jsonPath("$[0].synopsis", equalTo("A wealthy entrepreneur secretly creates a theme park featuring living dinosaurs drawn from prehistoric DNA.")))
                .andExpect(jsonPath("$[1].title", equalTo("Ratatouille")))
                .andExpect(jsonPath("$[1].coverImage", equalTo("https://www.themoviedb.org/t/p/w600_and_h900_bestv2/npHNjldbeTHdKKw28bJKs7lzqzj.jpg")))
                .andExpect(jsonPath("$[1].director", equalTo("Brad Bird")))
                .andExpect(jsonPath("$[1].year", equalTo(2007)))
                .andExpect(jsonPath("$[1].synopsis", equalTo("Remy, a resident of Paris, appreciates good food and has quite a sophisticated palate. He would love to become a chef so he can create and enjoy culinary masterpieces to his heart's delight. The only problem is, Remy is a rat.")))
                .andDo(print());
    }

    @Test
    void allowsToCreateNewMovie() throws Exception {
        mockMvc.perform(
                post("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"title\": \"Jurassic Park\", \"coverImage\": \"https://www.themoviedb.org/t/p/w600_and_h900_bestv2/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg\", \"director\": \"Steven Spielberg\", \"year\": 1993, \"synopsis\": \"A wealthy entrepreneur secretly creates a theme park featuring living dinosaurs drawn from prehistoric DNA.\" }")
        ).andExpect(status().isOk());

        List<Movie> listaMovies =  movieRepository.findAll();

        assertThat(listaMovies, contains(allOf(
                hasProperty("title", is("Jurassic Park")),
                hasProperty("coverImage", is("https://www.themoviedb.org/t/p/w600_and_h900_bestv2/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg")),
                hasProperty("director", is("Steven Spielberg")),
                hasProperty("year", is(1993)),
                hasProperty("synopsis", is("A wealthy entrepreneur secretly creates a theme park featuring living dinosaurs drawn from prehistoric DNA."))
        )));
    }

    @Test
    void allowsToFindMovieById() throws Exception {
        Movie movie = movieRepository.save(
                new Movie("Jurassic Park",
                        "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg",
                        "Steven Spielberg",
                        1993,
                        "A wealthy entrepreneur secretly creates a theme park featuring living dinosaurs drawn from prehistoric DNA.")
        );

        mockMvc
                .perform(get("/movies/"+movie.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", equalTo(movie.getTitle())))
                .andExpect(jsonPath("$.coverImage", equalTo(movie.getCoverImage())))
                .andExpect(jsonPath("$.director", equalTo(movie.getDirector())))
                .andExpect(jsonPath("$.year", equalTo(movie.getYear())))
                .andExpect(jsonPath("$.synopsis", equalTo(movie.getSynopsis())));
    }

    @Test
    void returnsNotFoundExceptionIfMovieToFindDoesNotExists() throws Exception {
        mockMvc.perform(get("/movies/20")).andExpect(status().isNotFound());
    }

    @Test
    void allowsToDeleteMovie() throws Exception {
        Movie movieToDelete = movieRepository.save(
                new Movie("Jurassic Park",
                        "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg",
                        "Steven Spielberg",
                        1993,
                        "A wealthy entrepreneur secretly creates a theme park featuring living dinosaurs drawn from prehistoric DNA.")
        );

        mockMvc.perform(delete("/movies/"+movieToDelete.getId())).andExpect(status().isOk());

        List<Movie> movie = movieRepository.findAll();

        assertThat(movie, not(contains(allOf(
                hasProperty("title", is(movieToDelete.getTitle())),
                hasProperty("coverImage", is(movieToDelete.getCoverImage())),
                hasProperty("director", is(movieToDelete.getDirector())),
                hasProperty("year", is(movieToDelete.getYear())),
                hasProperty("synopsis", is(movieToDelete.getSynopsis()))
        ))));
    }

    @Test
    void returnsNotFoundExceptionIfMovieToDeleteDoesNotExists() throws Exception {
        mockMvc.perform(delete("/movies/22")).andExpect(status().isNotFound());
    }

    @Test
    void allowsToModifyMovie() throws Exception {
        Movie movie = movieRepository.save(
                new Movie("Jurassic Park",
                        "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg",
                        "Steven Spielberg",
                        1993,
                        "A wealthy entrepreneur secretly creates a theme park featuring living dinosaurs drawn from prehistoric DNA.")
        );

        mockMvc.perform(
                put("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": \""+ movie.getId() +"\", \"title\": \"New Movie\", \"coverImage\": \"https://www.themoviedb.org/t/p/w600_and_h900_bestv2/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg\", \"director\": \"Steven Spielberg\", \"year\": 1993, \"synopsis\": \"A wealthy entrepreneur secretly creates a theme park featuring living dinosaurs drawn from prehistoric DNA.\" }")
        ).andExpect(status().isOk());

        List<Movie> movies = movieRepository.findAll();

        assertThat(movies, hasSize(1));
        assertThat(movies.get(0).getTitle(), equalTo("New Movie"));
    }

    @Test
    void returnsNotFoundExceptionIfMovieToModifyDoesNotExists() throws Exception {
        mockMvc.perform(
                put("/movies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"id\": \"50\", \"title\": \"New Movie\", \"coverImage\": \"https://www.themoviedb.org/t/p/w600_and_h900_bestv2/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg\", \"director\": \"Steven Spielberg\", \"year\": 1993, \"synopsis\": \"A wealthy entrepreneur secretly creates a theme park featuring living dinosaurs drawn from prehistoric DNA.\" }")
        ).andExpect(status().isNotFound());
    }

    @Test
    void allowsToBookMovie() throws Exception {
        Movie movie = movieRepository.save(
                new Movie("Jurassic Park",
                        "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg",
                        "Steven Spielberg",
                        1993,
                        "A wealthy entrepreneur secretly creates a theme park featuring living dinosaurs drawn from prehistoric DNA.")
        );

        mockMvc.perform(put("/movies/"+movie.getId()+"/book?renter=Mohamed")).andExpect(status().isOk());

        Movie rentedMovie = movieRepository.findById(movie.getId()).get();

        assertThat(rentedMovie.isBooked(), equalTo(true));
        assertThat(rentedMovie.getRenter(), equalTo("Mohamed"));
    }

    @Test
    void returnsNotFoundExceptionIfMovieToBookDoesNotExists() throws Exception {
        mockMvc.perform(put("/movies/55/book?renter=Mohamed")).andExpect(status().isNotFound());
    }

    @Test
    void allowsToReturnMovie() throws Exception {
        Movie movie = movieRepository.save(
                new Movie(
                        "Jurassic Park",
                        "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg",
                        "Steven Spielberg",
                        1993,
                        "A wealthy entrepreneur secretly creates a theme park featuring living dinosaurs drawn from prehistoric DNA.",
                        true,
                        "Mohamed"
                )
        );

        mockMvc.perform(put("/movies/"+movie.getId()+"/return")).andExpect(status().isOk());

        Movie returnedMovie = movieRepository.findById(movie.getId()).get();

        assertThat(returnedMovie.isBooked(), equalTo(false));
        assertThat(returnedMovie.getRenter(), equalTo(null));
    }

    @Test
    void returnsNotFoundExceptionIfMovieToReturnDoesNotExists() throws Exception {
        mockMvc.perform(put("/movies/88/return")).andExpect(status().isNotFound());
    }

    @Test
    void allowsToGiveMovieScore() throws Exception {
        Movie movie = movieRepository.save(
                new Movie(
                        "Jurassic Park",
                        "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg",
                        "Steven Spielberg",
                        1993,
                        "A wealthy entrepreneur secretly creates a theme park featuring living dinosaurs drawn from prehistoric DNA."
                )
        );

        mockMvc.perform(
                put("/movies/"+movie.getId()+"/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"score\": 2 }")
        ).andExpect(status().isOk());

        Movie ratingMovie = movieRepository.findById(movie.getId()).get();

        assertThat(ratingMovie.getRating(), equalTo(2));
    }

    @Test
    void returnsInvalidScoreExceptionIfScoreIsNotBetweenZeroAndFive() throws Exception {
        Movie movie = movieRepository.save(
                new Movie(
                        "Jurassic Park",
                        "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg",
                        "Steven Spielberg",
                        1993,
                        "A wealthy entrepreneur secretly creates a theme park featuring living dinosaurs drawn from prehistoric DNA."
                )
        );

        mockMvc.perform(
                put("/movies/"+movie.getId()+"/rating")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"score\": -1 }")
        ).andExpect(status().isBadRequest());
    }

    private void addSampleMovies() {
        List<Movie> movies = List.of(
                new Movie("Jurassic Park",
                        "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/oU7Oq2kFAAlGqbU4VoAE36g4hoI.jpg",
                        "Steven Spielberg",
                        1993,
                        "A wealthy entrepreneur secretly creates a theme park featuring living dinosaurs drawn from prehistoric DNA."),
                new Movie("Ratatouille",
                        "https://www.themoviedb.org/t/p/w600_and_h900_bestv2/npHNjldbeTHdKKw28bJKs7lzqzj.jpg",
                        "Brad Bird",
                        2007,
                        "Remy, a resident of Paris, appreciates good food and has quite a sophisticated palate. He would love to become a chef so he can create and enjoy culinary masterpieces to his heart's delight. The only problem is, Remy is a rat.")
        );

        movieRepository.saveAll(movies);
    }

}