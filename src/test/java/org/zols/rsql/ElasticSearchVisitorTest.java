package org.zols.rsql;

import cz.jirutka.rsql.parser.ast.AndNode;
import cz.jirutka.rsql.parser.ast.ComparisonNode;
import cz.jirutka.rsql.parser.ast.ComparisonOperator;
import cz.jirutka.rsql.parser.ast.OrNode;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;

import static java.util.Arrays.asList;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Unit test for simple App.
 */
public class ElasticSearchVisitorTest {

    @InjectMocks
    ElasticSearchVisitor elasticVisitor;

    @Mock
    ComparisonNodeInterpreter<QueryBuilder> comparisonNodeInterpreter;

    @Before
    public void before() {
        initMocks(this);
    }

    String selector = "foo";
    String arg = "4.2";
    List<String> args = Collections.singletonList(arg);
    ComparisonOperator equals = new ComparisonOperator("==", false);
    ComparisonOperator notEquals = new ComparisonOperator("!=", false);
    ComparisonNode equalsNode = new ComparisonNode(equals, selector, args);
    ComparisonNode notEqualsNode = new ComparisonNode(notEquals, selector, args);
    List<ComparisonNode> comparisonNodes = asList(equalsNode, notEqualsNode);

    @Test
    public void testVisitOr() {
        // Arrange
        QueryBuilder expectedQuery = boolQuery()
                .should(boolQuery())
                .should(boolQuery());
        when(comparisonNodeInterpreter.interpret(any(ComparisonNode.class))).thenReturn(boolQuery());
        OrNode node = new OrNode(comparisonNodes);

        // Act
        QueryBuilder actualQuery = elasticVisitor.visit(node);

        // Assert
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testVisitAnd() {
        // Arrange
        QueryBuilder query = boolQuery();
        when(comparisonNodeInterpreter.interpret(any(ComparisonNode.class))).thenReturn(query);
        AndNode node = new AndNode(comparisonNodes);
        QueryBuilder expectedQuery = boolQuery()
                .must(boolQuery())
                .must(boolQuery());

        // Act
        QueryBuilder actualQuery = elasticVisitor.visit(node);

        // Assert
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testVisitAndOr() {
        // Arrange
        QueryBuilder query = boolQuery();
        when(comparisonNodeInterpreter.interpret(any(ComparisonNode.class))).thenReturn(query);
        OrNode orNode = new OrNode(comparisonNodes);
        AndNode andNode = new AndNode(comparisonNodes).withChildren(
                Collections.singletonList(orNode));
        QueryBuilder expectedQuery = boolQuery()
                .must(boolQuery().should(boolQuery()).should(boolQuery()));

        // Act
        QueryBuilder actualQuery = elasticVisitor.visit(andNode);

        // Assert
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testVisitComparison() {
        // Arrange
        QueryBuilder expectedQuery = boolQuery();
        ComparisonOperator comparisonOperator = new ComparisonOperator("==", false);
        ComparisonNode node = new ComparisonNode(comparisonOperator, "foo", Collections.singletonList("bar"));
        when(comparisonNodeInterpreter.interpret(any(ComparisonNode.class))).thenReturn(boolQuery());

        // Act
        QueryBuilder actualQuery = elasticVisitor.visit(node);

        // Assert
        assertEquals(expectedQuery, actualQuery);
    }

    @Test
    public void testVisitInvalid() {
        // Arrange
        QueryBuilder query = boolQuery();
        when(comparisonNodeInterpreter.interpret(any(ComparisonNode.class))).thenReturn(query);
        OrNode orNode = new OrNode(comparisonNodes);
        AndNode andNode = new AndNode(comparisonNodes).withChildren(
                Collections.singletonList(orNode));
        QueryBuilder expectedQuery = boolQuery()
                .must(boolQuery().should(boolQuery()).should(boolQuery()));

        // Act
        QueryBuilder actualQuery = elasticVisitor.visit(andNode);

        // Assert
        assertEquals(expectedQuery, actualQuery);
    }
}
