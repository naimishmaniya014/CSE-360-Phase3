package models;

public class SpecialGroupAccessRights {
    private long groupId;
    private String username;
    private AccessRight accessRight;

    public SpecialGroupAccessRights(long groupId, String username, AccessRight accessRight) {
        this.groupId = groupId;
        this.username = username;
        this.accessRight = accessRight;
    }

    public long getGroupId() {
        return groupId;
    }

    public String getUsername() {
        return username;
    }

    public AccessRight getAccessRight() {
        return accessRight;
    }

    public void setAccessRight(AccessRight accessRight) {
        this.accessRight = accessRight;
    }

    public enum AccessRight {
        ADMIN,
        VIEW_DECRYPTED
    }
}
