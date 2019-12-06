package fr.auchan.nexus3.gitlabauth.plugin.api;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import fr.auchan.nexus3.gitlabauth.plugin.GitlabAuthenticationException;
import fr.auchan.nexus3.gitlabauth.plugin.GitlabPrincipal;
import fr.auchan.nexus3.gitlabauth.plugin.config.GitlabAuthConfiguration;
import org.gitlab.api.GitlabAPI;
import org.gitlab.api.Pagination;
import org.gitlab.api.models.GitlabGroup;
import org.gitlab.api.models.GitlabUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Singleton
@Named("GitlabApiClient")
public class GitlabApiClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitlabApiClient.class);

    private GitlabAPI client;
    private GitlabAuthConfiguration configuration;
    // Cache token lookups to reduce the load on Github's User API to prevent hitting the rate limit.
    private Cache<String, GitlabPrincipal> tokenToPrincipalCache;

    @Inject
    public GitlabApiClient(GitlabAuthConfiguration configuration) {
        this.client = GitlabAPI.connect(configuration.getGitlabApiUrl(), configuration.getGitlabApiKey());
        this.client.ignoreCertificateErrors(configuration.getGitlabIgnoreCertificateErrors());
        this.configuration = configuration;
        this.tokenToPrincipalCache = CacheBuilder.newBuilder()
                .expireAfterWrite(configuration.getPrincipalCacheTtl().toMillis(), TimeUnit.MILLISECONDS).build();
    }

    public GitlabPrincipal authz(String login, String password) throws GitlabAuthenticationException {
        // Combine the login and the token as the cache key since they are both used to generate the principal. If either changes we should obtain a new
        // principal.
        String cacheKey = login + "|" + password;
        GitlabPrincipal cached = tokenToPrincipalCache.getIfPresent(cacheKey);
        if (cached != null) {
            LOGGER.debug("Using cached principal for login: {}", login);
            return cached;
        } else {
            GitlabPrincipal principal = doAuthz(login, password);
            tokenToPrincipalCache.put(cacheKey, principal);
            return principal;
        }
    }

    private GitlabPrincipal doAuthz(String loginName, String password) throws GitlabAuthenticationException {
        GitlabUser gitlabUser;

        try {
            GitlabAPI gitlabAPI = GitlabAPI.connect(configuration.getGitlabApiUrl(), password);
            gitlabUser = gitlabAPI.getUser();
        } catch (Exception e) {
            throw new GitlabAuthenticationException(e);
        }

        if (gitlabUser == null) {
            throw new GitlabAuthenticationException("Given username not found!");
        }

        if ( ! loginName.equals(gitlabUser.getUsername()) && ! loginName.equals(gitlabUser.getEmail())) {
            throw new GitlabAuthenticationException("Given username does not match GitLab username or email!");
        }

        return new GitlabPrincipal(
                gitlabUser.getUsername(),
                password,
                gitlabUser.getEmail(),
                getGroups((gitlabUser.getUsername())));
    }

    private Set<String> getGroups(String username) {
        List<GitlabGroup> groups;
        try {
            groups = client.getGroupsViaSudo(username,new Pagination().withPerPage(Pagination.MAX_ITEMS_PER_PAGE));
        } catch (Throwable e) {
            LOGGER.error("Could not fetch groups for username {}", username);
            return Collections.emptySet();
        }
        return groups.stream().map(GitlabGroup::getPath).collect(Collectors.toSet());
    }


}