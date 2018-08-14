package az.ldap.zabbix.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import az.ldap.zabbix.entity.HostTemplate;
import az.ldap.zabbix.repository.HostTemplateRepository;

@Service
public class HostTemplateServiceImpl implements HostTemplateService {

	/** Host template repository reference. */
	@Autowired
	private HostTemplateRepository hostTemplateRepo;

	@Override
	public HostTemplate save(HostTemplate hostTemplate) throws Exception {
		return hostTemplateRepo.save(hostTemplate);
	}

	@Override
	public HostTemplate update(HostTemplate hostTemplate) throws Exception {
		return hostTemplateRepo.save(hostTemplate);
	}

	@Override
	public void delete(HostTemplate hostTemplate) throws Exception {
		hostTemplateRepo.delete(hostTemplate);
	}

	@Override
	public void delete(String id) throws Exception {
		hostTemplateRepo.delete(id);
	}

	@Override
	public HostTemplate find(String id) throws Exception {
		return hostTemplateRepo.findOne(id);
	}

	@Override
	public List<HostTemplate> findAll() throws Exception {
		return hostTemplateRepo.findAll();
	}

	@Override
	public Page<HostTemplate> findAll(PagingAndSorting pagingAndSorting) throws Exception {
		return hostTemplateRepo.findAll(pagingAndSorting.toPageRequest());
	}

}
