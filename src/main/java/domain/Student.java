package domain;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "student")
public class Student extends BaseEntity {

	private String firstName;

	private String lastName;

    @Column(unique = true)	
	private String studentID;
	
	private LocalDate birthDate;
	
    @Lob
    private byte[] profilePic;	

	@OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Exam> exams;
	

	public Student() {
		this.setExams(new ArrayList<>());
	}

	public Student(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.setExams(new ArrayList<>());
	}

	public void addExam(Exam exam) {
		this.exams.add(exam);
		exam.setStudent(this);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime * result + ((exams == null) ? 0 : exams.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + Arrays.hashCode(profilePic);
		result = prime * result + ((studentID == null) ? 0 : studentID.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Student other = (Student) obj;
		if (birthDate == null) {
			if (other.birthDate != null)
				return false;
		} else if (!birthDate.equals(other.birthDate))
			return false;
		if (exams == null) {
			if (other.exams != null)
				return false;
		} else if (!exams.equals(other.exams))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (!Arrays.equals(profilePic, other.profilePic))
			return false;
		if (studentID == null) {
			if (other.studentID != null)
				return false;
		} else if (!studentID.equals(other.studentID))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Student [name=" + firstName + lastName + "]";
	}

	public List<Exam> getExams() {
		return exams;
	}

	public void setExams(List<Exam> exams) {
		this.exams = exams;
	}

	public boolean removeExam(Exam exam) {
		boolean res = exams.remove(exam);
		if (res) {
			exam.setStudent(null);
		}
		return res;
	}

	public int findExam(Exam exam) {
		return exams.stream().filter(e -> e.getCourse().getTitle().equals(exam.getCourse().getTitle()))
				.map(exams::indexOf).findFirst().orElse(-1);
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public byte[] getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(byte[] profilePic) {
		this.profilePic = profilePic;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

}
