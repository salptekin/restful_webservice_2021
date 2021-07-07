package restfulwebservice03;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SF03StudentBeanService {
	
	private SF03StudentBeanRepository studentRepo;

	@Autowired
	public SF03StudentBeanService(SF03StudentBeanRepository studentRepo) {
		this.studentRepo = studentRepo;
	}
	
	public List<SF03StudentBean> listStudents(){
		return studentRepo.findAll();
	}
	
	public SF03StudentBean selectStdById(Long id) {		
		if(studentRepo.findById(id).isPresent()) {
			return studentRepo.findById(id).get();			
		}else{
			return new SF03StudentBean();	
		}
	}
	//For invalid ids, the method will throw IllegalStateException with "id does not exist" message
	//For valid ids, student with the id will be removed from the database and you will get a success
	//message on the console like "Student whose id is 'id' is successfully deleted"
	public String deleteStdById(Long id) {
		if(!studentRepo.existsById(id)) {
			throw new IllegalStateException(id + " does not exist");
		}
			studentRepo.deleteById(id);
			return "Student whose id is " + id + " is successfully deleted";	
	}
	
	//The method is for fully update
	public SF03StudentBean updateStudent(Long id, SF03StudentBean newStudent) {
		
		SF03StudentBean existingStudentById = studentRepo
												.findById(id)
												.orElseThrow(()->new IllegalStateException(id + " id does not exist.."));
		
		String name = existingStudentById.getName();		
		if(newStudent.getName()==null) {			
			existingStudentById.setName(null);			
		}else if(existingStudentById.getName()==null) {			
			existingStudentById.setName(newStudent.getName());			
		}else if(!name.equals(newStudent.getName())) {			
			existingStudentById.setName(newStudent.getName());			
		}
		
		Optional<SF03StudentBean> existingStudentByEmail = studentRepo.findSF03StudentBeanByEmail(newStudent.getEmail());
		
		if(existingStudentByEmail.isPresent()) {			
			throw new IllegalStateException("Email is taken, cannot be used again...");			
		}else if(newStudent.getEmail()==null) {			
			throw new IllegalArgumentException("Email must be sent...");			
		}else if(!newStudent.getEmail().contains("@")) {			
			throw new IllegalArgumentException("Invalid email id is used, fix it...");			
		}else if(!existingStudentById.getEmail().equals(newStudent.getEmail())){			
			existingStudentById.setEmail(newStudent.getEmail());			
		}
		
		if(Period.between(newStudent.getDob(), LocalDate.now()).isNegative()) {
			throw new IllegalStateException("Date of birth cannot be selected from future...");
		}else if(!existingStudentById.getDob().equals(newStudent.getDob())) {
			existingStudentById.setDob(newStudent.getDob());
		}
		
		return studentRepo.save(newStudent);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
