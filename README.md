# FindIT --- Lost & Found Management System

CampusFind is a college-focused Lost & Found Management System. It
allows students to report lost items, register found items, search
records, and identify possible matches between lost and found reports.

## Current Implementation

The current application is a **Java Swing desktop application**
connected to **MySQL through JDBC**.

``` text
Java Swing
    ↓
Java Application Logic
    ↓
JDBC
    ↓
MySQL
```

### Main features

-   Register lost items
-   Register found items
-   Automatically generated lost-item IDs
-   Match found items with lost reports using item name and color
-   View lost items with found/not-found status
-   View found items with matched/new-found status
-   Search lost and found records by item name
-   Dashboard counters for lost and found records
-   SQL database persistence

## Technology Stack

### Implemented

-   Java
-   Java Swing
-   JDBC
-   MySQL
-   IntelliJ IDEA

### Planned

-   Spring Boot
-   REST APIs
-   HTML
-   CSS
-   JavaScript

> Spring Boot, REST APIs, and the web frontend are planned next-stage
> work and are not part of the current completed Swing implementation.

## Project Structure

``` text
lostandfound/
│
├── src/
│   ├── DBConnection.java
│   ├── MainFrame.java
│   ├── LostItemForm.java
│   ├── FoundItemForm.java
│   ├── ViewLostItems.java
│   └── ViewFoundItems.java
│
├── lib/
│   └── MySQL JDBC driver / project libraries
│
├── .gitignore
└── README.md
```

## Lost Item

A student can submit:

-   Item name
-   Color
-   Location lost
-   Date lost
-   Student name
-   Department
-   Register number

The ID is generated automatically by the database.

## Found Item

A person who finds an item can submit:

-   Item name
-   Color
-   Location found
-   Date found
-   Founder name
-   Email ID

The system checks the item name and color against existing lost-item
reports. If a match exists, the found item can be linked to that lost
report; otherwise it can be registered as a new found item.

## Search

Both lost-item and found-item views provide search functionality.

For example:

``` text
watch
```

can match:

``` text
Watch
Samsung Watch
Smart Watch
```

## Database

The current application uses:

``` text
Database: lost_found_db
```

Main tables:

``` text
lost_items
found_items
```

Database connectivity is handled by:

``` text
DBConnection.java
```

Example connection URL:

``` text
jdbc:mysql://localhost:3306/lost_found_db
```

Do not commit real MySQL passwords or other credentials to GitHub. Use
environment variables or secure configuration for credentials.

## How to Run

1.  Install Java JDK, IntelliJ IDEA, MySQL Server, and MySQL
    Connector/J.
2.  Create the database:

``` sql
CREATE DATABASE lost_found_db;
```

3.  Create the required `lost_items` and `found_items` tables using the
    project's SQL schema.
4.  Open `DBConnection.java`.
5.  Set your own MySQL username and password.
6.  Make sure the MySQL JDBC driver is added to the IntelliJ project.
7.  Run:

``` text
MainFrame.java
```

## Application Flow

``` text
                    CampusFind
                        |
             +----------+----------+
             |                     |
        Lost Item               Found Item
          Form                    Form
             |                     |
             v                     v
          MySQL              Check Match
                                   |
                         +---------+---------+
                         |                   |
                       Match              No Match
                         |                   |
                         v                   v
                    Matched Found        New Found
                         |                   |
                         +---------+---------+
                                   |
                                   v
                                 MySQL
```

## Team

  Member           Responsibility
  ---------------- -----------------------
  **ROHIT**        Frontend / Java Swing
  **ABHIJAY**      Backend / Java
  **SHUDHANSHU**   JDBC & SQL

## Development Progress

### Completed

-   Requirements and database design
-   MySQL database and tables
-   JDBC connection
-   Lost-item registration
-   Found-item registration
-   Lost/found matching
-   Java Swing dashboard
-   Lost and found table views
-   Search functionality
-   Item status tracking

### Pending / Next

-   Spring Boot backend design and implementation
-   REST API endpoints
-   HTML/CSS web pages
-   JavaScript functionality
-   Web frontend ↔ Spring Boot integration
-   Additional validation and testing
-   Final deployment and documentation

## Planned Full-Stack Architecture

``` text
Web Browser
     |
HTML + CSS + JavaScript
     |
REST API
     |
Spring Boot
     |
JDBC / JPA
     |
MySQL
```

The planned web version will separate the frontend, backend, and
database layers and make the system accessible through a browser.

## Project Goal

The goal of CampusFind is to make campus lost-and-found management
simpler by connecting lost-item reports with found-item reports and
providing a structured way to search and track item status.

------------------------------------------------------------------------

**Project:** CampusFind --- Smart Lost & Found Management System\
**Current Platform:** Java Swing Desktop Application\
**Database:** MySQL
