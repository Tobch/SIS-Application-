import java.util.*;

public class SISApplication {
    private static final String ADMIN_ID = "12345";

    private Scanner scanner;
    private String loggedInUserId;

    // In-memory data structures to store student, instructor, course, and grade information
    private Map<String, Map<String, String>> studentsMap; // Student ID -> Student Info
    private Map<String, Map<String, String>> instructorsMap; // Instructor ID -> Instructor Info
    private Map<String, Map<String, Object>> coursesMap; // Course ID -> Course Info
    private List<Map<String, Object>> gradesList; // List of grade records
    private Map<String, List<String>> departmentMap; // Department ID -> List of Instructor IDs

    public SISApplication() {
        scanner = new Scanner(System.in);
        studentsMap = new HashMap<>();
        instructorsMap = new HashMap<>();
        coursesMap = new HashMap<>();
        gradesList = new ArrayList<>();
        departmentMap = new HashMap<>();
    }

    public boolean studentLogin() {
        System.out.print("Enter student ID: ");
        String studentId = scanner.nextLine();
        if (studentsMap.containsKey(studentId)) {
            loggedInUserId = studentId;
            return true;
        } else {
            return false;
        }
    }

    public boolean instructorLogin() {
        System.out.print("Enter instructor ID: ");
        String instructorId = scanner.nextLine();
        if (instructorsMap.containsKey(instructorId)) {
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

    public void createStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student date of birth: ");
        String dateOfBirth = scanner.nextLine();
        System.out.print("Enter student address: ");
        String address = scanner.nextLine();
        System.out.print("Enter student contact: ");
        String contact = scanner.nextLine();

        String studentId = UUID.randomUUID().toString(); // Generate a unique ID for the student
        Map<String, String> studentInfo = new HashMap<>();
        studentInfo.put("name", name);
        studentInfo.put("dateOfBirth", dateOfBirth);
        studentInfo.put("address", address);
        studentInfo.put("contact", contact);

        studentsMap.put(studentId, studentInfo); // Add student info to the map with generated ID
        System.out.println("Student created successfully with ID: " + studentId);
    }

    public void displayStudentInfo(String studentId) {
        Map<String, String> studentInfo = studentsMap.get(studentId);
        if (studentInfo != null) {
            System.out.println("Student Information:");
            System.out.println("Name: " + studentInfo.get("name"));
            System.out.println("Date of Birth: " + studentInfo.get("dateOfBirth"));
            System.out.println("Address: " + studentInfo.get("address"));
            System.out.println("Contact: " + studentInfo.get("contact"));
        } else {
            System.out.println("Student not found.");
        }
    }

    public void registerCourses(String studentId) {
        System.out.println("Enter course details:");
        System.out.print("Course Name: ");
        String courseName = scanner.nextLine();
        System.out.print("Course Code: ");
        String courseCode = scanner.nextLine();
        System.out.print("Course Credits: ");
        int courseCredits = Integer.parseInt(scanner.nextLine());
    
        Map<String, Object> courseInfo = new HashMap<>();
        courseInfo.put("courseName", courseName);
        courseInfo.put("courseCode", courseCode);
        courseInfo.put("courseCredits", courseCredits);
    
        String courseId = UUID.randomUUID().toString(); // Generate a unique ID for the course
        coursesMap.put(courseId, courseInfo); // Add course info to the map with generated ID
        
        // Update grades list to register the student for the course
        Map<String, Object> gradeInfo = new HashMap<>();
        gradeInfo.put("studentId", studentId);
        gradeInfo.put("courseId", courseId);
        gradeInfo.put("grade", null); // Initialize grade as null
        gradesList.add(gradeInfo);
        
        System.out.println("Course registered successfully with ID: " + courseId);
    }
    
    public void displayRegisteredCourses(String studentId) {
        System.out.println("Registered Courses for Student " + studentsMap.get(studentId).get("name") + ":");
        boolean foundCourses = false;
        for (String courseId : coursesMap.keySet()) {
            for (Map<String, Object> gradeInfo : gradesList) {
                if (gradeInfo.get("studentId").equals(studentId) && gradeInfo.get("courseId").equals(courseId)) {
                    String courseName = (String) coursesMap.get(courseId).get("courseName");
                    System.out.println("Course: " + courseName);
                    foundCourses = true;
                    break;
                }
            }
        }
        if (!foundCourses) {
            System.out.println("No courses found for this student.");
        }
    }
    
    
    public void assignGrade(String studentId, String courseId, double grade) {
        Map<String, Object> gradeInfo = new HashMap<>();
        gradeInfo.put("studentId", studentId);
        gradeInfo.put("courseId", courseId);
        gradeInfo.put("grade", grade);

        gradesList.add(gradeInfo);
        System.out.println("Grade assigned successfully.");
    }

    public void viewGradesForStudent(String studentId) {
        System.out.println("Grades for Student " + studentsMap.get(studentId).get("name") + ":");
        boolean foundGrades = false;
        for (Map<String, Object> gradeInfo : gradesList) {
            if (gradeInfo.get("studentId").equals(studentId)) {
                String courseId = (String) gradeInfo.get("courseId");
                String courseName = (String) coursesMap.get(courseId).get("courseName");
                double grade = (double) gradeInfo.get("grade");
                System.out.println("Course: " + courseName + ", Grade: " + grade);
                foundGrades = true;
            }
        }
        if (!foundGrades) {
            System.out.println("No grades found for this student.");
        }
    }

    public void createInstructor() {
        System.out.print("Enter instructor name: ");
        String name = scanner.nextLine();
        System.out.print("Enter instructor contact: ");
        String contact = scanner.nextLine();

        String instructorId = UUID.randomUUID().toString(); // Generate a unique ID for the instructor
        Map<String, String> instructorInfo = new HashMap<>();
        instructorInfo.put("name", name);
        instructorInfo.put("contact", contact);

        instructorsMap.put(instructorId, instructorInfo); // Add instructor info to the map with generated ID
        System.out.println("Instructor created successfully with ID: " + instructorId);
    }

    public void editInstructorInfo(String instructorId) {
        Map<String, String> instructorInfo = instructorsMap.get(instructorId);
        if (instructorInfo != null) {
            System.out.println("Enter new instructor information:");
            System.out.print("Name: ");
            instructorInfo.put("name", scanner.nextLine());
            System.out.print("Contact: ");
            instructorInfo.put("contact", scanner.nextLine());
            System.out.println("Instructor information updated successfully.");
        } else {
            System.out.println("Instructor not found.");
        }
    }
    public void editStudentInfo(String studentId) {
        Map<String, String> studentInfo = studentsMap.get(studentId);
        if (studentInfo != null) {
            System.out.println("Enter new student information:");
            System.out.print("Name: ");
            studentInfo.put("name", scanner.nextLine());
            System.out.print("Date of Birth: ");
            studentInfo.put("dateOfBirth", scanner.nextLine());
            System.out.print("Address: ");
            studentInfo.put("address", scanner.nextLine());
            System.out.print("Contact: ");
            studentInfo.put("contact", scanner.nextLine());
            System.out.println("Student information updated successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }
    

    public void removeInstructor() {
        System.out.print("Enter instructor ID to remove: ");
        String instructorId = scanner.nextLine();
        if (instructorsMap.containsKey(instructorId)) {
            // Remove from the instructor map
            instructorsMap.remove(instructorId);
            // Remove from the department map if the instructor is associated with any department
            for (List<String> instructorList : departmentMap.values()) {
                instructorList.remove(instructorId);
            }
            System.out.println("Instructor removed successfully.");
        } else {
            System.out.println("Instructor not found.");
        }
    }

    public void addDepartment() {
        System.out.print("Enter department name: ");
        String departmentName = scanner.nextLine();
        String departmentId = UUID.randomUUID().toString(); // Generate a unique ID for the department
        departmentMap.put(departmentId, new ArrayList<>());
        System.out.println("Department added successfully with ID: " + departmentId);
    }

    public void removeDepartment() {
        System.out.print("Enter department ID to remove: ");
        String departmentId = scanner.nextLine();
        if (departmentMap.containsKey(departmentId)) {
            // Remove from the department map
            departmentMap.remove(departmentId);
            System.out.println("Department removed successfully.");
        } else {
            System.out.println("Department not found.");
        }
    }

    public void removeStudent() {
        System.out.print("Enter student ID to remove: ");
        String studentId = scanner.nextLine();
        if (studentsMap.containsKey(studentId)) {
            // Remove from the students map
            studentsMap.remove(studentId);
            System.out.println("Student removed successfully.");
        } else {
            System.out.println("Student not found.");
        }
    }

    public void removeCourse() {
        System.out.print("Enter course ID to remove: ");
        String courseId = scanner.nextLine();
        if (coursesMap.containsKey(courseId)) {
            // Remove from the courses map
            coursesMap.remove(courseId);
            // Remove from the grades list if any grades are associated with the course
            gradesList.removeIf(gradeInfo -> gradeInfo.get("courseId").equals(courseId));
            System.out.println("Course removed successfully.");
        } else {
            System.out.println("Course not found.");
        }
    }

    public void logout() {
        loggedInUserId = null;
        System.out.println("Logged out successfully.");
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
                    boolean loggedInAsStudent = sisApp.studentLogin();
                    if (!loggedInAsStudent) {
                        System.out.println("Invalid student ID. Please try again.");
                    } else {
                        boolean loggedInExit = false;
                        while (!loggedInExit) {
                            System.out.println("\nStudent Options:");
                            System.out.println("1. Display Student Information");
                            System.out.println("2. Display all the Registered Courses ");
                            System.out.println("3. View Grades");
                            System.out.println("4. Logout");
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
                                    sisApp.displayStudentInfo(sisApp.loggedInUserId);
                                    break;
                                case 2:
                                     sisApp.displayRegisteredCourses(sisApp.loggedInUserId);
                                    break;
                                case 3:
                                    sisApp.viewGradesForStudent(sisApp.loggedInUserId);
                                    break;
                                case 4:
                                    sisApp.logout();
                                    loggedInExit = true;
                                    break;
                                default:
                                    System.out.println("Invalid option. Please try again.");
                            }
                        }
                    }
                    break;
                case 2:
                    boolean loggedInAsInstructor = sisApp.instructorLogin();
                    if (!loggedInAsInstructor) {
                        System.out.println("Invalid instructor ID. Please try again.");
                    } else {
                        boolean loggedInExit = false;
                        while (!loggedInExit) {
                            System.out.println("\nInstructor Options:");
                            System.out.println("1. Edit Student Information");
                            System.out.println("2. Assign Grades to Students");
                            System.out.println("3. Logout");
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
                                    System.out.print("Enter student ID to edit info: ");
                                    String studentId = scanner.nextLine();
                                    sisApp.editStudentInfo(studentId);
                                    break;
                                case 2:
                                    System.out.print("Enter student ID: ");
                                    studentId = scanner.nextLine();
                                    System.out.print("Enter course ID: ");
                                    String courseId = scanner.nextLine();
                                    System.out.print("Enter grade: ");
                                    double grade = Double.parseDouble(scanner.nextLine());
                                    sisApp.assignGrade(studentId, courseId, grade);
                                    break;
                                case 3:
                                    sisApp.logout();
                                    loggedInExit = true;
                                    break;
                                default:
                                    System.out.println("Invalid option. Please try again.");
                            }
                        }
                    }
                    break;
                case 3:
                    boolean loggedInAsAdmin = sisApp.adminLogin();
                    if (!loggedInAsAdmin) {
                        System.out.println("Invalid admin ID. Please try again.");
                    } else {
                        boolean loggedInExit = false;
                        while (!loggedInExit) {
                            System.out.println("\nAdmin Options:");
                            System.out.println("1. Create Instructor");
                            System.out.println("2. Edit Instructor Information");
                            System.out.println("3. Remove Instructor");
                            System.out.println("4. Add Department");
                            System.out.println("5. Remove Department");
                            System.out.println("6. Create Student");
                            System.out.println("7. Remove Student");
                            System.out.println("8. Register Courses");
                            System.out.println("9. Remove Course");
                            System.out.println("10. Logout");
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
                                    sisApp.createInstructor();
                                    break;
                                case 2:
                                    System.out.print("Enter instructor ID to edit info: ");
                                    String instructorId = scanner.nextLine();
                                    sisApp.editInstructorInfo(instructorId);
                                    break;
                                case 3:
                                    sisApp.removeInstructor();
                                    break;
                                case 4:
                                    sisApp.addDepartment();
                                    break;
                                case 5:
                                    sisApp.removeDepartment();
                                    break;
                                case 6:
                                    sisApp.createStudent();
                                    break;
                                case 7:
                                    sisApp.removeStudent();
                                    break;
                                case 8:
                                    System.out.print("Enter student ID to register the course: ");
                                    String studentId = scanner.nextLine();
                                    sisApp.registerCourses(studentId);
                                    break;
                                case 9:
                                    sisApp.removeCourse();
                                    break;
                                case 10:
                                    sisApp.logout();
                                    loggedInExit = true;
                                    break;
                                default:
                                    System.out.println("Invalid option. Please try again.");
                            }
                        }
                    }
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
