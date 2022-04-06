package org.factoriaf5.filmcity.controllers;

import org.factoriaf5.filmcity.domain.Movie;
import org.factoriaf5.filmcity.repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class MoviesController {

    private final MovieRepository movieRepository;

    @Autowired
    MoviesController(MovieRepository movieRepository){
        this.movieRepository = movieRepository;
    }

    @GetMapping("/movies")
    public List<Movie> allMovies() {
        return movieRepository.findAll();
    }

    @PostMapping("/movies")
    public Movie create(@RequestBody Movie movie) {
        return movieRepository.save(movie);
    }

    @GetMapping("/movies/{id}")
    public Movie findById(@PathVariable Long id) {
        return movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
    }

    @DeleteMapping("/movies/{id}")
    public Movie deleteById(@PathVariable Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
        movieRepository.deleteById(id);
        return movie;
    }

    @PutMapping("/movies")
    public Movie update(@RequestBody Movie movie) {
        movieRepository.findById(movie.getId()).orElseThrow(MovieNotFoundException::new);
        return movieRepository.save(movie);
    }

    @PutMapping("/movies/{id}/book")
    public Movie book(@PathVariable Long id, @RequestParam String renter) {
        Movie movie = movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
        movie.setBooked(true);
        movie.setRenter(renter);
        return movieRepository.save(movie);
    }

    @PutMapping("/movies/{id}/return")
    public Movie unbook(@PathVariable Long id) {
        Movie movie = movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
        movie.setBooked(false);
        movie.setRenter(null);
        return movieRepository.save(movie);
    }

    @PutMapping("/movies/{id}/rating")
    public Movie rating(@PathVariable Long id, @RequestBody() Map<String, Integer> data) {
        int score = data.get("score");
        if(score < 0 || score > 5) {
            throw new InvalidScoreException();
        }
        Movie movie = movieRepository.findById(id).orElseThrow(MovieNotFoundException::new);
        movie.setRating(score);
        return movieRepository.save(movie);
    }
}