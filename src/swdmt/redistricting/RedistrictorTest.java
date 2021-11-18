package swdmt.redistricting;

import org.hamcrest.MatcherAssert;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.Set;

/**
 * Tests for redistrictor.
 *
 * @author Dr. Jody Paul
 * @version 20191006
 */
public class RedistrictorTest {
    /**
     * Default constructor for test class RedistrictorTest.
     */
    public RedistrictorTest() {
    }

    /**
     * Sets up the test fixture.
     * <p>
     * Called before every test case method.
     */
    @BeforeEach
    public void setUp() {
    }

    /**
     * Tears down the test fixture.
     * <p>
     * Called after every test case method.
     */
    @AfterEach
    public void tearDown() {
    }

    @Test(expected = IllegalArgumentException.class)
    public void invalidRegionParameterConstructorTest() {
        Redistrictor r = new Redistrictor(null);
    }

    @Test
    public void generateDistrictsSquareSingleDistrictTest() {
        Region region;
        Set<District> districtSet;
        region = new Region();
        districtSet = Redistrictor.generateDistricts(region, 1);
        MatcherAssert.assertThat(districtSet.size(), is(1));
        MatcherAssert.assertThat(districtSet.iterator().next().size(), is(0));
        region = new Region(1);
        districtSet = Redistrictor.generateDistricts(region, 1);
        MatcherAssert.assertThat(districtSet.size(), is(1));
        MatcherAssert.assertThat(districtSet.iterator().next().size(), is(1));
        region = new Region(4);
        districtSet = Redistrictor.generateDistricts(region, 1);
        MatcherAssert.assertThat(districtSet.size(), is(1));
        MatcherAssert.assertThat(districtSet.iterator().next().size(), is(4));
        region = new Region(9);
        districtSet = Redistrictor.generateDistricts(region, 1);
        MatcherAssert.assertThat(districtSet.size(), is(1));
        MatcherAssert.assertThat(districtSet.iterator().next().size(), is(9));
        region = new Region(16);
        districtSet = Redistrictor.generateDistricts(region, 1);
        MatcherAssert.assertThat(districtSet.size(), is(1));
        MatcherAssert.assertThat(districtSet.iterator().next().size(), is(16));
    }

    /**
     * Checks generation of districts for a square region,
     * verifying only the number of districts, the number of
     * locations per district, and the uniqueness of locations
     * per district.
     * N.B. The contiguity of locations in each district is NOT
     * verified.
     */
    @Test
    public void generateDistrictsSquareAppropriateNumberAndSizeTest() {
        Region region;
        Set<District> districtSet;
        region = new Region();
        districtSet = Redistrictor.generateDistricts(region, 1);
        MatcherAssert.assertThat(districtSet.size(), is(1));
        assertTrue(locationsUnique(districtSet));
        MatcherAssert.assertThat(districtSet.iterator().next().size(), is(0));

        region = new Region(1);
        districtSet = Redistrictor.generateDistricts(region, 1);
        MatcherAssert.assertThat(districtSet.size(), is(1));
        assertTrue(locationsUnique(districtSet));
        MatcherAssert.assertThat(districtSet.iterator().next().size(), is(1));

        region = new Region(4);
        districtSet = Redistrictor.generateDistricts(region, 2);
        MatcherAssert.assertThat(districtSet.size(), is(2));
        assertTrue(locationsUnique(districtSet));
        MatcherAssert.assertThat(districtSet.iterator().next().size(), is(2));

        region = new Region(9);
        districtSet = Redistrictor.generateDistricts(region, 1);
        MatcherAssert.assertThat(districtSet.size(), is(1));
        assertTrue(locationsUnique(districtSet));
        MatcherAssert.assertThat(districtSet.iterator().next().size(), is(9));

        districtSet = Redistrictor.generateDistricts(region, 3);
        MatcherAssert.assertThat(districtSet.size(), is(3));
        assertTrue(locationsUnique(districtSet));
        MatcherAssert.assertThat(districtSet.iterator().next().size(), is(3));
        for (District d : districtSet) {
            MatcherAssert.assertThat(d.size(), is(3));
        }

        districtSet = Redistrictor.generateDistricts(region, 2);
        MatcherAssert.assertThat(districtSet.size(), is(2));
        assertTrue(locationsUnique(districtSet));
        for (District d : districtSet) {
            MatcherAssert.assertThat(d.size(), anyOf(is(4), is(5)));
        }
        int numLocations = 0;
        for (District d : districtSet) {
            numLocations += d.size();
        }
        MatcherAssert.assertThat(numLocations, is(9));

        districtSet = Redistrictor.generateDistricts(region, 4);
        MatcherAssert.assertThat(districtSet.size(), is(4));
        assertTrue(locationsUnique(districtSet));
        for (District d : districtSet) {
            MatcherAssert.assertThat(d.size(), anyOf(is(2), is(3)));
        }
        numLocations = 0;
        for (District d : districtSet) {
            numLocations += d.size();
        }
        MatcherAssert.assertThat(numLocations, is(9));

        districtSet = Redistrictor.generateDistricts(region, 5);
        MatcherAssert.assertThat(districtSet.size(), is(5));
        assertTrue(locationsUnique(districtSet));
        for (District d : districtSet) {
            MatcherAssert.assertThat(d.size(), anyOf(is(1), is(2)));
        }
        numLocations = 0;
        for (District d : districtSet) {
            numLocations += d.size();
        }
        MatcherAssert.assertThat(numLocations, is(9));
    }

