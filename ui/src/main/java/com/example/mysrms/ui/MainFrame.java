package com.example.mysrms.ui;

import com.example.mysrms.backend.*;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.github.lgooddatepicker.components.DatePicker;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import com.example.mysrms.backend.User;
import com.example.mysrms.backend.UserDAO;
import org.mindrot.jbcrypt.BCrypt;

import javax.swing.*;
import javax.swing.table.TableRowSorter;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import java.awt.*;
import java.sql.SQLException;


public class MainFrame extends JFrame {

    // DAOs (Ensure these match your backend classes)
    private StudentDAO studentDAO = new StudentDAO();
    private CourseDAO courseDAO = new CourseDAO();
    private UserDAO userDAO = new UserDAO();

    // UI Components
    private JTabbedPane tabbedPane;
    private JTable studentTable, courseTable, userTable;
    private DefaultTableModel studentTableModel, courseTableModel, userTableModel;

    // Input Fields
    private JTextField studentNameField,
            studentRollNumberField, studentAdmissionYearField, studentCollegeField, studentUniversityField, studentPhoneNoField;
    private JTextField courseNameField, descriptionField;
    private JTextField userNameField, userRoleField;
    private JPasswordField passwordField;

    private JLabel fixedCollegeCodeLabel;
    private JComboBox<String> branchComboBoxForRoll;
    private JSpinner admissionYearSpinner;
    private JSpinner sequenceSpinner;
    private JPanel rollNumberPanel;



    private JComboBox<String> studentGenderField,sectionComboBox,branchComboBox;
    // Replace JTextField studentAgeField with:
    private DatePicker dateOfBirthPicker;

    public MainFrame() {
        setTitle("Student Record Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        initializeUI();
        SwingUtilities.invokeLater(() -> loadDataOnStartup());
    }

    private void initializeUI() {
        // Main Tabbed Pane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Students", createStudentPanel());
        tabbedPane.addTab("Courses", createCoursePanel());
        tabbedPane.addTab("Users", createUserPanel());
        add(tabbedPane, BorderLayout.CENTER);

        // Button Panel (Common for all tabs)
        add(createGlobalButtonPanel(), BorderLayout.SOUTH);
    }
    // EDit Dialogs

    public class EditStudentDialog extends JDialog {
        private Student student;
        private StudentDAO studentDAO = new StudentDAO();

        // Components
        private JTextField nameField, rollNumberField,
                admissionYearField, collegeField, universityField, phoneNoField;
        private JComboBox<String> genderField, sectionField;
        private JComboBox<String> branchComboBox;
        private DatePicker dateOfBirthPicker;

        public EditStudentDialog(JFrame parent, Student student) {
            super(parent, "Edit Student", true);
            this.student = student;
            initializeUI();
        }

        private void initializeUI() {
            setLayout(new GridLayout(11, 2, 5, 5)); // 11 rows for all components
            setSize(500, 400);
            setLocationRelativeTo(getParent());

            // Initialize all components first
            nameField = new JTextField(student.getName());
            dateOfBirthPicker = new DatePicker();
            dateOfBirthPicker.setDate(student.getBirthDate());
            genderField = new JComboBox<>(new String[]{"Male", "Female", "Other"});
            genderField.setSelectedItem(student.getGender());
            sectionField = new JComboBox<>(new String[]{"A", "B", "C", "D", "E", "-"});
            sectionField.setSelectedItem(student.getSection());
            // In initializeUI():
            branchComboBox = new JComboBox<>(new String[]{"CS", "IT", "EC", "ME", "CE"});
            branchComboBox.setEditable(true);
            branchComboBox.setSelectedItem(student.getBranch_name());
            add(branchComboBox);

            rollNumberField = new JTextField(student.getRoll_number());
            admissionYearField = new JTextField(String.valueOf(student.getAdmission_year()));
            collegeField = new JTextField(student.getCollege_name());
            universityField = new JTextField(student.getUniversity_name());
            phoneNoField = new JTextField(student.getPhone_no());

            // Add components in proper order
            add(new JLabel(" Name:"));
            add(nameField);
            add(new JLabel(" D.O.B:"));
            add(dateOfBirthPicker);
            add(new JLabel(" Gender:"));
            add(genderField);
            add(new JLabel(" Branch:"));
            add(branchComboBox);
            add(new JLabel(" Section:"));
            add(sectionField);
            add(new JLabel(" Roll Number:"));
            add(rollNumberField);
            add(new JLabel(" Admission Year:"));
            add(admissionYearField);
            add(new JLabel(" College:"));
            add(collegeField);
            add(new JLabel(" University:"));
            add(universityField);
            add(new JLabel(" Phone Number:"));
            add(phoneNoField);

            // Buttons
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> saveStudent());
            add(saveButton);

            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> dispose());
            add(cancelButton);
        }

