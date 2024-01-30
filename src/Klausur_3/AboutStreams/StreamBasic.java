package Klausur_3.AboutStreams;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * There are 2 types of Operation in Stream:
 * <ol>
 *     <li><strong>Intermediate Operation</strong> = lazy, changes a Stream to another Stream</li>
 *     <li><strong>Terminal Operation</strong> = produce a result/ side effect, stream pipeline can't be used again</li>
 * </ol>
 */
public class StreamBasic<T> {
    /*
    ====================================================================================================================
                                               STREAM CREATION
    ====================================================================================================================
     */
    private final Stream<T> streamFromVarargs;
    private final Stream<T> streamFromArray;
    private final Stream<T> streamFromCollection; // List, Queue, etc.
    private final Stream<T> streamFromSupplier;
    private final Stream<T> emptyStream;

    // https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html
    @SafeVarargs
    public StreamBasic(Supplier<T> inputSupplier, Collection<T> inputCollection, T[] inputArray, T... inputVarargs){
        this.streamFromVarargs = Stream.of(inputVarargs);
        this.streamFromArray = Arrays.stream(inputArray);
        this.streamFromCollection = inputCollection.stream();

        // The Stream.generate() method in Java creates an infinite,
        // unordered stream where each element is generated by the provided Supplier.
        this.streamFromSupplier = Stream.generate(inputSupplier);

        this.emptyStream = Stream.empty(); // an empty sequential Stream (useful for case handling)
    }

    /*
    ====================================================================================================================
                                               Basic Methods
    .allMatch(), .anyMatch(), .noneMatch(), .limit(), .takeWhile(), .forEach(), .concat(), .distinct(), .count()
    .filter(), .map(), .peek(), .flatMap(), .collect(), .reduce()
    ====================================================================================================================
     */

    /**
     * Example usage of .allMatch(), .anyMatch(), and .noneMatch()
     * @param list List&lt;Integer&gt;
     */
    public static void checkIfPrime(List<Integer> list){
        // if no element is >= 2, then no prime
        if(list.stream().noneMatch(x -> x >= 2)){
            System.out.println("prime must be positive, the number 1 is not prime");
        }

        if(list.stream().allMatch(n -> n >= 2 && IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(i -> n % i == 0))){
            // check by division if all is prime (also short circuit check >= 2 for all)
            System.out.println("All is Prime");
        }
        else if(list.stream().anyMatch(n -> IntStream.rangeClosed(2, (int) Math.sqrt(n)).noneMatch(i -> n % i == 0))) {
            // check if any number >= 2 is prime
            System.out.println("At least an element is Prime");
        }
        else{
            // no element satisfy the division rule
            System.out.println("No Prime");
        }
    }

    /**
     * Suppose the list is infinite, using .limit() and .takeWhile() to reduce it, then .forEach() to print
     * @param list Integer
     * @param limit int
     */
    public static void positiveLimitedStream(List<Integer> list, int limit){
        Stream<Integer> result = list.stream()
                .limit(limit) // limit incoming Element to n-th element
                .takeWhile(x -> x > 0); // stops if an Element is not > 0

        result.forEach(System.out::println);
    }

    /**
     * Given two Streams, we use .concat() to combine, then .distinct() and .count() to calculate distinct elements count
     */
    public static void distinctElementsCount(Stream<Integer> stream1, Stream<Integer> stream2){
        Stream<Integer> concat = Stream.concat(stream1, stream2);
        Stream<Integer> concat1 = Stream.concat(stream1, stream2);

        int totalElementCount = (int) concat.count(); // terminal operation
        int distinctElementCount = (int) concat1.distinct().count(); // terminal operation

        System.out.println("Total Element: " + totalElementCount + ", Distinct Element: " + distinctElementCount);
    }

    /**
     * Example usage of .filter() and .map()
     */
    public static Stream<Integer> squareOfOdds(Stream<Integer> stream){
        return stream.filter(i -> i % 2 != 0)
                .map(i -> i * i); // modify each element in the stream based on given Function
    }

