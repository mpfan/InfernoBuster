package infernobuster.detector;

import org.junit.Test;

import infernobuster.model.Anomaly;
import junit.framework.TestCase;

public class AnomalyTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	@Test	
	public	void testToString() {	
			assertEquals("REDUNDANCY",Anomaly.REDUNDANCY.name());
			assertEquals("INCONSISTENCY",Anomaly.INCONSISTENCY.name());
			assertEquals("SHADOWING",Anomaly.SHADOWING.name());
			assertEquals("DOWN_REDUNDANT",Anomaly.DOWN_REDUNDANT.name());
			assertEquals("GENERALIZIATION",Anomaly.GENERALIZIATION.name());
			assertEquals("UP_REDUNDANT",Anomaly.UP_REDUNDANT.name());
			assertEquals("CORRELATION",Anomaly.CORRELATION.name());
			assertEquals("PARTIAL_REDUNDANCY",Anomaly.PARTIAL_REDUNDANCY.name());

				}

		/**
		 * Test method for fromString() method.
		 */
	@Test
	public void testFromString() {
			assertEquals(null, Anomaly.fromString(""));
	}


}
