package models;

import java.time.LocalDateTime;

public class SearchRequest {
    private String username;
    private String query;
    private LocalDateTime timestamp;

    public SearchRequest(String username, String query, LocalDateTime timestamp) {
        this.username = username;
        this.query = query;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