        private void saveStudent() {
            try {
                // Get phone number from field
                String phoneNo = phoneNoField.getText();

                // Add validation check
                if (!((MainFrame) getParent()).isValidPhoneNumber(phoneNo)) {
                    JOptionPane.showMessageDialog(this,
                            "Phone number must be 10 digits!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                    return;
                }

                student.setName(nameField.getText());
                student.setBirthDate(dateOfBirthPicker.getDate());
                student.setGender((String) genderField.getSelectedItem());
                student.setSection((String) sectionField.getSelectedItem());
                student.setBranch_name(branchComboBox.getSelectedItem().toString());
                student.setRoll_number(rollNumberField.getText());
                student.setAdmission_year(Integer.parseInt(admissionYearField.getText()));
                student.setCollege_name(collegeField.getText());
                student.setUniversity_name(universityField.getText());
                student.setPhone_no(phoneNoField.getText());

                studentDAO.updateStudent(student);
                JOptionPane.showMessageDialog(this, "Student updated successfully!");
                dispose();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Invalid Admission Year format!");
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage());
            }
        }
    }

    public class EditCourseDialog extends JDialog {
        private Course course;
        private CourseDAO courseDAO = new CourseDAO();

        // Text Fields
        private JTextField courseNameField, descriptionField;

        public EditCourseDialog(JFrame parent, Course course) {
            super(parent, "Edit Course", true);
            this.course = course;
            initializeUI();
        }

        private void initializeUI() {
            setLayout(new GridLayout(3, 2, 5, 5));
            setSize(400, 200);
            setLocationRelativeTo(getParent());

            // Initialize Fields with Course Data
            courseNameField = new JTextField(course.getCourseName());
            descriptionField = new JTextField(course.getDescription());

            // Add Components
            add(new JLabel("Course Name:"));
            add(courseNameField);
            add(new JLabel("Description:"));
            add(descriptionField);

            // Save Button
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> saveCourse());
            add(saveButton);

            // Cancel Button
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> dispose());
            add(cancelButton);
        }

        private void saveCourse() {
            try {
                // Get the original course name before any edits
                String originalCourseName = course.getOriginalCourseName();

                // Update the course object with new values
                course.setCourseName(courseNameField.getText());
                course.setDescription(descriptionField.getText());

                // Update in the database using the original name
                courseDAO.updateCourse(course, originalCourseName);

                JOptionPane.showMessageDialog(this, "Course updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close the dialog
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    public class EditUserDialog extends JDialog {
        private User user;
        private UserDAO userDAO = new UserDAO();

        // Text Fields
        private JTextField usernameField, roleField;
        private JPasswordField passwordField; // Changed to JPasswordField

        public EditUserDialog(JFrame parent, User user) {
            super(parent, "Edit User", true);
            this.user = user;
            initializeUI();
        }

        private void initializeUI() {
            setLayout(new GridLayout(4, 2, 5, 5));
            setSize(400, 200);
            setLocationRelativeTo(getParent());

            // Initialize Fields with User Data
            usernameField = new JTextField(user.getUserName());
            passwordField = new JPasswordField(user.getPassword()); // Changed
            roleField = new JTextField(user.getUserRole());

            // Add Components
            add(new JLabel("Username:"));
            add(usernameField);
            add(new JLabel("Password:"));
            add(passwordField);
            add(new JLabel("Role:"));
            add(roleField);

            // Save Button
            JButton saveButton = new JButton("Save");
            saveButton.addActionListener(e -> saveUser());
            add(saveButton);

            // Cancel Button
            JButton cancelButton = new JButton("Cancel");
            cancelButton.addActionListener(e -> dispose());
            add(cancelButton);
        }

        private void saveUser() {
            try {
                // Get the original course name before any edits
                String originalUserName = user.getOriginalUserName();
                // Update the user object
                user.setUserName(usernameField.getText());
                user.setPassword(new String(passwordField.getPassword())); // Get password
                user.setUserRole(roleField.getText());

                // Update in the database
                userDAO.updateUser(user, originalUserName);
                JOptionPane.showMessageDialog(this, "User updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                dispose(); // Close the dialog
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void initializeRollNumberComponents() {
        // Fixed college code (0905)
        fixedCollegeCodeLabel = new JLabel("0905");

        // Branch dropdown (editable)
        branchComboBoxForRoll = new JComboBox<>(new String[]{"IT", "EC", "CS", "CE", "ME"});
        branchComboBoxForRoll.setEditable(true);
        branchComboBoxForRoll.setPreferredSize(new Dimension(50, 25));

        // Year spinner (2-digit format, e.g., 22 for 2022)
        SpinnerNumberModel yearModel = new SpinnerNumberModel(22, 0, 99, 1);
        admissionYearSpinner = new JSpinner(yearModel);
        JSpinner.NumberEditor yearEditor = new JSpinner.NumberEditor(admissionYearSpinner, "00");
        admissionYearSpinner.setEditor(yearEditor);

        // Sequence dropdown (1001 to 1999)
        SpinnerNumberModel sequenceModel = new SpinnerNumberModel(1001, 1001, 1200, 1);
        sequenceSpinner = new JSpinner(sequenceModel);
        JSpinner.NumberEditor sequenceEditor = new JSpinner.NumberEditor(sequenceSpinner, "0000");
        sequenceSpinner.setEditor(sequenceEditor);


        // Layout for roll number components
        rollNumberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        rollNumberPanel.add(fixedCollegeCodeLabel);
        rollNumberPanel.add(branchComboBoxForRoll);
        rollNumberPanel.add(admissionYearSpinner);
        rollNumberPanel.add(sequenceSpinner);

        branchComboBoxForRoll.getEditor().getEditorComponent().addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                JTextField tf = (JTextField) e.getSource();
                if (tf.getText().length() >= 2) {
                    e.consume(); // Block input after 2 characters
                }
            }
        });
    }

    private void configurePhoneNumberField() {
        // Set document filter
        ((AbstractDocument) studentPhoneNoField.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr)
                    throws BadLocationException {
                String newText = fb.getDocument().getText(0, fb.getDocument().getLength()) + string;
                if (isValidPhoneInput(newText, offset)) {
                    super.insertString(fb, offset, string, attr);
                }
            }

            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs)
                    throws BadLocationException {
                String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                String newText = currentText.substring(0, offset) + text + currentText.substring(offset + length);
                if (isValidPhoneInput(newText, offset)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }

            private boolean isValidPhoneInput(String text, int offset) {
                // Prevent modification of +91 prefix
                if (offset < 3) return false;

                // Allow only digits after prefix
                if (text.length() > 3 && !text.substring(3).matches("\\d*")) {
                    return false;
                }

                // Max length 13 characters (+91 + 10 digits)
                return text.length() <= 13;
            }
        });

        // Add input validation
        studentPhoneNoField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                JTextField source = (JTextField) e.getSource();
                String text = source.getText();

                // Prevent deletion of +91 prefix
                if (text.length() <= 3 && e.getKeyChar() == KeyEvent.VK_BACK_SPACE) {
                    e.consume();
                }
            }
        });
    }

    //--------------------- Student Tab ---------------------
    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Search Row (row 0)
        gbc.gridy = 0;
        gbc.gridx = 0;
        inputPanel.add(new JLabel("Search:"), gbc);
        gbc.gridx = 1;
        JTextField searchField = new JTextField(20);
        inputPanel.add(searchField, gbc);
        gbc.gridx = 2;
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> filterStudentTable(searchField.getText()));
        inputPanel.add(searchButton, gbc);

        // Row 1: Name and DOB
        gbc.gridy = 1;
        gbc.gridx = 0;
        inputPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        studentNameField = new JTextField(20);
        inputPanel.add(studentNameField, gbc);
        gbc.gridx = 2;
        inputPanel.add(new JLabel("D.O.B:"), gbc);
        gbc.gridx = 3;
        dateOfBirthPicker = new DatePicker();
        inputPanel.add(dateOfBirthPicker, gbc);

        // Row 2: Gender + Branch/Section
        gbc.gridy = 2;
        gbc.gridx = 0;
        inputPanel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        studentGenderField = new JComboBox<>(new String[]{"Male", "Female", "Other"});
        inputPanel.add(studentGenderField, gbc);


        // Branch + Section in same row (modified)
        JPanel branchSectionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        branchSectionPanel.add(new JLabel("Branch:"));
        branchComboBox = new JComboBox<>(new String[]{"CS", "IT", "EC", "ME", "CE"});
        branchComboBox.setEditable(true);
        branchComboBox.setPreferredSize(new Dimension(75, 25));
        branchSectionPanel.add(branchComboBox);
        branchSectionPanel.add(new JLabel("Section:"));
        sectionComboBox = new JComboBox<>(new String[]{"A", "B", "C", "D", "E", "-"});
        branchSectionPanel.add(sectionComboBox);

        gbc.gridx = 2;
        gbc.gridwidth = 2;
        inputPanel.add(branchSectionPanel, gbc);
        gbc.gridwidth = 1;

        // Row 3: Roll Number + Admission Year
        gbc.gridy = 3;
        gbc.gridx = 0;
        inputPanel.add(new JLabel("Roll Number:"), gbc);
        gbc.gridx = 1;
        initializeRollNumberComponents(); // Initialize components
        inputPanel.add(rollNumberPanel, gbc); // Add the roll number panel
        gbc.gridx = 2;
        inputPanel.add(new JLabel("Admission Year:"), gbc);
        gbc.gridx = 3;
        studentAdmissionYearField = new JTextField(10);
        inputPanel.add(studentAdmissionYearField, gbc);

        // 🔽🔽🔽 ADD THE LISTENER RIGHT HERE 🔽🔽🔽
        admissionYearSpinner.addChangeListener(e -> {
            int year = (Integer) admissionYearSpinner.getValue();
            studentAdmissionYearField.setText("20" + String.format("%02d", year)); // e.g., 22 → 2022
        });


        // Row 4: College and University
        gbc.gridy = 4;
        gbc.gridx = 0;
        inputPanel.add(new JLabel("College:"), gbc);
        gbc.gridx = 1;
        studentCollegeField = new JTextField("ITM Gwalior", 20);
        inputPanel.add(studentCollegeField, gbc);
        gbc.gridx = 2;
        inputPanel.add(new JLabel("University:"), gbc);
        gbc.gridx = 3;
        studentUniversityField = new JTextField("RGPV Bhopal", 20);
        inputPanel.add(studentUniversityField, gbc);

        // Phone Number (Row 5)
        gbc.gridy = 5;
        gbc.gridx = 0;
        inputPanel.add(new JLabel("Phone Number:"), gbc);

        gbc.gridx = 1;
        studentPhoneNoField = new JTextField(15);
        studentPhoneNoField.setPreferredSize(new Dimension(200, 25));

