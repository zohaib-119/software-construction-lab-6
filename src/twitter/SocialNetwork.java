/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package twitter;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
	public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
	    // Map to store each author's followers (users they mention)
	    Map<String, Set<String>> followsGraph = new HashMap<>();
	    // Pattern to match mentions in the format '@username'
	    Pattern mentionPattern = Pattern.compile("@(\\w+)");

	    // Iterate through all tweets
	    for (Tweet tweet : tweets) {
	        String author = tweet.getAuthor().toLowerCase(); // Author of the tweet (case-insensitive)
	        Matcher matcher = mentionPattern.matcher(tweet.getText()); // Find mentions in the tweet text
	        followsGraph.putIfAbsent(author, new HashSet<>()); // Initialize the author's entry if absent

	        // Add all valid mentioned users to the author's set of follows
	        while (matcher.find()) {
	            String mentionedUser = matcher.group(1).toLowerCase(); // Extract mentioned username
	            if (!mentionedUser.equals(author)) { // Avoid self-mentions
	                followsGraph.get(author).add(mentionedUser);
	            }
	        }
	    }

	    return followsGraph; // Return the completed follows graph
	}

	
	/**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */

    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        Map<String, Integer> followerCounts = new HashMap<>();

        // Calculate follower count for each user
        for (Set<String> followers : followsGraph.values()) {
            for (String user : followers) {
                followerCounts.put(user, followerCounts.getOrDefault(user, 0) + 1);
            }
        }

        // Sort users by follower count in descending order
        List<String> influencers = new ArrayList<>(followerCounts.keySet());
        influencers.sort((a, b) -> followerCounts.get(b) - followerCounts.get(a));

        return influencers;
    }

}