    /**
     * Utility to verify that all locations in a collection
     * of districts are unique; that is, no two districts
     * contain the same location.
     *
     * @param districts the districts to consider
     * @return true if all locations are unique; false otherwise
     */
    private static boolean locationsUnique(final Set<District> districts) {
        boolean allUnique = true;
        for (District d : districts) {
            for (Location loc : d.locations()) {
                for (District other : districts) {
                    if (d != other) {
                        allUnique &= !other.locations().contains(loc);
                    }
                }
            }
        }
        return allUnique;
    }

    /**
     * Checks cases where a single district is the
     * appropriate response for all districts of a specified size.
     */
    @Test
    public void allDistrictsOfSpecificSizeSingleDistrictTest() {
        Region region;
        Set<District> districtSet;
        region = new Region();
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 1).size(), is(0));
        region = new Region(1);
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 1).size(), is(1));
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 2).size(), is(1));
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 4).size(), is(1));
        region = new Region(4);
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 4).size(), is(1));
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 9).size(), is(1));
    }

    @Test
    public void allDistrictsOfSpecificSizeTest() {
        Region region;
        Set<District> districtSet;
        region = new Region(4);
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 2).size(), is(4));
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 3).size(), is(4));
        region = new Region(9);
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 2).size(), is(12));
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 3).size(), is(22));
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 4).size(), is(28));
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 8).size(), is(5));
        region = new Region(16);
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 2).size(), is(24));
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 3).size(), is(52));
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 4).size(), is(89));
        region = new Region(25);
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 2).size(), is(40));
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 3).size(), is(94));
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 4).size(), is(180));
        region = new Region(64);
        MatcherAssert.assertThat(Redistrictor.allDistrictsOfSpecificSize(region, 2).size(), is(112));
    }

    /**
     * Checks generation of districts for a square region,
     * verifying only the contiguity of locations in each
     * district.
     */
    @Test
    public void generateDistrictsSquareContiguityTest() {
        Region region;
        Set<District> districtSet;
        region = new Region();
        districtSet = Redistrictor.generateDistricts(region, 1);
        for (District d : districtSet) {
            assertTrue("Checking contiguity of district " + d, d.contiguityValid());
        }
        region = new Region(1);
        districtSet = Redistrictor.generateDistricts(region, 1);
        for (District d : districtSet) {
            assertTrue("Checking contiguity of district " + d, d.contiguityValid());
        }

        region = new Region(4);
        districtSet = Redistrictor.generateDistricts(region, 2);
        for (District d : districtSet) {
            assertTrue("Checking contiguity of district " + d, d.contiguityValid());
        }

        region = new Region(9);
        districtSet = Redistrictor.generateDistricts(region, 1);
        for (District d : districtSet) {
            assertTrue("Contiguity error for district " + d, d.contiguityValid());
        }
        districtSet = Redistrictor.generateDistricts(region, 2);
        for (District d : districtSet) {
            assertTrue("Contiguity error for district " + d, d.contiguityValid());
        }
        districtSet = Redistrictor.generateDistricts(region, 3);
        for (District d : districtSet) {
            assertTrue("Contiguity error for district " + d, d.contiguityValid());
        }
        districtSet = Redistrictor.generateDistricts(region, 4);
        for (District d : districtSet) {
            assertTrue("Contiguity error for district " + d, d.contiguityValid());
        }
        districtSet = Redistrictor.generateDistricts(region, 5);
        for (District d : districtSet) {
            assertTrue("Contiguity error for district " + d, d.contiguityValid());
        }
    }
}

