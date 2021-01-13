package infernobuster.parser;

/**
 * 
 * @author 
 *
 */
public class IpRange {
	private int start; // CIDR
	private int end; // CIDR
	
	/**
	 * 
	 * @param ip
	 */
	public IpRange(String ip) {
		resolveRange(ip);
	}
	
	/**
	 * 
	 * @return
	 */
	public int getStart() {
		return start;
	}

	/**
	 * 
	 * @return
	 */
	public int getEnd() {
		return end;
	}
	
	/**
	 * 
	 * @param ipRange
	 * @return
	 */
	public boolean equals(IpRange ipRange) {
	 return Integer.compareUnsigned(start, ipRange.start) == 0 && Integer.compareUnsigned(end, ipRange.end) == 0;
	}
	
	/**
	 * 
	 * @param ipRange
	 * @return
	 */
	public boolean isSubset(IpRange ipRange) {
		return Integer.compareUnsigned(start, ipRange.start) >= 0 && Integer.compareUnsigned(end, ipRange.end) <= 0;
	}
	
	/**
	 * 
	 * @param ipRange
	 * @return
	 */
	public boolean isSuperset(IpRange ipRange) {
		return Integer.compareUnsigned(start, ipRange.start) <= 0 && Integer.compareUnsigned(end, ipRange.end) >= 0;
	}
	
	/**
	 * 
	 * @param ipRange
	 * @return
	 */
	public boolean isIntersecting(IpRange ipRange) {
		return !isSubset(ipRange) && !isSubset(ipRange) && (Integer.compareUnsigned(start, ipRange.start) <= 0 && Integer.compareUnsigned(end, ipRange.end) <= 0)
				|| Integer.compareUnsigned(start, ipRange.start) >= 0 && Integer.compareUnsigned(end, ipRange.end) >= 0;
	}
	
	/**
	 * 
	 */
	public String toString() {
		return start + " - " + end; 
	}
	
	/**
	 * 
	 * @param ip
	 */
	private void resolveRange(String ip) {
		if(ip.equalsIgnoreCase("any")) {
			ip = "0.0.0.0/0";
		}
		
		String[] parsed = ip.split("/"); // Splits the address and the mask
        String[] startingIp = parsed[0].split("\\.");
        
        int mask = 1;
        if(parsed.length > 1) {
            mask = Integer.parseInt(parsed[1]);
        }
        
        int ipStart = 0;
        // Converting to byte
        for(String octet : startingIp) {
            ipStart += Integer.parseInt(octet);
            ipStart = ipStart << 8;
        }

        this.start = ipStart;
        this.end = ipStart + ~mask;
	}
}
