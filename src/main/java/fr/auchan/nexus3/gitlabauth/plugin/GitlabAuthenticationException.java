package fr.auchan.nexus3.gitlabauth.plugin;

public class GitlabAuthenticationException extends Exception {

    private static final long serialVersionUID = 1L;

    public GitlabAuthenticationException(String message){
        super(message);
    }

    public GitlabAuthenticationException(Throwable cause){
        super(cause);
    }

}