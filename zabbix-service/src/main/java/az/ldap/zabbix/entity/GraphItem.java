package az.ldap.zabbix.entity;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "graph_items")
@SuppressWarnings("serial")
public class GraphItem implements Serializable {

	@Id
	private String id;

	@Field("itemid")
	private String itemId;
	
	@Field("gitemid")
	private String gitemId;

	@Field("color")
	private String color;
	
	@Field("hostid")
	private String hostId;
	
	@Field("graphid")
	private String graphId;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the itemId
	 */
	public String getItemId() {
		return itemId;
	}

	/**
	 * @param itemId the itemId to set
	 */
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * @return the gitemId
	 */
	public String getGitemId() {
		return gitemId;
	}

	/**
	 * @param gitemId the gitemId to set
	 */
	public void setGitemId(String gitemId) {
		this.gitemId = gitemId;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getGraphId() {
		return graphId;
	}

	public void setGraphId(String graphId) {
		this.graphId = graphId;
	}
}
