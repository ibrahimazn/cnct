package az.ldap.sync.domain.entity;

import org.springframework.ldap.odm.annotations.Attribute;


public final class Domain {

	@Attribute(name = "ou")
    private String ou;

	public String getOu() {
		return ou;
	}

	public void setOu(String ou) {
		this.ou = ou;
	}
}
