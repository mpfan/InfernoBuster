package infernobuster.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Souheil, Michael, Hoang, Tamer
 */
public class RuleTest {


    @Test
    public void isRedundant() {
        Rule rule1 = new Rule("10.0.0.2","10.0.0.2",0,0,Action.ALLOW, Direction.IN, Protocol.ANY, 0);
        Rule rule2 = new Rule("10.0.0.2","10.0.0.2",0,0,Action.ALLOW, Direction.IN, Protocol.ANY, 0);
        assertTrue(rule1.isRedundant(rule2));
    }

    @Test
    public void isInconsistent() {
        Rule rule1 = new Rule("10.0.0.2","10.0.0.2",0,0,Action.ALLOW, Direction.IN, Protocol.ANY, 0);
        Rule rule2 = new Rule("10.0.0.2","10.0.0.2",0,0,Action.DENY, Direction.IN, Protocol.ANY, 0);
        assertTrue(rule1.isInconsistent(rule2));
    }

    @Test
    public void isShadowing() {
        Rule rule1 = new Rule("125.168.24.0/23", "119.2.2.10/24", 33, 33, Action.ALLOW, Direction.IN, Protocol.TCP, 1);
        Rule rule2 = new Rule("125.168.24.0/16", "119.2.2.10/24", 33, 33, Action.DENY, Direction.IN, Protocol.TCP, 0);
        assertTrue(rule2.isShadowing(rule1));
    }

    @Test
    public void isDownRedundant() {
        Rule rule1 = new Rule("125.168.28.0/23", "119.2.2.10/24", 33, 33, Action.ALLOW, Direction.IN, Protocol.TCP, 1);
        Rule rule2 = new Rule("125.168.28.0/16", "119.2.2.10/24", 33, 33, Action.ALLOW, Direction.IN, Protocol.TCP, 0);
        assertTrue(rule2.isDownRedundant(rule1));
    }

    @Test
    public void isGeneralizing() {
        Rule rule1 = new Rule("125.168.14.0/23", "119.2.2.10/24", 33, 33, Action.ALLOW, Direction.IN, Protocol.TCP, 1);
        Rule rule2 = new Rule("125.168.14.0/24", "119.2.2.10/24", 33, 33, Action.DENY, Direction.IN, Protocol.TCP, 0);
        assertTrue(rule2.isGeneralizing(rule1));
    }

    @Test
    public void isUpRedundant() {
        Rule rule1 = new Rule("125.168.20.0/17", "119.2.2.10/24", 33, 33, Action.ALLOW, Direction.IN, Protocol.TCP, 1);
        Rule rule2 = new Rule("125.168.20.0/24", "119.2.2.10/24", 33, 33, Action.ALLOW, Direction.IN, Protocol.TCP, 0);
        assertTrue(rule2.isUpRedundant(rule1));
    }

    @Test
    public void isCorrelating() {
        Rule rule1 = new Rule("255.255.0.1/16", "10.0.4.0/23", 33, 33, Action.ALLOW, Direction.IN, Protocol.TCP, 0);
        Rule rule2 = new Rule("255.255.255.128/25", "10.0.4.0/23", 33, 33, Action.DENY, Direction.IN, Protocol.TCP, 0);
        assertTrue(rule2.isCorrelating(rule1));
    }

    @Test
    public void isPartialRedundant() {
        Rule rule1 = new Rule("255.255.0.1/16", "119.2.2.10/24", 33, 33, Action.ALLOW, Direction.IN, Protocol.TCP, 0);
        Rule rule2 = new Rule("255.255.255.128/25", "119.2.2.10/24", 33, 33, Action.ALLOW, Direction.IN, Protocol.TCP, 0);
        assertTrue(rule1.isPartialRedundant(rule2));
    }

}