package az.ldap.sync.util;


public class Trend {

    private String hostId;
    
    private String hostName;
    
    private String itemId;
    
    private String category;
	
    private String clock;
	
    private String value_min;
	
    private String value_avg;
	
    private String value_max;

	public String getClock() {
		return clock;
	}

	public void setClock(String clock) {
		this.clock = clock;
	}

	public String getValue_min() {
		return value_min;
	}

	public void setValue_min(String value_min) {
		this.value_min = value_min;
	}

	public String getValue_avg() {
		return value_avg;
	}

	public void setValue_avg(String value_avg) {
		this.value_avg = value_avg;
	}

	public String getValue_max() {
		return value_max;
	}

	public void setValue_max(String value_max) {
		this.value_max = value_max;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
