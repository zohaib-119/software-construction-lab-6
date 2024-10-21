package twitter;

import static org.junit.Assert.*;
import java.time.Instant;
import java.util.*;
import org.junit.Test;

public class SocialNetworkTest {


    // Test 1: Empty List of Tweets
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }

    // Test 2: Tweets Without Mentions
    @Test
    public void testGuessFollowsGraphNoMentions() {
        List<Tweet> tweets = List.of(
            new Tweet(1, "alice", "Hello World!", Instant.now())
        );

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue("expected empty graph", followsGraph.get("alice").isEmpty());
    }

    // Test 3: Single Mention
    @Test
    public void testGuessFollowsGraphSingleMention() {
        List<Tweet> tweets = List.of(
            new Tweet(1, "alice", "Hello @bob", Instant.now())
        );

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.get("alice").contains("bob"));
    }

    // Test 4: Multiple Mentions in One Tweet
    @Test
    public void testGuessFollowsGraphMultipleMentions() {
        List<Tweet> tweets = List.of(
            new Tweet(1, "alice", "Hello @bob @charlie", Instant.now())
        );

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.get("alice").contains("bob"));
        assertTrue(followsGraph.get("alice").contains("charlie"));
    }

    // Test 5: Multiple Tweets from One User
    @Test
    public void testGuessFollowsGraphMultipleTweets() {
        List<Tweet> tweets = List.of(
            new Tweet(1, "alice", "Hi @bob", Instant.now()),
            new Tweet(2, "alice", "Hey @charlie", Instant.now())
        );

        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(tweets);
        assertTrue(followsGraph.get("alice").contains("bob"));
        assertTrue(followsGraph.get("alice").contains("charlie"));
    }

    // Test 6: Empty Graph for Influencers
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected empty list", influencers.isEmpty());
    }

    // Test 7: Single User Without Followers
    @Test
    public void testSingleUserWithoutFollowers() {
        Map<String, Set<String>> followsGraph = Map.of("alice", Set.of());
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertTrue("expected empty list", influencers.isEmpty());
    }

    // Test 8: Single Influencer
    @Test
    public void testSingleInfluencer() {
        Map<String, Set<String>> followsGraph = Map.of("alice", Set.of("bob"));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals(List.of("bob"), influencers);
    }

    // Test 9: Multiple Influencers
    @Test
    public void testMultipleInfluencers() {
        Map<String, Set<String>> followsGraph = Map.of(
            "alice", Set.of("bob", "charlie"),
            "dave", Set.of("charlie")
        );

        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals(List.of("charlie", "bob"), influencers);
    }

    // Test 10: Tied Influence
    @Test
    public void testTiedInfluence() {
        Map<String, Set<String>> followsGraph = Map.of(
            "alice", Set.of("bob"),
            "charlie", Set.of("bob"),
            "dave", Set.of("eve")
        );

        List<String> influencers = SocialNetwork.influencers(followsGraph);
        assertEquals(List.of("bob", "eve"), influencers);
    }
}
