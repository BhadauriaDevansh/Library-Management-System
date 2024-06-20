package Controller;

import Model.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.validation.Valid;
import javax.ws.rs.core.Response.Status;

@Path("/library")
public class LibraryResource {
    private BookDatabase bookDatabase = new BookDatabase();
    private LoanDatabase loanDatabase = new LoanDatabase();
    private MemberDatabase memberDatabase = new MemberDatabase();

    @GET
    @Path("/books")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBooks() {
        List<Book> books = bookDatabase.getAllBooks();
        return Response.ok(books).build();
    }

    @GET
    @Path("/loans")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoans() {
        List<Loan> loans = loanDatabase.getAllLoans();
        return Response.ok(loans).build();
    }

    @GET
    @Path("/members")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMembers() {
        List<Member> members = memberDatabase.getAllMembers();
        return Response.ok(members).build();
    }

    @GET
    @Path("/books/available")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Integer> getAvailableBookIds() {
        return bookDatabase.getAvailableBookIds();
    }


    @GET
    @Path("/members/ids")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Integer> getAllMemberIds() {
        return memberDatabase.getAllMemberIds();
    }



    @GET
    @Path("/books/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("id") int id) {
        Book book = bookDatabase.getBookById(id);
        if (book != null) {
            return Response.ok(book).build();
        } else {
            return Response.status(Status.NOT_FOUND).entity("Book not found").build();
        }
    }

    @GET
    @Path("/members/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMemberById(@PathParam("id") int id) {
        Member member = memberDatabase.getMemberById(id);
        if (member != null) {
            return Response.ok(member).build();
        } else {
            return Response.status(Status.NOT_FOUND).entity("Member not found").build();
        }
    }

    @GET
    @Path("/loans/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getLoanById(@PathParam("id") int id) {
        Loan loan = loanDatabase.getLoanById(id);
        if (loan != null) {
            return Response.ok(loan).build();
        } else {
            return Response.status(Status.NOT_FOUND).entity("loan ID not found").build();
        }
    }


    @POST
    @Path("/books")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBook(@Valid Book book) {
        try {
            bookDatabase.addBook(book);
            return Response.ok(book).build();
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            List<String> messages = new ArrayList<>();
            for (ConstraintViolation<?> violation : violations) {
                messages.add(violation.getPropertyPath() + " " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(messages)
                    .build();
        }
    }

    @POST
    @Path("/loans")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addLoan(@Valid Loan loan) {
        try {
            loanDatabase.addLoan(loan);
            return Response.ok(loan).build();
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            List<String> messages = new ArrayList<>();
            for (ConstraintViolation<?> violation : violations) {
                messages.add(violation.getPropertyPath() + " " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(messages)
                    .build();
        }
    }

    @POST
    @Path("/members")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addMember(@Valid Member member) {
        try {
            memberDatabase.addMember(member);
            return Response.ok(member).build();
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            List<String> messages = new ArrayList<>();
            for (ConstraintViolation<?> violation : violations) {
                messages.add(violation.getPropertyPath() + " " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(messages)
                    .build();
        }
    }

    @DELETE
    @Path("/books/{bookId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("bookId") int bookId) {
        bookDatabase.deleteBook(bookId);
        return Response.status(Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/members/{memberId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMember(@PathParam("memberId") int memberId) {
        memberDatabase.deleteMember(memberId);
        return Response.status(Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/loans/{loanId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteLoan(@PathParam("loanId") int loanId) {
        loanDatabase.deleteLoan(loanId);
        return Response.status(Status.NO_CONTENT).build();
    }



    @PUT
    @Path("/books/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("id") int id, @Valid Book book) {
        try {
            book.setBookId(id); // Set the book ID from the path parameter
            bookDatabase.updateBook(book);
            return Response.ok(book).build();
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            List<String> messages = new ArrayList<>();
            for (ConstraintViolation<?> violation : violations) {
                messages.add(violation.getPropertyPath() + " " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(messages)
                    .build();
        }
    }

    @PUT
    @Path("/members/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMember(@PathParam("id") int id, @Valid Member member) {
        try {
            member.setMemberId(id); // Set the book ID from the path parameter
            memberDatabase.updateMember(member);
            return Response.ok(member).build();
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            List<String> messages = new ArrayList<>();
            for (ConstraintViolation<?> violation : violations) {
                messages.add(violation.getPropertyPath() + " " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(messages)
                    .build();
        }
    }

    @PUT
    @Path("/loans/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateLoan(@PathParam("id") int id, @Valid Loan loan) {
        try {
            loan.setLoanId(id); // Set the book ID from the path parameter
            loanDatabase.updateLoan(loan);
            return Response.ok(loan).build();
        } catch (ConstraintViolationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            List<String> messages = new ArrayList<>();
            for (ConstraintViolation<?> violation : violations) {
                messages.add(violation.getPropertyPath() + " " + violation.getMessage());
            }
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(messages)
                    .build();
        }
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response loginUser(Member loginRequest) {
        Member member = memberDatabase.loginUser(loginRequest.getMemberUsername(), loginRequest.getMemberPassword());
        if (member != null) {
            return Response.ok(member).build();
        } else {
            return Response.status(Status.UNAUTHORIZED).entity("Invalid username or password").build();
        }
    }

}

