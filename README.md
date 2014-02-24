COSC2353_Bank
=============

COSC2353 Electronic Commerce and Enterprise
ACME Bank system
Authors: nanxinglin, peter, yunfei

University final year project, using J2EE framework to develop a Java software project as a team that implements simple banking systems that interact with each other. 

The first system, the savings system, representing a legacy system that uses JDBC and stateful session beans to provide the core functionality and a simple application for employees. This system was designed to 'reinvent the wheel' by not fully utilizing avaliable frameworks, representing a 'legacy' system to contrast against the second system.

The second system, the home loan system, is a more sophisticated project that utilizes more of the frameworks avaliable for Java. It extends the core functionality, implementing the ability for customers to create home loans, implemented using the Java Persistence API rather than dealing with JBDC directly. The system interacts with the legacy' savings system using the Java Message Service and message beans, and provides both a web client for customers implementing using JavaServer Faces and a simple RESTful web service.
