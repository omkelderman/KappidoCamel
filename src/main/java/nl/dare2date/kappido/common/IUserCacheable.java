package nl.dare2date.kappido.common;

/**
 * Every class implementing this interface implies it has (optional!) functionality that uses a {@link IUserCache}.
 * This functionality can than be enabled by calling the {@link #setCache(IUserCache)} method.
 */
public interface IUserCacheable<User> {

    /**
     * Sets the {@link IUserCache} to use for the caching functionality or <code>null</code> to disable caching
     *
     * @param userCache The {@link IUserCache} to use, or <code>null</code>
     */
    void setCache(IUserCache<User> userCache);
}
