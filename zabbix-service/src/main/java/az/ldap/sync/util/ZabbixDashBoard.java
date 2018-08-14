package az.ldap.sync.util;

import java.util.List;

import az.ldap.zabbix.entity.Graph;

public class ZabbixDashBoard {
	
    private Integer activeHosts;
    
    private Integer inActiveHosts;
    
    private Integer activeItems;
    
    private Integer InActiveItems;
    
    private Integer activeGraphs;
    
    private Integer inActiveGraphs;
    
    private Integer activeTriggers;
    
    private Integer inActiveTriggers;
    
    private List<Trend> trends;
    
    private List<Graph> favGraphs;
    
    private Integer infoAlerts;
    
    private Integer warnAlerts;
    
    private Integer criticalAlerts;

	public Integer getActiveHosts() {
		return activeHosts;
	}

	public void setActiveHosts(Integer activeHosts) {
		this.activeHosts = activeHosts;
	}

	public Integer getInActiveHosts() {
		return inActiveHosts;
	}

	public void setInActiveHosts(Integer inActiveHosts) {
		this.inActiveHosts = inActiveHosts;
	}

	public Integer getActiveItems() {
		return activeItems;
	}

	public void setActiveItems(Integer activeItems) {
		this.activeItems = activeItems;
	}

	public Integer getInActiveItems() {
		return InActiveItems;
	}

	public void setInActiveItems(Integer inActiveItems) {
		InActiveItems = inActiveItems;
	}

	public Integer getActiveGraphs() {
		return activeGraphs;
	}

	public void setActiveGraphs(Integer activeGraphs) {
		this.activeGraphs = activeGraphs;
	}

	public Integer getInActiveGraphs() {
		return inActiveGraphs;
	}

	public void setInActiveGraphs(Integer inActiveGraphs) {
		this.inActiveGraphs = inActiveGraphs;
	}

	public Integer getActiveTriggers() {
		return activeTriggers;
	}

	public void setActiveTriggers(Integer activeTriggers) {
		this.activeTriggers = activeTriggers;
	}

	public Integer getInActiveTriggers() {
		return inActiveTriggers;
	}

	public void setInActiveTriggers(Integer inActiveTriggers) {
		this.inActiveTriggers = inActiveTriggers;
	}

	public List<Trend> getTrends() {
		return trends;
	}

	public void setTrends(List<Trend> trends) {
		this.trends = trends;
	}

	public List<Graph> getFavGraphs() {
		return favGraphs;
	}

	public void setFavGraphs(List<Graph> favGraphs) {
		this.favGraphs = favGraphs;
	}

	public Integer getInfoAlerts() {
		return infoAlerts;
	}

	public void setInfoAlerts(Integer infoAlerts) {
		this.infoAlerts = infoAlerts;
	}

	public Integer getWarnAlerts() {
		return warnAlerts;
	}

	public void setWarnAlerts(Integer warnAlerts) {
		this.warnAlerts = warnAlerts;
	}

	public Integer getCriticalAlerts() {
		return criticalAlerts;
	}

	public void setCriticalAlerts(Integer criticalAlerts) {
		this.criticalAlerts = criticalAlerts;
	}
}
