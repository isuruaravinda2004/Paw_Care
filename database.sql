-- PawCare, MySQL 8+ / MariaDB 10.4+. Fresh installation script.
-- No DROP statements: rerunning against an existing installation stops at the first table.
CREATE DATABASE IF NOT EXISTS pet_grooming_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE pet_grooming_db;
CREATE TABLE users (
 user_id INT PRIMARY KEY AUTO_INCREMENT,
 username VARCHAR(50) NOT NULL UNIQUE,
 password_hash VARCHAR(255) NOT NULL,
 role VARCHAR(30) NOT NULL DEFAULT 'Admin'
) ENGINE=InnoDB;
CREATE TABLE customers (
 customer_id INT PRIMARY KEY AUTO_INCREMENT,
 name VARCHAR(100) NOT NULL,
 phone VARCHAR(10) NOT NULL,
 email VARCHAR(150) NOT NULL,
 address VARCHAR(255) NOT NULL
) ENGINE=InnoDB;
CREATE TABLE pets (
 pet_id INT PRIMARY KEY AUTO_INCREMENT,
 customer_id INT NOT NULL,
 pet_name VARCHAR(80) NOT NULL,
 animal_type VARCHAR(20) NOT NULL,
 breed VARCHAR(80) NOT NULL,
 gender VARCHAR(10) NOT NULL,
 age INT NOT NULL,
 CONSTRAINT chk_pet_age CHECK (age > 0 AND age <= 100),
 UNIQUE KEY pet_owner (pet_id, customer_id),
 FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE RESTRICT
) ENGINE=InnoDB;
CREATE TABLE services (
 service_id INT PRIMARY KEY AUTO_INCREMENT,
 service_name VARCHAR(100) NOT NULL UNIQUE,
 description VARCHAR(255) NOT NULL,
 price DECIMAL(10,2) NOT NULL,
 duration_minutes INT NOT NULL,
 CONSTRAINT chk_service_price CHECK (price > 0),
 CONSTRAINT chk_duration CHECK (duration_minutes BETWEEN 5 AND 480)
) ENGINE=InnoDB;
CREATE TABLE appointments (
 appointment_id INT PRIMARY KEY AUTO_INCREMENT,
 customer_id INT NOT NULL,
 pet_id INT NOT NULL,
 service_id INT NOT NULL,
 appointment_date DATE NOT NULL,
 appointment_time TIME NOT NULL,
 status VARCHAR(20) NOT NULL DEFAULT 'Pending',
 booked_price DECIMAL(10,2) NOT NULL,
 duration_minutes INT NOT NULL,
 CONSTRAINT chk_status CHECK (status IN ('Pending','Confirmed','Completed','Cancelled')),
 CONSTRAINT chk_booked_price CHECK (booked_price > 0),
 CONSTRAINT chk_booked_duration CHECK (duration_minutes BETWEEN 5 AND 480),
 FOREIGN KEY (customer_id) REFERENCES customers(customer_id) ON DELETE RESTRICT,
 FOREIGN KEY (pet_id, customer_id) REFERENCES pets(pet_id, customer_id) ON DELETE RESTRICT,
 FOREIGN KEY (service_id) REFERENCES services(service_id) ON DELETE RESTRICT,
 INDEX ix_appointment_day (appointment_date, appointment_time)
) ENGINE=InnoDB;
CREATE TABLE payments (
 payment_id INT PRIMARY KEY AUTO_INCREMENT,
 appointment_id INT NOT NULL UNIQUE,
 amount DECIMAL(10,2) NOT NULL,
 payment_method VARCHAR(20) NOT NULL,
 payment_date DATE NOT NULL,
 CONSTRAINT chk_amount CHECK (amount > 0),
 CONSTRAINT chk_method CHECK (payment_method IN ('Cash','Card','Bank Transfer')),
 FOREIGN KEY (appointment_id) REFERENCES appointments(appointment_id) ON DELETE RESTRICT,
 INDEX ix_payment_day (payment_date)
) ENGINE=InnoDB;
-- One small infrastructure table serializes booking/payment changes across app instances.
CREATE TABLE system_locks (lock_name VARCHAR(30) PRIMARY KEY) ENGINE=InnoDB;
INSERT INTO system_locks VALUES ('appointments');
INSERT INTO users(username,password_hash,role) VALUES ('admin','pbkdf2$210000$UGF3Q2FyZURlbW9Pbmx5MDE=$8pdLaztLij2T6c49gnRCo6EUlb3pyHDraFFeW/ngSOg=','Admin');
INSERT INTO customers(name,phone,email,address) VALUES
 ('Nimali Perera','0771234567','nimali@example.com','Galle'),
 ('Kasun Silva','0712345678','kasun@example.com','Matara');
INSERT INTO pets(customer_id,pet_name,animal_type,breed,gender,age) VALUES
 (1,'Buddy','Dog','Labrador','Male',3),(1,'Milo','Cat','Persian','Male',2),(2,'Luna','Dog','Poodle','Female',4);
INSERT INTO services(service_name,description,price,duration_minutes) VALUES
 ('Basic Bath','Gentle wash, dry and brush',1500,45),
 ('Hair Cut','Coat trim and styling',2000,60),
 ('Nail Trimming','Careful nail trim',800,20),
 ('Full Grooming','Bath, haircut, nails and ear cleaning',3500,90),
 ('Ear Cleaning','Gentle outer ear cleaning',500,15);
INSERT INTO appointments(customer_id,pet_id,service_id,appointment_date,appointment_time,status,booked_price,duration_minutes) VALUES
 (1,1,1,CURDATE(),'09:00:00','Completed',1500,45),
 (2,3,4,DATE_ADD(CURDATE(),INTERVAL 1 DAY),'10:00:00','Confirmed',3500,90),
 (1,2,3,DATE_ADD(CURDATE(),INTERVAL 1 DAY),'13:00:00','Pending',800,20);
INSERT INTO payments(appointment_id,amount,payment_method,payment_date) VALUES (1,1500,'Cash',CURDATE());
