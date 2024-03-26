// File: StudentResource.java
package src;

import jakarta.annotation.security.RolesAllowed;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import jakarta.inject.Singleton;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Singleton
@Path("students")
public class StudentResource {
   private final String DB_URL = "jdbc:mysql://localhost:3306/studenttest?useSSL=false";
   private final String DB_USER = "root";
   private final String DB_PASSWORD = "";

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   public Response getAllStudents() {
      try {
         Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM students");

         List<StudentDTO> studentList = new ArrayList<>();
         while (rs.next()) {
            int id = rs.getInt("id");
            String name = rs.getString("name");
            double gpa = rs.getDouble("gpa");

            StudentDTO student = new StudentDTO(id, name, gpa);
            studentList.add(student);
         }

         conn.close();
         return Response.ok(studentList).build();
      } catch (SQLException e) {
          System.out.println(e);
         return Response.serverError().entity("Error retrieving students").build();
      }
   }

   @GET
   @Path("{id}")
//@RolesAllowed("admin")
   @Produces(MediaType.APPLICATION_JSON)
   public Response getStudentById(@PathParam("id") int id) {
      try {
         Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM students WHERE id = ?");
         stmt.setInt(1, id);
         ResultSet rs = stmt.executeQuery();

         if (rs.next()) {
            String name = rs.getString("name");
            double gpa = rs.getDouble("gpa");

            StudentDTO student = new StudentDTO(id, name, gpa);
            conn.close();
            return Response.ok(student).build();
         } else {
            conn.close();
            return Response.status(Response.Status.NOT_FOUND).entity("Student not found").build();
         }
      } catch (SQLException e) {
         return Response.serverError().entity("Error retrieving student").build();
      }
   }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   public Response addStudent(StudentDTO student) {
      try {
         Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement stmt = conn.prepareStatement("INSERT INTO students (id, name, gpa) VALUES (?, ?, ?)");
         stmt.setInt(1, student.getId());
         stmt.setString(2, student.getName());
         stmt.setDouble(3, student.getGpa());
         stmt.executeUpdate();

         conn.close();
         return Response.status(Response.Status.CREATED).entity("Student added successfully").build();
      } catch (SQLException e) {
         return Response.serverError().entity("Error adding student").build();
      }
   }

   @PUT
   @Path("{id}")
   @Consumes(MediaType.APPLICATION_JSON)
   public Response updateStudent(@PathParam("id") int id, StudentDTO student) {
      try {
         Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement stmt = conn.prepareStatement("UPDATE students SET name = ?, gpa = ? WHERE id = ?");
         stmt.setString(1, student.getName());
         stmt.setDouble(2, student.getGpa());
         stmt.setInt(3, id);
         int rowsUpdated = stmt.executeUpdate();

         if (rowsUpdated > 0) {
            conn.close();
            return Response.ok("Student updated successfully").build();
         } else {
            conn.close();
            return Response.status(Response.Status.NOT_FOUND).entity("Student not found").build();
         }
      } catch (SQLException e) {
         return Response.serverError().entity("Error updating student").build();
      }
   }

   @DELETE
   @Path("{id}")
   public Response deleteStudent(@PathParam("id") int id) {
      try {
         Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
         PreparedStatement stmt = conn.prepareStatement("DELETE FROM students WHERE id = ?");
         stmt.setInt(1, id);
         int rowsDeleted = stmt.executeUpdate();

         if (rowsDeleted > 0) {
            conn.close();
            return Response.ok("Student deleted successfully").build();
         } else {
            conn.close();
            return Response.status(Response.Status.NOT_FOUND).entity("Student not found").build();
         }
      } catch (SQLException e) {
         return Response.serverError().entity("Error deleting student").build();
      }
   }
}