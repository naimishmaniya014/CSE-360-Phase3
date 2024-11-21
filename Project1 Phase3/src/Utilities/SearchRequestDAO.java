package Utilities;

import models.SearchRequest;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SearchRequestDAO {
    private Connection connection;

    public SearchRequestDAO() throws SQLException {
        connection = DatabaseManager.getInstance().getConnection();
    }

    public void addSearchRequest(SearchRequest request) throws SQLException {
        String insertSQL = "INSERT INTO SearchRequests (username, query, timestamp) VALUES (?, ?, ?);";
        try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
            pstmt.setString(1, request.getUsername());
            pstmt.setString(2, request.getQuery());
            pstmt.setTimestamp(3, Timestamp.valueOf(request.getTimestamp()));
            pstmt.executeUpdate();
        }
    }

    public List<SearchRequest> getAllSearchRequests() throws SQLException {
        List<SearchRequest> requests = new ArrayList<>();
        String selectSQL = "SELECT * FROM SearchRequests;";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(selectSQL)) {
            while (rs.next()) {
                SearchRequest request = new SearchRequest(
                        rs.getString("username"),
                        rs.getString("query"),
                        rs.getTimestamp("timestamp").toLocalDateTime()
                );
                requests.add(request);
            }
        }
        return requests;
    }
}
