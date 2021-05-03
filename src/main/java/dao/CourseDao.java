package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import domain.Course;

public class CourseDao {

	private EntityManagerFactory emf;

	public CourseDao(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public boolean save(Course entity) {
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

	public Course findOne(Long id) {
		EntityManager em = emf.createEntityManager();
		return em.find(Course.class, id);
	}

	public List<Course> findAll() {
		EntityManager em = emf.createEntityManager();
		return em.createQuery("from Course " + " ORDER BY id DESC", Course.class).getResultList();
	}

	public boolean update(Course entity) {
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

	public boolean delete(Course entity) {
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

			Course entity = findOne(entityId);
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

	public Course findByTitle(String titleToFind) {
		EntityManager em = emf.createEntityManager();
		List<Course> result = em.createQuery("from Course " + "where title = :title ", Course.class)
				.setParameter("title", titleToFind).getResultList();
		if (result.isEmpty())
			return null;

		return result.get(0);
	}
}
