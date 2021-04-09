package infernobuster.model;

import org.apache.commons.net.util.*;


/**
 * This class is used to unfold the IP address and subnet mask, and hold the address coverage as a range
 */
public class IpRange {
    private long start; // CIDR
    private long end; // CIDR

    /**
     * @param ip
     */
    public IpRange(String ip) {
        resolveRange(ip);
    }

    /**
     * @return
     */
    public long getStart() {
        return start;
    }

    /**
     * @return
     */
    public long getEnd() {
        return end;
    }

    /**
     * @param ipRange
     * @return
     */
    public boolean equals(IpRange ipRange) {
        return Long.compareUnsigned(start, ipRange.start) == 0 && Long.compareUnsigned(end, ipRange.end) == 0;
    }

    /**
     * @param ipRange
     * @return
     */
    public boolean isSubset(IpRange ipRange) {
        return Long.compareUnsigned(start, ipRange.start) >= 0 && Long.compareUnsigned(end, ipRange.end) <= 0;
    }

    /**
     * @param ipRange
     * @return
     */
    public boolean isSuperset(IpRange ipRange) {
        return Long.compareUnsigned(start, ipRange.start) <= 0 && Long.compareUnsigned(end, ipRange.end) >= 0;
    }

    /**
     * Logic: start > other.start && start < other.end && end > other.end
     * or
     * start < other.start && end > other.start && end < other.end
     *
     * @param ipRange
     * @return
     */
    public boolean isIntersecting(IpRange ipRange) {
        return (Long.compareUnsigned(start, ipRange.start) <= 0 && Long.compareUnsigned(end, ipRange.start) >= 0 && Long.compareUnsigned(end, ipRange.end) <= 0)
                || (Long.compareUnsigned(start, ipRange.start) >= 0 && Long.compareUnsigned(start, ipRange.end) <= 0 && Long.compareUnsigned(end, ipRange.end) >= 0);
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
        if (ip.equalsIgnoreCase("any")) {
            ip = "0.0.0.0/0";
        }

        String[] parsed = ip.split("/"); // Splits the address and the mask

        String ipStart = parsed[0];
        String ipEnd = ipStart;

        if (parsed.length > 1) {            // If subnet mask exists, unfold mask
            SubnetUtils utils = new SubnetUtils(ip);
            String[] allIps = utils.getInfo().getAllAddresses();
            ipStart = allIps[0];
            ipEnd = allIps[allIps.length - 1];                    //allIps will contain all the ip address in the subnet, select last for range end
        }
        this.start = ipToLong(ipStart);
        this.end = ipToLong(ipEnd);
    }

	/**
	 * Refere to https://stackoverflow.com/questions/36836155/convert-ip-between-ipv4-and-numerical-format-in-java
	 * for reference
	 *
	 * @param ipAddress
	 * @return
	 */
	public long ipToLong(String ipAddress) {
        String[] ipAddressInArray = ipAddress.split("\\.");
        long result = 0;
        for (int i = 0; i < ipAddressInArray.length; i++) {
            int power = 3 - i;
            int ip = Integer.parseInt(ipAddressInArray[i]);
            result += ip * Math.pow(256, power);
        }
        return result;
    }
}
