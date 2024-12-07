package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p> Title: GroupWithArticlesTest Class </p>
 * 
 * <p> Description: This class provides a set of tests for the {@link GroupWithArticles} class.
 * It validates the functionalities of the GroupWithArticles class, including constructors,
 * getters, setters, and the overridden {@code toString()} method.
 * The test outputs indicate the success or failure of each test case, ensuring that
 * the GroupWithArticles class behaves as expected. </p>
 * 
 * @version 1.00  2024-10-29  Initial version.
 */
public class GroupWithArticlesTest {

    /**
     * Tests the default constructor of the GroupWithArticles class.
     */
    @Test
    @DisplayName("Test Default Constructor")
    public void testDefaultConstructor() {
        GroupWithArticles gwa = new GroupWithArticles();

        assertNull(gwa.getGroup(), "Default constructor should initialize group to null.");
        assertNull(gwa.getArticles(), "Default constructor should initialize articles to null.");
    }

    /**
     * Tests the parameterized constructor of the GroupWithArticles class.
     */
    @Test
    @DisplayName("Test Parameterized Constructor")
    public void testParameterizedConstructor() {
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

        assertEquals(group, gwa.getGroup(), "Parameterized constructor should set the group correctly.");
        assertNotNull(gwa.getArticles(), "Articles list should not be null.");
        assertEquals(2, gwa.getArticles().size(), "Articles list should contain exactly 2 articles.");
        assertTrue(gwa.getArticles().contains(article1), "Articles list should contain article1.");
        assertTrue(gwa.getArticles().contains(article2), "Articles list should contain article2.");
    }

    /**
     * Tests the getters and setters of the GroupWithArticles class.
     */
    @Test
    @DisplayName("Test Getters and Setters")
    public void testGettersAndSetters() {
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

        assertEquals(group, gwa.getGroup(), "Setter should correctly set the group.");
        assertNotNull(gwa.getArticles(), "Articles list should not be null.");
        assertEquals(1, gwa.getArticles().size(), "Articles list should contain exactly 1 article.");
        assertTrue(gwa.getArticles().contains(article), "Articles list should contain the specified article.");
    }

    /**
     * Tests the overridden {@code toString()} method of the GroupWithArticles class.
     */
    @Test
    @DisplayName("Test toString Method")
    public void testToString() {
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

        assertEquals(expected, actual, "toString() should return the correct string representation.");
    }
}
