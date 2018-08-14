package az.ldap.sync.domain.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.naming.Name;

import org.json.JSONObject;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import az.ldap.sync.util.JsonUtil;

@Entry(objectClasses = { "top", "organizationalUnit" })
@JsonIgnoreProperties(ignoreUnknown = true)
public class LdapDomainGroup implements Serializable {

	private static final long serialVersionUID = -4834076760889079134L;

	@Id
	private Name dn;
	
	@Attribute(name = "objectClass", syntax = "1.3.6.1.4.1.1466.115.121.1.38")
    private List<String> objectClass = new ArrayList<String>();
	
	@Attribute(name = "ou", syntax = "1.3.6.1.4.1.1466.115.121.1.15")
    private String ou;

	public Name getDn() {
		return dn;
	}

	public void setDn(Name dn) {
		this.dn = dn;
	}

	public LdapDomainGroup() {

	}
	
	public LdapDomainGroup(Name dn, String street, String description) {
		this.dn = dn;
		objectClass.add("top");
		objectClass.add("organizationalUnit");
	}

	
	/**
     * Convert JSONObject to domain entity.
     *
     * @param jsonObject json object
     * @return domain entity object.
     * @throws Exception unhandled errors.
     */
    public static LdapDomainGroup convert(JSONObject jsonObject) throws Exception {
    	LdapDomainGroup domain = new LdapDomainGroup();
    	List<String> objectClass = new ArrayList<String>();
		objectClass.add("top");
		objectClass.add("organizationalUnit");
		domain.setObjectClass(objectClass);
		domain.setOu(JsonUtil.getStringValue(jsonObject, "name"));
        return domain;
    }   
   

    /**
     * Mapping entity object into list.
     *
     * @param domainList list of domains.
     * @return domain map
     */
    public static Map<Name, LdapDomainGroup> convert(List<LdapDomainGroup> domainList) {
        Map<Name, LdapDomainGroup> domainMap = new HashMap<Name, LdapDomainGroup>();
        for (LdapDomainGroup domain : domainList) {
            domainMap.put(domain.getDn(), domain);
        }
        return domainMap;
    }
	
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	/**
	 * @return the objectClass
	 */
	public List<String> getObjectClass() {
		return objectClass;
	}
	
	/**
	 * @param objectClass the objectClass to set
	 */
	public void setObjectClass(List<String> objectClass) {
		this.objectClass = objectClass;
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
	
}
