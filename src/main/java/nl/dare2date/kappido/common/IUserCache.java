package nl.dare2date.kappido.common;

/**
 * Basic user cache interface
 */
public interface IUserCache<User> {

    /**
     * Gets an object by its String id. This could be a cached value, or it could be instantiated when not in cache.
     *
     * @param id The id that belongs to the user
     * @return The user-object
     */
    User getUserById(String id);

    /**
     * Directly injects an object into the cache with its id as key. This is used when calls to an API wrapper are made
     * and give info about more than just what was asked for in the initial HTTP request (for example, when Twitch
     * followings are requested, more than just the name of the following is returned. Data that could save another
     * HTTP request).
     *
     * @param user The user object being injected.
     * @param id   The id that belongs to the user. This same id should be used to get the user using {@link IUserCache#getUserById(String)}.
     */
    void addToCache(User user, String id);
}
