-- Create the books table
CREATE TABLE books (
    bookId INT NOT NULL AUTO_INCREMENT UNIQUE,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL,
    isbn VARCHAR(20) NOT NULL,
    published_date VARCHAR(10) NOT NULL,
    quantity VARCHAR(10) NOT NULL,
    PRIMARY KEY (bookId)
);

-- Create the members table
CREATE TABLE members (
    memberId INT NOT NULL AUTO_INCREMENT UNIQUE,
    memberName VARCHAR(255) NOT NULL,
    memberUsername VARCHAR(255) NOT NULL,
    memberEmail VARCHAR(255) NOT NULL,
    memberPhone INT NOT NULL,
    membership_date VARCHAR(10) NOT NULL,
    memberPassword VARCHAR(255) NOT NULL,
    memberConfirmPassword VARCHAR(255) NOT NULL,
    PRIMARY KEY (memberId)
);

-- Create the loans table
CREATE TABLE loans (
    loanId INT NOT NULL AUTO_INCREMENT UNIQUE,
    bookId INT NOT NULL,
    memberId INT NOT NULL,
    loanDate VARCHAR(10) NOT NULL,
    returnDate VARCHAR(10) NOT NULL,
    PRIMARY KEY (loanId),
    FOREIGN KEY (bookId) REFERENCES books(bookId),
    FOREIGN KEY (memberId) REFERENCES members(memberId)
);
