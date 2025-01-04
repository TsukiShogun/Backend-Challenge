package looqbox.challenge.microservicepokeapi.service;

import looqbox.challenge.microservicepokeapi.exception.ApiRequestException;
import looqbox.challenge.microservicepokeapi.infra.CacheComponet;
import looqbox.challenge.microservicepokeapi.infra.HttpRequestComponet;
import looqbox.challenge.microservicepokeapi.model.PokedexResultHighlight;
import looqbox.challenge.microservicepokeapi.model.Pokemon;
import looqbox.challenge.microservicepokeapi.util.sorting.SortingStrategyFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@TestPropertySource(properties = {"pokeapi.url=https://pokeapi.co/api/v2/pokemon?limit=1500"})
@ExtendWith(MockitoExtension.class)
class PokemonServiceTest {

    @Mock
    private HttpRequestComponet httpRequestComponet;

    @Mock
    private SortingStrategyFactory sortingStrategyFactory;
    @Mock
    private CacheComponet<String, List<Pokemon>> cacheComponet;

    @InjectMocks
    private PokemonService pokemonService;

    @Value("${pokeapi.url}")
    private String pokeApiUrl;

    private final ResponseEntity mockResponse = mock(ResponseEntity.class);
    private List<Pokemon> mockPokemons = List.of(
            new Pokemon("bulbasaur"),
            new Pokemon("pikachu"),
            new Pokemon("charizard")
    );

    @Test
    @DisplayName("should return a list of pokemons when the API response is valid")
    void getAllPokemons_shouldReturnPokemons() {


        when(httpRequestComponet.sendGetRequest(pokeApiUrl, PokedexResultHighlight.class))
                .thenReturn(mockResponse);

        PokedexResultHighlight pokedexResultHighlight = new PokedexResultHighlight();
        pokedexResultHighlight.setResults(mockPokemons);

        when(mockResponse.getBody()).thenReturn(pokedexResultHighlight);

        List<Pokemon> pokemons = pokemonService.getAllPokemons();

        assertNotNull(pokemons);
        assertEquals(3, pokemons.size());
        assertEquals("bulbasaur", pokemons.get(0).getName());
        assertEquals("pikachu", pokemons.get(1).getName());
        assertEquals("charizard", pokemons.get(2).getName());

    }

    @Test
    @DisplayName("should throw ApiRequestException when the API response is empty or null")
    void getAllPokemons_shouldThrowApiRequestExceptionWhenEmptyResponse() {
        when(httpRequestComponet.sendGetRequest(pokeApiUrl, PokedexResultHighlight.class))
                .thenReturn(mockResponse);

        when(mockResponse.getBody()).thenReturn(null);

        assertThrows(ApiRequestException.class, () -> pokemonService.getAllPokemons());
    }

    @Nested
    @DisplayName("Filter Pokemons")
    class FilterPokemons {

        @Test
        @DisplayName("should filter pokemons correctly based on query")
        void shouldFilterPokemonsByQuery() {

            List<Pokemon> filteredPokemons = pokemonService.filterPokemons(mockPokemons, "chu");

            assertEquals(1, filteredPokemons.size());
            assertTrue(filteredPokemons.stream().anyMatch(pokemon -> pokemon.getName().equals("pikachu")));
            assertFalse(filteredPokemons.stream().anyMatch(pokemon -> pokemon.getName().equals("charizard")));
        }

        @Test
        @DisplayName("should return all pokemons when the query is empty")
        void shouldReturnAllPokemonsWhenQueryIsEmpty() {
            List<Pokemon> filteredPokemons = pokemonService.filterPokemons(mockPokemons, "");

            assertEquals(3, filteredPokemons.size());
        }

        @Test
        @DisplayName("should return all pokemons when the query is null")
        void shouldReturnAllPokemonsWhenQueryIsNull() {
            List<Pokemon> filteredPokemons = pokemonService.filterPokemons(mockPokemons, null);

            assertEquals(3, filteredPokemons.size());
        }
    }

