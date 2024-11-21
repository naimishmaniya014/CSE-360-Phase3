package models;

import java.util.Objects;

public class SpecialGroupAdminRights {
    private long groupId;
    private String username;

    public SpecialGroupAdminRights(long groupId, String username) {
        this.groupId = groupId;
        this.username = username;
    }

    public long getGroupId() {
        return groupId;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SpecialGroupAdminRights that = (SpecialGroupAdminRights) o;

        if (groupId != that.groupId) return false;
        return Objects.equals(username, that.username);
    }

    @Override
    public int hashCode() {
        int result = (int) (groupId ^ (groupId >>> 32));
        result = 31 * result + (username != null ? username.hashCode() : 0);
        return result;
    }
}
