
package stream;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import model.*;

import static org.junit.Assert.assertEquals;

/**
 * Java 8 stream tutorial.
 * 
 * @author aus
 */
public class ComplicatedStreamTest
{
    private Map<String, List<Name>> map;

    private Name name1 = new Name("John", "Smith", Gender.M);
    private Name name2 = new Name("Paul", "Mccartney", Gender.M);
    private Name name3 = new Name("Will", "Smith", Gender.M);
    private Name name4 = new Name("Paul", "Gascoigne", Gender.M);
    private Name name5 = new Name("Linda", "Mccartney", Gender.F);

    @Before
    public void setUp() throws Exception
    {
        map = new HashMap<>();
        map.put("Group A", Arrays.asList(name1, name2));
        map.put("Group B", Arrays.asList(name3, name4));
        map.put("Group C", Arrays.asList(name5));
        map.put("Group D", Arrays.asList(name1, name2, name4, name5));
    }

    /**
     * When filtering key, the size of the map changes.
     */
    @Test
    public void filterKey()
    {
        Map<String, List<Name>> newMap =
                map.entrySet().stream().filter(p -> p.getKey().equals("Group C")).collect(Collectors.toMap(p -> p.getKey(),
                                                                                                           p -> p.getValue()));

        assertEquals(1, newMap.size());
        assertEquals(Arrays.asList(name5), newMap.get("Group C"));
    }

    /**
     * Extract the predicate out as a parameter.
     */
    @Test
    public void filterKeyWithPredicate()
    {
        Predicate<Entry<String, List<Name>>> predicateGroupC =
                p -> p.getKey().equals("Group C");

        Map<String, List<Name>> newMap =
                map.entrySet().stream().filter(predicateGroupC).collect(Collectors.toMap(p -> p.getKey(),
                                                                                         p -> p.getValue()));

        assertEquals(1, newMap.size());
        assertEquals(Arrays.asList(name5), newMap.get("Group C"));

    }

    /**
     * When filtering value, the size of the map (i.e. number of key ) does not
     * change.
     * 
     * Note : When we filter key, we put filter after "map.entrySet().stream()".
     * But when we filter value, we don't put filter at the same place. Do you
     * know why ?
     * 
     * The reason is that the value part of returned structure of
     * map.entrySet().stream() is List<List<Name>>. This is not what you want to
     * change.
     * 
     * If you want to change each List<Name>. It is in p -> p.getValue()
     */
    @Test
    public void filterValue()
    {
        Map<String, List<Name>> newMap =
                map.entrySet().stream().collect(Collectors.toMap(p -> p.getKey(),
                                                                 p -> p.getValue().stream().filter(n -> n.getFirstName().equals("Paul")).collect(Collectors.toList())));

        assertEquals(4, newMap.size());
        assertEquals(1, newMap.get("Group A").size());
        assertEquals(1, newMap.get("Group B").size());
        assertEquals(0, newMap.get("Group C").size());
        assertEquals(2, newMap.get("Group D").size());
    }

    /**
     * Extract predicate out as a parameter. And the predicate uses lambda
     * expression.
     */
    @Test
    public void filterValueWithLambdaExpressionPredicate()
    {
        Predicate<Name> lambdaExpressionPredicate =
                n -> n.getFirstName().equals("Paul");

        Map<String, List<Name>> newMap =
                map.entrySet().stream().collect(Collectors.toMap(p -> p.getKey(),
                                                                 p -> p.getValue().stream().filter(lambdaExpressionPredicate).collect(Collectors.toList())));

        assertEquals(4, newMap.size());
        assertEquals(1, newMap.get("Group A").size());
        assertEquals(1, newMap.get("Group B").size());
        assertEquals(0, newMap.get("Group C").size());
        assertEquals(2, newMap.get("Group D").size());
    }

    /**
     * Extract predicate out as a parameter. And the predicate uses method
     * reference.
     */
    @Test
    public void filterValueWithMethodReferencePredicate()
    {
        Predicate<Name> methodReferencePredicate = Name::isMale;

        Map<String, List<Name>> newMap =
                map.entrySet().stream().collect(Collectors.toMap(p -> p.getKey(),
                                                                 p -> p.getValue().stream().filter(methodReferencePredicate).collect(Collectors.toList())));

        assertEquals(4, newMap.size());
        assertEquals(2, newMap.get("Group A").size());
        assertEquals(2, newMap.get("Group B").size());
        assertEquals(0, newMap.get("Group C").size());
        assertEquals(3, newMap.get("Group D").size());
    }

    /**
     * After using stream() on map, the returned structure is List<List<Name>>.
     * flatmap(l -> l.stream() ) can transform it to List<Name>
     */
    @Test
    public void flatMap()
    {
        List<Name> names =
                map.values().stream().flatMap(l -> l.stream()).collect(Collectors.toList());
        assertEquals(9, names.size());

        // use distinct to get unique names.
        names =
                map.values().stream().flatMap(l -> l.stream()).distinct().collect(Collectors.toList());
        assertEquals(5, names.size());
    }
}
