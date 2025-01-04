package looqbox.challenge.microservicepokeapi.util.sorting;

import looqbox.challenge.microservicepokeapi.exception.SortingException;
import looqbox.challenge.microservicepokeapi.model.Pokemon;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * Logic Explanation:
 * QuickSort is a "divide-and-conquer" algorithm. It works by selecting a pivot element, partitioning the list
 * around this pivot, and recursively sorting the sublists on either side of the pivot.

 * Big-θ Analysis:
 * - Best Case: θ(n log n), occurs when the pivot divides the list into two nearly equal parts.
 * - Average Case: θ(n log n), generally observed for randomly ordered lists.
 * - Worst Case: θ(n²), occurs when the pivot consistently results in unbalanced partitions (e.g., already sorted list with bad pivot selection).

 * Advantages:
 * - Efficient for large datasets.
 * - In-place sorting (requires no extra space apart from the recursion stack).

 * Disadvantages:
 * - Not stable (relative order of equal elements may not be preserved).
 * - Recursive nature can lead to stack overflow for very large inputs.
 */
public class QuickSort {

    // Private constructor to prevent instantiation of this utility class.
    private QuickSort() {
        throw new UnsupportedOperationException("Utility class");
    }

    /**
     * Sorts a list of Pokémon using the QuickSort algorithm.
     *
     * @param items      The list of Pokémon to sort.
     * @param comparator The comparator defining the sorting criteria.
     * @throws SortingException If the list is empty or if any error occurs during sorting.
     */
    public static void sort(List<Pokemon> items, Comparator<Pokemon> comparator) {

        Objects.requireNonNull(items, "List of items cannot be null.");
        Objects.requireNonNull(comparator, "Comparator cannot be null.");

        if (items.isEmpty()) {
            throw new SortingException("List of items cannot be empty.");
        }


        quickSort(items, 0, items.size() - 1, comparator);
    }

    /**
     * Recursive QuickSort function that partitions the list and sorts the sublists.
     *
     * @param items      The list to sort.
     * @param low        The starting index of the current partition.
     * @param high       The ending index of the current partition.
     * @param comparator The comparator defining the sorting criteria.
     */
    private static void quickSort(List<Pokemon> items, int low, int high, Comparator<Pokemon> comparator) {

        if (low < high) {

            int pivotIndex = partition(items, low, high, comparator);


            quickSort(items, low, Math.min(pivotIndex - 1, high), comparator);

            quickSort(items, Math.max(pivotIndex + 1, low), high, comparator);
        }
    }

    /**
     * Partitions the list around a pivot element, placing smaller elements on the left
     * and larger elements on the right.
     *
     * @param items      The list to partition.
     * @param low        The starting index of the partition.
     * @param high       The ending index of the partition.
     * @param comparator The comparator defining the sorting criteria.
     * @return The index of the pivot after partitioning.
     */
    private static int partition(List<Pokemon> items, int low, int high, Comparator<Pokemon> comparator) {
        validateIndices(items, low, high);

        Pokemon pivot = items.get(high);
        int leftPointer = low - 1;

        for (int rightPointer = low; rightPointer < high; rightPointer++) {
            try {
                if (comparator.compare(items.get(rightPointer), pivot) <= 0) {
                    leftPointer++;

                    swap(items, leftPointer, rightPointer);
                }
            } catch (Exception e) {
                throw new SortingException("Error occurred during comparison of Pokémon.", e);
            }
        }

        swap(items, leftPointer + 1, high);
        return leftPointer + 1;
    }

    /**
     * Swaps two elements in the list at the specified indices.
     *
     * @param items      The list where the swap occurs.
     * @param leftIndex  The index of the first element.
     * @param rightIndex The index of the second element.
     */
    private static void swap(List<Pokemon> items, int leftIndex, int rightIndex) {
        validateIndices(items, leftIndex, rightIndex);

        Pokemon temp = items.get(leftIndex);
        items.set(leftIndex, items.get(rightIndex));
        items.set(rightIndex, temp);
    }

    /**
     * Validates that the indices are within the bounds of the list.
     *
     * @param items      The list being checked.
     * @param leftIndex  The index of the first element.
     * @param rightIndex The index of the second element.
     * @throws SortingException If the indices are invalid.
     */
    private static void validateIndices(List<Pokemon> items, int leftIndex, int rightIndex) {
        if (leftIndex < 0 || rightIndex < 0 || leftIndex >= items.size() || rightIndex >= items.size()) {
            throw new SortingException("Invalid indices: leftIndex=" + leftIndex + ", rightIndex=" + rightIndex);
        }
    }
}
