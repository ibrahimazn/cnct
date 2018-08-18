package com.assistanz.slmlp.container.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.assistanz.slmlp.container.entity.Sampling;
import com.assistanz.slmlp.container.repository.SamplingRepository;
import com.assistanz.slmlp.container.util.sorting.PagingAndSorting;

@Service
public class SamplingServiceImpl implements SamplingService {

	/** Sampling Repository reference. */
	@Autowired
	private SamplingRepository samplingRepo;

  /* (non-Javadoc)
   * @see com.assistanz.slmlp.container.util.service.CRUDService#save(java.lang.Object)
   */
  @Override
  public Sampling save(Sampling sampling) throws Exception {
    return samplingRepo.save(sampling);
  }

  /* (non-Javadoc)
   * @see com.assistanz.slmlp.container.util.service.CRUDService#update(java.lang.Object)
   */
  @Override
  public Sampling update(Sampling sampling) throws Exception {
    return samplingRepo.save(sampling);
  }

  /* (non-Javadoc)
   * @see com.assistanz.slmlp.container.util.service.CRUDService#delete(java.lang.Object)
   */
  @Override
  public void delete(Sampling sampling) throws Exception {
    // TODO Auto-generated method stub
    samplingRepo.delete(sampling);
  }

  /* (non-Javadoc)
   * @see com.assistanz.slmlp.container.util.service.CRUDService#delete(java.lang.Long)
   */
  @Override
  public void delete(Long id) throws Exception {
    // TODO Auto-generated method stub
    samplingRepo.delete(id);
  }

  /* (non-Javadoc)
   * @see com.assistanz.slmlp.container.util.service.CRUDService#find(java.lang.Long)
   */
  @Override
  public Sampling find(Long id) throws Exception {
    // TODO Auto-generated method stub
    return samplingRepo.findOne(id);
  }

  /* (non-Javadoc)
   * @see com.assistanz.slmlp.container.util.service.CRUDService#findAll(com.assistanz.slmlp.container.util.sorting.PagingAndSorting)
   */
  @Override
  public Page<Sampling> findAll(PagingAndSorting pagingAndSorting) throws Exception {
    // TODO Auto-generated method stub
    return samplingRepo.findAll(pagingAndSorting.toPageRequest());
  }

  /* (non-Javadoc)
   * @see com.assistanz.slmlp.container.util.service.CRUDService#findAll()
   */
  @Override
  public List<Sampling> findAll() throws Exception {
    // TODO Auto-generated method stub
    return (List<Sampling>) samplingRepo.findAll();
  }
	
}