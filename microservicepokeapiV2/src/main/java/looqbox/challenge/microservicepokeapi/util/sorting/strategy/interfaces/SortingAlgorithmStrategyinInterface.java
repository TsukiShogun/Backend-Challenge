package looqbox.challenge.microservicepokeapi.util.sorting.strategy.interfaces;

import java.util.List;

public interface SortingAlgorithmStrategyinInterface<T> {
    void sort(List<T> items);
}