    /**
     * Example usage of .peek(), .flatMap(), .collect(), and .reduce()
     * <ul>
     *     <li>.peek() = Returns a copy of the stream, additionally performing action on each element (original stream is consumed)</li>
     *     <li>.flatMap() = changes a Stream of Collection to a Stream of its' elements</li>
     *     <li>.reduce() = Performs a reduction on the elements</li>
     *     <li>.collect() = Performs a mutable reduction operation on the elements of this stream using a Collector</li>
     * </ul>
     */
    public static void sumOfList_OfList_OfInteger(List<List<Integer>> listOfLists){
        // default Template
        listOfLists = Arrays.asList(
                Arrays.asList(1, 2, 3),
                Arrays.asList(4, 5, 6),
                Arrays.asList(7, 8, 9)
        );

        List<Integer> combinedList = listOfLists.stream()

                // prints each list (intermediate operation)
                .peek(System.out::println)

                // flattens the lists into a single stream (intermediate operation)
                .flatMap(List::stream)

                // prints each element in the flattened stream (intermediate operation)
                .peek(System.out::println)

                // collects the elements into a new list (.toList();)
                .collect(Collectors.toList());


        int elementSum = combinedList.stream()
                .reduce(0, (x, y) -> x + y); // reduces the list to the sum of its elements (Integer::sum)

        System.out.println("Sum: " + elementSum);
    }

    /*
    ====================================================================================================================
                                                   Extra Example
                                  .sorted(), Comparator.comparing(), .flatMap()
    ====================================================================================================================
     */

    /**
     * Sort a Stream using .sorted() and Comparator.comparing()
     */
    public static Stream<Human> sortHumanByAge(Stream<Human> stream, boolean descending){
        if(descending){
            return stream.sorted(Comparator.comparing(Human::getAge).reversed());
        }
        else{
            return stream.sorted(Comparator.comparing(Human::getAge));
        }
    }

    /**
     * Example of .flatMap()
     */
    public static Stream<Human> humanAndFrieds(Stream<Human> stream){
        // Stream.of(T t)= Returns a sequential Stream containing a single element.
        return stream.flatMap(human -> Stream.concat(Stream.of(human), Arrays.stream(Human.getFriends(human))));
    }

    /*
    ====================================================================================================================
                                             Stream to Other Data types
                        https://docs.oracle.com/javase/8/docs/api/java/util/stream/Collectors.html
    ====================================================================================================================
     */

    /**
     * Stream to List
     */
    public static List<Human> streamToList(Stream<Human> stream) {
        return stream.collect((Collectors.toList())); // alternative: stream.toList()
    }

    /**
     * Stream to Array
     */
    public static Human[] streamToArray(Stream<Human> stream) {
        return stream.toArray(Human[]::new);
        // return stream.toArray(size -> new Human[size]); // equivalent
    }

    /**
     * Stream to Map, with value is the sum. of each Human's Age
     */
    public static Map<Human.Ethnic, Integer> totalAgeBasedOnEthnic(Stream<Human> stream){
        // returns a HashMap by default when used with Collectors.groupingBy
        // thread-safe: Collectors.groupingByConcurrent() returns ConcurrentHashMap
        return stream.collect(Collectors.groupingBy(Human::getEthnic, Collectors.summingInt(Human::getAge)));
    }

    /**
     * Stream to Map, with value is the average of Humans' Age
     */
    public static Map<Human.Ethnic, Double> averageAgesBasedOnEthnic(Stream<Human> stream){
        return stream.collect(Collectors.groupingBy(Human::getEthnic,
                Collectors.averagingInt(Human::getAge)));
    }

    /**
     * Stream to Map, with value is the List of Human Ages
     */
    public static Map<Human.Ethnic, List<Integer>> groupingAgesBasedOnEthnic(Stream<Human> stream){
        // returns a HashMap by default when used with Collectors.groupingBy
        // thread-safe: Collectors.groupingByConcurrent() returns ConcurrentHashMap
        return stream.collect(Collectors.groupingBy(Human::getEthnic,
                Collectors.mapping(Human::getAge, Collectors.toList())));
    }

    /**
     * Stream to Map, with value is the List of Humans
     */
    public static Map<Human.Ethnic, List<Human>> groupingHumanBasedOnEthnic(Stream<Human> stream){
        // returns a HashMap by default when used with Collectors.groupingBy
        // thread-safe: Collectors.groupingByConcurrent() returns ConcurrentHashMap
        return stream.collect(Collectors.groupingBy(Human::getEthnic));
    }

    /*
    ====================================================================================================================
                                                   TEST HERE
    ====================================================================================================================
     */
    public static void main(String[] args) {
        Human human = new Human("a", 10);
        Stream<Human> stream = Stream.of(human);
        Stream<Human> biggerStream = StreamBasic.humanAndFrieds(stream);

        // System.out.println(groupingHumanBasedOnEthnic(biggerStream));
        System.out.println(groupingAgesBasedOnEthnic(biggerStream));
    }
}
