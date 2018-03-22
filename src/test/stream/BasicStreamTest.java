
package stream;

import  org.junit.Before;
import  org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

/**
 * Java 8 stream tutorial.
 * 
 * @author aus
 */
public class BasicStreamTest
{
    private List<String> list;
    private Map<String, Integer> map;

    @Before
    public void setUp() throws Exception
    {
        list = Arrays.asList("A1", "A2", "B1", "B2", "C1", "C2");
        map = new HashMap<>();
        map.put("A", 11);
        map.put("B", 12);
        map.put("C", 13);
        map.put("D", 14);
        map.put("E", 15);
    }

    /**
     * Introduce stream(), collect(), toList(), toMap()
     */
    @Test
    public void streamThenCollectShouldChangeNothing()
    {
        List<String> newList = list.stream().collect(Collectors.toList());
        assertEquals(list, newList);

        Map<String, Integer> newMap =
                map.entrySet().stream().collect(Collectors.toMap(p -> p.getKey(),
                                                                 p -> p.getValue()));

        assertEquals(map, newMap);
    }

    /**
     * filter has Predicate parameter (i.e. return boolean )
     */
    @Test
    public void filterKey()
    {
        List<String> newList =
                list.stream().filter(p -> p.startsWith("C")).collect(Collectors.toList());
        assertEquals(2, newList.size());

        Map<String, Integer> newMap =
                map.entrySet().stream().filter(p -> p.getKey().equals("B")).collect(Collectors.toMap(p -> p.getKey(),
                                                                                                     p -> p.getValue()));
        assertEquals(1, newMap.size());
    }

    /**
     * map can transform structure. (e.g. List<String> to List<Integer>, Map to
     * List etc )
     */
    @Test
    public void mapShouldTransform()
    {
        // transform List<String> to List<Integer>
        List<Integer> countList =
                list.stream().map(p -> p.length()).collect(Collectors.toList());
        List<Integer> expected1 = Arrays.asList(2, 2, 2, 2, 2, 2);
        assertEquals(expected1, countList);

        // transform Map<String,Integer> to List<String> of key
        List<String> mapKeyList =
                map.entrySet().stream().map(Map.Entry::getKey).collect(Collectors.toList());
        List<String> expected2 = Arrays.asList("A", "B", "C", "D", "E");
        assertEquals(expected2, mapKeyList);

        // transform Map<String,Integer> to List<String> of ( key + value )
        List<String> keyPlusValueList =
                map.entrySet().stream().map(p -> p.getKey() + p.getValue()).collect(Collectors.toList());
        List<String> expected3 =
                Arrays.asList("A11", "B12", "C13", "D14", "E15");
        assertEquals(expected3, keyPlusValueList);
    }

    /**
     * transform list to map.
     */
    @Test
    public void groupingBy()
    {
        // Note : s.charAt(0) is a char, which is not an object. As you want key
        // to be String, we wrap it into String.valueOf() method. ( Also I
        // didn't check the length of the s to simplify the groupingBy
        // function.)
        Map<String, List<String>> groupingByFirstCharMap =
                list.stream().collect(Collectors.groupingBy(s -> String.valueOf(s.charAt(0))));

        Map<String, List<String>> expected = new HashMap<>();
        expected.put("A", Arrays.asList("A1", "A2"));
        expected.put("B", Arrays.asList("B1", "B2"));
        expected.put("C", Arrays.asList("C1", "C2"));

        assertEquals(expected, groupingByFirstCharMap);
    }

    /**
     * min and max use comparator and return Optional
     */
    @Test
    public void countSumMinMaxWithOptionalEtc()
    {
        // count
        assertEquals(5, map.values().stream().count());

        // sum
        assertEquals(65, map.values().stream().mapToLong(i -> i).sum());
        assertEquals(10,
                     map.values().stream().mapToLong(i -> String.valueOf(i).length()).sum());

        // min and max use comparator and return Optional
        final Comparator<Integer> compareByValue = Comparator.comparing(i -> i);
        assertEquals(Integer.valueOf(11),
                     map.values().stream().min(compareByValue).get());

        assertEquals(Integer.valueOf(15),
                     map.values().stream().max(compareByValue).get());
    }

    /**
     * accumulated = accumulated + " " + eachItem
     */
    @Test
    public void reduceTakeTwoParameter()
    {
        String reduce1 =
                list.stream().reduce((accumulated, i) -> accumulated + " " + i).get();
        assertEquals("A1 A2 B1 B2 C1 C2", reduce1);
    }

}
