import org.bson.Document;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class SISApplication {
    private static final String ADMIN_ID = "12345";
    private static final String DATABASE_NAME = "sis_database";

    private Scanner scanner;
    private String loggedInUserId;
    private MongoCollection<Document> studentsCollection;
    private MongoCollection<Document> instructorsCollection;
    private MongoCollection<Document> coursesCollection;
    private MongoCollection<Document> gradesCollection;
    private MongoCollection<Document> departmentsCollection;

    public SISApplication() {
        scanner = new Scanner(System.in);

        // Connect to MongoDB
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        com.mongodb.client.MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);

        studentsCollection = database.getCollection("students");
        instructorsCollection = database.getCollection("instructors");
        coursesCollection = database.getCollection("courses");
        gradesCollection = database.getCollection("grades");
        departmentsCollection = database.getCollection("departments");
    }

    // Student methods
    public void createStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student date of birth: ");
        String dateOfBirth = scanner.nextLine();
        System.out.print("Enter student address: ");
        String address = scanner.nextLine();
        System.out.print("Enter student contact: ");
        String contact = scanner.nextLine();
    
        String studentId = UUID.randomUUID().toString();
        Document studentDoc = new Document("_id", studentId)
                .append("name", name)
                .append("dateOfBirth", dateOfBirth)
                .append("address", address)
                .append("contact", contact)
                .append("courses", new ArrayList<>());

        studentsCollection.insertOne(studentDoc);
        System.out.println("Student created successfully with ID: " + studentId);
    }

    public void readStudent(String studentId) {
        Document studentDoc = studentsCollection.find(new Document("_id", studentId)).first();
        if (studentDoc != null) {
            System.out.println("Student Information:");
            System.out.println("ID: " + studentDoc.getString("_id"));
            System.out.println("Name: " + studentDoc.getString("name"));
            System.out.println("Date of Birth: " + studentDoc.getString("dateOfBirth"));
            System.out.println("Address: " + studentDoc.getString("address"));
            System.out.println("Contact: " + studentDoc.getString("contact"));
            if (studentDoc.containsKey("gpa")) {
                System.out.println("GPA: " + studentDoc.getDouble("gpa"));
            } else {
                System.out.println("GPA: N/A");
            }
        } else {
            System.out.println("Student not found.");
        }
    }
    
