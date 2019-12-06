package fr.auchan.nexus3.gitlabauth.plugin;

import fr.auchan.nexus3.gitlabauth.plugin.api.GitlabApiClient;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.eclipse.sisu.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sonatype.nexus.security.user.User;
import org.sonatype.nexus.security.user.UserManager;
import org.sonatype.nexus.security.user.UserStatus;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import java.util.Collections;

@Singleton
@Named
@Description("Gitlab Authentication Realm")
public class GitlabAuthenticatingRealm extends AuthorizingRealm {
    private GitlabApiClient gitlabClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(GitlabAuthenticatingRealm.class);

    private UserManager userManager;

    @Inject
    public GitlabAuthenticatingRealm(final GitlabApiClient gitlabClient, final UserManager userManager) {
        this.gitlabClient = gitlabClient;
        this.userManager = userManager;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        AuthorizationInfo info = null;
        Object principal = principals.getPrimaryPrincipal();
        if (principal instanceof GitlabPrincipal) {
            GitlabPrincipal gitlabPrincipal = (GitlabPrincipal) principal;
            LOGGER.info("doGetAuthorizationInfo for user {} with roles {}", gitlabPrincipal.getUsername(), gitlabPrincipal.getGroups());
            info = new SimpleAuthorizationInfo(gitlabPrincipal.getGroups());
        }
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        AuthenticationInfo info = null;
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        String username = token.getUsername();
        String password = String.valueOf((char[])token.getCredentials());
        LOGGER.info("doGetAuthenticationInfo for {}", username);

        try {
            GitlabPrincipal principal = gitlabClient.authz(username, password);
            createUserIfNecessary(principal);
            info = new SimpleAuthenticationInfo(principal, password, getName());
            LOGGER.info("Successfully authenticated {}", username);
        } catch (GitlabAuthenticationException e) {
            LOGGER.warn("Failed authentication", e);
        }
        return info;
    }
    
    private void createUserIfNecessary(GitlabPrincipal principal) {
        String username = principal.getUsername();
        if (!userManager.listUserIds().contains(username)) {
            User user = new User();
            user.setUserId(username);
            user.setStatus(UserStatus.active);
            user.setEmailAddress(principal.getEmail());
            user.setRoles(Collections.emptySet());
            userManager.addUser(user, principal.getPassword());
        }
    }
}