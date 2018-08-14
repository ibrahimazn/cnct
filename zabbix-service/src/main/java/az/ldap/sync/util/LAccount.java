package az.ldap.sync.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Group members
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public final class LAccount {
    private String dn;

    private String name;

    private String description;
    
    private Set<LUser> members = new HashSet<LUser>();
    
    private String ou;

	/**
	 * @return the dn
	 */
	public String getDn() {
		return dn;
	}

	/**
	 * @param dn the dn to set
	 */
	public void setDn(String dn) {
		this.dn = dn;
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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the ou
	 */
	public String getOu() {
		return ou;
	}

	/**
	 * @param ou the ou to set
	 */
	public void setOu(String ou) {
		this.ou = ou;
	}

	public void setMembers(List<LUser> lUsers) {
		Set set = new HashSet(lUsers);
		this.members = set;
	} 
}