public void updateStudent(String studentId) {
    Document studentDoc = studentsCollection.find(new Document("_id", studentId)).first();
    if (studentDoc != null) {
        System.out.println("Enter new student information:");
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Date of Birth: ");
        String dateOfBirth = scanner.nextLine();
        System.out.print("Address: ");
        String address = scanner.nextLine();
        System.out.print("Contact: ");
        String contact = scanner.nextLine();
        
        studentDoc.put("name", name);
        studentDoc.put("dateOfBirth", dateOfBirth);
        studentDoc.put("address", address);
        studentDoc.put("contact", contact);
        
        studentsCollection.replaceOne(new Document("_id", studentId), studentDoc);
        System.out.println("Student information updated successfully.");
    } else {
        System.out.println("Student not found.");
    }
}

    public String findStudentIdByName(String studentName) {
        // Search for the student in the database using their name
        Document query = new Document("name", studentName);
        Document studentDoc = studentsCollection.find(query).first();
        
        if (studentDoc != null) {
            // Retrieve the student's ID
            return studentDoc.getString("_id");
        } else {
            // If student not found, return null
            return null;
        }
    }
    
    public void deleteStudent(String studentName) {
        Document studentDoc = studentsCollection.find(new Document("name", studentName)).first();
        if (studentDoc != null) {
            studentsCollection.deleteOne(new Document("name", studentName));
            System.out.println("Student deleted successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }

    public void displayAllStudents() {
        MongoCursor<Document> cursor = studentsCollection.find().iterator();
        try {
            System.out.println("All Students:");
            while (cursor.hasNext()) {
                Document studentDoc = cursor.next();
                System.out.println("Student ID: " + studentDoc.getString("_id"));
                System.out.println("Name: " + studentDoc.getString("name"));
                System.out.println("Date of Birth: " + studentDoc.getString("dateOfBirth"));
                System.out.println("Address: " + studentDoc.getString("address"));
                System.out.println("Contact: " + studentDoc.getString("contact"));
                System.out.println("--------------------------------------");
            }
        } finally {
            cursor.close();
        }
    }
    // Instructor methods

    public void createInstructor() {
        System.out.print("Enter instructor name: ");
        String name = scanner.nextLine();
        System.out.print("Enter instructor contact: ");
        String contact = scanner.nextLine();
        System.out.print("Enter instructor email Address: ");
        String email = scanner.nextLine();

        String instructorId = UUID.randomUUID().toString();
        Document instructorDoc = new Document("_id", instructorId)
                .append("name", name)
                .append("contact", contact)
                .append( "email Address", email);

        instructorsCollection.insertOne(instructorDoc);
        System.out.println("Instructor created successfully with ID: " + instructorId);
    }

    public void readInstructor(String instructorName) {
        Document instructorDoc = instructorsCollection.find(new Document("name", instructorName)).first();
        if (instructorDoc != null) {
            System.out.println("Instructor Information:");
            System.out.println("ID: " + instructorDoc.getString("_id"));
            System.out.println("Name: " + instructorDoc.getString("name"));
            System.out.println("Contact: " + instructorDoc.getString("contact"));
            System.out.println("email Address: " + instructorDoc.getString("email Address"));
        } else {
            System.out.println("Instructor not found.");
        }
    }
    
    public void updateInstructor(String instructorName) {
        Document instructorDoc = instructorsCollection.find(new Document("name", instructorName)).first();
        if (instructorDoc != null) {
            System.out.println("Enter new instructor information:");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Contact: ");
            String contact = scanner.nextLine();
            System.out.print("email Address: ");
            String email = scanner.nextLine();
    
            instructorDoc.put("name", name);
            instructorDoc.put("contact", contact);
            instructorDoc.put("email Address:", email);
    
            instructorsCollection.replaceOne(new Document("name", instructorName), instructorDoc);
            System.out.println("Instructor information updated successfully.");
        } else {
            System.out.println("Instructor not found.");
        }
    }
    
    public void deleteInstructor(String instructorName) {
        Document instructorDoc = instructorsCollection.find(new Document("name", instructorName)).first();
        if (instructorDoc != null) {
            instructorsCollection.deleteOne(new Document("name", instructorName));
            System.out.println("Instructor deleted successfully.");
        } else {
            System.out.println("Instructor not found.");
        }
    }

    public void displayAllInstructors() {
        MongoCursor<Document> cursor = instructorsCollection.find().iterator();
        try {
            System.out.println("All Instructors:");
            while (cursor.hasNext()) {
                Document instructorDoc = cursor.next();
                System.out.println("Instructor ID: " + instructorDoc.getString("_id"));
                System.out.println("Name: " + instructorDoc.getString("name"));
                System.out.println("Contact: " + instructorDoc.getString("contact"));
                System.out.println("Email Address: " + instructorDoc.getString("email Address"));
                System.out.println("--------------------------------------");
            }
        } finally {
            cursor.close();
        }
    }
    
    // Course methods

    public void createCourse() {
        System.out.print("Enter course name: ");
        String name = scanner.nextLine();
        System.out.print("Enter course code: ");
        String code = scanner.nextLine();
        System.out.print("Enter course credits: ");
        int credits = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter number of hours: ");
        int hours = Integer.parseInt(scanner.nextLine());
    
        // Check if the course with the provided code already exists
        Document existingCourse = coursesCollection.find(new Document("code", code)).first();
        if (existingCourse != null) {
            System.out.println("Course with code " + code + " already exists.");
            return;
        }
    
        String courseId = UUID.randomUUID().toString();
        Document courseDoc = new Document("_id", courseId)
                .append("name", name)
                .append("code", code)
                .append("credits", credits)
                .append("hours", hours)  // Adding number of hours
                .append("registeredStudents", new ArrayList<>())
                .append("numberOfStudents", 0); // Initial value of number of students is 0
    
        coursesCollection.insertOne(courseDoc);
    
        System.out.println("Course created successfully with code: " + code);
    }
    
    public void registerCourse(String studentId, String courseName, String studentName) {
        System.out.println("Attempting to register student for course: " + courseName);
    
        // Check if the course exists
        Document courseDoc = coursesCollection.find(new Document("name", courseName)).first();
        if (courseDoc != null) {
            // Retrieve course information
            String courseId = courseDoc.getString("_id"); // Get the course ID
            int currentNumberOfStudents = courseDoc.getInteger("numberOfStudents", 0); // Get the current number of students
    
            // Update the student document with the course name
            studentsCollection.updateOne(
                new Document("_id", studentId),
                new Document("$addToSet", new Document("courses", courseName)) // Add courseId to the courses array
            );
    
            // Update the course document with the new student
            coursesCollection.updateOne(
                new Document("_id", courseId),
                new Document("$push", new Document("registeredStudents", new Document("studentId", studentId)
                                                                                        .append("studentName", studentName)))
                    .append("$set", new Document("numberOfStudents", currentNumberOfStudents + 1 )) // Increment the number of students
            );
    
            // Print registration information
            System.out.println("Student registered successfully for the course:");
            System.out.println("Course Name: " + courseName);
            System.out.println("Course ID: " + courseId); // Print the course ID
        } else {
            System.out.println("Course not found. Creating a new course.");
    
            // Prompt the user to enter course code, credits, and hours
            System.out.print("Enter course code: ");
            String courseCode = scanner.nextLine();
            System.out.print("Enter course credits: ");
            int courseCredits = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter number of hours: ");
            int hours = Integer.parseInt(scanner.nextLine());
    
            // Create a new course document
            Document newCourse = new Document("_id", UUID.randomUUID().toString())
                                    .append("name", courseName)
                                    .append("code", courseCode)
                                    .append("credits", courseCredits)
                                    .append("hours", hours)
                                    .append("registeredStudents", new ArrayList<>())
                                    .append("numberOfStudents", 0); // Initial value of number of students is 0
    
            // Insert the new course document into the collection
            coursesCollection.insertOne(newCourse);
    
            // Now register the student for the newly created course
            registerCourse(studentId, courseName, studentName);
        }
    }    
    
    private void displayRegisteredCourses(String studentId) {
        Document studentDoc = studentsCollection.find(new Document("_id", studentId)).first();
        if (studentDoc != null) {
            java.util.List<String> courses = studentDoc.getList("courses", String.class);
            if (courses != null && !courses.isEmpty()) {
                System.out.println("Registered Courses for Student " + studentId + ":");
                for (String courseName : courses) {
                    Document courseDoc = coursesCollection.find(new Document("name", courseName)).first();
                    if (courseDoc != null) {
                        System.out.println("Course Name: " + courseDoc.getString("name"));
                        System.out.println("Course Code: " + courseDoc.getString("code"));
                        System.out.println("Course Credits: " + courseDoc.getInteger("credits"));
                        System.out.println("Number of Hours: " + courseDoc.getInteger("hours"));
                        System.out.println("--------------------------------------");
                    } else {
                        System.out.println("Course not found with Name: " + courseName);
                    }
                }
            } else {
                System.out.println("No courses registered for this student.");
            }
        } else {
            System.out.println("Student not found with ID: " + studentId);
        }
    }
    
    public void displayAllCourses() {
        MongoCursor<Document> cursor = coursesCollection.find().iterator();
        try {
            System.out.println("All Courses:");
            while (cursor.hasNext()) {
                Document courseDoc = cursor.next();
                System.out.println("Course ID: " + courseDoc.getString("_id"));
                System.out.println("Name: " + courseDoc.getString("name"));
                System.out.println("Code: " + courseDoc.getString("code"));
                System.out.println("Credits: " + courseDoc.getInteger("credits"));
                System.out.println("Number of Hours: " + courseDoc.getInteger("hours"));
                System.out.println("--------------------------------------");
            }
        } finally {
            cursor.close();
        }
    }
    
    public void readCourse(String courseCode) {
        Document courseDoc = coursesCollection.find(new Document("code", courseCode)).first();
        if (courseDoc != null) {
            System.out.println("Course Information:");
            System.out.println("ID: " + courseDoc.getString("_id"));
            System.out.println("Name: " + courseDoc.getString("name"));
            System.out.println("Code: " + courseDoc.getString("code"));
            System.out.println("Credits: " + courseDoc.getInteger("credits"));
            System.out.println("Number of Hours: " + courseDoc.getInteger("hours"));
        } else {
            System.out.println("Course not found.");
        }
    }
    
    public void updateCourse(String courseCode) {
        Document courseDoc = coursesCollection.find(new Document("code", courseCode)).first();
        if (courseDoc != null) {
            System.out.println("Enter new course information:");
            System.out.print("Name: ");
            String name = scanner.nextLine();
            System.out.print("Code: ");
            String newCode = scanner.nextLine();
            System.out.print("Credits: ");
            int credits = Integer.parseInt(scanner.nextLine());
            System.out.print("Enter number of hours: ");
            int hours = Integer.parseInt(scanner.nextLine());
    
            courseDoc.put("name", name);
            courseDoc.put("code", newCode);
            courseDoc.put("credits", credits);
            courseDoc.put("hours", hours);
    
            coursesCollection.replaceOne(new Document("code", courseCode), courseDoc);
            System.out.println("Course information updated successfully.");
        } else {
            System.out.println("Course not found.");
        }
    }    
    
    public void deleteCourse(String courseCode) {
        Document courseDoc = coursesCollection.find(new Document("code", courseCode)).first();
        if (courseDoc != null) {
            coursesCollection.deleteOne(new Document("code", courseCode));
            System.out.println("Course deleted successfully.");
        } else {
            System.out.println("Course not found.");
        }
    }
    
    public void withdrawCourse(String studentName, String courseName) {
        // Find the student document by name
        Document studentDoc = studentsCollection.find(new Document("name", studentName)).first();
        if (studentDoc != null) {
            String studentId = studentDoc.getString("_id");
            // Check if the student is registered for the course
            java.util.List<String> courses = studentDoc.getList("courses", String.class);
            if (courses != null && courses.contains(courseName)) {
                // Remove the course from the student's list of registered courses
                studentsCollection.updateOne(
                    new Document("_id", studentId),
                    new Document("$pull", new Document("courses", courseName))
                );
                
                // Find the course document
                Document courseDoc = coursesCollection.find(new Document("name", courseName)).first();
                if (courseDoc != null) {
                    // Remove the student's information from the course's list of registered students
                    coursesCollection.updateOne(
                        new Document("_id", courseDoc.getString("_id")),
                        new Document("$pull", new Document("registeredStudents", new Document("studentId", studentId)))
                    );
                    
                    System.out.println("Course with name " + courseName + " withdrawn successfully from student: " + studentName);
                } else {
                    System.out.println("Course with name " + courseName + " not found.");
                }
            } else {
                System.out.println("Student with name " + studentName + " is not registered for course: " + courseName);
            }
        } else {
            System.out.println("Student not found with name: " + studentName);
        }
    }

    // Grade methods

    public void createGrade(String studentName,String studentId, String courseId, String courseName, double grade) {
        String gradeId = UUID.randomUUID().toString();
        Document gradeDoc = new Document("_id", gradeId)
                .append("Student Name: ", studentName)
                .append("studentId", studentId)
                .append("courseId", courseId)
                .append("courseName", courseName) // Include the course name
                .append("grade", grade);
    
        gradesCollection.insertOne(gradeDoc);
        System.out.println("Grade assigned successfully with ID: " + gradeId);
    }
 
    public void readGrade(String studentId) {
        Document studentDoc = studentsCollection.find(new Document("_id", studentId)).first();
        if (studentDoc != null) {
            String studentName = studentDoc.getString("name");
            java.util.List<Document> gradeDocs = gradesCollection.find(new Document("studentId", studentId)).into(new ArrayList<>());
            if (!gradeDocs.isEmpty()) {
                System.out.println("Grades for Student: " + studentName);
                java.util.List<Document> coursesWithGrades = new ArrayList<>();
                for (Document gradeDoc : gradeDocs) {
                    String courseName = gradeDoc.getString("courseName");
                    Double grade = gradeDoc.getDouble("grade");
                    Document courseWithGrade = new Document("courseName", courseName)
                                                    .append("grade", grade);
                    coursesWithGrades.add(courseWithGrade);
                }
                System.out.println("Student Name: " + studentName);
                System.out.println("Courses and Grades:");
                for (Document courseGrade : coursesWithGrades) {
                    System.out.println("Course: " + courseGrade.getString("courseName") + ", Grade: " + courseGrade.getDouble("grade"));
                }
            } else {
                System.out.println("No grades found for Student: " + studentName);
            }
        } else {
            System.out.println("Student not found.");
        }
    }

    public void updateGrade(String gradeId, double grade) {
        Document gradeDoc = gradesCollection.find(new Document("_id", gradeId)).first();
        if (gradeDoc != null) {
            gradeDoc.put("grade", grade);

            gradesCollection.replaceOne(new Document("_id", gradeId), gradeDoc);
            System.out.println("Grade updated successfully.");
        } else {
            System.out.println("Grade not found.");
        }
    }


    public void deleteGrade(String gradeId) {
        Document gradeDoc = gradesCollection.find(new Document("_id", gradeId)).first();
        if (gradeDoc != null) {
            gradesCollection.deleteOne(new Document("_id", gradeId));
            System.out.println("Grade deleted successfully.");
        } else {
            System.out.println("Grade not found.");
        }
    }
    private void calculateAndDisplayGPA(String studentId) {
        Document studentDoc = studentsCollection.find(new Document("_id", studentId)).first();
        if (studentDoc != null) {
            java.util.List<String> courses = studentDoc.getList("courses", String.class);
            if (courses != null && !courses.isEmpty()) {
                double totalGradePoints = 0.0;
                int totalHours = 0;
                for (String courseName : courses) {
                    // Find the course document by course name
                    Document courseDoc = coursesCollection.find(new Document("name", courseName)).first();
                    if (courseDoc != null) {
                        int hours = courseDoc.getInteger("hours", 0);
                        totalHours += hours;
                        // Find the grade for the course
                        Document gradeDoc = gradesCollection.find(new Document("studentId", studentId)
                                                                     .append("courseName", courseName)).first();
                        if (gradeDoc != null) {
                            // Retrieve the grade directly as Double
                            Double grade = gradeDoc.getDouble("grade");
                            if (grade != null) {
                                // Assign grade points based on the provided grading scale
                                double gradePoints = calculateGradePoints(grade);
                                totalGradePoints += (gradePoints * hours);
                            } else {
                                System.out.println("Invalid grade format for student " + studentId);
                            }
                        } else {
                            System.out.println("No grade found for course " + courseName);
                        }
                    } else {
                        System.out.println("Course not found with name " + courseName);
                    }
                }
                if (totalHours > 0) {
                    double gpa = totalGradePoints / totalHours;
                    System.out.println("GPA for Student ID " + studentId + ": " + gpa);
                } else {
                    System.out.println("No courses with grades found for the student.");
                }
            } else {
                System.out.println("No courses registered for this student.");
            }
        } else {
            System.out.println("Student not found with ID: " + studentId);
        }
    }
    
    // Method to calculate grade points based on the provided grading scale
    private double calculateGradePoints(double grade) {
        if (grade >= 4.0) {
            return 4.0; // A+
        } else if (grade >= 3.7) {
            return 3.7; // A
        } else if (grade >= 3.3) {
            return 3.3; // B+
        } else if (grade >= 3.0) {
            return 3.0; // B
        } else if (grade >= 2.7) {
            return 2.7; // B-
        } else if (grade >= 2.3) {
            return 2.3; // C+
        } else if (grade >= 2.0) {
            return 2.0; // C
        } else if (grade >= 1.7) {
            return 1.7; // C-
        } else if (grade >= 1.0) {
            return 1.0; // D
        } else {
            return 0.0; // F or any other grade below D
        }
    }

    // Department methods

    public void createDepartment() {
        System.out.print("Enter department name: ");
        String name = scanner.nextLine();
    
        String departmentId = UUID.randomUUID().toString();
        Document departmentDoc = new Document("_id", departmentId)
                .append("name", name);
    
        departmentsCollection.insertOne(departmentDoc);
        System.out.println("Department created successfully with ID: " + departmentId);
    
        // Ask the admin to add courses to the department
        System.out.println("Add courses to the department:");
        boolean addMoreCourses = true;
        java.util.List<String> courses = new ArrayList<>();
        while (addMoreCourses) {
            System.out.print("Enter course name: ");
            String courseName = scanner.nextLine();
            courses.add(courseName);
            System.out.print("Do you want to add more courses? (yes/no): ");
            String choice = scanner.nextLine();
            if (!choice.equalsIgnoreCase("yes")) {
                addMoreCourses = false;
            }
        }
    
        // Update the department document to include the list of courses
        Document updateQuery = new Document("_id", departmentId);
        Document updateCommand = new Document("$set", new Document("courses", courses));
        departmentsCollection.updateOne(updateQuery, updateCommand);
    
        System.out.println("Courses added to the department successfully.");
    }
    
    public void readDepartment(String departmentName) {
        Document departmentDoc = departmentsCollection.find(new Document("name", departmentName)).first();
        if (departmentDoc != null) {
            System.out.println("Department Information:");
            System.out.println("ID: " + departmentDoc.getString("_id"));
            System.out.println("Name: " + departmentDoc.getString("name"));
        } else {
            System.out.println("Department not found.");
        }
    }
    
    public void updateDepartment(String departmentName) {
        Document departmentDoc = departmentsCollection.find(new Document("name", departmentName)).first();
        if (departmentDoc != null) {
            System.out.println("Enter new department information:");
            System.out.print("Name: ");
            String name = scanner.nextLine();
    
            // Prompt the admin to update courses if needed
            System.out.print("Do you want to update courses for this department? (yes/no): ");
            String choice = scanner.nextLine();
            java.util.List<String> courses = new ArrayList<>();
            if (choice.equalsIgnoreCase("yes")) {
                System.out.println("Enter new courses for the department:");
                boolean addMoreCourses = true;
                while (addMoreCourses) {
                    System.out.print("Enter course name: ");
                    String courseName = scanner.nextLine();
                    courses.add(courseName);
                    System.out.print("Do you want to add more courses? (yes/no): ");
                    choice = scanner.nextLine();
                    if (!choice.equalsIgnoreCase("yes")) {
                        addMoreCourses = false;
                    }
                }
            } else {
                // If admin chooses not to update courses, retain the existing courses
                courses = departmentDoc.getList("courses", String.class);
            }
    
            // Update the department document
            departmentDoc.put("name", name);
            departmentDoc.put("courses", courses);
    
            departmentsCollection.replaceOne(new Document("name", departmentName), departmentDoc);
            System.out.println("Department information updated successfully.");
        } else {
            System.out.println("Department not found.");
        }
    }
    
    public void deleteDepartment(String departmentName) {
        Document departmentDoc = departmentsCollection.find(new Document("name", departmentName)).first();
        if (departmentDoc != null) {
            departmentsCollection.deleteOne(new Document("name", departmentName));
            System.out.println("Department deleted successfully.");
        } else {
            System.out.println("Department not found.");
        }
    }
    
    // LOGIN methods

    public boolean studentLogin() {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();
        
        // Query the students collection in MongoDB to check if the student exists
        Document studentDoc = studentsCollection.find(new Document("_id", studentId)).first();
        
        if (studentDoc != null) {
            loggedInUserId = studentId;
            return true;
        } else {
            return false;
        }
    }
    

    public boolean instructorLogin() {
        System.out.print("Enter instructor ID: ");
        String instructorId = scanner.nextLine();
        
        // Query the instructors collection in MongoDB to check if the instructor exists
        Document instructorDoc = instructorsCollection.find(new Document("_id", instructorId)).first();
        
        if (instructorDoc != null) {
            loggedInUserId = instructorId;
            return true;
        } else {
            return false;
        }
    }
    

    public boolean adminLogin() {
        System.out.print("Enter admin ID: ");
        String adminId = scanner.nextLine();
        if (adminId.equals(ADMIN_ID)) {
            loggedInUserId = adminId;
            return true;
        } else {
            return false;
        }
    }

    public void logout() {
        loggedInUserId = null;
        System.out.println("Logged out successfully.");
    }

    public void studentOptions() {
        boolean loggedInExit = false;
        while (!loggedInExit) {
            System.out.println("Student Options:");
            System.out.println("1. Display Student Information");
            System.out.println("2. Display all the Registered Courses ");
            System.out.println("3. View Grades");
            System.out.println("4. view GPA");
            System.out.println("5. Logout");
            System.out.print("Enter option: ");
            int loggedInOption;
            try {
                loggedInOption = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please enter a number.");
                continue;
            }
    
            switch (loggedInOption) {
                case 1:
                    readStudent(loggedInUserId);
                    break;
                case 2:
                    // Display registered courses
                    displayRegisteredCourses(loggedInUserId);
                    break;
                case 3:
                    // View grades
                    readGrade(loggedInUserId);
                    break;
                case 4:
                    // Calculate GPA
                    calculateAndDisplayGPA(loggedInUserId);
                    break;
                case 5:
                    logout();
                    loggedInExit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

   public void instructorOptions() {
    boolean loggedInExit = false;
    while (!loggedInExit) {
        System.out.println("Instructor Options:");
        System.out.println("1. Edit Student Information");
        System.out.println("2. retreive Student ID");
        System.out.println("3. Show all the Courses");
        System.out.println("4. Update Course Information");
        System.out.println("5. Assign Grades to Students");
        System.out.println("6. View All Students"); 
        System.out.println("7. Logout");
        System.out.print("Enter option: ");
        int loggedInOption;
        try {
            loggedInOption = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid option. Please enter a number.");
            continue;
        }

        switch (loggedInOption) {
            case 1:
                // Edit Student Information
                System.out.print("Enter student Name to edit info: ");
                String studentname = scanner.nextLine();
                updateStudent(studentname);
                break;

            case 2: 
                 // Search for student by name
                System.out.print("Enter student name: ");
                String studentName = scanner.nextLine();
                String studentId = findStudentIdByName(studentName);
                if (studentId != null) {
                // Student found, display their ID
                System.out.println("Student ID: " + studentId);
                 } else {
                 // Student not found
                 System.out.println("Student not found.");
                 }
                break;


            case 3:
                displayAllCourses();
                break;
                
            case 4:
                // Update Course Information
                System.out.println("Enter course Code you want to edit:");
                String courseId = scanner.nextLine();
                updateCourse(courseId);
                break;
                
            case 5:
                // Assign grades to students
                System.out.print("Enter student Name: ");
                String studentNameForGrade = scanner.nextLine();
                System.out.print("Enter student ID: ");
                String studentIdForGrade = scanner.nextLine();
                System.out.print("Enter course ID: ");
                String courseIdForGrade = scanner.nextLine();
                System.out.print("Enter course name: ");
                String courseNameForGrade = scanner.nextLine();
                System.out.print("Enter grade: ");
                double gradeForStudent = Double.parseDouble(scanner.nextLine());
                createGrade(studentNameForGrade, studentIdForGrade, courseIdForGrade, courseNameForGrade, gradeForStudent);
                break;

            case 6:
                // View All Students with Registered Courses
                displayAllStudents();
                break;

            case 7:
                logout();
                loggedInExit = true;
                break;

            default:
                System.out.println("Invalid option. Please try again.");
        }
    }
}

    public void adminOptions() {
        boolean loggedInExit = false;
        while (!loggedInExit) {
            System.out.println("Admin Options:");
            System.out.println("1. Create Instructor");
            System.out.println("2. Edit Instructor Information");
            System.out.println("3. Remove Instructor");
            System.out.println("4. Display all the Instructors");
            System.out.println("5. Add Department");
            System.out.println("6. update Department");
            System.out.println("7. Remove Department");
            System.out.println("8. Create Student");
            System.out.println("9. Remove Student");
            System.out.println("10. Create Courses");
            System.out.println("11. Register Courses");
            System.out.println("12. Remove Course");
            System.out.println("13. Withdraw a Course from student");
            System.out.println("14. Logout");
            System.out.print("Enter option: ");
            int loggedInOption;
            try {
                loggedInOption = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please enter a number.");
                continue;
            }

            switch (loggedInOption) {
                case 1:
                    createInstructor();
                    break;

                case 2:
                    System.out.print("Enter instructor Name to edit info: ");
                    String instructorId = scanner.nextLine();
                    updateInstructor(instructorId);
                    break;

                case 3:
                    System.out.print("Enter instructor name to remove: ");
                    instructorId = scanner.nextLine();
                    deleteInstructor(instructorId);
                    break;

                    case 4:
                    displayAllInstructors();
                    break;

                case 5:
                    createDepartment();
                    break;

                case 6:
                    System.out.print("Enter department name to update: ");
                    String updateDepartmentId = scanner.nextLine();
                    updateDepartment(updateDepartmentId); // Call the updateDepartment() method
                    break;

                case 7:
                    System.out.print("Enter department name to remove: ");
                    String departmentId = scanner.nextLine();
                    deleteDepartment(departmentId);
                    break;

                case 8:
                    createStudent();
                    break;

                case 9:
                    System.out.print("Enter student ID to remove: ");
                    String studentId = scanner.nextLine();
                    deleteStudent(studentId);
                    break;

                case 10:
                    createCourse();
                    break;

                case 11:
                    System.out.print("Enter student ID to register the course to: ");
                    String studentIdToRegister = scanner.nextLine();
                    System.out.print("Enter student name: ");
                    String studentName = scanner.nextLine();
                    System.out.print("Enter course name: ");
                    String courseNameToRegister = scanner.nextLine();
                    registerCourse(studentIdToRegister, courseNameToRegister, studentName); // Register the student for the course
                    break; 

                case 12:
                    System.out.print("Enter course Code to remove: ");
                    String courseId = scanner.nextLine();
                    deleteCourse(courseId);
                    break;

                case 13:
                    System.out.print("Enter student name: ");
                    String withdrawalStudentName = scanner.nextLine(); // Use a different variable name
                    System.out.print("Enter course Name: ");
                    String withdrawalCourseCode = scanner.nextLine(); // Use a different variable name
                    withdrawCourse(withdrawalStudentName, withdrawalCourseCode);
                    break;

                case 14:
                    logout();
                    loggedInExit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    public static void main(String[] args) {
        SISApplication sisApp = new SISApplication();
        Scanner scanner = sisApp.scanner;

        boolean exit = false;
        while (!exit) {
            System.out.println("Choose an option:");
            System.out.println("1. Login as a student");
            System.out.println("2. Login as an instructor");
            System.out.println("3. Login as an admin");
            System.out.println("4. Exit");
            System.out.print("Enter option: ");
            int option;
            try {
                option = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please enter a number.");
                continue;
            }

            switch (option) {
                case 1:
                    sisApp.studentLogin();
                    sisApp.studentOptions();
                    break;
                case 2:
                    sisApp.instructorLogin();
                    sisApp.instructorOptions();
                    break;
                case 3:
                    sisApp.adminLogin();
                    sisApp.adminOptions();
                    break;
                case 4:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }
}

/*
 * GUI wrapper class.
 * - This class was added to provide a Swing-based GUI for the existing SISApplication
 * - The original SISApplication class above was left unchanged (no removals or edits to its logic).
 * - The GUI uses its own MongoDB connection and implements similar CRUD operations so that users
 *   can interact with the same MongoDB collections via a graphical interface. This avoids modifying
 *   the original console-based methods which rely on Scanner input.
 */
class SISApplicationGUI {
    private static final String DATABASE_NAME = "sis_database";

    private MongoCollection<Document> studentsCollection;
    private MongoCollection<Document> instructorsCollection;
    private MongoCollection<Document> coursesCollection;
    private MongoCollection<Document> gradesCollection;
    private MongoCollection<Document> departmentsCollection;

    // GUI fields
    private JFrame frame;
    private JTextArea outputAreaStudent;
    private JTextArea outputAreaCourse;
    private JTextArea outputAreaInstructor;
    private JTextArea outputAreaGrade;
    private JTextArea outputAreaDepartment;

    private JTabbedPane tabs;                       // made a field to allow role toggling
    private enum Role { NONE, STUDENT, INSTRUCTOR, ADMIN }
    private Role currentRole = Role.NONE;
    private String loggedInUserId = null;
    private static final String ADMIN_ID = "12345"; // keep in sync with console admin id

    public SISApplicationGUI() {
        connectToDb();
        createAndShowGUI();
    }

    private void connectToDb() {
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017");
        MongoClientSettings settings = MongoClientSettings.builder().applyConnectionString(connectionString).build();
        com.mongodb.client.MongoClient mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase(DATABASE_NAME);
        studentsCollection = database.getCollection("students");
        instructorsCollection = database.getCollection("instructors");
        coursesCollection = database.getCollection("courses");
        gradesCollection = database.getCollection("grades");
        departmentsCollection = database.getCollection("departments");
    }

    private void createAndShowGUI() {
        frame = new JFrame("SIS Application GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);

        tabs = new JTabbedPane();

        tabs.addTab("Students", createStudentPanel());
        tabs.addTab("Instructors", createInstructorPanel());
        tabs.addTab("Courses", createCoursePanel());
        tabs.addTab("Grades", createGradePanel());
        tabs.addTab("Departments", createDepartmentPanel());

        // Initially disable all tabs until login
        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.setEnabledAt(i, false);
        }

        JPanel loginPanel = createLoginPanel();

        frame.getContentPane().add(loginPanel, BorderLayout.NORTH);
        frame.getContentPane().add(tabs, BorderLayout.CENTER);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel form = new JPanel(new GridLayout(6, 2, 6, 6));
        JTextField nameField = new JTextField();
        JTextField dobField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField idField = new JTextField();

        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Date of Birth:")); form.add(dobField);
        form.add(new JLabel("Address:")); form.add(addressField);
        form.add(new JLabel("Contact:")); form.add(contactField);
        form.add(new JLabel("Student ID (for lookup/update/delete):")); form.add(idField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton createBtn = new JButton("Create Student");
        JButton readBtn = new JButton("Read by ID");
        JButton updateBtn = new JButton("Update by ID");
        JButton deleteBtn = new JButton("Delete by Name");
        JButton listBtn = new JButton("List All Students");

        buttons.add(createBtn); buttons.add(readBtn); buttons.add(updateBtn); buttons.add(deleteBtn); buttons.add(listBtn);

        outputAreaStudent = new JTextArea(12, 60);
        outputAreaStudent.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputAreaStudent);

        panel.add(form, BorderLayout.NORTH);
        panel.add(buttons, BorderLayout.CENTER);
        panel.add(scroll, BorderLayout.SOUTH);

        createBtn.addActionListener(e -> {
            if (currentRole != Role.ADMIN) { JOptionPane.showMessageDialog(frame, "Only admins can create students."); return; }
            String name = nameField.getText().trim();
            String dob = dobField.getText().trim();
            String address = addressField.getText().trim();
            String contact = contactField.getText().trim();
            if (name.isEmpty()) { JOptionPane.showMessageDialog(frame, "Name is required."); return; }
            createStudentFromGUI(name, dob, address, contact);
            outputAreaStudent.setText("Created student: " + name + "");
        });

        readBtn.addActionListener(e -> {
            String id = idField.getText().trim();
            if (id.isEmpty()) { JOptionPane.showMessageDialog(frame, "Student ID is required for lookup."); return; }
            // allow student to read only their own record
            if (currentRole == Role.STUDENT && !id.equals(loggedInUserId)) { JOptionPane.showMessageDialog(frame, "Students can only view their own record."); return; }
            outputAreaStudent.setText(readStudentById(id));
        });

        updateBtn.addActionListener(e -> {
            if (currentRole == Role.STUDENT) { JOptionPane.showMessageDialog(frame, "Students cannot update records."); return; }
            if (currentRole == Role.INSTRUCTOR) { JOptionPane.showMessageDialog(frame, "Instructors cannot update student personal info here."); return; }
            String id = idField.getText().trim();
            if (id.isEmpty()) { JOptionPane.showMessageDialog(frame, "Student ID is required for update."); return; }
            String name = nameField.getText().trim();
            String dob = dobField.getText().trim();
            String address = addressField.getText().trim();
            String contact = contactField.getText().trim();
            updateStudentById(id, name, dob, address, contact);
            outputAreaStudent.setText("Updated student: " + id + "");
        });

        deleteBtn.addActionListener(e -> {
            if (currentRole != Role.ADMIN) { JOptionPane.showMessageDialog(frame, "Only admins can delete students."); return; }
            String name = nameField.getText().trim();
            if (name.isEmpty()) { JOptionPane.showMessageDialog(frame, "Name is required for deletion."); return; }
            deleteStudentByName(name);
            outputAreaStudent.setText("Deleted student with name: " + name + "");
        });

        listBtn.addActionListener(e -> {
            if (currentRole == Role.STUDENT) { JOptionPane.showMessageDialog(frame, "Students cannot list all students."); return; }
            outputAreaStudent.setText(listAllStudents());
        });

        return panel;
    }

    private JPanel createInstructorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(4, 2, 6, 6));
        JTextField nameField = new JTextField();
        JTextField contactField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField lookupField = new JTextField();

        form.add(new JLabel("Name:")); form.add(nameField);
        form.add(new JLabel("Contact:")); form.add(contactField);
        form.add(new JLabel("Email:")); form.add(emailField);
        form.add(new JLabel("Instructor Name (for lookup/delete):")); form.add(lookupField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton createBtn = new JButton("Create Instructor");
        JButton readBtn = new JButton("Read Instructor");
        JButton deleteBtn = new JButton("Delete Instructor");
        JButton listBtn = new JButton("List All Instructors");
        buttons.add(createBtn); buttons.add(readBtn); buttons.add(deleteBtn); buttons.add(listBtn);

        outputAreaInstructor = new JTextArea(12, 60); outputAreaInstructor.setEditable(false);

        createBtn.addActionListener(e -> {
            if (currentRole != Role.ADMIN) { JOptionPane.showMessageDialog(frame, "Only admins can create instructors."); return; }
            if (nameField.getText().trim().isEmpty()) { JOptionPane.showMessageDialog(frame, "Name required"); return; }
            createInstructorFromGUI(nameField.getText().trim(), contactField.getText().trim(), emailField.getText().trim());
            outputAreaInstructor.setText("Instructor created: " + nameField.getText().trim());
        });
        readBtn.addActionListener(e -> outputAreaInstructor.setText(readInstructorByName(lookupField.getText().trim())));
        deleteBtn.addActionListener(e -> { if (currentRole!=Role.ADMIN) { JOptionPane.showMessageDialog(frame, "Only admins can delete instructors."); return; } deleteInstructorByName(lookupField.getText().trim()); outputAreaInstructor.setText("Deleted instructor: " + lookupField.getText().trim()); });
        listBtn.addActionListener(e -> outputAreaInstructor.setText(listAllInstructors()));

        panel.add(form, BorderLayout.NORTH);
        panel.add(buttons, BorderLayout.CENTER);
        panel.add(new JScrollPane(outputAreaInstructor), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createCoursePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(6,2,6,6));
        JTextField nameField = new JTextField();
        JTextField codeField = new JTextField();
        JTextField creditsField = new JTextField();
        JTextField hoursField = new JTextField();
        JTextField lookupField = new JTextField();

        form.add(new JLabel("Course Name:")); form.add(nameField);
        form.add(new JLabel("Code:")); form.add(codeField);
        form.add(new JLabel("Credits:")); form.add(creditsField);
        form.add(new JLabel("Hours:")); form.add(hoursField);
        form.add(new JLabel("Course Code (lookup/delete):")); form.add(lookupField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton createBtn = new JButton("Create Course");
        JButton readBtn = new JButton("Read by Code");
        JButton deleteBtn = new JButton("Delete by Code");
        JButton listBtn = new JButton("List All Courses");
        JButton registerBtn = new JButton("Register Student to Course");
        buttons.add(createBtn); buttons.add(readBtn); buttons.add(deleteBtn); buttons.add(listBtn); buttons.add(registerBtn);

        outputAreaCourse = new JTextArea(12,60); outputAreaCourse.setEditable(false);

        createBtn.addActionListener(e -> {
            if (currentRole != Role.ADMIN) { JOptionPane.showMessageDialog(frame, "Only admins can create courses."); return; }
            try {
                String name = nameField.getText().trim();
                String code = codeField.getText().trim();
                int credits = Integer.parseInt(creditsField.getText().trim());
                int hours = Integer.parseInt(hoursField.getText().trim());
                createCourseFromGUI(name, code, credits, hours);
                outputAreaCourse.setText("Created course: " + name + " (" + code + ")");
            } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Check fields (credits/hours must be integers)."); }
        });

        readBtn.addActionListener(e -> outputAreaCourse.setText(readCourseByCode(lookupField.getText().trim())));
        deleteBtn.addActionListener(e -> { if (currentRole!=Role.ADMIN) { JOptionPane.showMessageDialog(frame, "Only admins can delete courses."); return; } deleteCourseByCode(lookupField.getText().trim()); outputAreaCourse.setText("Deleted if existed: " + lookupField.getText().trim()); });
        listBtn.addActionListener(e -> outputAreaCourse.setText(listAllCourses()));

        registerBtn.addActionListener(e -> {
            if (currentRole==Role.NONE) { JOptionPane.showMessageDialog(frame, "Please login first."); return; }
            JPanel p = new JPanel(new GridLayout(3,2));
            JTextField sid = new JTextField(); JTextField sname = new JTextField(); JTextField cname = new JTextField();
            p.add(new JLabel("Student ID:")); p.add(sid);
            p.add(new JLabel("Student Name:")); p.add(sname);
            p.add(new JLabel("Course Name:")); p.add(cname);
            int res = JOptionPane.showConfirmDialog(frame, p, "Register Student to Course", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                registerStudentToCourse(sid.getText().trim(), cname.getText().trim(), sname.getText().trim());
                outputAreaCourse.setText("Attempted registration of " + sname.getText().trim() + " to " + cname.getText().trim());
            }
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(buttons, BorderLayout.CENTER);
        panel.add(new JScrollPane(outputAreaCourse), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createGradePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(5,2,6,6));
        JTextField studentIdField = new JTextField();
        JTextField studentNameField = new JTextField();
        JTextField courseIdField = new JTextField();
        JTextField courseNameField = new JTextField();
        JTextField gradeField = new JTextField();

        form.add(new JLabel("Student Name:")); form.add(studentNameField);
        form.add(new JLabel("Student ID:")); form.add(studentIdField);
        form.add(new JLabel("Course ID:")); form.add(courseIdField);
        form.add(new JLabel("Course Name:")); form.add(courseNameField);
        form.add(new JLabel("Grade (number):")); form.add(gradeField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton assignBtn = new JButton("Assign Grade");
        JButton viewBtn = new JButton("View Grades by Student ID");
        buttons.add(assignBtn); buttons.add(viewBtn);

        outputAreaGrade = new JTextArea(12,60); outputAreaGrade.setEditable(false);

        assignBtn.addActionListener(e -> {
            if (currentRole==Role.STUDENT) { JOptionPane.showMessageDialog(frame, "Students cannot assign grades."); return; }
            try {
                String sname = studentNameField.getText().trim();
                String sid = studentIdField.getText().trim();
                String cid = courseIdField.getText().trim();
                String cname = courseNameField.getText().trim();
                double g = Double.parseDouble(gradeField.getText().trim());
                createGradeFromGUI(sname, sid, cid, cname, g);
                outputAreaGrade.setText("Assigned grade " + g + " to " + sname + " for " + cname);
            } catch (Exception ex) { JOptionPane.showMessageDialog(frame, "Check inputs. Grade must be a number."); }
        });

        viewBtn.addActionListener(e -> {
            String sid = studentIdField.getText().trim();
            if (sid.isEmpty()) { JOptionPane.showMessageDialog(frame, "Student ID is required."); return; }
            if (currentRole==Role.STUDENT && !sid.equals(loggedInUserId)) { JOptionPane.showMessageDialog(frame, "Students can only view their own grades."); return; }
            outputAreaGrade.setText(readGradeByStudentId(sid));
        });

        panel.add(form, BorderLayout.NORTH);
        panel.add(buttons, BorderLayout.CENTER);
        panel.add(new JScrollPane(outputAreaGrade), BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createDepartmentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel form = new JPanel(new GridLayout(3,2,6,6));
        JTextField nameField = new JTextField();
        JTextField courseField = new JTextField();
        JTextField lookupField = new JTextField();

        form.add(new JLabel("Department Name:")); form.add(nameField);
        form.add(new JLabel("One Course to Add (use comma to add many):")); form.add(courseField);
        form.add(new JLabel("Department Name (lookup/delete):")); form.add(lookupField);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton createBtn = new JButton("Create Department");
        JButton readBtn = new JButton("Read Department");
        JButton deleteBtn = new JButton("Delete Department");
        buttons.add(createBtn); buttons.add(readBtn); buttons.add(deleteBtn);

        outputAreaDepartment = new JTextArea(12,60); outputAreaDepartment.setEditable(false);

        createBtn.addActionListener(e -> {
            if (currentRole != Role.ADMIN) { JOptionPane.showMessageDialog(frame, "Only admins can create departments."); return; }
            String name = nameField.getText().trim();
            if (name.isEmpty()) { JOptionPane.showMessageDialog(frame, "Name required"); return; }
            String courses = courseField.getText().trim();
            createDepartmentFromGUI(name, courses);
            outputAreaDepartment.setText("Created department: " + name);
        });
        readBtn.addActionListener(e -> outputAreaDepartment.setText(readDepartmentByName(lookupField.getText().trim())));
        deleteBtn.addActionListener(e -> { if (currentRole!=Role.ADMIN) { JOptionPane.showMessageDialog(frame, "Only admins can delete departments."); return; } deleteDepartmentByName(lookupField.getText().trim()); outputAreaDepartment.setText("Deleted: " + lookupField.getText().trim()); });

        panel.add(form, BorderLayout.NORTH);
        panel.add(buttons, BorderLayout.CENTER);
        panel.add(new JScrollPane(outputAreaDepartment), BorderLayout.SOUTH);
        return panel;
    }

    // --- Login panel and role enforcement ---
    private JPanel createLoginPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        String[] roles = new String[] { "Student", "Instructor", "Admin" };
        JComboBox<String> roleBox = new JComboBox<>(roles);
        JTextField idField = new JTextField(12);
        JButton loginBtn = new JButton("Login");
        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setEnabled(false);

        p.add(new JLabel("Role:"));
        p.add(roleBox);
        p.add(new JLabel("ID:"));
        p.add(idField);
        p.add(loginBtn);
        p.add(logoutBtn);

        loginBtn.addActionListener(e -> {
            String selected = (String) roleBox.getSelectedItem();
            String id = idField.getText().trim();
            boolean ok = false;

            if (selected.equals("Admin")) {
                if (id.equals(ADMIN_ID)) {
                    currentRole = Role.ADMIN;
                    loggedInUserId = id;
                    ok = true;
                }
            } else if (selected.equals("Student")) {
                if (!id.isEmpty()) {
                    Document studentDoc = studentsCollection.find(new Document("_id", id)).first();
                    if (studentDoc != null) { currentRole = Role.STUDENT; loggedInUserId = id; ok = true; }
                }
            } else if (selected.equals("Instructor")) {
                if (!id.isEmpty()) {
                    Document instDoc = instructorsCollection.find(new Document("_id", id)).first();
                    if (instDoc != null) { currentRole = Role.INSTRUCTOR; loggedInUserId = id; ok = true; }
                }
            }

            if (ok) {
                JOptionPane.showMessageDialog(frame, "Logged in as " + selected);
                setTabsForRole();
                loginBtn.setEnabled(false);
                logoutBtn.setEnabled(true);
                idField.setEditable(false);
                roleBox.setEnabled(false);
            } else {
                JOptionPane.showMessageDialog(frame, "Login failed (invalid ID for role)");
            }
        });

        logoutBtn.addActionListener(e -> {
            currentRole = Role.NONE;
            loggedInUserId = null;
            JOptionPane.showMessageDialog(frame, "Logged out");
            setTabsForRole();
            loginBtn.setEnabled(true);
            logoutBtn.setEnabled(false);
            idField.setEditable(true);
            roleBox.setEnabled(true);
        });

        return p;
    }

    private void setTabsForRole() {
        // disable all tabs first
        for (int i = 0; i < tabs.getTabCount(); i++) tabs.setEnabledAt(i, false);

        if (currentRole == Role.ADMIN) {
            for (int i = 0; i < tabs.getTabCount(); i++) tabs.setEnabledAt(i, true);
        } else if (currentRole == Role.INSTRUCTOR) {
            // Allow instructor to see instructors, courses, grades and students (view)
            int t;
            t = tabs.indexOfTab("Instructors"); if (t >= 0) tabs.setEnabledAt(t, true);
            t = tabs.indexOfTab("Courses"); if (t >= 0) tabs.setEnabledAt(t, true);
            t = tabs.indexOfTab("Grades"); if (t >= 0) tabs.setEnabledAt(t, true);
            t = tabs.indexOfTab("Students"); if (t >= 0) tabs.setEnabledAt(t, true);
        } else if (currentRole == Role.STUDENT) {
            // Student sees only their Students tab (to view own info), Courses and Grades
            int t;
            t = tabs.indexOfTab("Students"); if (t >= 0) tabs.setEnabledAt(t, true);
            t = tabs.indexOfTab("Courses"); if (t >= 0) tabs.setEnabledAt(t, true);
            t = tabs.indexOfTab("Grades"); if (t >= 0) tabs.setEnabledAt(t, true);
        } else {
            // No role -> nothing enabled
        }
    }

    // --- DB operations used by GUI (these are independent implementations so original code remains intact) ---
    private void createStudentFromGUI(String name, String dob, String address, String contact) {
        String studentId = UUID.randomUUID().toString();
        Document studentDoc = new Document("_id", studentId)
                .append("name", name)
                .append("dateOfBirth", dob)
                .append("address", address)
                .append("contact", contact)
                .append("courses", new ArrayList<>());
        studentsCollection.insertOne(studentDoc);
    }

    private String readStudentById(String studentId) {
        Document studentDoc = studentsCollection.find(new Document("_id", studentId)).first();
        if (studentDoc == null) return "Student not found.";
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(studentDoc.getString("_id")).append("");
        sb.append("Name: ").append(studentDoc.getString("name")).append("");
        sb.append("DOB: ").append(studentDoc.getString("dateOfBirth")).append("");
        sb.append("Address: ").append(studentDoc.getString("address")).append("");
        sb.append("Contact: ").append(studentDoc.getString("contact")).append("");
        if (studentDoc.containsKey("gpa")) sb.append("GPA: ").append(studentDoc.getDouble("gpa")).append("");
        java.util.List<String> courses = studentDoc.getList("courses", String.class);
        if (courses != null && !courses.isEmpty()) {
            sb.append("Courses:");
            for (String c : courses) sb.append(" - ").append(c).append("");
        }
        return sb.toString();
    }

    private void updateStudentById(String studentId, String name, String dob, String address, String contact) {
        Document studentDoc = studentsCollection.find(new Document("_id", studentId)).first();
        if (studentDoc == null) return;
        if (!name.isEmpty()) studentDoc.put("name", name);
        if (!dob.isEmpty()) studentDoc.put("dateOfBirth", dob);
        if (!address.isEmpty()) studentDoc.put("address", address);
        if (!contact.isEmpty()) studentDoc.put("contact", contact);
        studentsCollection.replaceOne(new Document("_id", studentId), studentDoc);
    }

    private void deleteStudentByName(String name) {
        studentsCollection.deleteOne(new Document("name", name));
    }

    private String listAllStudents() {
        StringBuilder sb = new StringBuilder();
        MongoCursor<Document> cursor = studentsCollection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document doc = cursor.next();
                sb.append("ID: ").append(doc.getString("_id")).append(" | Name: ").append(doc.getString("name")).append("");
            }
        } finally { cursor.close(); }
        return sb.length() == 0 ? "No students found." : sb.toString();
    }

    // Instructor operations
    private void createInstructorFromGUI(String name, String contact, String email) {
        String instructorId = UUID.randomUUID().toString();
        Document instructorDoc = new Document("_id", instructorId)
                .append("name", name)
                .append("contact", contact)
                .append("email Address", email);
        instructorsCollection.insertOne(instructorDoc);
    }
    private String readInstructorByName(String name) {
        Document doc = instructorsCollection.find(new Document("name", name)).first();
        if (doc == null) return "Instructor not found.";
        return "ID: " + doc.getString("_id") + "Name: " + doc.getString("name") + "Contact: " + doc.getString("contact") + "Email: " + doc.getString("email Address");
    }
    private void deleteInstructorByName(String name) { instructorsCollection.deleteOne(new Document("name", name)); }
    private String listAllInstructors() {
        StringBuilder sb = new StringBuilder();
        MongoCursor<Document> cursor = instructorsCollection.find().iterator();
        try { while (cursor.hasNext()) { Document d = cursor.next(); sb.append(d.getString("_id")).append(" | ").append(d.getString("name")).append(""); } } finally { cursor.close(); }
        return sb.length()==0?"No instructors":sb.toString();
    }

    // Courses
    private void createCourseFromGUI(String name, String code, int credits, int hours) {
        Document existing = coursesCollection.find(new Document("code", code)).first();
        if (existing != null) return;
        Document doc = new Document("_id", UUID.randomUUID().toString())
                .append("name", name)
                .append("code", code)
                .append("credits", credits)
                .append("hours", hours)
                .append("registeredStudents", new ArrayList<>())
                .append("numberOfStudents", 0);
        coursesCollection.insertOne(doc);
    }
    private String readCourseByCode(String code) {
        Document doc = coursesCollection.find(new Document("code", code)).first();
        if (doc==null) return "Course not found.";
        return "ID: " + doc.getString("_id") + "Name: " + doc.getString("name") + "Code: " + doc.getString("code") + "Credits: " + doc.getInteger("credits") + "Hours: " + doc.getInteger("hours");
    }
    private void deleteCourseByCode(String code) { coursesCollection.deleteOne(new Document("code", code)); }
    private String listAllCourses() {
        StringBuilder sb = new StringBuilder();
        MongoCursor<Document> cursor = coursesCollection.find().iterator();
        try { while (cursor.hasNext()) { Document d = cursor.next(); sb.append(d.getString("code")).append(" | ").append(d.getString("name")).append(""); } } finally { cursor.close(); }
        return sb.length()==0?"No courses":sb.toString();
    }

    private void registerStudentToCourse(String studentId, String courseName, String studentName) {
        Document courseDoc = coursesCollection.find(new Document("name", courseName)).first();
        if (courseDoc != null) {
            String courseId = courseDoc.getString("_id");
            int current = courseDoc.getInteger("numberOfStudents", 0);
            studentsCollection.updateOne(new Document("_id", studentId), new Document("$addToSet", new Document("courses", courseName)));
            coursesCollection.updateOne(new Document("_id", courseId), new Document("$push", new Document("registeredStudents", new Document("studentId", studentId).append("studentName", studentName))).append("$set", new Document("numberOfStudents", current+1)));
        } else {
            // If not found, create minimal course and register
            Document newCourse = new Document("_id", UUID.randomUUID().toString()).append("name", courseName).append("code", "auto").append("credits", 0).append("hours", 0).append("registeredStudents", new ArrayList<>()).append("numberOfStudents", 0);
            coursesCollection.insertOne(newCourse);
            registerStudentToCourse(studentId, courseName, studentName);
        }
    }

    // Grades
    private void createGradeFromGUI(String studentName, String studentId, String courseId, String courseName, double grade) {
        String gradeId = UUID.randomUUID().toString();
        Document gradeDoc = new Document("_id", gradeId).append("Student Name: ", studentName).append("studentId", studentId).append("courseId", courseId).append("courseName", courseName).append("grade", grade);
        gradesCollection.insertOne(gradeDoc);
    }
    private String readGradeByStudentId(String studentId) {
        Document studentDoc = studentsCollection.find(new Document("_id", studentId)).first();
        if (studentDoc==null) return "Student not found.";
        java.util.List<Document> gradeDocs = gradesCollection.find(new Document("studentId", studentId)).into(new ArrayList<>());
        if (gradeDocs.isEmpty()) return "No grades found for this student.";
        StringBuilder sb = new StringBuilder();
        sb.append("Grades for ").append(studentDoc.getString("name")).append("");
        for (Document g : gradeDocs) sb.append(g.getString("courseName")).append(" : ").append(g.getDouble("grade")).append("");
        return sb.toString();
    }

    // Departments
    private void createDepartmentFromGUI(String name, String coursesCommaSeparated) {
        String id = UUID.randomUUID().toString();
        java.util.List<String> courses = new ArrayList<>();
        if (coursesCommaSeparated != null && !coursesCommaSeparated.isEmpty()) {
            String[] parts = coursesCommaSeparated.split(",");
            for (String p : parts) courses.add(p.trim());
        }
        Document doc = new Document("_id", id).append("name", name).append("courses", courses);
        departmentsCollection.insertOne(doc);
    }
    private String readDepartmentByName(String name) {
        Document doc = departmentsCollection.find(new Document("name", name)).first();
        if (doc==null) return "Department not found.";
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(doc.getString("_id")).append("Name: ").append(doc.getString("name")).append("Courses:");
        java.util.List<String> cs = doc.getList("courses", String.class);
        if (cs != null) for (String c: cs) sb.append(" - ").append(c).append("");
        return sb.toString();
    }
    private void deleteDepartmentByName(String name) { departmentsCollection.deleteOne(new Document("name", name)); }

    public static void main(String[] args) {
        // Run GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new SISApplicationGUI());
    }
}
