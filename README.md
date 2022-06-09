# My_Projects

CREATE DATABASE IF NOT EXISTS wholesale_market;
USE wholesale_market;

CREATE TABLE IF NOT EXISTS customer(
	CustId VARCHAR(6) NOT NULL,
	CustName VARCHAR(30) NOT NULL,
	CustAddress VARCHAR(30),
	City VARCHAR(20),
	Province VARCHAR(20),
	PostalCode VARCHAR(9) NOT NULL,
	CONSTRAINT PRIMARY KEY(CustId)
);

CREATE TABLE IF NOT EXISTS orders(
	ordId VARCHAR(6) NOT NULL,
	orderDate DATE,
	CustId VARCHAR(6),
	total DECIMAL(12,2);
	CONSTRAINT PRIMARY KEY (ordId),
	CONSTRAINT FOREIGN KEY(CustId) REFERENCES customer(CustId) on Delete Cascade on Update Cascade
);

CREATE TABLE IF NOT EXISTS item(
	ItemCode VARCHAR(6) NOT NULL,
	Description VARCHAR(50),
	PackSize VARCHAR(20),
	UnitPrice DECIMAL(10,2),
	QtyOnHand INT,
	CONSTRAINT PRIMARY KEY(ItemCode)
);

CREATE TABLE IF NOT EXISTS Order_detail(
	OrderId VARCHAR(6) NOT NULL,
	CustId VARCHAR(6) NOT NULL,
	itemCode VARCHAR(6) NOT NULL,
	OrderQty INT,
	total DECIMAL(12,2),
	CONSTRAINT PRIMARY KEY (OrderId, CustId),
	CONSTRAINT FOREIGN KEY(OrderId) REFERENCES orders(OrdId) on Delete Cascade on Update Cascade,
	CONSTRAINT FOREIGN KEY(CustId) REFERENCES customer(CustId) on Delete Cascade on Update Cascade,
	CONSTRAINT FOREIGN KEY(itemCode) REFERENCES item(ItemCode) on Delete Cascade on Update Cascade
);	
