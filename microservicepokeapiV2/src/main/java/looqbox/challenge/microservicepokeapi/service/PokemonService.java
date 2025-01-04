package looqbox.challenge.microservicepokeapi.service;

import looqbox.challenge.microservicepokeapi.exception.ApiRequestException;
import looqbox.challenge.microservicepokeapi.infra.CacheComponet;
import looqbox.challenge.microservicepokeapi.infra.HttpRequestComponet;
import looqbox.challenge.microservicepokeapi.model.PokedexResultHighlight;
import looqbox.challenge.microservicepokeapi.model.Pokemon;
import looqbox.challenge.microservicepokeapi.util.sorting.SortingStrategyFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PokemonService {

    private final HttpRequestComponet httpRequestComponet;
    private final SortingStrategyFactory sortingStrategyFactory;
    private final CacheComponet<String, List<Pokemon>> cacheComponet;

    @Value("${pokeapi.url}")
    private String pokeApiUrl;
    private static final long CACHE_TTL = 5L * 60 * 1000;


    public PokemonService(HttpRequestComponet httpRequestComponet, SortingStrategyFactory sortingStrategyFactory, CacheComponet<String, List<Pokemon>> cacheComponet) {
        this.httpRequestComponet = httpRequestComponet;
        this.sortingStrategyFactory = sortingStrategyFactory;
        this.cacheComponet = cacheComponet;
    }

    public List<Pokemon> getAllPokemons() {
        String cacheKey = "all_pokemons";


        List<Pokemon> cachedPokemons = cacheComponet.get(cacheKey);
        if (cachedPokemons != null) {

            return cachedPokemons;
        }


        ResponseEntity<PokedexResultHighlight> response = httpRequestComponet.sendGetRequest(pokeApiUrl, PokedexResultHighlight.class);
        List<Pokemon> pokemons = Optional.ofNullable(response.getBody())
                .map(PokedexResultHighlight::getResults)
                .orElseThrow(() -> new ApiRequestException("Empty or invalid response from PokéAPI"));


        cacheComponet.put(cacheKey, pokemons, CACHE_TTL);
        return pokemons;
    }


    public List<Pokemon> filterPokemons(List<Pokemon> pokemons, String query) {
        if (query == null || query.isEmpty()) {
            return pokemons;
        }
        String lowerCaseQuery = query.toLowerCase();
        return pokemons.stream()
                .filter(pokemon -> pokemon.getName().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());
    }

    public List<Pokemon> sortPokemons(List<Pokemon> pokemons, String sortType) {
        return sortingStrategyFactory.sort(pokemons, sortType);
    }


    public List<Pokemon> getPokemonsWithHighlight(List<Pokemon> pokemons, String query) {
        if (query == null || query.isEmpty()) {
            return pokemons;
        }
        return pokemons.stream()
                .map(pokemon -> {
                    String name = pokemon.getName();
                    String highlightedName = name.toLowerCase().contains(query.toLowerCase()) ?
                            name.replace(query, "<pre>" + query + "</pre>") : name;
                    pokemon.setHighlight(highlightedName);
                    return pokemon;
                })
                .collect(Collectors.toList());
    }
}
