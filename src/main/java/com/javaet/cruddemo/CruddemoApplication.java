package com.javaet.cruddemo;

import com.javaet.cruddemo.dao.StudentDAO;
import com.javaet.cruddemo.entity.Student;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@SpringBootApplication
public class CruddemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CruddemoApplication.class, args);
	}

	/*Executed after spring beans have been loading*/
	@Bean
	public CommandLineRunner commandLineRunner(StudentDAO studentDAO){
		//Java Lambda Expression Short-Cut Notation for Providing an Implementation of the CommandLineRunner Interface
		return runner -> {
			createStudent(studentDAO);
			//readStudent(studentDAO);
			//queryForStudents(studentDAO);
			//queryForStudentsByLastName(studentDAO);
			//updateStudent(studentDAO);
			//updateLastNameWithLastName(studentDAO);
			//deleteStudent(studentDAO);
			//deleteStudentWithName(studentDAO);
			//deleteAllStudents(studentDAO);

		};
	}
	/*When using CommandLineRunner, Spring will check the arguments to the method. Spring will resolve the method arguments
	  by injecting the appropriate Spring Bean. In this case, there is no need to use @Autowired.
	  Spring will inject the beans automatically behind the scenes.*/

	/*
	  You shouldn't use @Autowired. @PersistenceContext takes care to create a unique EntityManager for every thread.
	  In a production application you can have multiple clients calling your application in the same time.
	  For each call, the application creates a thread.
	  Each thread should use its own EntityManager.
	  Imagine what would happen if they share the same EntityManager: different users would access the same entities.
	  https://stackoverflow.com/questions/31335211/autowired-vs-persistencecontext-for-entitymanager-bean
	  */

	/*
	* The @PersistenceContext annotation in your code is being used to indicate that the EntityManager must be
	* automatically injected, in other words its lifecycle will be managed by the container running your application
	* (which is a good thing). The other option would be having all required configurations provided by you
	* (application managed) via different options, all of them cumbersome (config files or beans) and running the
	* risk of tying your application to some environment-specific configuration (which is a bad thing).
	* https://stackoverflow.com/questions/46114254/spring-boot-persistence-context-annotation#:~:text=The%20%40PersistenceContext%20annotation%20in%20your,which%20is%20a%20good%20thing).*/

	private void createStudent(StudentDAO studentDAO){
		// create the student object
		System.out.println("Creating new student object ...");
		Student tempStudent = new Student("Paul", "Doe", "paul@luv2code.com");
		// save the student object
		System.out.println("Saving the student ...");
		studentDAO.save (tempStudent);
		// display id of the saved student
		System.out.println("Saved student. Generated id: " + tempStudent.getId());
	}

	private void readStudent (StudentDAO studentDAO) {
		//create a student object
		System.out.println("Creating new student object...");
		Student tempStudent = new Student ("Daffy", "Duck", "daffy@luv2code.com");
		//save the student object
		System.out.println("Saving the student...");
		studentDAO.save (tempStudent);
		//display id of the saved student
		System.out.println ("Saved student. Generated id: " + tempStudent.getId());
		//retrieve student based on the id: primary key
		System.out.println ("\nRetrieving student with id: " + tempStudent.getId());
		Student myStudent = studentDAO.findById(tempStudent.getId());
		System.out.println("Found the student: " + myStudent);
	}

	private void queryForStudents (StudentDAO studentDAO) {
		//get a list of students
		List<Student> theStudents = studentDAO.findAll() ;
		//display list of students
		for (Student tempStudent: theStudents) {
			System.out.println (tempStudent);
		}
	}

	private void queryForStudentsByLastName(StudentDAO studentDAO) {
		//get a list of students
		List<Student> theStudents = studentDAO.findByLastName("Doe");
		//display list of students
		for (Student tempStudent: theStudents) {
			System.out.println (tempStudent);
		}
	}

	private void updateStudent(StudentDAO studentDAO) {
		//retrieve student based on the id: primary key
		int studentId = 1;
		System.out.println("Getting student with id: " + studentId);
		Student myStudent = studentDAO. findById(studentId);
		System.out.println("Updating student...");
		//change first name to "Scooby"
		myStudent.setFirstName ("Scooby");
		studentDAO.update(myStudent);
		//display updated student
		System.out.println("Updated student: " + myStudent);
	}

	private void updateLastNameWithLastName(StudentDAO studentDAO){
		int numRowsUpdated = studentDAO.updateLastNameWithLastName("NewNewDoe","NewNewDoe2");

		System.out.println("Updated number of rows: "+numRowsUpdated);
	}

	private void deleteStudent(StudentDAO studentDAO) {
		//delete the student
		int studentId = 1;
		System.out.println("Deleting student id: "+ studentId);
		studentDAO.delete(studentId);
	}

	private void deleteStudentWithName(StudentDAO studentDAO){
		int numRowsUpdated = studentDAO.deleteStudentWithName("Daffy");

		System.out.println("Deleted number of rows: "+numRowsUpdated);
	}

	private void deleteAllStudents(StudentDAO studentDAO) {
		System.out.println("Deleting all students");
		int numRowsDeleted = studentDAO.deleteAll();
		System.out.println("Deleted row count: " + numRowsDeleted);
	}

}
