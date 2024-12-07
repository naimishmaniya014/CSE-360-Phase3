package models;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * <p> Title: HelpArticleTest Class </p>
 * 
 * <p> Description: This class provides a set of tests for the {@link HelpArticle} class.
 * It validates the functionalities of the HelpArticle class, including constructors,
 * getters, setters, and the overridden {@code equals()}, {@code hashCode()}, and {@code toString()} methods.
 * The test outputs indicate the success or failure of each test case, ensuring that
 * the HelpArticle class behaves as expected. </p>
 * 
 * @version 1.00  2024-10-29  Initial version.
 */
public class HelpArticleTest {

    /**
     * Tests the default constructor of the HelpArticle class.
     */
    @Test
    @DisplayName("Test Default Constructor")
    public void testDefaultConstructor() {
        HelpArticle article = new HelpArticle();

        assertEquals(0, article.getId(), "Default constructor should initialize id to 0.");
        assertNull(article.getHeader(), "Default constructor should initialize header to null.");
        assertNull(article.getTitle(), "Default constructor should initialize title to null.");
        assertNull(article.getShortDescription(), "Default constructor should initialize shortDescription to null.");
        assertNull(article.getKeywords(), "Default constructor should initialize keywords to null.");
        assertNull(article.getBody(), "Default constructor should initialize body to null.");
        assertNull(article.getReferenceLinks(), "Default constructor should initialize referenceLinks to null.");
    }

    /**
     * Tests the parameterized constructor of the HelpArticle class.
     */
    @Test
    @DisplayName("Test Parameterized Constructor")
    public void testParameterizedConstructor() {
        HelpArticle article = new HelpArticle(
                1,
                "Header1",
                "Introduction to Java",
                "A brief overview of Java programming language.",
                Arrays.asList("java", "programming"),
                "Java is a high-level, class-based, object-oriented programming language.",
                Arrays.asList("https://java.com", "https://docs.oracle.com/javase/8/")
        );

        assertEquals(1, article.getId(), "Parameterized constructor should set id correctly.");
        assertEquals("Header1", article.getHeader(), "Parameterized constructor should set header correctly.");
        assertEquals("Introduction to Java", article.getTitle(), "Parameterized constructor should set title correctly.");
        assertEquals("A brief overview of Java programming language.", article.getShortDescription(), "Parameterized constructor should set shortDescription correctly.");
        assertNotNull(article.getKeywords(), "Keywords should not be null.");
        assertEquals(2, article.getKeywords().size(), "Keywords should contain exactly 2 items.");
        assertTrue(article.getKeywords().contains("java"), "Keywords should contain 'java'.");
        assertTrue(article.getKeywords().contains("programming"), "Keywords should contain 'programming'.");
        assertEquals("Java is a high-level, class-based, object-oriented programming language.", article.getBody(), "Parameterized constructor should set body correctly.");
        assertNotNull(article.getReferenceLinks(), "ReferenceLinks should not be null.");
        assertEquals(2, article.getReferenceLinks().size(), "ReferenceLinks should contain exactly 2 items.");
        assertTrue(article.getReferenceLinks().contains("https://java.com"), "ReferenceLinks should contain 'https://java.com'.");
        assertTrue(article.getReferenceLinks().contains("https://docs.oracle.com/javase/8/"), "ReferenceLinks should contain 'https://docs.oracle.com/javase/8/'.");
    }

