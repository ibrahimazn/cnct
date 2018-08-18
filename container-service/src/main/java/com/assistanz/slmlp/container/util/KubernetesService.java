package com.assistanz.slmlp.container.util;

import org.springframework.stereotype.Service;

@Service
public class KubernetesService {

	public String convertNameToK8sFromat(String name) {
		if(name != null) {
			name = name.replaceAll("[^A-Za-z0-9]+", "-").toLowerCase();
		}
		return name;
	}
}