    @Nested
    @DisplayName("Sort Pokemons")
    class SortPokemons {

        @Mock
        private SortingStrategyFactory mockSortingStrategyFactory;

        @Test
        @DisplayName("should sort pokemons alphabetically in ascending order")
        void shouldSortPokemonsAlphabetically() {

            List<Pokemon> sortedPokemons = List.of(
                    new Pokemon("bulbasaur"),
                    new Pokemon("charizard"),
                    new Pokemon("pikachu")
            );

            when(sortingStrategyFactory.sort(mockPokemons, "alphabetical"))
                    .thenReturn(sortedPokemons);

            List<Pokemon> sortedPokemonsResult  = pokemonService.sortPokemons(mockPokemons, "alphabetical");

            assertNotNull(sortedPokemonsResult);
            assertEquals("bulbasaur", sortedPokemonsResult .get(0).getName());
            assertEquals("charizard", sortedPokemonsResult .get(1).getName());
            assertEquals("pikachu", sortedPokemonsResult .get(2).getName());
        }

        @Test
        @DisplayName("should sort pokemons by name length in ascending order")
        void shouldSortPokemonsByLength() {
            List<Pokemon> sortedPokemons = List.of(
                    new Pokemon("pikachu"),
                    new Pokemon("bulbasaur"),
                    new Pokemon("charizard")
            );

            when(sortingStrategyFactory.sort(mockPokemons, "length"))
                    .thenReturn(sortedPokemons);

            List<Pokemon> sortedPokemonsResult = pokemonService.sortPokemons(mockPokemons, "length");

            assertNotNull(sortedPokemonsResult);
            assertEquals("pikachu", sortedPokemonsResult.get(0).getName());
            assertEquals("bulbasaur", sortedPokemonsResult.get(1).getName());
            assertEquals("charizard", sortedPokemonsResult.get(2).getName());
        }
    }

    @Nested
    @DisplayName("Highlight Pokemons")
    class HighlightPokemons {

        @Test
        @DisplayName("should highlight the pokemon name correctly when query is matched")
        void shouldHighlightPokemonName() {
            String query = "chu";
            Pokemon pokemon = new Pokemon("pikachu");

            List<Pokemon> highlightedPokemons = pokemonService.getPokemonsWithHighlight(List.of(pokemon), query);

            assertEquals("pika<pre>chu</pre>", highlightedPokemons.get(0).getHighlight());
        }

        @Test
        @DisplayName("should not highlight the pokemon name when query is null")
        void shouldNotHighlightWhenQueryIsNull() {
            Pokemon pokemon = new Pokemon("pikachu");

            List<Pokemon> highlightedPokemons = pokemonService.getPokemonsWithHighlight(List.of(pokemon), null);

            assertNull(highlightedPokemons.get(0).getHighlight());
        }

        @Test
        @DisplayName("should not highlight the pokemon name when query is empty")
        void shouldNotHighlightWhenQueryIsEmpty() {
            Pokemon pokemon = new Pokemon("pikachu");

            List<Pokemon> highlightedPokemons = pokemonService.getPokemonsWithHighlight(List.of(pokemon), "");

            assertNull(highlightedPokemons.get(0).getHighlight());
        }
    }
    @Nested
    @DisplayName("Handle invalid API response body")
    class ApiRequestExceptionTests {

        @Test
        @DisplayName("should throw ApiRequestException when response body is null")
        void shouldThrowApiRequestExceptionWhenResponseIsNull() {
            ResponseEntity<PokedexResultHighlight> response = mock(ResponseEntity.class);

            when(httpRequestComponet.sendGetRequest(pokeApiUrl, PokedexResultHighlight.class)).thenReturn(response);
            when(response.getBody()).thenReturn(null);

            assertThrows(ApiRequestException.class, () -> pokemonService.getAllPokemons());
        }
    }
}
