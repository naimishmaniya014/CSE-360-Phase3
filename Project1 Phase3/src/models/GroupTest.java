package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * <p> Title: GroupTest Class </p>
 * 
 * <p> Description: This class provides a set of tests for the {@link Group} class.
 * It validates the functionalities of the Group class, including constructors,
 * getters, setters, and the overridden {@code toString()} method.
 * The test outputs indicate the success or failure of each test case, ensuring that
 * the Group class behaves as expected. </p>
 * 
 * @version 1.00  2024-10-29  Initial version.
 */
public class GroupTest {

    /**
     * Tests the default constructor of the Group class.
     */
    @Test
    @DisplayName("Test Default Constructor")
    public void testDefaultConstructor() {
        Group group = new Group();
        
        assertEquals(0, group.getId(), "Default constructor should initialize id to 0.");
        assertNull(group.getName(), "Default constructor should initialize name to null.");
    }

    /**
     * Tests the parameterized constructor of the Group class.
     */
    @Test
    @DisplayName("Test Parameterized Constructor")
    public void testParameterizedConstructor() {
        Group group = new Group(1, "Mathematics");
        
        assertEquals(1, group.getId(), "Parameterized constructor should set id correctly.");
        assertEquals("Mathematics", group.getName(), "Parameterized constructor should set name correctly.");
    }

    /**
     * Tests the getters and setters of the Group class.
     */
    @Test
    @DisplayName("Test Getters and Setters")
    public void testGettersAndSetters() {
        Group group = new Group();
        group.setId(2);
        group.setName("Physics");
        
        assertEquals(2, group.getId(), "Setter should correctly set the id.");
        assertEquals("Physics", group.getName(), "Setter should correctly set the name.");
    }

    /**
     * Tests the overridden {@code toString()} method of the Group class.
     */
    @Test
    @DisplayName("Test toString Method")
    public void testToString() {
        Group group = new Group(5, "Computer Science");
        String expected = "Computer Science";
        
        assertEquals(expected, group.toString(), "toString() should return the group's name.");
    }
}
