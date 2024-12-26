CREATE TABLE event (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   event_name VARCHAR(200) NOT NULL,
   presenter_name VARCHAR(100) NOT NULL,
   event_location VARCHAR(100) NOT NULL,
   event_date DATETIME NOT NULL,
   apply_start_date DATE NOT NULL,
   apply_end_date DATE NOT NULL,
   max_capacity INT NOT NULL,
   current_applicants INT NOT NULL,
   reg_dt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE event_application (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   user_id BIGINT NOT NULL,
   event_id BIGINT NOT NULL,
   reg_dt DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
   FOREIGN KEY (event_id) REFERENCES event(id)
);

INSERT INTO event (event_name, presenter_name, event_location, event_date, apply_start_date, apply_end_date, max_capacity, current_applicants, reg_dt)
VALUES
    ('Spring Boot Workshop', 'John Doe', 'Seoul', '2024-12-30 10:00:00', '2024-12-01', '2024-12-25', 30, 0, NOW()),
    ('Kubernetes for Beginners', 'Jane Smith', 'Busan', '2025-01-10 14:00:00', '2024-12-05', '2025-01-05', 30, 0, NOW()),
    ('AI in Healthcare', 'Dr. Kim', 'Incheon', '2025-02-20 09:00:00', '2024-12-10', '2025-02-10', 30, 0, NOW());