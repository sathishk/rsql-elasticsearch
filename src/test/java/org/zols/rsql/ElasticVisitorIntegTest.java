/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zols.rsql;

import com.carrotsearch.randomizedtesting.RandomizedRunner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import cz.jirutka.rsql.parser.RSQLParser;
import cz.jirutka.rsql.parser.ast.Node;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.test.ESIntegTestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

//@ESIntegTestCase.ClusterScope(scope = ESIntegTestCase.Scope.SUITE, numDataNodes = 1)
//@RunWith(RandomizedRunner.class)
public class ElasticVisitorIntegTest {

//    private static final Faker FAKER = new Faker();
//
//    String id1 = UUID.randomUUID().toString();
//    String id2 = UUID.randomUUID().toString();
//    String id3 = UUID.randomUUID().toString();
//    String tweet1 = tweet(id1);
//    String tweet2 = tweet(id2);
//    String tweet3 = tweet(id3);
//
//    String index = "twitter";
//    String type = "tweet";
//    ElasticSearchVisitor elasticVisitor = new ElasticSearchVisitor(new ElasticComparisonNodeInterpreter());
//
//    String mapping
//            = "{\n"
//            + "  \"properties\": {\n"
//            + "    \"id\": {\n"
//            + "      \"type\": \"keyword\",\n"
//            + "      \"index\": true\n"
//            + "    },\n"
//            + "    \"author\": {\n"
//            + "      \"type\": \"text\",\n"
//            + "      \"index\": true\n"
//            + "    },\n"
//            + "    \"text\": {\n"
//            + "      \"type\": \"text\",\n"
//            + "      \"index\": true\n"
//            + "    },\n"
//            + "    \"ts\": {\n"
//            + "      \"type\": \"long\",\n"
//            + "      \"index\": true\n"
//            + "    }\n"
//            + "  }\n"
//            + "}";
//
//    @Before
//    public void setup() throws IOException {
//        ensureGreen();
//
//        if (!indexExists(index)) {
//            createIndex(index);
//
//            client().admin().indices().preparePutMapping()
//                    .setIndices(index)
//                    .setType(type)
//                    .setSource(mapping)
//                    .get();
//
//            initStaticTweets();
//
//            for (int i = 0; i < 7; i++) {
//                String id = UUID.randomUUID().toString();
//                indexTweet(id, tweet(id));
//            }
//
//            flushAndRefresh();
//        }
//    }
//
//    /*@Test
//    public void testQueries() throws IOException {
//        testIdEquals();
//        testIdIn();
//        testIdEqualsAndEquals();
//        testIdInOrFooEqualsBar();
//        testTextEqualsWildcard();
//        testTextEqualsWildcardAndIdNotIn();
//    }*/
//    @Test
//    public void testIdEquals() throws IOException {
//        Node rootNode = new RSQLParser().parse("id==" + id1);
//        QueryBuilder queryBuilder = rootNode.accept(elasticVisitor);
//
//        Map<String, String> tweets = tweets(queryBuilder);
//        assertEquals(1, tweets.size());
//        assertEquals(tweet1, tweets.get(id1));
//    }
//
//    @Test
//    public void testIdIn() throws IOException {
//        Node rootNode = new RSQLParser().parse("id=in=(" + id1 + "," + id2 + "," + id3 + ")");
//
//        QueryBuilder queryBuilder = rootNode.accept(elasticVisitor);
//
//        Map<String, String> tweets = tweets(queryBuilder);
//        assertEquals(3, tweets.size());
//        assertEquals(tweet1, tweets.get(id1));
//        assertEquals(tweet2, tweets.get(id2));
//        assertEquals(tweet3, tweets.get(id3));
//    }
//
//    @Test
//    public void testIdInOrFooEqualsBar() throws IOException {
//        Node rootNode = new RSQLParser().parse("id=in=(" + id1 + "," + id2 + "),foo==bar");
//
//        QueryBuilder queryBuilder = rootNode.accept(elasticVisitor);
//
//        Map<String, String> tweets = tweets(queryBuilder);
//        assertEquals(2, tweets.size());
//        assertEquals(tweet1, tweets.get(id1));
//        assertEquals(tweet2, tweets.get(id2));
//    }
//
//    @Test
//    public void testIdEqualsAndEquals() throws IOException {
//        Node rootNode = new RSQLParser().parse("id==" + id1 + ";id==" + id2);
//
//        QueryBuilder queryBuilder = rootNode.accept(elasticVisitor);
//
//        Map<String, String> tweets = tweets(queryBuilder);
//        assertEquals(0, tweets.size());
//    }
//
//    @Test
//    public void testTextEqualsWildcard() throws IOException {
//        // each quote has an e in it - most popular letter. lucky
//        Node rootNode = new RSQLParser().parse("text==*e*");
//
//        QueryBuilder queryBuilder = rootNode.accept(elasticVisitor);
//
//        Map<String, String> tweets = tweets(queryBuilder);
//        assertEquals(10, tweets.size());
//    }
//
//    @Test
//    public void testTextEqualsWildcardAndIdNotIn() throws IOException {
//        Node rootNode = new RSQLParser().parse("text==*e*;id=out=(" + id1 + "," + id2 + "," + id3 + ")");
//
//        QueryBuilder queryBuilder = rootNode.accept(elasticVisitor);
//
//        Map<String, String> tweets = tweets(queryBuilder);
//        assertEquals(7, tweets.size());
//        assertFalse(tweets.containsKey(id1));
//        assertFalse(tweets.containsKey(id2));
//        assertFalse(tweets.containsKey(id3));
//    }
//
//    @Test
//    public void testTextNotEqualsWildcardAndIdNotIn() throws IOException {
//        Node rootNode = new RSQLParser().parse("text!=*e*");
//
//        QueryBuilder queryBuilder = rootNode.accept(elasticVisitor);
//
//        Map<String, String> tweets = tweets(queryBuilder);
//        assertEquals(0, tweets.size());
//    }
//
//    @Test
//    public void testTextEqualsWildcardAndTsGreaterThan() throws IOException {
//        Node rootNode = new RSQLParser().parse("text==*e*;ts=gt=(0)");
//
//        QueryBuilder queryBuilder = rootNode.accept(elasticVisitor);
//
//        Map<String, String> tweets = tweets(queryBuilder);
//        assertEquals(10, tweets.size());
//        assertEquals(tweet1, tweets.get(id1));
//        assertEquals(tweet2, tweets.get(id2));
//        assertEquals(tweet3, tweets.get(id3));
//    }
//
//    @Test
//    public void testTextEqualsWildcardAndTsLessThan() throws IOException {
//        Node rootNode = new RSQLParser().parse("text==*e*;ts=lt=(0)");
//
//        QueryBuilder queryBuilder = rootNode.accept(elasticVisitor);
//
//        Map<String, String> tweets = tweets(queryBuilder);
//        assertEquals(0, tweets.size());
//    }
//
//    private void initStaticTweets() throws JsonProcessingException {
//        indexTweet(id1, tweet1);
//        indexTweet(id2, tweet2);
//        indexTweet(id3, tweet3);
//    }
//
//    private String tweet(String id) {
//        return String.format(
//                "{ \"id\": \"%s\", \"author\": \"%s\", \"text\": \"%s\", \"ts\": %d }",
//                id,
//                FAKER.name().fullName(),
//                FAKER.shakespeare().hamletQuote(),
//                FAKER.number().randomNumber());
//    }
//
//    private void indexTweet(String id, String tweet) throws JsonProcessingException {
//        client().prepareIndex(index, type)
//                .setId(id)
//                .setSource(tweet)
//                .get();
//    }
//
//    private Map<String, String> tweets(QueryBuilder queryBuilder) throws IOException {
//        SearchResponse searchResponse = client().prepareSearch()
//                .setIndices(index)
//                .setTypes(type)
//                .setQuery(queryBuilder)
//                .get();
//
//        Map<String, String> tweets = new HashMap<>();
//        for (SearchHit searchHit : searchResponse.getHits().getHits()) {
//            tweets.put(searchHit.getId(), searchHit.getSourceAsString());
//        }
//
//        return tweets;
//    }
}
