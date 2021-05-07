package app;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dao.CourseDao;
import dao.ExamDao;
import dao.StudentDao;
import domain.Course;
import domain.Exam;
import domain.Student;

public class ormExample {

	private static final Logger LOGGER = LogManager.getLogger();

	public static void main(String args[]) {

		EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("MYSQL");


// 		Uncomment to populate your DB
//		populateCoursesDB(entityManagerFactory, 100);
//		populateStudDB(entityManagerFactory, 500, 18);

//		Uncomment to verify if if there is the N+1 queries problem
//		fetchExam(entityManagerFactory);
		
		
//		Uncomment to measure find all exams with entities
//		measureFindAllExams(entityManagerFactory);

//		Uncomment to measure find all exams with a class-defined DTO
//		measureFindAllExamsDTO(entityManagerFactory);
		
//		Uncomment to measure find all exams with a Tuple-defined DTO
//		measureFindAllExamsDTOTuple(entityManagerFactory);
		
//		Uncomment this to measure a massive write operation with JDBC batching disable
//		LOGGER.info("Bulk insert with no batch prosessing");
//		testBatchInsert(entityManagerFactory);
		
//		Uncomment this to measure a massive write operation with JDBC batching enable
//		EntityManagerFactory BatchedEntityManagerFactory = Persistence.createEntityManagerFactory("MYSQL-batched");
//		LOGGER.info("Bulk insert with batch prosessing");
//		testBatchInsert(BatchedEntityManagerFactory);

		
		


	}

	public static void testBatchInsert(EntityManagerFactory entityManagerFactory) {
		StudentDao studentDao = new StudentDao(entityManagerFactory);

		List<Student> newStudents;

		int iterations = 20;
//		int iterations = 1;
		long startRetrieve;
		long endRetrieve;
		long timeRetrieve = 0;
		for (int i = 0; i < iterations; i++) {

			newStudents = generateListOfStudents(10000);
			startRetrieve = System.nanoTime();

			// method execution
			studentDao.persistStudents(newStudents);
//			studentDao.persistStudentsBatch(newStudents);

			endRetrieve = System.nanoTime();

			if (i != 0)
				timeRetrieve += endRetrieve - startRetrieve;

			LOGGER.info("Partial Students insertion  took {} millis",
					TimeUnit.NANOSECONDS.toMillis(endRetrieve - startRetrieve));

			// cleaning operation
			deleteListOfStudents(newStudents, entityManagerFactory);
		}

		LOGGER.info("students insertion  took {} millis",
				TimeUnit.NANOSECONDS.toMillis(timeRetrieve / (long) iterations));
	}

	public static void deleteListOfStudents(List<Student> students, EntityManagerFactory entityManagerFactory) {
		StudentDao studentDao = new StudentDao(entityManagerFactory);
		for (Student student : students) {
			studentDao.delete(student);
		}
	}

	public static List<Student> generateListOfStudents(int numOfStudents) {
		List<Student> newStudents = new ArrayList<Student>();

		Random random = new Random();
		Student student;
		for (int i = 0; i < numOfStudents; i++) {

			student = new Student();
			student.setFirstName("NewStudentFirstName" + i);
			student.setLastName("NewStudentLastName" + i);
			student.setStudentID("NEWSTUD" + i);

			int month = 1 + random.nextInt(12);
			int day = 1 + random.nextInt(28);
			int year = 1990 + random.nextInt(15);

			LocalDate birthDay = LocalDate.of(year, month, day);

			student.setBirthDate(birthDay);

			byte[] profilePic = new byte[600];
			Arrays.fill(profilePic, (byte) 0);
//			student.setProfilePic(profilePic);

			newStudents.add(student);
		}

		return newStudents;
	}

	public static void fetchAllExamDTO(EntityManagerFactory em) {
		LOGGER.info("Trying to retrieve all ExamInfo");
		ExamDao examDao = new ExamDao(em);

		examDao.findAllReadOnly();
	}

	public static void fetchExam(EntityManagerFactory em) {
		LOGGER.info("N+1 Queries Problem verification on Exam entities");
		ExamDao examDao = new ExamDao(em);

		examDao.findAll();
	}

	public static void measureFindAllExams(EntityManagerFactory entityManagerFactory) {

		LOGGER.info("All Exams Entities Retrieval Test");
		ExamDao examDao = new ExamDao(entityManagerFactory);
		List<Exam> exams;
		int iterations = 1000;
		long startRetrieve;
		long endRetrieve;
		long timeRetrieve = 0;
		for (int i = 0; i < iterations; i++) {
			startRetrieve = System.nanoTime();

			// method execution
			exams = examDao.findAll();

			endRetrieve = System.nanoTime();
			timeRetrieve += endRetrieve - startRetrieve;
		}

		LOGGER.info("All Exams Retrieval  took {} millis",
				TimeUnit.NANOSECONDS.toMillis(timeRetrieve / (long) iterations));
	}

