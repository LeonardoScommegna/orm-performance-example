package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.hibernate.Session;

import domain.Exam;
import domain.Student;

public class StudentDao {
	private EntityManagerFactory emf;

	public StudentDao(EntityManagerFactory emf) {
		this.emf = emf;
	}

	public boolean save(Student entity) {
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

	public Student findOne(Long id) {
		EntityManager em = emf.createEntityManager();
		return em.find(Student.class, id);
	}

	public List<Student> findAll() {
		EntityManager em = emf.createEntityManager();
		return em.createQuery("from Student " + " ORDER BY id DESC", Student.class).getResultList();
	}

	public boolean update(Student entity) {
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

	public boolean delete(Student entity) {
		boolean success = false;

		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = null;
		try {
			tx = em.getTransaction();
			tx.begin();

			em.remove(em.contains(entity) ? entity : em.merge(entity));
//			em.remove(entity);
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

			Student entity = findOne(entityId);
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

	public Student findByName(String name) {
		EntityManager em = emf.createEntityManager();
		List<Student> result = em.createQuery("from Student " + "where name = :name ", Student.class)
				.setParameter("name", name).getResultList();

		if (result.isEmpty())
			return null;

		return result.get(0);
	}

	public List<Exam> getAllExams(Long id) {
		EntityManager em = emf.createEntityManager();

		List<Student> result = em.createQuery(
				"select stud " + "from Student stud " + "left join fetch stud.exams " + "where stud.id = :id",
				Student.class).setParameter("id", id).getResultList();

		if (result.isEmpty())
			return null; // sarebbe meglio lanciare un'eccezione

		return result.get(0).getExams();
	}

	public void persistStudents(List<Student> students) {
		EntityManager em = emf.createEntityManager();

		EntityTransaction tx = em.getTransaction();
		em.unwrap(Session.class)
        .setJdbcBatchSize(0);	
		try {
			tx.begin();
			for (int i = 0; i < students.size(); i++) {
//		        if (i > 0 && i % batchSize == 0) {
//		            entityTransaction.commit();
//		            entityTransaction.begin();
//		 
//		            entityManager.clear();
//		        }

				Student student = students.get(i);
				em.persist(student);
			}

			tx.commit();

		} catch (RuntimeException e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}

	public void persistStudentsBatch(List<Student> students) {
		EntityManager em = emf.createEntityManager();
		int batchSize = 25;
//		em.unwrap(Session.class)
//        .setJdbcBatchSize(batchSize);	

		EntityTransaction tx = em.getTransaction();
		try {
			tx.begin();
			for (int i = 0; i < students.size(); i++) {
		        if (i > 0 && i % batchSize == 0) {
		            tx.commit();
		            tx.begin();
		            em.clear();
//		            em.flush();
//		            em.clear();
		        }

				Student student = students.get(i);
				em.persist(student);
			}

			tx.commit();

		} catch (RuntimeException e) {
			if (tx.isActive()) {
				tx.rollback();
			}
			throw e;
		} finally {
			em.close();
		}
	}
}
