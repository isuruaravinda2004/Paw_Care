package com.pawcare.util;
import com.pawcare.model.User;
public final class Session {
    private Session() { }
    private static User currentUser;
    public static User getUser() { return currentUser; }
    public static void setUser(User user) { currentUser=user; }
    public static void clear() { currentUser=null; }
}