// Set default value and configure input restrictions
        studentPhoneNoField.setText("+91"); // Default prefix
        configurePhoneNumberField();

        inputPanel.add(studentPhoneNoField, gbc);

        // Table
        studentTableModel = new DefaultTableModel(
                new Object[]{
                        "S. No.",
                        "Name",
                        "Roll Number",  // Moved before Branch/Section
                        "Branch",
                        "Section",
                        "D.O.B",
                        "Gender",
                        "Admission Year",
                        "College",
                        "University",
                        "Phone Number"
                },
                0
        );
        studentTable = new JTable(studentTableModel);
        JScrollPane scrollPane = new JScrollPane(studentTable);

        // Add alternating row colors
        studentTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240)); // Alternating colors
                }
                return c;
            }
        });
        // With this: 26/2
        styleTable(studentTable);

        // Style the table header
        studentTable.getTableHeader().setBackground(new Color(0, 120, 215)); // Blue header
        studentTable.getTableHeader().setForeground(Color.WHITE); // White text
        studentTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14)); // Bold font

        // Set column widths
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(50);   // S. No.
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(150);  // Name
        studentTable.getColumnModel().getColumn(2).setPreferredWidth(100);  // Roll Number
        studentTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Branch
        studentTable.getColumnModel().getColumn(4).setPreferredWidth(80);   // Section
        studentTable.getColumnModel().getColumn(5).setPreferredWidth(50); // DOB column
        studentTable.getColumnModel().getColumn(6).setPreferredWidth(80); // Gender column
        studentTable.getColumnModel().getColumn(7).setPreferredWidth(100); // Admission Year column
        studentTable.getColumnModel().getColumn(8).setPreferredWidth(150); // College column
        studentTable.getColumnModel().getColumn(9).setPreferredWidth(150); // University column
        studentTable.getColumnModel().getColumn(10).setPreferredWidth(100);

        // Set row height
        studentTable.setRowHeight(25);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }


    private JPanel createCoursePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        // Input Fields
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add padding
        gbc.anchor = GridBagConstraints.WEST; // Align components to the left

        // Search Bar (Top Row)
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(new JLabel("Search:"), gbc);

        gbc.gridx = 1;
        JTextField searchField = new JTextField(30);
        inputPanel.add(searchField, gbc);

        // Search Button (Centered between Course Name and Description)
        gbc.gridx = 2;
        gbc.gridy = 0;
        JButton searchButton = new JButton("Search");
        searchButton.addActionListener(e -> filterCourseTable(searchField.getText()));
        inputPanel.add(searchButton, gbc);

        // Course Name and Description (Second Row)
        gbc.gridx = 0;
        gbc.gridy = 1;
        inputPanel.add(new JLabel("Course Name:"), gbc);

        gbc.gridx = 1;
        courseNameField = new JTextField(30);
        inputPanel.add(courseNameField, gbc);

        gbc.gridx = 2;
        inputPanel.add(new JLabel("Description:"), gbc);

        gbc.gridx = 3;
        descriptionField = new JTextField(30);
        inputPanel.add(descriptionField, gbc);

        // Table
        courseTableModel = new DefaultTableModel(new Object[]{"S. No.", "Course Name", "Description"}, 0);
        courseTable = new JTable(courseTableModel);
        JScrollPane scrollPane = new JScrollPane(courseTable);

        // Add alternating row colors
        courseTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240)); // Alternating colors
                }
                return c;
            }
        });
        // With this: 26/2
        styleTable(studentTable);

        // Style the table header
        courseTable.getTableHeader().setBackground(new Color(0, 120, 215)); // Blue header
        courseTable.getTableHeader().setForeground(Color.WHITE); // White text
        courseTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14)); // Bold font

        // Set column widths
        courseTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID column
        courseTable.getColumnModel().getColumn(1).setPreferredWidth(150); // Course Name column
        courseTable.getColumnModel().getColumn(2).setPreferredWidth(200); // Description column

        // Set row height
        courseTable.setRowHeight(25);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    //--------------------- User Tab ---------------------
    private JPanel createUserPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 5, 5));
        userNameField = new JTextField();
        passwordField = new JPasswordField(); // Added
        userRoleField = new JTextField();

        inputPanel.add(new JLabel("Username:"));
        inputPanel.add(userNameField);
        inputPanel.add(new JLabel("Password:")); // Added
        inputPanel.add(passwordField);
        inputPanel.add(new JLabel("Role:"));
        inputPanel.add(userRoleField);

        userTableModel = new DefaultTableModel(new Object[]{"S. No.", "Username", "Role"}, 0);
        userTable = new JTable(userTableModel);
        JScrollPane scrollPane = new JScrollPane(userTable);

        // Add alternating row colors
        userTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240)); // Alternating colors
                }
                return c;
            }
        });
        // With this: 26/2
        styleTable(studentTable);

        // Style the table header
        userTable.getTableHeader().setBackground(new Color(0, 120, 215)); // Blue header
        userTable.getTableHeader().setForeground(Color.WHITE); // White text
        userTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14)); // Bold font

        // Add column widths HERE
        userTable.getColumnModel().getColumn(0).setPreferredWidth(50); // ID column
        userTable.getColumnModel().getColumn(1).setPreferredWidth(150); // UserName column
        userTable.getColumnModel().getColumn(2).setPreferredWidth(50); // Role column

        // Set row height
        userTable.setRowHeight(25);

        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    //--------------------- Global Buttons ---------------------
    private JPanel createGlobalButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton refreshButton = new JButton("Refresh");

        // Add Export Button
        JButton exportButton = new JButton("Export to CSV");
        exportButton.addActionListener(e -> exportToCSV());

        // Add Chart Button
        JButton chartButton = new JButton("Generate Chart");
        chartButton.addActionListener(e -> generateChart());

        // Add Action Listeners
        addButton.addActionListener(e -> handleAdd());
        updateButton.addActionListener(e -> handleUpdate());
        deleteButton.addActionListener(e -> handleDelete());
        refreshButton.addActionListener(e -> refreshAllTables());

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        buttonPanel.add(exportButton); // Add export button
        buttonPanel.add(chartButton); // Add Chart Button


        return buttonPanel;
    }

    //--------------------- Core Logic ---------------------
    private void handleAdd() {
        int tabIndex = tabbedPane.getSelectedIndex();
        try {
            switch (tabIndex) {
                case 0: addStudent(); break;
                case 1: addCourse(); break;
                case 2: addUser(); break;
            }
        } catch (SQLException ex) {
            showError("Database Error: " + ex.getMessage());
        }
    }

    private void addStudent() throws SQLException {
        if (!validateStudentFields()) return;

        Student student = new Student();

        // Add this validation check before saving
        String phone = studentPhoneNoField.getText();
        if (!isValidPhoneNumber(phone)) {
            showError("Invalid phone number! Must be +91 followed by 10 digits.");
            return;
        }

        // Set all student fields first
        student.setName(studentNameField.getText());
        student.setBirthDate(dateOfBirthPicker.getDate());
        student.setGender((String) studentGenderField.getSelectedItem());
        student.setSection((String) sectionComboBox.getSelectedItem());

        String branch = branchComboBoxForRoll.getSelectedItem().toString();
        student.setBranch_name(branch);

        student.setRoll_number(generateRollNumber()); // Auto-generated roll number
        student.setAdmission_year(Integer.parseInt(studentAdmissionYearField.getText()));
        student.setCollege_name(studentCollegeField.getText());
        student.setUniversity_name(studentUniversityField.getText());
        student.setPhone_no(studentPhoneNoField.getText());

        // Single database call
        studentDAO.addStudent(student);

        // Update branch combo box
        DefaultComboBoxModel<String> model = (DefaultComboBoxModel<String>) branchComboBox.getModel();
        if (model.getIndexOf(branch) == -1) {  // Correct check using getIndexOf()
            model.addElement(branch);
        }
        clearStudentFields();
        displayStudents();
        showSuccess("Student added!");
    }

    private void addCourse() throws SQLException {
        if (courseNameField.getText().isEmpty() || descriptionField.getText().isEmpty()) {
            showError("Course fields cannot be empty!");
            return;
        }

        Course course = new Course();
        course.setCourseName(courseNameField.getText());
        course.setDescription(descriptionField.getText());
        courseDAO.addCourse(course);
        clearCourseFields();
        displayCourses();
        showSuccess("Course added!");
    }

    private void addUser() throws SQLException {
        String password = new String(passwordField.getPassword()); // Get password
        if (userNameField.getText().isEmpty() || password.isEmpty() || userRoleField.getText().isEmpty()) {
            showError("User fields cannot be empty!");
            return;
        }

        User user = new User();
        user.setUserName(userNameField.getText());
        user.setPassword(password); // Set password
        user.setUserRole(userRoleField.getText());
        userDAO.addUser(user);
        clearUserFields();
        displayUsers();
        showSuccess("User added!");
    }

    private void handleUpdate() {
        int tabIndex = tabbedPane.getSelectedIndex();
        try {
            switch (tabIndex) {
                case 0: updateStudent(); break;
                case 1: updateCourse(); break;
                case 2: updateUser(); break;
            }
        } catch (SQLException ex) {
            showError("Update failed: " + ex.getMessage());
        }
    }

    private void updateStudent() throws SQLException {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a student to update!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Retrieve the roll_number from the selected row
        String originalRollNumber = (String) studentTableModel.getValueAt(selectedRow, 2); // Column 5: Roll Number// Assuming roll_number is in column 1
        System.out.println("Selected Roll Number: " + originalRollNumber); // Debug statement

        // Fetch the student by roll_number
        Student student = studentDAO.getStudentByRollNumber(originalRollNumber);
        if (student != null) {
            // Open the edit dialog
            EditStudentDialog dialog = new EditStudentDialog(this, student);
            dialog.setVisible(true);

            // Refresh the table after updating
            displayStudents();
        } else {
            JOptionPane.showMessageDialog(this, "Student not found with Roll Number: " + originalRollNumber, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCourse() throws SQLException {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a course to update!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Retrieve the course name from the selected row (column 1)
        String courseName = (String) courseTableModel.getValueAt(selectedRow, 1);

        // Fetch the course by name
        Course course = courseDAO.getCourseByName(courseName);
        if (course != null) {
            // Open the edit dialog
            EditCourseDialog dialog = new EditCourseDialog(this, course);
            dialog.setVisible(true); // This blocks until the dialog is closed

            // Refresh the table after the dialog closes
            displayCourses();
        } else {
            JOptionPane.showMessageDialog(this, "Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateUser() throws SQLException {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a user to update!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String userName = (String) userTableModel.getValueAt(selectedRow, 1);
        User user = userDAO.getUserByUsername(userName);

        // Open edit dialog
        EditUserDialog dialog = new EditUserDialog(this, user);
        dialog.setVisible(true);
        displayUsers(); // Refresh table
    }

    private void handleDelete() {
        int tabIndex = tabbedPane.getSelectedIndex();
        try {
            switch (tabIndex) {
                case 0: deleteStudent(); break;
                case 1: deleteCourse(); break;
                case 2: deleteUser(); break;
            }
        } catch (SQLException ex) {
            showError("Delete failed: " + ex.getMessage());
        }
    }

    // Method to delete a student
    private void deleteStudent() throws SQLException {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Select a student to delete!");
            return;
        }

        String rollNumber = (String) studentTableModel.getValueAt(selectedRow, 2); // Roll Number is at column 5
        studentDAO.deleteStudent(rollNumber); // Pass rollNumber as a String
        displayStudents(); // Refresh the table
        showSuccess("Student deleted!");
    }
    private void renumberStudentIds() {
        for (int i = 0; i < studentTableModel.getRowCount(); i++) {
            studentTableModel.setValueAt(i + 1, i, 0); // Set S. No. starting from 1
        }
    }

    // Method to delete a course
    private void deleteCourse() throws SQLException {
        int selectedRow = courseTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Select a course to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String courseName = (String) courseTableModel.getValueAt(selectedRow, 1); // Column 1: Course Name
        courseDAO.deleteCourse(courseName);
        displayCourses(); // Refresh the table to update S. No.
    }

    // Method to delete a user
    private void deleteUser() throws SQLException {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            showError("Select a user to delete!");
            return;
        }
        String username = (String) userTableModel.getValueAt(selectedRow, 1);
        userDAO.deleteUser(username);
        displayUsers();
        showSuccess("User deleted!");
    }


    //--------------------- Helpers ---------------------
    private void loadDataOnStartup() {
        try {
            displayStudents();
            displayCourses();
            displayUsers();
        } catch (SQLException ex) {
            showError("Failed to load data: " + ex.getMessage());
        }
    }

    private void displayStudents() throws SQLException {
        List<Student> students = studentDAO.getAllStudents();
        studentTableModel.setRowCount(0); // Clear the table
        for (Student s : students) {
            studentTableModel.addRow(new Object[]{
                    s.getSerialNumber(),          // S. No.
                    s.getName(),                  // Name
                    s.getRoll_number(),           // Roll Number (now 3rd column)
                    s.getBranch_name(),           // Branch
                    s.getSection(),               // Section
                    (s.getBirthDate() != null) ? s.getBirthDate().format(DateTimeFormatter.ISO_DATE) : "-", // D.O.B
                    s.getGender(),                // Gender
                    s.getAdmission_year(),        // Admission Year
                    s.getCollege_name(),          // College
                    s.getUniversity_name(),        // University
                    s.getPhone_no()
            });
        }
    }

    private void displayCourses() throws SQLException {
        List<Course> courses = courseDAO.getAllCourses();
        courseTableModel.setRowCount(0); // Clear existing data
        for (Course course : courses) {
            courseTableModel.addRow(new Object[]{
                    course.getSerialNumber(), // Dynamically generated S. No.
                    course.getCourseName(),
                    course.getDescription()
            });
        }
    }

    private void displayUsers() throws SQLException {
        userTableModel.setRowCount(0);
        List<User> users = userDAO.getAllUsers();
        for (User u : users) {
            userTableModel.addRow(new Object[]{u.getSerialNumber(), u.getUserName(), u.getUserRole()});
        }
    }

    private void refreshAllTables() {
        try {
            displayStudents();
            displayCourses();
            displayUsers();
        } catch (SQLException ex) {
            showError("Refresh failed: " + ex.getMessage());
        }
    }
    private void exportToCSV() {
        JTable table = null;
        int tabIndex = tabbedPane.getSelectedIndex();
        switch (tabIndex) {
            case 0: table = studentTable; break;
            case 1: table = courseTable; break;
            case 2: table = userTable; break;
        }

        if (table == null) return;

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file to save");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(fileToSave)) {
                // Write headers
                for (int i = 0; i < table.getColumnCount(); i++) {
                    writer.write(table.getColumnName(i) + ",");
                }
                writer.write("\n");

                // Write data
                for (int i = 0; i < table.getRowCount(); i++) {
                    for (int j = 0; j < table.getColumnCount(); j++) {
                        writer.write(table.getValueAt(i, j).toString() + ",");
                    }
                    writer.write("\n");
                }

                JOptionPane.showMessageDialog(this, "Data exported to " + fileToSave.getName(), "Success", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error exporting data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    // Adding filters

    private void filterTable(JTable table, String query) {
        DefaultTableModel model = (DefaultTableModel) table.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);
        sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query)); // Case-insensitive search
    }
    private void filterStudentTable(String query) {
        filterTable(studentTable, query);
    }
    private void filterCourseTable(String query) {
        filterTable(courseTable, query);
    }
    private void filterUserTable(String query) {
        filterTable(userTable, query);
    }


    private void generateChart() {
        // Create a dataset
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Students", studentTable.getRowCount());
        dataset.setValue("Courses", courseTable.getRowCount());
        dataset.setValue("Users", userTable.getRowCount());

        // Create a pie chart
        JFreeChart chart = ChartFactory.createPieChart(
                "Record Statistics", // Chart title
                dataset,            // Dataset
                true,               // Include legend
                true,               // Include tooltips
                false               // Exclude URLs
        );

        // Display the chart in a frame
        ChartFrame frame = new ChartFrame("Chart", chart);
        frame.pack();
        frame.setVisible(true);
    }

    private void clearCourseFields() {
        courseNameField.setText("");
        descriptionField.setText("");
    }

    private void clearUserFields() {
        userNameField.setText("");
        userRoleField.setText("");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    private void styleTable(JTable table) {
        // Add alternating row colors
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(240, 240, 240)); // Alternating colors
                }
                return c;
            }
        });

        // Style the table header
        table.getTableHeader().setBackground(new Color(0, 120, 215)); // Blue header
        table.getTableHeader().setForeground(Color.WHITE); // White text
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14)); // Bold font

        // Set row height
        table.setRowHeight(25);
    }
    // New 26/2
    private boolean validateStudentFields() {
        if (studentNameField.getText().isEmpty()) {
            showError("Name field is required!");
            return false;
        }
        if (sectionComboBox.getSelectedItem() == null) {
            showError("Section selection is required!");
            return false;
        }
        if (branchComboBoxForRoll.getSelectedItem() == null ||  // ✅ Correct combo box
                branchComboBoxForRoll.getSelectedItem().toString().length() != 2) {
            showError("Branch must be 2 characters!");
            return false;
        }
        if (studentAdmissionYearField.getText().isEmpty() || !studentAdmissionYearField.getText().matches("\\d+")) {
            showError("Admission Year must be a valid number!");
            return false;
        }
        return true;
    }
            private boolean isValidPhoneNumber(String phoneNumber) {
                return phoneNumber.matches("^\\+91\\d{10}$"); // +91 followed by 10 digits
            }
    private String generateRollNumber() {
        String branch = branchComboBoxForRoll.getSelectedItem().toString();
        int year = (Integer) admissionYearSpinner.getValue();
        int sequence = (Integer) sequenceSpinner.getValue();
        return String.format("0905%s%02d%04d", branch, year, sequence);
    }

    private void clearStudentFields() {
        studentNameField.setText("");
        dateOfBirthPicker.clear();
        studentGenderField.setSelectedIndex(0);
        sectionComboBox.setSelectedIndex(0);
        branchComboBoxForRoll.setSelectedIndex(0); // Update branch combo box for roll
        admissionYearSpinner.setValue(22);
        sequenceSpinner.setValue(1001);
        studentAdmissionYearField.setText(""); // Clear admission year field
        studentCollegeField.setText("ITM Gwalior");
        studentUniversityField.setText("RGPV Bhopal");
        studentPhoneNoField.setText("+91 ");
    }
}