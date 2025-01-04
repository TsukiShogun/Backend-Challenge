# PokéAPI REST Service

This project is a solution to the Looqbox Backend Challenge.

## Table of Contents

- [How to Run the Project](#how-to-run-the-project)
- [Endpoints](#endpoints)
- [How the Sorting Algorithm Works](#how-the-sorting-algorithm-works)
- [Bottlenecks and Proposed Solutions](#bottlenecks-and-proposed-solutions)

---

## How to Run the Project

### Prerequisites

- Java 21.
- Docker and Docker Compose (optional, for running in containers).

### Running with Docker

Clone this repository:

```bash
git clone https://github.com/TsukiShogun/Backend-Challenge/tree/main
cd Backend-Challenge
 ```
To build the Docker image and run the service, execute:
    
    docker-compose up --build
    
The service will be accessible at http://localhost:8080.

Running Locally

If you prefer to run the project locally, use Gradle:

    ./gradlew bootRun

The service will be available at http://localhost:8080.


---

## Endpoints

GET /pokemons

Parameters:
- query (optional): Filters pokémons by name, accepting a substring of the pokémon name (case insensitive).
- sort (optional): Specifies the sorting type. Can be:
    - alphabetical (ascending order of the name, default).
    - length (ascending order by the length of the name).

Example Response (if query is "pi"):

    {
    "result": [
    "pichu",
    "pikachu"
    ]
    }

GET /pokemons/highlight

Parameters:
- query (optional): Filters pokémons by name, accepting a substring of the pokémon name (case insensitive).
- sort (optional): Specifies the sorting type. Can be:
    - alphabetical (ascending order of the name, default).
    - length (ascending order by the length of the name).

Example Response (if query is "pi"):

    {
    "result": [
    {
    "name": "pichu",
    "highlight": "<pre>pi</pre>chu"
    },
    {
    "name": "pikachu",
    "highlight": "<pre>pi</pre>kachu"
    }
    ]
    }

---


## How the Sorting Algorithm Works

The chosen sorting algorithm is QuickSort. QuickSort is an efficient sorting algorithm that follows the "divide and conquer" approach. It works by selecting a "pivot", partitioning the list into two sublists (one with elements smaller than the pivot and another with elements greater), and then recursively sorting the sublists.

Big-θ Complexity Analysis

Best Case: The complexity is θ(n log n), which occurs when the pivot divides the list into two approximately equal parts. 
Average Case: The complexity is also θ(n log n) for random lists, which is the expected behavior for most inputs. 
Worst Case: The complexity in the worst case is θ(n²), which occurs when the pivot chosen is always the smallest or largest element in the list, resulting in unbalanced partitions. This can happen, for example, if the list is already sorted or inversely sorted.

Advantages:

Efficiency for large datasets: QuickSort is very efficient for large lists due to its average time complexity of θ(n log n).
In-place sorting: The algorithm does not require significant extra space beyond the recursion stack.

Disadvantages:

Not stable: The relative order of equal elements may be altered during sorting. 
Stack overflow risk: The algorithm may cause a stack overflow if the recursion depth is too large in extreme cases (e.g., very large or already sorted lists).
---

## Bottlenecks and Proposed Solutions

### Bottleneck 1: Filtering and Sorting Operations

Currently, the filtering and sorting operations of Pokémon are being performed on every request, even when the data remains the same. This leads to a waste of resources, especially when there is a large number of Pokémon.

**Proposed Solution:**  
Implement a cache to store the results of filtered and sorted queries. This way, if the same query is executed multiple times, the system can return the cached results, avoiding the repetition of filtering and sorting operations. This would reduce response time and decrease the load on the system.

### Bottleneck 2: Sorting with QuickSort

The QuickSort algorithm, while efficient in many cases, can become problematic when the pivot is poorly chosen, such as in sorted lists or lists with a large number of elements. This can lead to the worst-case time complexity, i.e., O(n²), making the sorting process slower.

**Proposed Solution:**  
Improve pivot selection. A simple strategy would be to use the "median pivot," which can improve performance in cases of sorted or nearly sorted lists. Another alternative is to use hybrid sorting algorithms, such as Timsort (used in Python), which combines the efficiency of MergeSort and QuickSort.

### Bottleneck 3: Cache Expiration and Cleanup

Currently, cache cleanup is performed every minute using `scheduleAtFixedRate`, which can result in overhead, especially if the cache is large. Additionally, cleanup may be unnecessary at times, as the cache isn't always full or in need of cleanup.

**Proposed Solution:**  
Make the cache cleanup process more adaptive, based on metrics like cache size or access frequency. For instance, cleanup could be triggered when the cache reaches a certain size or after a period of inactivity. This would prevent unnecessary cleanup tasks from running continuously, saving resources.

### Bottleneck 4: Thread Contention in Cache

Although `ConcurrentHashMap` supports high concurrency, in a high-traffic scenario, multiple threads accessing and modifying the cache simultaneously can cause contention. This happens when several threads try to access or update the cache at the same time, which can lead to degraded performance.

**Proposed Solution:**  
Implement batch operations for the cache. Instead of performing individual operations for each access, you could group requests and update the cache in fewer operations. This would reduce contention. Additionally, a good alternative would be to adopt a more advanced caching solution, such as [Caffeine](https://github.com/ben-manes/caffeine), which offers more efficient and scalable caching algorithms like LRU (Least Recently Used) and is better equipped to handle high volumes of requests.

---