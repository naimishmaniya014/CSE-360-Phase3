//package models;
//
//import java.util.Arrays;
//
///**
// * <p> Title: HelpArticleTest Class </p>
// * 
// * <p> Description: This class provides a set of tests for the {@link HelpArticle} class.
// * It validates the functionalities of the HelpArticle class, including constructors,
// * getters, setters, and the overridden {@code equals()}, {@code hashCode()}, and {@code toString()} methods.
// * The test outputs indicate the success or failure of each test case, ensuring that
// * the HelpArticle class behaves as expected. </p>
// * 
// * @author Naimish Maniya
// * 
// * <p> @version 1.00  2024-10-29  Initial version. </p>
// */
//public class HelpArticleTest {
//
//    /**
//     * The main method to execute the HelpArticle tests.
//     *
//     * @param args Command-line arguments (not used).
//     */
//    public static void main(String[] args) {
//        HelpArticleTest tester = new HelpArticleTest();
//        tester.runTests();
//    }
//
//    /**
//     * Executes all test cases for the HelpArticle class.
//     */
//    public void runTests() {
//        System.out.println("Running HelpArticle class tests...");
//        
//        testDefaultConstructor();
//        testParameterizedConstructor();
//        testGettersAndSetters();
//        testToString();
//        
//        System.out.println("HelpArticle class tests completed.");
//    }
//
//    /**
//     * Tests the default constructor of the HelpArticle class.
//     */
//    public void testDefaultConstructor() {
//        System.out.println("\nTest: Default Constructor");
//        HelpArticle article = new HelpArticle();
//        
//        if (article.getId() == 0 &&
//            article.getHeader() == null &&
//            article.getTitle() == null &&
//            article.getShortDescription() == null &&
//            article.getKeywords() == null &&
//            article.getBody() == null &&
//            article.getReferenceLinks() == null) {
//            System.out.println("Passed: Default constructor initializes fields to default values.");
//        } else {
//            System.out.println("Failed: Default constructor does not initialize fields correctly.");
//        }
//    }
//
//    /**
//     * Tests the parameterized constructor of the HelpArticle class.
//     */
//    public void testParameterizedConstructor() {
//        System.out.println("\nTest: Parameterized Constructor");
//        HelpArticle article = new HelpArticle(
//                1,
//                "Header1",
//                "Introduction to Java",
//                "A brief overview of Java programming language.",
//                Arrays.asList("java", "programming"),
//                "Java is a high-level, class-based, object-oriented programming language.",
//                Arrays.asList("https://java.com", "https://docs.oracle.com/javase/8/")
//        );
//        
//        boolean idTest = article.getId() == 1;
//        boolean headerTest = "Header1".equals(article.getHeader());
//        boolean titleTest = "Introduction to Java".equals(article.getTitle());
//        boolean shortDescTest = "A brief overview of Java programming language.".equals(article.getShortDescription());
//        boolean keywordsTest = article.getKeywords().size() == 2 && article.getKeywords().contains("java") && article.getKeywords().contains("programming");
//        boolean bodyTest = "Java is a high-level, class-based, object-oriented programming language.".equals(article.getBody());
//        boolean referencesTest = article.getReferenceLinks().size() == 2 && article.getReferenceLinks().contains("https://java.com") && article.getReferenceLinks().contains("https://docs.oracle.com/javase/8/");
//        
//        if (idTest && headerTest && titleTest && shortDescTest && keywordsTest && bodyTest && referencesTest) {
//            System.out.println("Passed: Parameterized constructor initializes fields correctly.");
//        } else {
//            System.out.println("Failed: Parameterized constructor does not initialize fields correctly.");
//        }
//    }
//
//    /**
//     * Tests the getters and setters of the HelpArticle class.
//     */
//    public void testGettersAndSetters() {
//        System.out.println("\nTest: Getters and Setters");
//        HelpArticle article = new HelpArticle();
//        article.setId(2);
//        article.setHeader("Header2");
//        article.setTitle("Advanced Java");
//        article.setShortDescription("An in-depth look into Java's advanced features.");
//        article.setKeywords(Arrays.asList("java", "advanced", "features"));
//        article.setBody("This article explores advanced topics in Java, including concurrency, streams, and more.");
//        article.setReferenceLinks(Arrays.asList("https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html", "https://www.baeldung.com/java-concurrency"));
//        
//        boolean idTest = article.getId() == 2;
//        boolean headerTest = "Header2".equals(article.getHeader());
//        boolean titleTest = "Advanced Java".equals(article.getTitle());
//        boolean shortDescTest = "An in-depth look into Java's advanced features.".equals(article.getShortDescription());
//        boolean keywordsTest = article.getKeywords().size() == 3 && article.getKeywords().contains("java") && article.getKeywords().contains("advanced") && article.getKeywords().contains("features");
//        boolean bodyTest = "This article explores advanced topics in Java, including concurrency, streams, and more.".equals(article.getBody());
//        boolean referencesTest = article.getReferenceLinks().size() == 2 && article.getReferenceLinks().contains("https://docs.oracle.com/javase/8/docs/api/java/util/stream/package-summary.html") && article.getReferenceLinks().contains("https://www.baeldung.com/java-concurrency");
//        
//        if (idTest && headerTest && titleTest && shortDescTest && keywordsTest && bodyTest && referencesTest) {
//            System.out.println("Passed: Getters and setters work correctly.");
//        } else {
//            System.out.println("Failed: Getters and setters do not work correctly.");
//        }
//    }
//
//    /**
//     * Tests the overridden {@code toString()} method of the HelpArticle class.
//     */
//    public void testToString() {
//        System.out.println("\nTest: toString()");
//        HelpArticle article = new HelpArticle(
//                5,
//                "Header5",
//                "Data Structures",
//                "An overview of common data structures.",
//                Arrays.asList("data structures", "algorithms"),
//                "This article covers arrays, linked lists, trees, and graphs.",
//                Arrays.asList("https://en.wikipedia.org/wiki/Data_structure", "https://www.geeksforgeeks.org/data-structures/")
//        );
//        String expected = "Data Structures";
//        
//        if (expected.equals(article.toString())) {
//            System.out.println("Passed: toString() method returns the correct string.");
//        } else {
//            System.out.println("Failed: toString() method does not return the correct string.");
//        }
//    }
//}
