package models;

/**
 * <p> Title: Role Enum. </p>
 * 
 * <p> Description: This enum represents the different roles a user can have in the system.
 * Each role has a corresponding code. The roles available are ADMIN, STUDENT, and INSTRUCTOR. </p>
 * 
 * <p> Each role is associated with a specific code:
 * <ul>
 *   <li> ADMIN: "A" </li>
 *   <li> STUDENT: "S" </li>
 *   <li> INSTRUCTOR: "I" </li>
 * </ul>
 * </p>
 * 
 * @author Naimish Maniya
 * 
 * @version 1.00   2024-10-09  Initial version.
 */
public enum Role {
    ADMIN("A"),
    STUDENT("S"),
    INSTRUCTOR("I");

    private final String code;

    /**
     * Constructor that assigns a specific code to each role.
     * 
     * @param code The code associated with the role.
     */
    Role(String code) {
        this.code = code;
    }

    /**
     * Returns the code associated with the role.
     * 
     * @return The code corresponding to the role.
     */
    public String getCode() {
        return code;
    }
}
