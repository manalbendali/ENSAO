package application;

public class Student {
	private int Id;
	private String FirstName;
	private String LastName;
	private int CNE;
	private String Email;

	//definition des constructeurs
	public Student() {
		super();
	}
	
	public Student(String FirstName,String LastName,int CNE,String Email) {
		super();
		this.FirstName=FirstName;
		this.LastName=LastName;
		this.CNE=CNE;
		this.Email=Email;
	}
	
	//definition des getters
	public int getId() {
		return Id;
	}
	public String getFirstName() {
		return FirstName;
	}
	public String getLastName() {
		return LastName;
	}
	public int getCNE() {
		return CNE;
	}
	public String getEmail() {
		return Email;
	}
	
	//definition des setters
	public void setId(int Id) {
		this.Id=Id;
	}
	public void setFirstName(String FirstName) {
		this.FirstName=FirstName;
	}
	public void setLastName(String LastName) {
		this.LastName=LastName;
	}
	public void setCNE(int CNE) {
		this.CNE=CNE;
	}
	public void setEmail(String Email) {
		this.Email=Email;
	}

}
