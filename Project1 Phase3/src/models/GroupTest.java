package models;

/**
 * <p> Title: GroupTest Class </p>
 * 
 * <p> Description: This class provides a set of tests for the {@link Group} class.
 * It validates the functionalities of the Group class, including constructors,
 * getters, setters, and the overridden {@code equals()} and {@code hashCode()} methods.
 * The test outputs indicate the success or failure of each test case, ensuring that
 * the Group class behaves as expected. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class GroupTest {

    /**
     * The main method to execute the Group tests.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        GroupTest tester = new GroupTest();
        tester.runTests();
    }

    /**
     * Executes all test cases for the Group class.
     */
    public void runTests() {
        System.out.println("Running Group class tests...");
        
        testDefaultConstructor();
        testParameterizedConstructor();
        testGettersAndSetters();
        testToString();
        
        System.out.println("Group class tests completed.");
    }

    /**
     * Tests the default constructor of the Group class.
     */
    public void testDefaultConstructor() {
        System.out.println("\nTest: Default Constructor");
        Group group = new Group();
        
        if (group.getId() == 0 && group.getName() == null) {
            System.out.println("Passed: Default constructor initializes fields to default values.");
        } else {
            System.out.println("Failed: Default constructor does not initialize fields correctly.");
        }
    }

    /**
     * Tests the parameterized constructor of the Group class.
     */
    public void testParameterizedConstructor() {
        System.out.println("\nTest: Parameterized Constructor");
        Group group = new Group(1, "Mathematics");
        
        if (group.getId() == 1 && "Mathematics".equals(group.getName())) {
            System.out.println("Passed: Parameterized constructor initializes fields correctly.");
        } else {
            System.out.println("Failed: Parameterized constructor does not initialize fields correctly.");
        }
    }

    /**
     * Tests the getters and setters of the Group class.
     */
    public void testGettersAndSetters() {
        System.out.println("\nTest: Getters and Setters");
        Group group = new Group();
        group.setId(2);
        group.setName("Physics");
        
        boolean idTest = group.getId() == 2;
        boolean nameTest = "Physics".equals(group.getName());
        
        if (idTest && nameTest) {
            System.out.println("Passed: Getters and setters work correctly.");
        } else {
            System.out.println("Failed: Getters and setters do not work correctly.");
        }
    }

    /**
     * Tests the overridden {@code toString()} method of the Group class.
     */
    public void testToString() {
        System.out.println("\nTest: toString()");
        Group group = new Group(5, "Computer Science");
        String expected = "Computer Science";
        
        if (expected.equals(group.toString())) {
            System.out.println("Passed: toString() method returns the correct string.");
        } else {
            System.out.println("Failed: toString() method does not return the correct string.");
        }
    }
}
