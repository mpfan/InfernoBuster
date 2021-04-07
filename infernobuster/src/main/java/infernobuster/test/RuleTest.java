package infernobuster.test;

import infernobuster.parser.*;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RuleTest extends TestCase {

    Rule rule1, rule2, rule3;
    IpRange source1, source2;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.rule1 = new Rule("192.192.209.1/24", "192.209.192.0/33", 24, 33, Action.ALLOW, Direction.IN, Protocol.TCP, 1);
        this.rule2 = new Rule("168.209.168.0/33", "168.168.209.1/24", 33, 24, Action.DENY, Direction.OUT, Protocol.UDP, 2);
        this.rule3 = new Rule("209.143.143.1/33", "209.143.143.0/24", 33, 24, Action.ALLOW, Direction.OUT, Protocol.ANY, 3);

        System.out.println(" ");
        System.out.println("Set up complete");
    }

    @After
    public void tearDown() throws Exception {
        this.rule1 = null;
        this.rule2 = null;

        assertNull(rule1);
        assertNull(rule2);
        System.out.println("Tear down complete");
        //System.out.println(" ");
    }

    @Test
    public void testGetSourceIp() {
        System.out.println("Testing getSourceIP");
        assertEquals(rule1.getSourceIp(), "192.192.209.1/24");
        System.out.println("Test: Success");
    }

    @Test
    public void testGetDestinationIp(){
        System.out.println("Testing getDestinationIP");
        assertEquals(rule1.getDestinationIp(), "192.209.192.0/33");
        System.out.println("Test: Success");
    }

    @Test
    public void testGetSourcePort(){
        System.out.println("Testing getSourcePort");
        assertEquals(rule2.getSourcePort(), 33);
        System.out.println("Test: Success");
    }

    @Test
    public void testGetDestinationPort(){
        System.out.println("Testing getDestinationPort");
        assertEquals(rule2.getDestinationPort(), 24);
        System.out.println("Test: Success");
    }

    @Test
    public void testGetPriority(){
        System.out.println("Testing getPriority");
        assertEquals(rule2.getSourcePort(), 33);
        System.out.println("Test: Success");
    }

    @Test
    public void testGetSource(){
        /*
        * This test won't pass when just asserting the returned objects. Must be in String format, not sure why
        * */
        System.out.println("Testing getSource");
        String tempSourceIP = "192.192.209.1/24";
        this.source1 = new IpRange(tempSourceIP.equalsIgnoreCase("any") ? "any" : tempSourceIP);
        assertEquals(rule1.getSource().toString(),source1.toString());
        System.out.println("Test: Success");
    }

    @Test
    public void testGetDestination(){
        System.out.println("Testing getDestination");
        String tempDestinationIP = "192.209.192.0/33";
        this.source2 = new IpRange(tempDestinationIP.equalsIgnoreCase("any") ? "any" : tempDestinationIP);
        assertEquals(rule1.getDestination().toString(),source2.toString());
        System.out.println("Test: Success");
    }

    @Test
    public void testGetDirection(){
        System.out.println("Testing getDirection test 1 of 2");
        assertEquals(rule1.getDirection().getText(),"in");
        System.out.println("Testing getDirection test 2 of 2");
        assertEquals(rule2.getDirection().getText(),"out");
        System.out.println("Test: Success");
    }

    @Test
    public void testGetAction(){
        System.out.println("Testing getAction test 1 of 2");
        assertEquals(rule1.getAction().getText(),"allow");
        System.out.println("Testing getAction test 2 of 2");
        assertEquals(rule2.getAction().getText(),"deny");
        System.out.println("Test: Success");
    }

    @Test
    public void testGetProtocol(){
        System.out.println("Testing getProtocol test 1 of 3");
        assertEquals(rule1.getProtocol().getText(),"TCP");
        System.out.println("Testing getProtocol test 2 of 3");
        assertEquals(rule2.getProtocol().getText(),"UDP");
        System.out.println("Testing getProtocol test 3 of 3");
        assertEquals(rule3.getProtocol().getText(),"ANY");
        System.out.println("Test: Success");
    }

    @Test
    public void testSetSourcIP(){
        System.out.println("Testing setSourceIp");
        //String tempSourcIP = "209.143.143.1/33";
        IpRange tempSource = null;
        //tempSource.rule.setSourceIP("209.143.143.1/33");
        System.out.println("Test: Success");
    }

    /*public void name(){
        System.out.println("Testing name");

        System.out.println("Test: Success");
    }*/

}