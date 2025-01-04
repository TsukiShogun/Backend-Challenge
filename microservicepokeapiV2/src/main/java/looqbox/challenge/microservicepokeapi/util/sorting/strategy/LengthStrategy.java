package looqbox.challenge.microservicepokeapi.util.sorting.strategy;

import looqbox.challenge.microservicepokeapi.model.Pokemon;
import looqbox.challenge.microservicepokeapi.util.sorting.QuickSort;
import looqbox.challenge.microservicepokeapi.util.sorting.strategy.interfaces.SortingAlgorithmStrategyinInterface;

import java.util.Comparator;
import java.util.List;

public class LengthStrategy implements SortingAlgorithmStrategyinInterface<Pokemon> {

    @Override
    public void sort(List<Pokemon> items) {
        if (items == null || items.size() <= 1) return;

        Comparator<Pokemon> lengthComparator = Comparator.comparingInt(pokemon -> pokemon.getName().length());

        QuickSort.sort(items, lengthComparator);

    }
}
