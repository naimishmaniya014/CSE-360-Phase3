package models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class User {

    /**
     * <p> Title: User Model. </p>
     * 
     * <p> Description: This class represents a user in the system. It stores essential information such as
     * username, password, email, names, roles, and flags indicating the user's state like first login or password reset requirement.
     * It also includes a one-time password (OTP) for password resets and the expiration time for the OTP. </p>
     * 
     * <p> The user can have multiple roles, and methods are provided to add or remove roles. </p>
     * 
     * @author Naimish Maniya
     * 
     * @version 1.00   2024-10-09  Initial version.
     */

    private String username;
    private String password;
    private boolean isFirstLogin;
    private String email;
    private String firstName;
    private String middleName;
    private String lastName;
    private String preferredName;
    private List<Role> roles;
    private String oneTimePassword;
    private boolean resetRequired;
    private LocalDateTime otpExpiration; // New field

    /**
     * Constructor that initializes the user with a username and password.
     * The user is marked as needing a first login, with no roles assigned initially.
     * 
     * @param username The username of the user.
     * @param password The password of the user.
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.isFirstLogin = true;
        this.roles = new ArrayList<>();
        this.resetRequired = false;
        this.otpExpiration = null;
    }

    /**
     * Returns the OTP expiration time.
     * 
     * @return The LocalDateTime when the OTP expires.
     */
    public LocalDateTime getOtpExpiration() {
        return otpExpiration;
    }

    /**
     * Sets the OTP expiration time.
     * 
     * @param otpExpiration The time when the OTP will expire.
     */
    public void setOtpExpiration(LocalDateTime otpExpiration) {
        this.otpExpiration = otpExpiration;
    }

    /**
     * Returns the username of the user.
     * 
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets a new username for the user.
     * 
     * @param username The new username.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the password of the user.
     * 
     * @return The password.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets a new password for the user.
     * 
     * @param password The new password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Returns whether this is the user's first login.
     * 
     * @return True if it's the first login, false otherwise.
     */
    public boolean isFirstLogin() {
        return isFirstLogin;
    }

    /**
     * Sets the first login flag for the user.
     * 
     * @param isFirstLogin True if this is the user's first login.
     */
    public void setFirstLogin(boolean isFirstLogin) {
        this.isFirstLogin = isFirstLogin;
    }

    /**
     * Returns the user's email address.
     * 
     * @return The user's email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the user's email address.
     * 
     * @param email The new email address.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the user's first name.
     * 
     * @return The first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the user's first name.
     * 
     * @param firstName The new first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the user's middle name.
     * 
     * @return The middle name.
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the user's middle name.
     * 
     * @param middleName The new middle name.
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /**
     * Returns the user's last name.
     * 
     * @return The last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the user's last name.
     * 
     * @param lastName The new last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the user's preferred name.
     * 
     * @return The preferred name.
     */
    public String getPreferredName() {
        return preferredName;
    }

    /**
     * Sets the user's preferred name.
     * 
     * @param preferredName The new preferred name.
     */
    public void setPreferredName(String preferredName) {
        this.preferredName = preferredName;
    }

    /**
     * Returns the list of roles assigned to the user.
     * 
     * @return A list of roles.
     */
    public List<Role> getRoles() {
        return roles;
    }

    /**
     * Sets a new list of roles for the user.
     * 
     * @param roles The list of roles.
     */
    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    /**
     * Returns the user's one-time password (OTP).
     * 
     * @return The one-time password.
     */
    public String getOneTimePassword() {
        return oneTimePassword;
    }

    /**
     * Sets the user's one-time password (OTP).
     * 
     * @param oneTimePassword The new OTP.
     */
    public void setOneTimePassword(String oneTimePassword) {
        this.oneTimePassword = oneTimePassword;
    }

    /**
     * Returns whether the user is required to reset their password.
     * 
     * @return True if a password reset is required, false otherwise.
     */
    public boolean isResetRequired() {
        return resetRequired;
    }

    /**
     * Sets whether the user is required to reset their password.
     * 
     * @param resetRequired True if a password reset is required.
     */
    public void setResetRequired(boolean resetRequired) {
        this.resetRequired = resetRequired;
    }

    // Role management

    /**
     * Adds a role to the user's role list if it's not already present.
     * 
     * @param role The role to be added.
     */
    public void addRole(Role role) {
        if (!roles.contains(role)) {
            roles.add(role);
        }
    }

    /**
     * Removes a role from the user's role list.
     * 
     * @param role The role to be removed.
     */
    public void removeRole(Role role) {
        roles.remove(role);
    }
}
