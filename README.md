# 🎓 Student Record Management System

A Java-based desktop application to manage student records efficiently. Developed as part of my B.Tech (IT) academic project, this application features a user-friendly GUI to add, update, delete, and export student data securely using MySQL as the backend.

🛠️ Features

- ➕ Add, ✏️ update, 🗑 delete student records  
- 📤 Export student data to CSV  
- 💻 Java Swing GUI (AWT/Swing)  
- 🔐 Secure MySQL integration with JDBC  
- 📦 Packaged as EXE & JAR (Windows-compatible)  
- ⚡ Lightweight and easy to use  

## 📁 Project Structure

Student_Record_Management/
├── backend/                 → Java code with DB connectivity  
├── ui/                      → Java Swing GUI  
├── lib/                     → External libraries (e.g., MySQL Connector)  
│   └── mysql-connector-java-8.x.x.jar  
├── dist/                    → Compiled JAR or EXE files  
│   ├── StudentManagement.jar  
│   └── StudentManagement.exe  
├── student_db_setup.sql     → MySQL script to set up the DB  
├── README.txt               → Project instructions  
└── app-icon.ico             → Custom app icon  

📦 Tech Stack

- Java (AWT & Swing)  
- MySQL  
- JDBC  
- IntelliJ IDEA / VS Code  
- Maven (for builds)

## ⚙️ Requirements

- ✅ JDK 8 or higher  
- ✅ MySQL Server (offline/local)  
- ✅ MySQL Connector JAR (included in /lib)  
- ✅ Java IDE (optional for source build)

## 🚀 Setup Instructions

1. Clone the Repository
   git clone https://github.com/Anand-Leo/Student-Record-Management.git

2. Open the project in IntelliJ IDEA or VS Code

3. Set Up MySQL Database
   - Run the script student_db_setup.sql using MySQL Workbench or CLI
   - This creates student_db and inserts a default user:
     Username: admin
     Password: admin

4. Add MySQL Connector JAR
   - Go to: Project Structure > Libraries
   - Add: lib/mysql-connector-java-8.x.x.jar

5. Run the App
   - Run Main.java (UI module) from your IDE  
   - Or, use StudentManagement.jar / StudentManagement.exe from dist/

## 📥 Run Without IDE (No Setup Needed)

Want to try it without setting up Java/MySQL manually?  
✅ Just download the ZIP:

Download from Google Drive:
https://drive.google.com/file/d/1CD7xYnH-k2Ybf8VDbW8Yy9u4SlKctZiw/view?usp=sharing

Steps:
1. Go to the Drive link and download the ZIP (Don't worry it's virus free).  
2. Extract the ZIP file.  
3. Open README.txt inside.  
4. Run StudentManagement.exe.  
5. Done! 🎉

## 👨‍💻 Author

Anand Pal  
B.Tech – Information Technology  
- GitHub: https://github.com/Anand-Leo  
- LinkedIn: https://www.linkedin.com/in/anand-pal-it

## ✅ Future Enhancements

- 🔐 Add admin login authentication  
- 👥 Role-based access control (Admin/User)  
- ☁️ Auto-backup of student records  

📢 Feel free to fork ⭐, open issues, or contribute to the project!
