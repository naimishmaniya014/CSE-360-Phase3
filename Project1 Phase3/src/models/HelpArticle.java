package models;

import java.util.List;

public class HelpArticle {
    private long id;
    private String header;
    private String title;
    private String shortDescription;
    private List<String> keywords;
    private String body;
    private List<String> referenceLinks;

    // Constructors

    public HelpArticle() {
    }

    public HelpArticle(long id, String header, String title, String shortDescription, List<String> keywords, String body, List<String> referenceLinks) {
        this.id = id;
        this.header = header;
        this.title = title;
        this.shortDescription = shortDescription;
        this.keywords = keywords;
        this.body = body;
        this.referenceLinks = referenceLinks;
    }

    // Getters and Setters

    public long getId() {
        return id;
    }

    public void setId(long id) { this.id = id; }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) { this.header = header; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) { this.title = title; }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) { this.keywords = keywords; }

    public String getBody() {
        return body;
    }

    public void setBody(String body) { this.body = body; }

    public List<String> getReferenceLinks() {
        return referenceLinks;
    }

    public void setReferenceLinks(List<String> referenceLinks) { this.referenceLinks = referenceLinks; }
	
}
