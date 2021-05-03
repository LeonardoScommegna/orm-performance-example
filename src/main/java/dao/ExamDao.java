package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import domain.Exam;
import dto.ExamInfoDTO;

public class ExamDao {
	private EntityManagerFactory emf;

	public ExamDao(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public boolean save(Exam entity) {
		boolean success = false;

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();

			em.persist(entity);
			success = true;

			tx.commit();
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		} finally {
			em.close();
		}
		return success;
	}

	public Exam findOne(Long id) {
		EntityManager em = emf.createEntityManager();
		return em.find(Exam.class, id);
	}

	public List<Exam> findAll() {
		EntityManager em = emf.createEntityManager();
		return em.createQuery("select e from Exam e join fetch e.course", Exam.class).getResultList();
//		return em.createQuery("from Exam " + " ORDER BY id DESC", Exam.class).getResultList();
	}
	
	
	public List<ExamInfoDTO> findAllReadOnly() {
		EntityManager em = emf.createEntityManager();
		return em.createQuery("select "
				+ "new dto.ExamInfoDTO(c.title, e.grade) "
//				+ "new it.unifi.ing.stlab.dto.ExamInfoDTO(c.title, e.grade) "
				+ " from Exam e left join e.course c", ExamInfoDTO.class).getResultList();
//		return em.createQuery("from Exam " + " ORDER BY id DESC", Exam.class).getResultList();
	}

	public boolean update(Exam entity) {
		boolean success = false;

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();

			em.merge(entity);
			success = true;

			tx.commit();
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		} finally {
			em.close();
		}
		return success;
	}

	public boolean delete(Exam entity) {
		boolean success = false;

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();

			em.remove(em.contains(entity) ? entity : em.merge(entity));
			success = true;

			tx.commit();
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		} finally {
			em.close();
		}
		return success;
	}

	public boolean deletebyId(Long entityId) {
		boolean success = false;

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();

			Exam entity = findOne(entityId);
			em.remove(em.contains(entity) ? entity : em.merge(entity));
			success = true;

			tx.commit();
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
		} finally {
			em.close();
		}
		return success;
	}
}
