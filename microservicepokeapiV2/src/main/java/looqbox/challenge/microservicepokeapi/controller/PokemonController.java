package looqbox.challenge.microservicepokeapi.controller;

import looqbox.challenge.microservicepokeapi.model.PokedexResultHighlight;
import looqbox.challenge.microservicepokeapi.model.Pokemon;
import looqbox.challenge.microservicepokeapi.model.SimplePokedexResult;
import looqbox.challenge.microservicepokeapi.service.PokemonService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/pokemons")
public class PokemonController {

    private final PokemonService pokemonService;

    public PokemonController(PokemonService pokemonService) {
        this.pokemonService = pokemonService;
    }

    @GetMapping
    public ResponseEntity<SimplePokedexResult> getPokemons(@RequestParam(required = false) String query,
                                                           @RequestParam(defaultValue = "alphabetical") String sort) {
        List<Pokemon> pokemons = pokemonService.getAllPokemons();
        pokemons = pokemonService.filterPokemons(pokemons, query);
        pokemons = pokemonService.sortPokemons(pokemons, sort);
        List<String> pokemonsNames = pokemons.stream()
                .map(Pokemon::getName)
                .toList();

        return ResponseEntity.ok(new SimplePokedexResult(pokemonsNames));
    }
    @GetMapping("/highlight")
    public ResponseEntity<PokedexResultHighlight> getPokemonsWithHighlight(@RequestParam(required = false) String query,
                                                                           @RequestParam(defaultValue = "alphabetical") String sort){

        List<Pokemon> pokemons = pokemonService.getAllPokemons();
        List<Pokemon> filteredPokemons = pokemonService.filterPokemons(pokemons, query);
        List<Pokemon> sortedPokemons = pokemonService.sortPokemons(filteredPokemons, sort);


        List<Pokemon> highlightedPokemons = pokemonService.getPokemonsWithHighlight(sortedPokemons, query);

        return ResponseEntity.ok(new PokedexResultHighlight(highlightedPokemons));
    }

}