# SIWeb Java Client
**SIWeb Java Client** is the client side of our brand new SIWeb+ application.

**SIWeb+** is a fully featured and comprehensive student management system for students, lecturers, and administrators. It features a modern, visually appealing UX/UI and an intuitive interface that results in an excellent user experience.

**SIWeb+** also provides students with a centralized location to access all the information and resources they need. With easier access to learning materials and streamlined communication with lecturers, this application is designed to propel students towards success.

## Screenshot
![SIWeb+ Java Client Screenshot](https://static.legendarytechnology.net/doc/others/siwebplus.png)
*<p style="text-align: center;">Screenshot of the administrator area<br><small>(Administrators manage all users, enrollments, semesters, courses, sections etc.)</small></p>*

## Software Development Plan

### Development Planning
Our team involves 4 members. (Ronald, Kevin, Sam, Aaron)

Our target market consists of small to medium-sized educational institutions that currently have outdated or non-functional student management systems. Our main market strategy is Time-to-market. However, we adopted Scrum to increase the overall quality level.

The development plan focused primarily on the student user experiences. The aim is to provide students with a visually appealing UX/UI and an intuitive interface which is used as a centralized location to access all the information and resources they need.

**Estimated costs:**
- Cost of labors: $1000 X 12 months X 4 members = USD 48,000
- Cost of computers: $700 X 4 computers = USD 2,800
- Total cost: USD 50,800 (before official release)
- Basic maintenance costs are expected to be 20% of the licensing Prices in production

**Licensing Prices:**

- Price of basic software (per educational institution per year): USD 10,000
- Administrator accounts (No additional costs)
- Lecturer accounts (per account per year): USD 5
- Student accounts (per account per year): USD 2

Assuming on average, each educational institutions have 100 lecturers and 2000 students (with 20% maintenance costs):
(10000 + 100 X 5 + 2000 X 2) * 0.8 = USD 11,600

To break even in the first year, we will need 4 ~ 5 educational institutions to license our software in the first year.


### Development Process
We adopted **Scrum - an Agile project management and product development framework** to break down the project into smaller, manageable tasks and facilitate iterative progress. This approach enabled the team to deliver a high-quality application more efficiently by focusing on a limited set of features at any given time. Moreover, Agile (Scrum) allows us to prioritize the needs of our stakeholders (students, lecturers, and administrators) from the very beginning of the software development life cycle.


1. **Requirement analysis**
    Product Owner gathers and understands the key requirements from students, lecturers, and administrators. Create a list of features required and put them to the product backlog.

2. **Sprint Planning**
    Select items from the product backlog (work for 3 - 4 weeks) for the upcoming sprint. 

3. **Daily standups**
    Daily standups meetings to update each other on progress and identify any issues.

4. **Sprint Execution**
    Develop the items from the sprint planning.

5. **Sprint Review and retrospective**
    Demonstrate the completed work to students, lecturers, administrators. Gather feedback and discuss with the team for the improvements needed.

6. **Repeat**
    Move on the next sprint with improved processes.

### Members (Roles & Responsibilities)
- **Product Owner** (Ronald, Kevin)
Defines and prioritizes the product backlog, gathering requirements from students, lecturers, administrators.
- **Scrum Master** (Sam, Aaron)
Helps the team to stick with the Scrum practices, solves issues and ensures the team is continuously improving its processes.
- **Development Team** (Ronald, Kevin, Sam, Aaron)
A cross-functional group. Responsible for delivering the Sprint and possibly releasing the incremental changes.

### Requirements / Planned Features / Product Backlog
- ~~REQ - Gather requirements from stakeholders (Feb 2023)~~ **Done**
- ~~S-D - (Server-side) Database design, code structure design, Models setup~~ **Done**
- ~~S-AUTH - (Server-side) JWT authentication and role (students, admin, lecturers) authorization setup~~ **Done**
- ~~S-CRUD - (Server-side) Writing basic CRUD operation endpoints of the models for RESTful HTTP requests from the client-side~~ **Done**
- ~~C-D - (Client-side) Code structure design, Models setup~~ **Done**
- ~~C-AUTH - (Client-side) JWT authentication and role (students, admin, lecturers) authorization setup~~ **Done**
- ~~C-CRUD - (Client-side) Writing HTTP requests for basic CRUD operation of the models~~ **Done**
- ~~DEMO - Testing and Demo release~~ **Done**
- C-SCV - Student calendar view (Q2 2023)
- C-LCV - Lecturer calendar view (Q2 2023)
- C-CONT - Lecturers can upload and share courses content to students (Q2 2023)
- C-ASST - Lecturers can create courses assignments to students with deadlines (Q2 2023)
- C-QUIZ - Lecturers can create quizzes with auto-grading features. (Q3 2023)
- BETA - Testing and Close beta release (Q4 2023)
- VHAN - Visual Enhancements (TBD)
- C-EMAIL - Integrated email client (TBD)
- C-AUTOG - Auto-grades programing assignments (Auto-compile and compare outputs of the test cases) (TBD)
- RELEASE - Official release (Q1 2024)
- C-CHAT - Private and group real time chatting (TBD)
- C-APP - iOS and Android App client with push notifications (TBD)

### <a id="Schedule"></a>Schedule
> A Sprint Cycle contains:
> `Day 1: Sprint Planning`
> `Day 1-25: Development`
> `Day 25-26: Sprint Review`
> `Day 27: Sprint Retrospective`
- **February, 2023 (Sprint #1)** - REQ, S-AUTH, S-D (Ronald, Kevin), S-CRUD (Sam, Aaron)
- **March, 2023 (Sprint #2)** - C-D, C-AUTH (Ronald, Kevin), C-CRUD (Sam, Aaron)
- **April, 2023 (Sprint #3)** - DEMO (Ronald, Kevin, Sam, Aaron)
- **May, 2023 (Sprint #4)** - C-LCV (Ronald, Kevin), C-SCV (Sam, Aaron)
- **June, 2023 (Sprint #5)** - C-CONT (Ronald, Kevin), C-ASST (Sam, Aaron)
- **July, 2023 (Sprint #6)** - C-QUIZ (Ronald, Kevin, Sam, Aaron)
- **August, 2023 (Sprint #7)**
- **September, 2023 (Sprint #8)**
- **October, 2023 (Sprint #9)**
- **Q4 2023 (Sprint #10)** - BETA
- **TBD (Sprint #11)** 
- **Q1 2024 (Sprint #12)** - RELEASE
- **TBD (Sprint #13)**

### Algorithm
- **Requirement**
    - Gathering, analyzing, and documenting the requirements of students, lecturers, and administrators.
    - Will form interviews and surveys to collect information.
- **Design**
    - System and design elements are created on the requirements above.
    - Includes database schema designs and data flow diagrams.
    - Includes the basic structure and modules like user authentication / authorization, course and assignment management.
    - Plan out the product backlog for the upcoming Sprint. (Sprint Planning)
- **Implementation**
    - Actual coding and development of the application (Sprint Execution).
    - Use version systems Git to collaborate and track changes to the codebase.
- **Test / Validation**
    - Fix issues and bugs. Mainly test the newly added functionality for this Sprint.
- **Deployment**
    - Deploy the code and update the application to the production environment.
    - Review the improvement needed to the Sprint process and apply the improvement. (Sprint Review and retrospective)
    - Start another round of Sprint.
- **Maintenance**
    - After each deployment. Closely monitors the bugs and errors, and addresses user feedback.

### Current Status
- **Finished three sprints and released the demo**
- **Overall process:**
    - 25% done (updated on 2023-04-16)

### Future Plan
- Launches the beta version of SIWeb+ in Q4 2023.
- Official releases in Q1 2024.

## Demo
- Demonstration video: https://youtu.be/Dg64YjcQk2M (updated on 2023-04-16)

## Environments
- Programming language: Java 19 or above
- Required package: JavaFX 19 or above
- Operation System: 
    - Windows 10 or newer
    - Mac OS X Version 10.7.3 (Lion) or newer
    - Linux (Ubuntu 8.04 LTE or newer)
> Java 19 and JavaFX 19 (or above) are required to run the application but may be bundled in the distribution if allowed.

[JavaFX Release notes](https://github.com/openjdk/jfx/tree/jfx20/doc-files)

## Declaration
- 3rd Party Libraries:
    - [JavaFX](https://openjfx.io/)
    - [MaterialFX](https://github.com/palexdev/MaterialFX) by Alessadro Parisi
    - [JSON (org.json)](https://github.com/stleary/JSON-java) by Sean Leary
>
- Sample University logo & background:
    - [Sample University logo (shutterstock)](https://www.shutterstock.com/image-vector/university-academy-school-course-logo-design-1594746943)
    - [Sample University background (shutterstock)](https://www.shutterstock.com/image-photo/college-campus-spring-131270519)

