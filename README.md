COSC2353_Bank
=============

COSC2353 Electronic Commerce and Enterprise


Notes:
DB username / password
Username: "dbuser"
Password: "secret"






CREATE TABLE ACMEBANK.employee (
    id_employee INT NOT null PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    firstname varchar(100), 
    lastname varchar(100)
);
	
CREATE TABLE ACMEBANK.customer (
    id_customer INT NOT null PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
    firstname varchar(100), 
    lastname varchar(100),
	dateofbirth date,
	address varchar(128)
);

CREATE TABLE ACMEBANK.savings (
    id_savings INT NOT null PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	id_customer INT NOT null,
    balance INT,
	
	FOREIGN KEY (id_customer) REFERENCES ACMEBANK.customer(id_customer)
);

CREATE TABLE ACMEBANK.banktransaction (
    id_banktransaction INT NOT null PRIMARY KEY GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),
	id_savings INT NOT null,
    amount INT,
	description varchar(256),
	
	FOREIGN KEY (id_savings) REFERENCES ACMEBANK.savings(id_savings)
);
