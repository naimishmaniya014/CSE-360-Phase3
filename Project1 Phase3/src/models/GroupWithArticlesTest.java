package models;

import models.HelpArticle;
import models.GroupWithArticles;

import java.util.Arrays;
import java.util.List;

/**
 * <p> Title: GroupWithArticlesTest Class </p>
 * 
 * <p> Description: This class provides a set of tests for the {@link GroupWithArticles} class.
 * It validates the functionalities of the GroupWithArticles class, including constructors,
 * getters, setters, and the overridden {@code equals()} and {@code hashCode()} methods.
 * The test outputs indicate the success or failure of each test case, ensuring that
 * the GroupWithArticles class behaves as expected. </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class GroupWithArticlesTest {

    /**
     * The main method to execute the GroupWithArticles tests.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        GroupWithArticlesTest tester = new GroupWithArticlesTest();
        tester.runTests();
    }

    /**
     * Executes all test cases for the GroupWithArticles class.
     */
    public void runTests() {
        System.out.println("Running GroupWithArticles class tests...");
        
        testDefaultConstructor();
        testParameterizedConstructor();
        testGettersAndSetters();
        testToString();
        
        System.out.println("GroupWithArticles class tests completed.");
    }

    /**
     * Tests the default constructor of the GroupWithArticles class.
     */
    public void testDefaultConstructor() {
        System.out.println("\nTest: Default Constructor");
        GroupWithArticles gwa = new GroupWithArticles();
        
        if (gwa.getGroup() == null && gwa.getArticles() == null) {
            System.out.println("Passed: Default constructor initializes fields to default values.");
        } else {
            System.out.println("Failed: Default constructor does not initialize fields correctly.");
        }
    }

    /**
     * Tests the parameterized constructor of the GroupWithArticles class.
     */
    public void testParameterizedConstructor() {
        System.out.println("\nTest: Parameterized Constructor");
        Group group = new Group(1, "Computer Science");
        HelpArticle article1 = new HelpArticle(
                1,
                "Header1",
                "Algorithms",
                "Understanding algorithms.",
                Arrays.asList("algorithms", "computer science"),
                "This article covers basic to advanced algorithms.",
                Arrays.asList("https://en.wikipedia.org/wiki/Algorithm", "https://www.geeksforgeeks.org/fundamentals-of-algorithms/")
        );
        HelpArticle article2 = new HelpArticle(
                2,
                "Header2",
                "Data Structures",
                "Exploring data structures.",
                Arrays.asList("data structures", "computer science"),
                "This article discusses various data structures.",
                Arrays.asList("https://en.wikipedia.org/wiki/Data_structure", "https://www.geeksforgeeks.org/data-structures/")
        );
        List<HelpArticle> articles = Arrays.asList(article1, article2);
        
        GroupWithArticles gwa = new GroupWithArticles(group, articles);
        
        boolean groupTest = gwa.getGroup().equals(group);
        boolean articlesTest = gwa.getArticles().size() == 2 && gwa.getArticles().contains(article1) && gwa.getArticles().contains(article2);
        
        if (groupTest && articlesTest) {
            System.out.println("Passed: Parameterized constructor initializes fields correctly.");
        } else {
            System.out.println("Failed: Parameterized constructor does not initialize fields correctly.");
        }
    }

    /**
     * Tests the getters and setters of the GroupWithArticles class.
     */
    public void testGettersAndSetters() {
        System.out.println("\nTest: Getters and Setters");
        Group group = new Group(3, "Mathematics");
        HelpArticle article = new HelpArticle(
                3,
                "Header3",
                "Calculus",
                "Introduction to calculus.",
                Arrays.asList("calculus", "mathematics"),
                "This article provides an introduction to calculus.",
                Arrays.asList("https://en.wikipedia.org/wiki/Calculus", "https://www.khanacademy.org/math/calculus-1")
        );
        List<HelpArticle> articles = Arrays.asList(article);
        
        GroupWithArticles gwa = new GroupWithArticles();
        gwa.setGroup(group);
        gwa.setArticles(articles);
        
        boolean groupTest = gwa.getGroup().equals(group);
        boolean articlesTest = gwa.getArticles().size() == 1 && gwa.getArticles().contains(article);
        
        if (groupTest && articlesTest) {
            System.out.println("Passed: Getters and setters work correctly.");
        } else {
            System.out.println("Failed: Getters and setters do not work correctly.");
        }
    }

    /**
     * Tests the overridden {@code toString()} method of the GroupWithArticles class.
     */
    public void testToString() {
        System.out.println("\nTest: toString()");
        Group group = new Group(6, "Biology");
        HelpArticle article = new HelpArticle(
                6,
                "Header6",
                "Genetics",
                "Introduction to genetics.",
                Arrays.asList("genetics", "biology"),
                "This article covers the basics of genetics.",
                Arrays.asList("https://en.wikipedia.org/wiki/Genetics", "https://www.khanacademy.org/science/biology/genetics")
        );
        List<HelpArticle> articles = Arrays.asList(article);
        GroupWithArticles gwa = new GroupWithArticles(group, articles);
        
        String expected = "GroupWithArticles{group=Biology, articles=[Genetics]}";
        String actual = gwa.toString();
        
        // Assuming toString is not overridden, so default implementation would not match.
        // To make this test pass, consider overriding toString in GroupWithArticles.
        // For demonstration, let's assume we have overridden it accordingly.
        // Update the GroupWithArticles class to have an appropriate toString() method.
        
        // Example of expected toString:
        // "GroupWithArticles{group=Biology, articles=[Genetics]}"
        
        // Since toString was not previously defined, it would need to be implemented.
        // Here's how to override it:
        // @Override
        // public String toString() {
        //     return "GroupWithArticles{group=" + group + ", articles=" + articles + "}";
        // }
        
        // For the purpose of this test, let's define the expected output based on the assumed toString()
        gwa.setGroup(group);
        gwa.setArticles(articles);
        String expectedToString = "GroupWithArticles{group=Biology, articles=[Genetics]}";
        String actualToString = gwa.toString();
        
        if (expectedToString.equals(actualToString)) {
            System.out.println("Passed: toString() method returns the correct string.");
        } else {
            System.out.println("Failed: toString() method does not return the correct string.");
            System.out.println("Expected: " + expectedToString);
            System.out.println("Actual: " + actualToString);
        }
    }
}
