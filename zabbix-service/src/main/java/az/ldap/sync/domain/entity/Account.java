package az.ldap.sync.domain.entity;

import java.util.HashSet;
import java.util.Set;

import javax.naming.Name;

import com.google.gson.annotations.SerializedName;

public class Account {
	
	@SerializedName("name")
	private String name;

	@SerializedName("description")
	private String description;

	@SerializedName("ou")
	private String ou;
	
	@SerializedName("member")
	private Set<String> members = new HashSet<String>();

	public Set<String> getMembers() {
		return members;
	}

	public void setMembers(Set<String> members) {
		this.members = members;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOu() {
		return ou;
	}

	public void setOu(String ou) {
		this.ou = ou;
	}
}
