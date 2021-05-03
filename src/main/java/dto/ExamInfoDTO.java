package dto;

public class ExamInfoDTO {
	
	private String courseName;
	
	private int grade;

	
	public ExamInfoDTO(String courseName, int grade) {
		this.courseName = courseName;
		this.grade = grade;
	}

	public int getGrade() {
		return grade;
	}

	public String getCourseName() {
		return courseName;
	}

}
