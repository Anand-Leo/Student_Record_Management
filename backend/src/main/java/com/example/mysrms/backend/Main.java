package com.example.mysrms.backend;

import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());

    public static void main(String[] args) {

        StudentDAO studentDAO = new StudentDAO();
        CourseDAO courseDAO = new CourseDAO();
        UserDAO userDAO = new UserDAO();
        Scanner scanner = new Scanner(System.in);

        boolean running = true;

        while (running) {
            try {
                displayMenu();
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                switch (choice) {
                    case 1:
                        addStudent(scanner, studentDAO);
                        break;
                    case 2:
                        addCourse(scanner, courseDAO);
                        break;
                    case 3:
                        addUser(scanner, userDAO);
                        break;
                    case 4:
                        displayAllStudents(studentDAO);
                        break;
                    case 5:
                        displayAllCourses(courseDAO);
                        break;
                    case 6:
                        displayAllUsers(userDAO);
                        break;
                    case 7:
                        updateStudent(scanner, studentDAO);
                        break;
                    case 8:
                        deleteStudent(scanner, studentDAO);
                        break;
                    case 9:
                        updateCourse(scanner, courseDAO);
                        break;
                    case 10:
                        deleteCourse(scanner, courseDAO);
                        break;
                    case 11:
                        updateUser(scanner, userDAO);
                        break;
                    case 12:
                        deleteUser(scanner, userDAO);
                        break;
                    case 13:
                        running = false;
                        System.out.println("Exiting the system. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // Clear the invalid input
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "Database error: ", e);
            }
        }

        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n===== Student Management System Menu =====");
        System.out.println("1. Add Student");
        System.out.println("2. Add Course");
        System.out.println("3. Add User");
        System.out.println("4. Display All Students");
        System.out.println("5. Display All Courses");
        System.out.println("6. Display All Users");
        System.out.println("7. Update Student");
        System.out.println("8. Delete Student");
        System.out.println("9. Update Course");
        System.out.println("10. Delete Course");
        System.out.println("11. Update User");
        System.out.println("12. Delete User");
        System.out.println("13. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void addStudent(Scanner scanner, StudentDAO studentDAO) throws SQLException {
        Student student = new Student();

        System.out.print("Enter Student Name: ");
        student.setName(scanner.nextLine());

        // Date input validation
        LocalDate birthDate = null;
        boolean validDate = false;
        while (!validDate) {
            try {
                System.out.print("Enter Birth Date (YYYY-MM-DD): ");
                String dateInput = scanner.nextLine();
                if (!dateInput.isEmpty()) {
                    birthDate = LocalDate.parse(dateInput);
                }
                validDate = true;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Please use YYYY-MM-DD");
            }
        }
        student.setBirthDate(birthDate);

        // Gender input
        System.out.print("Enter Student Gender: ");
        student.setGender(scanner.nextLine());

        // Section input validation
        String section;
        do {
            System.out.print("Enter Section (A-E): ");
            section = scanner.nextLine().toUpperCase();
        } while (!section.matches("[A-E]"));
        student.setSection(section);

        // Branch input
        System.out.print("Enter Branch Name: ");
        student.setBranch_name(scanner.nextLine());

        // Roll Number input
        System.out.print("Enter Roll Number: ");
        student.setRoll_number(scanner.nextLine());

        // Admission Year input
        boolean validYear = false;
        while (!validYear) {
            try {
                System.out.print("Enter Admission Year: ");
                student.setAdmission_year(Integer.parseInt(scanner.nextLine()));
                validYear = true;
            } catch (NumberFormatException e) {
                System.out.println("Invalid year format! Please enter a number.");
            }
        }

        // College input
        System.out.print("Enter College Name: ");
        student.setCollege_name(scanner.nextLine());

        // University input
        System.out.print("Enter University Name: ");
        student.setUniversity_name(scanner.nextLine());

        studentDAO.addStudent(student);
        System.out.println("Student added Successfully!");
    }

    private static void addCourse(Scanner scanner, CourseDAO courseDAO) throws SQLException {
        Course course = new Course();
        System.out.print("Enter Course Name: ");
        course.setCourseName(scanner.nextLine());
        System.out.print("Enter Course Description: ");
        course.setDescription(scanner.nextLine());
        courseDAO.addCourse(course);
        System.out.println("Course added Successfully!");
    }

    private static void addUser(Scanner scanner, UserDAO userDAO) throws SQLException {
        User user = new User();
        System.out.print("Enter Username: ");
        user.setUserName(scanner.nextLine());
        System.out.print("Enter Password: ");
        user.setPassword(scanner.nextLine());
        System.out.print("Enter Role: ");
        user.setUserRole(scanner.nextLine());
        userDAO.addUser(user);
        System.out.println("User added Successfully!");
    }

    private static void displayAllStudents(StudentDAO studentDAO) throws SQLException {
        List<Student> students = studentDAO.getAllStudents();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println("\n===== List of All Students =====");
        System.out.printf("%-10s %-15s %-20s %-12s %-8s %-8s %-15s %-15s %-20s %-20s%n",
                "S. No.", "Roll Number", "Name", "Birth Date", "Gender", "Section",
                "Branch", "Admission Year", "College", "University");

        int serialNumber = 1; // Start serial number from 1
        for (Student s : students) {
            String formattedDate = (s.getBirthDate() != null) ?
                    s.getBirthDate().format(formatter) :
                    "N/A";
            System.out.printf("%-10d %-15s %-20s %-12s %-8s %-8s %-15s %-15d %-20s %-20s%n",
                    serialNumber++, // Dynamically generated S. No.
                    s.getRoll_number(),
                    s.getName(),
                    formattedDate,
                    s.getGender(),
                    s.getSection(),
                    s.getBranch_name(),
                    s.getAdmission_year(),
                    s.getCollege_name(),
                    s.getUniversity_name());
        }
    }

    private static void displayAllCourses(CourseDAO courseDAO) throws SQLException {
        List<Course> courses = courseDAO.getAllCourses();
        System.out.println("\n===== List of All Courses =====");
        System.out.printf("%-10s %-20s %-50s%n", "S. No.", "Course Name", "Description");
        for (Course c : courses) {
            System.out.printf("%-10d %-20s %-50s%n",
                    c.getSerialNumber(),
                    c.getCourseName(),
                    c.getDescription()
            );
        }
    }

    private static void displayAllUsers(UserDAO userDAO) throws SQLException {
        List<User> users = userDAO.getAllUsers();
        System.out.println("\n===== List of All Users =====");
        System.out.printf("%-10s %-20s %-20s%n", "S. No.", "Username", "Role"); // Fixed width
        for (User u : users) {
            System.out.printf("%-10d %-20s %-20s%n",  // Use %d for serialNumber
                    u.getSerialNumber(),
                    u.getUserName(),
                    u.getUserRole()
            );
        }
    }

    private static void updateStudent(Scanner scanner, StudentDAO studentDAO) throws SQLException {
        System.out.print("Enter student Roll Number to update: ");
        String rollNumber = scanner.nextLine(); // Use roll_number instead of id
        Student student = studentDAO.getStudentByRollNumber(rollNumber); // Use a method that retrieves by roll_number
        if (student != null) {
            System.out.print("Enter new student name: ");
            student.setName(scanner.nextLine());
            System.out.print("Enter new student age: ");
            LocalDate newDate = null;
            boolean validDate = false;
            while (!validDate) {
                try {
                    System.out.print("Enter new birth date (YYYY-MM-DD): ");
                    String dateInput = scanner.nextLine();
                    newDate = LocalDate.parse(dateInput);
                    validDate = true;
                } catch (DateTimeParseException e) {
                    System.out.println("Invalid date format! Please use YYYY-MM-DD");
                }
            }
            student.setBirthDate(newDate);

            scanner.nextLine(); // Consume newline
            System.out.print("Enter new student gender: ");
            student.setGender(scanner.nextLine());
            // Add section update
            String section;
            do {
                System.out.print("Enter new section (A-E): ");
                section = scanner.nextLine().toUpperCase();
            } while (!section.matches("[A-E]"));
            student.setSection(section);

            System.out.print("Enter new branch name: ");
            student.setBranch_name(scanner.nextLine());
            System.out.print("Enter new admission year: ");
            student.setAdmission_year(scanner.nextInt());
            scanner.nextLine(); // Consume newline
            System.out.print("Enter new college name: ");
            student.setCollege_name(scanner.nextLine());
            System.out.print("Enter new university name: ");
            student.setUniversity_name(scanner.nextLine());
            studentDAO.updateStudent(student);
            System.out.println("Student updated successfully!");
        } else {
            System.out.println("Student not found with Roll Number: " + rollNumber);
        }
    }



    private static void deleteStudent(Scanner scanner, StudentDAO studentDAO) throws SQLException {
        System.out.print("Enter student Roll Number to delete: ");
        String rollNumber = scanner.nextLine(); // Read rollNumber as a String

        // Delete the student
        studentDAO.deleteStudent(rollNumber);
        System.out.println("Student deleted successfully!");
    }


    private static void updateCourse(Scanner scanner, CourseDAO courseDAO) throws SQLException {
        System.out.print("Enter course name to update: ");
        String originalCourseName = scanner.nextLine();
        Course course = courseDAO.getCourseByName(originalCourseName);
        if (course != null) {
            System.out.print("Enter new course name: ");
            course.setCourseName(scanner.nextLine());
            System.out.print("Enter new description: ");
            course.setDescription(scanner.nextLine());
            courseDAO.updateCourse(course, originalCourseName);
            System.out.println("Course updated successfully!");
        } else {
            System.out.println("Course not found!");
        }
    }

    private static void deleteCourse(Scanner scanner, CourseDAO courseDAO) throws SQLException {
        System.out.print("Enter course name to delete: ");
        String courseName = scanner.nextLine(); // Read course name as input
        courseDAO.deleteCourse(courseName);
        System.out.println("Course deleted successfully!");
    }

    private static void updateUser(Scanner scanner, UserDAO userDAO) throws SQLException {
        System.out.print("Enter user username to update: ");
        String originalUserName  = scanner.nextLine();
        User user = userDAO.getUserByUsername(originalUserName);
        if (user != null) {
            System.out.print("Enter new username: ");
            user.setUserName(scanner.nextLine());
            System.out.print("Enter new password: ");
            user.setPassword(scanner.nextLine());
            System.out.print("Enter new role: ");
            user.setUserRole(scanner.nextLine());
            userDAO.updateUser(user, originalUserName);
            System.out.println("User updated successfully!");
        } else {
            System.out.println("User not found!");
        }
    }

    private static void deleteUser(Scanner scanner, UserDAO userDAO) throws SQLException {
        System.out.print("Enter username to delete: ");
        String username = scanner.nextLine();
        userDAO.deleteUser(username); // Changed to String parameter
        System.out.println("User deleted successfully!");
    }
}