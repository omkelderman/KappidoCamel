package nl.dare2date.kappido.common;

import java.util.HashMap;

/**
 * Basic partial cache implementation. Caches user objects and returns them when available in the cache. Creates
 * a new user by calling an abstract factory method when not in cache.
 */
public abstract class AbstractUserCache<User> implements IUserCache<User> {
    private HashMap<String, User> cache = new HashMap<>();

    @Override
    public User getUserById(String id) {
        User user = cache.get(id);
        if (user == null) {
            user = createNewUser(id);
            addToCache(user, id);
        }
        return user;
    }

    protected abstract User createNewUser(String id);

    @Override
    public void addToCache(User user, String id) {
        cache.put(id, user);
    }
}
