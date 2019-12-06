package fr.auchan.nexus3.gitlabauth.plugin;

import java.util.Set;

public class GitlabPrincipal {
    private final String username;
    private final String email;
    private final Set<String> groups;
    private final String password;

    public GitlabPrincipal(String username, String password, String email, Set<String> groups) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.groups = groups;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public Set<String> getGroups() {
        return groups;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return username;
    }
}