package az.ldap.sync.domain.entity;

import org.json.JSONObject;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.DnAttribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import az.ldap.sync.constants.CloudStackConstants;
import az.ldap.sync.util.JsonUtil;

import javax.naming.Name;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Group members
 */
@Entry(objectClasses = {"groupofNames", "top"})
@JsonIgnoreProperties(ignoreUnknown = true)
public final class Group {
    @Id
    private Name dn;

    @Attribute(name = "cn")
    @DnAttribute(value = "cn", index=1)
    private String name;

    @Attribute(name = "description")
    private String description;
    
    @Attribute(name = "member")
    private Set<Name> members = new HashSet<Name>();
    
    @Attribute(name = "ou")
    private String ou;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Name> getMembers() {
        return  members;
    }

    public void addMember(Name newMember) {
        members.add(newMember);
    }

    public void removeMember(Name member) {
        members.remove(member);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

	/**
	 * @param members the members to set
	 */
	public void setMembers(Set<Name> members) {
		this.members = members;
	}
	
	/**
     * Convert JSONObject into user object.
     *
     * @param jsonObject JSON object.
     * @return user object.
     * @throws Exception error occurs.
     */
    public static Group convert(JSONObject jsonObject) throws Exception {
    	Group group = new Group();
    	group.setName(JsonUtil.getStringValue(jsonObject, CloudStackConstants.CS_NAME) + "-"+JsonUtil.getStringValue(jsonObject, CloudStackConstants.CS_DOMAIN));
    	group.setOu(JsonUtil.getStringValue(jsonObject, CloudStackConstants.CS_DOMAIN));
    	return group;
    }
    

    /**
     * Mapping entity object into list.
     *
     * @param userList list of users.
     * @return user map
     */
	public static Map<Name, Group> convert(List<Group> userList) {
		Map<Name, Group> acountMap = new HashMap<Name, Group>();
		for (Group group : userList) {
			acountMap.put(group.getDn(), group);
		}
		return acountMap;
	}

	/**
	 * @return the dn
	 */
	public Name getDn() {
		return dn;
	}

	/**
	 * @param dn the dn to set
	 */
	public void setDn(Name dn) {
		this.dn = dn;
	}
    
}