    /**
     * Tests the getters and setters of the HelpArticle class.
     */
    @Test
    @DisplayName("Test Getters and Setters")
    public void testGettersAndSetters() {
        HelpArticle article = new HelpArticle();
        article.setId(2);
        article.setHeader("Header2");
        article.setTitle("Advanced Java");
        article.setShortDescription("An in-depth look into Java's advanced features.");
        article.setKeywords(Arrays.asList("java", "advanced", "features"));
        article.setBody("This article explores advanced topics in Java, including concurrency, streams, and more.");
        article.setReferenceLinks(Arrays.asList("https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html", "https://www.baeldung.com/java-concurrency"));

        assertEquals(2, article.getId(), "Setter should correctly set the id.");
        assertEquals("Header2", article.getHeader(), "Setter should correctly set the header.");
        assertEquals("Advanced Java", article.getTitle(), "Setter should correctly set the title.");
        assertEquals("An in-depth look into Java's advanced features.", article.getShortDescription(), "Setter should correctly set the shortDescription.");
        assertNotNull(article.getKeywords(), "Keywords should not be null.");
        assertEquals(3, article.getKeywords().size(), "Keywords should contain exactly 3 items.");
        assertTrue(article.getKeywords().contains("java"), "Keywords should contain 'java'.");
        assertTrue(article.getKeywords().contains("advanced"), "Keywords should contain 'advanced'.");
        assertTrue(article.getKeywords().contains("features"), "Keywords should contain 'features'.");
        assertEquals("This article explores advanced topics in Java, including concurrency, streams, and more.", article.getBody(), "Setter should correctly set the body.");
        assertNotNull(article.getReferenceLinks(), "ReferenceLinks should not be null.");
        assertEquals(2, article.getReferenceLinks().size(), "ReferenceLinks should contain exactly 2 items.");
        assertTrue(article.getReferenceLinks().contains("https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html"), "ReferenceLinks should contain 'https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html'.");
        assertTrue(article.getReferenceLinks().contains("https://www.baeldung.com/java-concurrency"), "ReferenceLinks should contain 'https://www.baeldung.com/java-concurrency'.");
    }

    /**
     * Tests the overridden {@code toString()} method of the HelpArticle class.
     */
    @Test
    @DisplayName("Test toString Method")
    public void testToString() {
        HelpArticle article = new HelpArticle(
                5,
                "Header5",
                "Data Structures",
                "An overview of common data structures.",
                Arrays.asList("data structures", "algorithms"),
                "This article covers arrays, linked lists, trees, and graphs.",
                Arrays.asList("https://en.wikipedia.org/wiki/Data_structure", "https://www.geeksforgeeks.org/data-structures/")
        );
        String expected = "Data Structures";

        assertEquals(expected, article.toString(), "toString() should return the article's title.");
    }

    /**
     * Tests the overridden {@code equals()} and {@code hashCode()} methods of the HelpArticle class.
     */
    @Test
    @DisplayName("Test equals and hashCode Methods")
    public void testEqualsAndHashCode() {
        HelpArticle article1 = new HelpArticle(
                10,
                "Header10",
                "Operating Systems",
                "Basics of Operating Systems.",
                Arrays.asList("os", "computing"),
                "This article introduces the fundamental concepts of operating systems.",
                Arrays.asList("https://en.wikipedia.org/wiki/Operating_system", "https://www.geeksforgeeks.org/operating-systems/")
        );

        HelpArticle article2 = new HelpArticle(
                10,
                "Header10",
                "Operating Systems",
                "Basics of Operating Systems.",
                Arrays.asList("os", "computing"),
                "This article introduces the fundamental concepts of operating systems.",
                Arrays.asList("https://en.wikipedia.org/wiki/Operating_system", "https://www.geeksforgeeks.org/operating-systems/")
        );

        HelpArticle article3 = new HelpArticle(
                11,
                "Header11",
                "Networks",
                "Introduction to Networking.",
                Arrays.asList("networking", "computing"),
                "This article covers the basics of computer networks.",
                Arrays.asList("https://en.wikipedia.org/wiki/Computer_network", "https://www.geeksforgeeks.org/computer-networks/")
        );

        assertEquals(article1, article2, "Articles with the same content should be equal.");
        assertEquals(article1.hashCode(), article2.hashCode(), "Hash codes should be equal for equal articles.");
        assertNotEquals(article1, article3, "Articles with different content should not be equal.");
        assertNotEquals(article1.hashCode(), article3.hashCode(), "Hash codes should not be equal for different articles.");
    }
}
