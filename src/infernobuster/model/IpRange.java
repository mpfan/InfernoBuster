package infernobuster.model;

/**
 * This class is used to unfold the IP address and subnet mask, and hold the address coverage as a range 
 *
 */
public class IpRange {
	private long start; // CIDR
	private long end; // CIDR
	
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
	public long getStart() {
		return start;
	}

	/**
	 * 
	 * @return
	 */
	public long getEnd() {
		return end;
	}
	
	/**
	 * 
	 * @param ipRange
	 * @return
	 */
	public boolean equals(IpRange ipRange) {
	 return Long.compareUnsigned(start, ipRange.start) == 0 && Long.compareUnsigned(end, ipRange.end) == 0;
	}
	
	/**
	 * 
	 * @param ipRange
	 * @return
	 */
	public boolean isSubset(IpRange ipRange) {
		return Long.compareUnsigned(start, ipRange.start) >= 0 && Long.compareUnsigned(end, ipRange.end) <= 0;
	}
	
	/**
	 * 
	 * @param ipRange
	 * @return
	 */
	public boolean isSuperset(IpRange ipRange) {
		return Long.compareUnsigned(start, ipRange.start) <= 0 && Long.compareUnsigned(end, ipRange.end) >= 0;
	}
	
	/**
	 * 
	 * @param ipRange
	 * @return
	 */
	public boolean isIntersecting(IpRange ipRange) {
		return !isSubset(ipRange) && !isSubset(ipRange) && (Long.compareUnsigned(start, ipRange.start) <= 0 && Long.compareUnsigned(end, ipRange.end) <= 0)
				|| Long.compareUnsigned(start, ipRange.start) >= 0 && Long.compareUnsigned(end, ipRange.end) >= 0;
	}
	
	/**
	 * 
	 */
	public String toString() {
		return start + " - " + end; 
	}
	
	/**
	 * Unfolds the mask into an ip range. 
	 */
	private void resolveRange(String ip) {
		if(ip.equalsIgnoreCase("any")) {
			ip = "0.0.0.0/0";
		}
		
		String[] parsed = ip.split("/"); // Splits the address and the mask
        String[] startingIp = parsed[0].split("\\.");
        
        long mask = 1;
        if(parsed.length > 1) {
            mask = Long.parseLong(parsed[1]);
        }
        
        long ipStart = 0;
        // Converting to byte
        for(String octet : startingIp) {
            ipStart += Long.parseLong(octet);
            ipStart = ipStart << 8;
        }

        this.start = ipStart;
        this.end = ipStart + ~mask;
	}
}
