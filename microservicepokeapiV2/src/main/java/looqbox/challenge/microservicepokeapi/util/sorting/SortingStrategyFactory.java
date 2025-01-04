package looqbox.challenge.microservicepokeapi.util.sorting;

import looqbox.challenge.microservicepokeapi.exception.InvalidSortingStrategyException;
import looqbox.challenge.microservicepokeapi.exception.SortingException;
import looqbox.challenge.microservicepokeapi.model.Pokemon;
import looqbox.challenge.microservicepokeapi.util.sorting.strategy.AlphabeticalStrategy;
import looqbox.challenge.microservicepokeapi.util.sorting.strategy.LengthStrategy;
import looqbox.challenge.microservicepokeapi.util.sorting.strategy.interfaces.SortingAlgorithmStrategyinInterface;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Map;


@Component
public class SortingStrategyFactory {

    private final Map<String, SortingAlgorithmStrategyinInterface<Pokemon>> mapStrategy = Map.of(
            "alphabetical", new AlphabeticalStrategy(),
            "length", new LengthStrategy()
    );

    public List<Pokemon> sort(List<Pokemon> pokemons, String sortType) {
        String normalizedSortType = sortType != null ? sortType.trim().toLowerCase() : "";
        SortingAlgorithmStrategyinInterface<Pokemon> strategy = mapStrategy.get(normalizedSortType);

        if (sortType == null || sortType.trim().isEmpty()) {
            throw new InvalidSortingStrategyException("Sort type cannot be null or empty");
        }

        try {
            strategy.sort(pokemons);
        } catch (Exception ex) {
            throw new SortingException("An error occurred while sorting the Pokémon list", ex);
        }

        return pokemons;
    }
}
