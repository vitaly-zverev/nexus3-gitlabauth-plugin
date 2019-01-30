package fr.auchan.nexus3.gitlabauth.plugin;

import java.io.Serializable;
import java.util.Set;

public class GitlabPrincipal implements Serializable {
    private String username;
    private String email;
    private Set<String> groups;

    public void setUsername(String username) {
        this.username = username;
    }

     public void setEmail(String email) {
        this.email = email;
    }

    public void setGroups(Set<String> groups) {
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

    @Override
    public String toString() {
        return username;
    }
}