	public static void measureFindAllExamsDTO(EntityManagerFactory entityManagerFactory) {

		LOGGER.info("All Exams DTO Retrieval Test");
		ExamDao examDao = new ExamDao(entityManagerFactory);
		List<Exam> exams;
		int iterations = 1000;
		long startRetrieve;
		long endRetrieve;
		long timeRetrieve = 0;
		for (int i = 0; i < iterations; i++) {
			startRetrieve = System.nanoTime();

			// method execution
			examDao.findAllReadOnly();

			endRetrieve = System.nanoTime();
			timeRetrieve += endRetrieve - startRetrieve;
		}

		LOGGER.info("All Exams Retrieval  took {} millis",
				TimeUnit.NANOSECONDS.toMillis(timeRetrieve / (long) iterations));
	}

	public static void measureFindAllExamsDTOTuple(EntityManagerFactory entityManagerFactory) {

		LOGGER.info("All Exams Tuple-DTO Retrieval Test");
		ExamDao examDao = new ExamDao(entityManagerFactory);
		List<Exam> exams;
		int iterations = 1000;
		long startRetrieve;
		long endRetrieve;
		long timeRetrieve = 0;
		for (int i = 0; i < iterations; i++) {
			startRetrieve = System.nanoTime();

			// method execution
			examDao.findAllReadOnlyTuple();

			endRetrieve = System.nanoTime();
			timeRetrieve += endRetrieve - startRetrieve;
		}

		LOGGER.info("All Exams Retrieval  took {} millis",
				TimeUnit.NANOSECONDS.toMillis(timeRetrieve / (long) iterations));
	}
	

	public static void populateCoursesDB(EntityManagerFactory entityManagerFactory, int numOfCourses) {
		CourseDao courseDao = new CourseDao(entityManagerFactory);
		Course course;
		for (int i = 0; i < numOfCourses; i++) {
			course = new Course();
			course.setTitle("course " + i);
			course.setAcademicYear("2020/2021");
			course.setCfu(6);
			course.setCourseCode("C" + i);
			course.setSSD("SSD" + i);
			course.setProfessor("Professor" + i);

			boolean success = courseDao.save(course);
			if (success)
				System.out.println("Inserito correttamente corso denominato: " + course.getTitle());
			else
				System.out.println("inserimento non riuscito");

		}
	}

	
	public static void populateStudDB(EntityManagerFactory entityManagerFactory, int numOfStudends, int maxExams) {
		StudentDao studentDao = new StudentDao(entityManagerFactory);
		CourseDao coursedao = new CourseDao(entityManagerFactory);

		List<Course> courses = new ArrayList<Course>();
		courses = coursedao.findAll();

		int totalCourses = courses.size();

		int numOfExams;
		int courseindex;

		Student student;
		Exam exam;

		Random random = new Random();

		for (int i = 0; i < numOfStudends; i++) {
			student = new Student();
			student.setFirstName("FirstName" + i);
			student.setLastName("LastName" + i);
			student.setStudentID("STUD" + i);

			int month = 1 + random.nextInt(12);
			int day = 1 + random.nextInt(28);
			int year = 1990 + random.nextInt(15);

			LocalDate birthDay = LocalDate.of(year, month, day);

			student.setBirthDate(birthDay);

			byte[] profilePic = new byte[600];
			Arrays.fill(profilePic, (byte) 0);
			student.setProfilePic(profilePic);

			numOfExams = random.nextInt(maxExams);

			for (int j = 0; j < numOfExams; j++) {
				courseindex = random.nextInt(totalCourses);
				Course selectedCourse = courses.get(courseindex);
				exam = new Exam();
				exam.setCourse(selectedCourse);
				exam.setGrade(18 + random.nextInt(12));

				month = 1 + random.nextInt(12);
				day = 1 + random.nextInt(28);
				year = 2015 + random.nextInt(6);

				LocalDate examDay = LocalDate.of(year, month, day);
				exam.setDate(examDay);

				student.addExam(exam);
			}

			boolean success = studentDao.save(student);
			if (success)
				System.out.println("Inserito correttamente studente denominato: " + student.getFirstName());
			else
				System.out.println("inserimento non riuscito");
		}

		
		
	}
	


}
