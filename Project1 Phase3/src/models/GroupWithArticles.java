package models;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * <p> Title: GroupWithArticles Class </p>
 * 
 * <p> Description: Represents a composite entity that combines a {@link Group} 
 * with its associated list of {@link HelpArticle} objects. It is useful for scenarios where 
 * both group information and related articles need to be processed or transferred together. 
 * </p>
 * 
 * @author Naimish Maniya
 * 
 * <p> @version 1.00  2024-10-29  Initial version. </p>
 */
public class GroupWithArticles implements Serializable {
    private static final long serialVersionUID = 1L;

    private Group group;
    private List<HelpArticle> articles;

    /**
     * Default constructor for GroupWithArticles.
     */
    public GroupWithArticles() {}

    /**
     * Constructs a GroupWithArticles instance with the specified group and articles.
     *
     * @param group    The {@link Group} entity.
     * @param articles The list of {@link HelpArticle} associated with the group.
     */
    public GroupWithArticles(Group group, List<HelpArticle> articles) {
        this.group = group;
        this.articles = articles;
    }

    /**
     * Retrieves the group.
     *
     * @return The {@link Group} entity.
     */
    public Group getGroup() {
        return group;
    }

    /**
     * Sets the group.
     *
     * @param group The {@link Group} entity to set.
     */
    public void setGroup(Group group) {
        this.group = group;
    }

    /**
     * Retrieves the list of associated articles.
     *
     * @return The list of {@link HelpArticle} objects.
     */
    public List<HelpArticle> getArticles() {
        return articles;
    }

    /**
     * Sets the list of associated articles.
     *
     * @param articles The list of {@link HelpArticle} objects to set.
     */
    public void setArticles(List<HelpArticle> articles) {
        this.articles = articles;
    }

    /**
     * Determines if two GroupWithArticles objects are equal based on their groups and articles.
     *
     * @param o The object to compare with.
     * @return True if both GroupWithArticles objects have the same group and articles, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        GroupWithArticles that = (GroupWithArticles) o;
        
        return Objects.equals(group, that.group) &&
               Objects.equals(articles, that.articles);
    }

    /**
     * Generates a hash code based on the group and articles.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(group, articles);
    }

    /**
     * Returns a string representation of the GroupWithArticles.
     *
     * @return A string detailing the group and its associated articles.
     */
    @Override
    public String toString() {
        return "GroupWithArticles{" +
                "group=" + group +
                ", articles=" + articles +
                '}';
    }
}
