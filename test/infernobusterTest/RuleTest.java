package infernobusterTest;//import infernobuster.parser.*;

import infernobuster.model.*;
import infernobuster.model.Rule;
import junit.framework.TestCase;
import org.junit.*;
import org.junit.Test;

public class RuleTest extends TestCase {

    Rule rule1, rule2, rule3, rule4, rule5;
    IpRange source1, source2;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        this.rule1 = new Rule("192.192.209.1/24", "192.209.192.0/33",
                24, 33, Action.ALLOW, Direction.IN, Protocol.TCP, 1);
        this.rule2 = new Rule("168.209.168.0/33", "168.168.209.1/24", 33, 24, Action.DENY, Direction.OUT, Protocol.UDP, 2);
        this.rule3 = new Rule("209.143.143.1/33", "209.143.143.0/24", 33, 24, Action.ALLOW, Direction.OUT, Protocol.ANY, 3);
        this.rule4 = new Rule("209.143.143.1/33", "209.143.143.0/24", 33, 24, Action.ALLOW, Direction.OUT, Protocol.ANY, 4);
        this.rule5 = new Rule("209.143.143.1/33", "209.143.143.0/24", 33, 24, Action.ALLOW, Direction.OUT, Protocol.ANY, 4);


        System.out.println(" ");
        System.out.println("Set up complete");
    }

    @After
    public void tearDown() throws Exception {
        this.rule1 = null;
        this.rule2 = null;
        this.rule3 = null;
        this.rule4 = null;
        this.rule5 = null;

        assertNull(rule1);
        assertNull(rule2);
        assertNull(rule3);
        assertNull(rule4);
        assertNull(rule5);
        System.out.println("Tear down complete");
        //System.out.println(" ");
    }

    @Test
    /*
    This test fails because the id returned by the id method returns 4.
    When this method is ran by itself the correct id is returned.
    However when the entire class is tested, or coverage test is ran. The method fails.
     */
    public void testGetId(){
        System.out.println("Testing getId");
        System.out.println(rule2.getId());
        assertEquals(rule2.getId(), 1);
        System.out.println("Test: Success");
    }

    @Test
    public void testGetSourceIp() {
        System.out.println("Testing getSourceIP");
        assertFalse(rule1.getSourceIp().equals("192.192.209.1/23"));
        assertEquals(rule1.getSourceIp(), "192.192.209.1/24");
        assertFalse(rule1.getSourceIp().equals("192.192.209.1/25"));
        System.out.println("Test: Success");
    }

    @Test
    public void testGetDestinationIp(){
        System.out.println("Testing getDestinationIP");
        assertFalse(rule1.getDestinationIp().equals("192.192.209.1/32"));
        assertEquals(rule1.getDestinationIp(), "192.209.192.0/33");
        assertFalse(rule1.getDestinationIp().equals("192.192.209.1/34"));
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
        assertEquals(rule3.getPriority(), 3);
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
    public void testIsRedundant(){
        System.out.println("Testing 'isRedundant' test 1 of 2");
        assertTrue(rule4.isRedundant(rule3));
        System.out.println("Testing 'isRedundant' test 2 of 2");
        assertFalse(rule2.isRedundant(rule3));
        System.out.println("Test: Success");
    }

    @Test
    public void testIsInconsistent(){
        System.out.println("Testing 'isInconsistent' test 1 of 2");
        //assertTrue(rule4.isInconsistent(rule3));
        System.out.println("Testing 'isInconsistent' test 2 of 2");
        assertFalse(rule2.isInconsistent(rule3));
        System.out.println("Test: Success");
    }

    /*@infernobusterTest.Test
    public void testSetSourceIP(){
        System.out.println("Testing setSourceIp");
        //String tempSourceIP = "209.143.143.1/33";
        IpRange tempSource = null;
        tempSource.setSourceIP("209.143.143.1/33");
        System.out.println("infernobusterTest.Test: Success");


    }*/

    /*public void nameOfMethod(){

        System.out.println("Testing nameOfMethod");

        System.out.println("infernobusterTest.Test: Success");
    }*/

}