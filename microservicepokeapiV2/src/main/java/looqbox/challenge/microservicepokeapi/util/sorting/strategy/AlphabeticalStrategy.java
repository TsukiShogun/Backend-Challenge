package looqbox.challenge.microservicepokeapi.util.sorting.strategy;

import looqbox.challenge.microservicepokeapi.model.Pokemon;
import looqbox.challenge.microservicepokeapi.util.sorting.QuickSort;
import looqbox.challenge.microservicepokeapi.util.sorting.strategy.interfaces.SortingAlgorithmStrategyinInterface;

import java.util.Comparator;
import java.util.List;

public class AlphabeticalStrategy implements SortingAlgorithmStrategyinInterface<Pokemon> {
    @Override
    public void sort(List<Pokemon> items) {
        if (items == null || items.size() <= 1) return;
        Comparator<Pokemon> alphabeticalComparator = Comparator.comparing(Pokemon::getName);
        QuickSort.sort(items,alphabeticalComparator);

    }
}
