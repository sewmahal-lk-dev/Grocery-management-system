package lk.evergreen.grocery.entity;

/**
 * Application access level stored on each {@link User}.
 * <ul>
 *   <li>{@link #USER} — normal customer (default on self-registration).</li>
 *   <li>{@link #ADMIN} — staff; not granted via public signup (set in DB or future admin tooling).</li>
 * </ul>
 */
public enum UserRole {
    USER,
    ADMIN
}
