package az.ldap.zabbix.entity;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("serial")
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "graphs")
public class Graph implements Serializable {

	@Id
	private String id;
	
	@Field("graphid")
	private String graphId;
	
	@Field("hostId")
	private String hostId;

	@Field("name")
	private String name;
	
	@Field("favorite")
	private Boolean favorite;
	
	@Field("type")
	private String type;
	
	@Field("trigger_status")
	private Boolean triggerStatus;
	
	@Field("trigger_count")
	private Integer triggerCount;

	@Field("width")
	private Integer width;

	@Field("height")
	private Integer height;

	@DBRef
	private List<GraphItem> graphItemsList;

	@Field("userid_")
	private Integer userid_;
	
	@Field("domainid_")
	private Integer domainid_;
	
	@Field("departmentid_")
	private Integer departmentid_;

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
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the width
	 */
	public Integer getWidth() {
		return width;
	}

	/**
	 * @param width the width to set
	 */
	public void setWidth(Integer width) {
		this.width = width;
	}
	
	public Boolean getTriggerStatus() {
		return triggerStatus;
	}

	public void setTriggerStatus(Boolean triggerStatus) {
		this.triggerStatus = triggerStatus;
	}

	/**
	 * @return the height
	 */
	public Integer getHeight() {
		return height;
	}

	/**
	 * @param height the height to set
	 */
	public void setHeight(Integer height) {
		this.height = height;
	}

	/**
	 * @return the graphItemsList
	 */
	public List<GraphItem> getGraphItemsList() {
		return graphItemsList;
	}

	/**
	 * @param graphItemsList the graphItemsList to set
	 */
	public void setGraphItemsList(List<GraphItem> graphItemsList) {
		this.graphItemsList = graphItemsList;
	}

	/**
	 * @return the graphId
	 */
	public String getGraphId() {
		return graphId;
	}

	/**
	 * @param graphId the graphId to set
	 */
	public void setGraphId(String graphId) {
		this.graphId = graphId;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getFavorite() {
		return favorite;
	}

	public void setFavorite(Boolean favorite) {
		this.favorite = favorite;
	}

	public Integer getUserid_() {
		return userid_;
	}

	public void setUserid_(Integer userid_) {
		this.userid_ = userid_;
	}

	public Integer getDomainid_() {
		return domainid_;
	}

	public void setDomainid_(Integer domainid_) {
		this.domainid_ = domainid_;
	}

	public Integer getDepartmentid_() {
		return departmentid_;
	}

	public void setDepartmentid_(Integer departmentid_) {
		this.departmentid_ = departmentid_;
	}

	public Integer getTriggerCount() {
		return triggerCount;
	}

	public void setTriggerCount(Integer triggerCount) {
		this.triggerCount = triggerCount;
	}
}
