package nl.dare2date.kappido.matching;

import nl.dare2date.kappido.services.MatchEntry;

import java.util.List;

/**
 * Interface for classes that describe a way to match users.
 */
public interface IMatcher {

    /**
     * Called when potential matches for a given Dare2Date user are requested. The method should return a list of
     * potential matches and their probability.
     *
     * @param dare2DateUser The user that is used to match with.
     * @return A list of Dare2Date user id's and their matching probability with the dare2DateUser. The list can
     * contain duplicates of the same user id's. In the matchmaking algorithm these are combined by adding up the
     * probabilities.
     */
    List<MatchEntry> findMatches(int dare2DateUser);
}
