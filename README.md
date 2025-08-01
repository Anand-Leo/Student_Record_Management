# 🎓 Student Record Management System

A **Java-based desktop application** to manage student records efficiently. This was developed as part of my B.Tech (IT) academic project. It provides an easy-to-use GUI to add, update, delete, and export student data securely using MySQL as the backend.

---

## 🛠️ Features

- Add, update, delete student records
- Export student data to CSV
- Java Swing GUI (AWT/Swing)
- Secure MySQL integration with JDBC
- EXE & JAR packaging support (Windows compatible)
- Lightweight and easy to run on any system

---

## 📁 Project Structure

Student_Record_Management/
├── backend/                 → Java code with DB connectivity  
├── ui/                      → Java Swing GUI  
├── lib/                     → External libraries (e.g. MySQL Connector)  
│   └── mysql-connector-java-8.x.x.jar  
├── dist/                    → Compiled JAR or EXE files  
│   ├── StudentManagement.jar  
│   └── StudentManagement.exe (if needed)  
├── export.csv               → Sample export file  
├── app-icon.ico             → Custom app icon  
└── README.txt               → Project documentation

---

## 📦 Tech Stack

- Java (AWT, Swing)
- MySQL
- JDBC
- IntelliJ IDEA or VS Code
- Maven (optional for builds)

---

## ⚙️ Requirements

- JDK 8 or higher
- MySQL server running
- MySQL JDBC connector (included in /lib)
- Java IDE (IntelliJ or VS Code)

---

## 🚀 Setup Instructions

1. **Clone the Repository:**

   git clone https://github.com/Anand-Leo/Student-Record-Management.git

2. **Open in IntelliJ or your preferred Java IDE.**

3. **Setup MySQL Database:**
   - Create a database and table manually or using your SQL script.
   - Update your DB username/password in the code (if needed).

4. **Add MySQL Connector JAR:**
   - Go to Project Structure > Libraries
   - Add `lib/mysql-connector-java-x.x.x.jar`

5. **Run the Project:**
   - Run `Main.java`
   - Or use the provided `.jar` or `.exe` file from the `/dist` folder.

---

## 👤 Author

**Anand Pal**  
B.Tech – Information Technology  
GitHub: https://github.com/Anand-Leo  
LinkedIn: www.linkedin.com/in/anand-pal-61963a258

---

## ✅ Next Steps

- Add login authentication for admins
- Enable role-based access control
- Create automated backups of student records

---

📢 *Feel free to fork, star, and contribute to this project!*
