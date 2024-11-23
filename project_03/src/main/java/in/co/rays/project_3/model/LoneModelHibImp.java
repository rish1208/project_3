package in.co.rays.project_3.model;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import in.co.rays.project_3.dto.LoneDTO;
import in.co.rays.project_3.exception.ApplicationException;
import in.co.rays.project_3.exception.DuplicateRecordException;
import in.co.rays.project_3.util.HibDataSource;

public class LoneModelHibImp implements LoneModelInt {

	@Override
	public long add(LoneDTO dto) throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;
//		ProductDTO duplicateCollegeName = fingByName(dto.getProductName());
//		if (duplicateCollegeName != null) {
//			throw new DuplicateRecordException("college name already exist");
//		}
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.save(dto);
			tx.commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();

			}
			throw new ApplicationException("Exception in Lone Add " + e.getMessage());
		} finally {
			session.close();
		}
		return dto.getId();
	}

	@Override
	public void delete(LoneDTO dto)  throws ApplicationException {
		Session session = null;
		Transaction tx = null;
		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			session.delete(dto);
			tx.commit();

		} catch (HibernateException e) {
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Lone Delete" + e.getMessage());
		} finally {
			session.close();
		}

	}
	@Override
	public void update(LoneDTO dto)  throws ApplicationException, DuplicateRecordException {
		Session session = null;
		Transaction tx = null;

		try {
			session = HibDataSource.getSession();
			tx = session.beginTransaction();
			System.out.println("before update");

			session.saveOrUpdate(dto);
			System.out.println("after update");
			tx.commit();

		} catch (HibernateException e) {
			e.printStackTrace();
			if (tx != null) {
				tx.rollback();
			}
			throw new ApplicationException("Exception in Lone update" + e.getMessage());
		} finally {
			session.close();
		}

	}
	@Override
	public List list() throws ApplicationException {
		return list(0, 0);
	}

	@Override
	public List list(int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(LoneDTO.class);
			if (pageSize > 0) {
				pageNo = ((pageNo - 1) * pageSize) + 1;
				criteria.setFirstResult(pageNo);
				criteria.setMaxResults(pageSize);
			}
			list = criteria.list();

		} catch (HibernateException e) {

			throw new ApplicationException("Exception : Exception in  College list");
		} finally {
			session.close();
		}

		return list;
	}


	@Override
	public List search(LoneDTO dto) throws ApplicationException {
	
		return search(dto, 0, 0);
	}

	@Override
	public List search(LoneDTO dto, int pageNo, int pageSize) throws ApplicationException {
		Session session = null;
		List list = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(LoneDTO.class);
			if (dto.getId() != null && dto.getId() > 0) {
				criteria.add(Restrictions.eq("id", dto.getId()));

			}
			if (dto.getLoneStartDate() != null && dto.getLoneStartDate().getDate() > 0) {
				criteria.add(Restrictions.eq("loneStartDate", dto.getLoneStartDate()));
			}
			if (dto.getCustomerId() != null && dto.getCustomerId().length() > 0) {
				criteria.add(Restrictions.like("customerId", dto.getCreatedDatetime() + "%"));
			}
			if (dto.getLoneAmount() != null && dto.getLoneAmount()> 0) {
				criteria.add(Restrictions.eq("loneAmount", dto.getLoneAmount()));
			}
			if (dto.getInterestRate() != null && dto.getInterestRate()> 0) {
				criteria.add(Restrictions.eq("interestRate", dto.getInterestRate()));
			}
			if (dto.getMobile() != null && dto.getMobile().length() > 0) {
				criteria.add(Restrictions.like("mobile", dto.getMobile() + "%"));
			}
			if (pageSize > 0) {
				criteria.setFirstResult((pageNo - 1) * pageSize);
				criteria.setMaxResults(pageSize);

			}
			list = criteria.list();
		} catch (HibernateException e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in Lone search");
		} finally {
			session.close();
		}
		return list;
	}

	@Override
	public LoneDTO findByPK(long pk) throws ApplicationException {
		System.out.println("======" + pk + "----------------------------------");
		Session session = null;
		LoneDTO dto = null;
		try {
			session = HibDataSource.getSession();

			dto = (LoneDTO) session.get(LoneDTO.class, pk);
			System.out.println(dto);
		} catch (HibernateException e) {

			throw new ApplicationException("Exception : Exception in getting course by pk");
		} finally {
			session.close();
		}
		System.out.println("++++" + dto);
		return dto;
	}

	@Override
	public LoneDTO fingByName(String name) throws ApplicationException {
		Session session = null;
		LoneDTO dto = null;
		try {
			session = HibDataSource.getSession();
			Criteria criteria = session.createCriteria(LoneDTO.class);
			criteria.add(Restrictions.eq("name", name));
			List list = criteria.list();
			if (list.size() == 1) {
				dto = (LoneDTO) list.get(0);
			}
		} catch (HibernateException e) {

			throw new ApplicationException("Exception in getting User by Login " + e.getMessage());

		} finally {
			session.close();
		}
		return dto;
	}

	

	
}