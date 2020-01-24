package com.gc.auth.security.session;

import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;

import java.util.List;

/**
 * @author shizhongming
 * 2020/1/17 9:02 下午
 */
public class RestSessionRegistryImpl implements SessionRegistry {
    @Override
    public List<Object> getAllPrincipals() {
        return null;
    }

    @Override
    public List<SessionInformation> getAllSessions(Object principal, boolean includeExpiredSessions) {
        return null;
    }

    @Override
    public SessionInformation getSessionInformation(String sessionId) {
        return null;
    }

    @Override
    public void refreshLastRequest(String sessionId) {
    }

    @Override
    public void registerNewSession(String sessionId, Object principal) {

    }

    @Override
    public void removeSessionInformation(String sessionId) {

    }
}
