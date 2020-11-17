package infernobuster.parser;

public class IpRange {
	private int start; // CIDR
	private int end; // CIDR
	
	public IpRange(String ip) {
		resolveRange(ip);
	}
	
	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}
	
	public boolean equals(IpRange ipRange) {
	 return Integer.compareUnsigned(start, ipRange.start) == 0 && Integer.compareUnsigned(end, ipRange.end) == 0;
	}
	
	public boolean isSubset(IpRange ipRange) {
		return Integer.compareUnsigned(start, ipRange.start) >= 0 && Integer.compareUnsigned(end, ipRange.end) <= 0;
	}
	
	public boolean isSuperset(IpRange ipRange) {
		return Integer.compareUnsigned(start, ipRange.start) <= 0 && Integer.compareUnsigned(end, ipRange.end) >= 0;
	}
	
	public boolean isIntersecting(IpRange ipRange) {
		return (Integer.compareUnsigned(start, ipRange.start) <= 0 && Integer.compareUnsigned(end, ipRange.end) <= 0)
				|| Integer.compareUnsigned(start, ipRange.start) >= 0 && Integer.compareUnsigned(end, ipRange.end) >= 0;
	}
	
	public String toString() {
		return start + " - " + end; 
	}
	
	private void resolveRange(String ip) {
		String[] parsed = ip.split("/"); // Splits the address and the mask
		String[] startingIp = parsed[0].split("\\.");
		int mask = Integer.parseInt(parsed[1]);
		
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
