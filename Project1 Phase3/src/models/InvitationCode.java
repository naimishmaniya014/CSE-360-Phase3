package models;

import java.time.LocalDateTime;
import Controllers.*;
import Utilities.*;
import java.util.ArrayList;
import java.util.List;

public class InvitationCode {

    /**
     * <p> Title: Invitation Code Model. </p>
     * 
     * <p> Description: This class represents an invitation code that can be used to create a new user.
     * The invitation code contains a unique code, a list of roles that the new user will have, and
     * a flag indicating whether the code has been used. </p>
     * 
     * @author Naimish Maniya
     * 
     * @version 1.00   2024-10-09  Initial version.
     */

    private String code;
    private List<Role> roles;
    private boolean isUsed;

    /**
     * Constructor that initializes the invitation code with a unique code and a list of roles.
     * The invitation is initially marked as unused.
     * 
     * @param code  The unique invitation code.
     * @param roles The list of roles assigned to the user who uses this invitation.
     */
    public InvitationCode(String code, List<Role> roles) {
        this.code = code;
        this.roles = roles;
        this.isUsed = false;
    }

    /**
     * Returns the invitation code.
     * 
     * @return The unique invitation code.
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets a new value for the invitation code.
     * 
     * @param code The new invitation code.
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Returns the list of roles assigned with this invitation code.
     * 
     * @return A list of roles.
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * Sets a new list of roles for this invitation code.
     * 
     * @param roles The list of roles to be assigned.
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * Returns whether this invitation code has been used.
     * 
     * @return True if the code has been used, false otherwise.
     */
    public boolean isUsed() {
        return isUsed;
    }

    /**
     * Marks this invitation code as used or unused.
     * 
     * @param isUsed True to mark the code as used, false to mark it as unused.
     */
    public void setUsed(boolean isUsed) {
        this.isUsed = isUsed;
    }
}
