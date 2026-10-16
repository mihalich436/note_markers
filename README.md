# Lazy DM Notes 🎲

Lazy DM Notes is a full-stack web application designed for tabletop RPG Game Masters (DMs). It provides a real-time collaborative platform where masters can manage game sessions, interact with shared maps, and synchronize notes instantly across multiple connected clients.

**Live Demo:** [lazydmnotes.app](https://lazydmnotes.app)  

---

## 🚀 Key Features

* **Real-Time Synchronization:** Multi-user collaborative workspace powered by WebSockets. Any note update or map marking instantly propagates to all active clients.
* **Interactive Map Viewer:** Load, view, and place markers on campaign maps seamlessly.
* **Server-Side Persistence:** Securely saves all session notes, room states, and user data to a relational database.
* **Modern Architecture:** Clean separation of concerns with a decoupled frontend and a robust Java Spring Boot backend.
* **Production Ready:** Configured for deployment on a Linux VPS behind an Nginx reverse proxy with SSL termination.

---

## 🛠 Tech Stack

### Backend
* **Core:** Java 17+, Spring Boot (Web, WebSockets)
* **Data Access:** Spring Data JPA, Hibernate ORM, JDBC
* **Database:** MySQL / PostgreSQL
* **Testing:** JUnit, Mockito

### Frontend
* **Core:** HTML5, CSS3, JavaScript (ES6+)
* **Styling:** Custom responsive design for desktop session management
* **Hosting:** GitHub Pages / Static hosting

### DevOps & Tools
* **Web Server:** Nginx (Reverse Proxy & Load Balancing)
* **Environment:** Linux (Ubuntu VPS), Git

---

## 📦 System Architecture & Data Flow

1. **Client Interaction:** Users open the application via their web browser.
2. **WebSocket Handshake:** The frontend initiates a fallback-supported WebSocket connection to the Spring Boot server.
3. **State Management:** When a GM updates notes or maps, an event payload is sent via WebSockets.
4. **Broadcast & Persistence:** The backend processes the message, updates the MySQL/PostgreSQL database via Hibernate, and broadcasts the state change to all other active session listeners.
5. **Nginx Layer:** Nginx safely intercepts incoming HTTP/WS traffic on port 80/443, handling SSL, and routes requests to the Spring Boot application running on internal port 8080.
