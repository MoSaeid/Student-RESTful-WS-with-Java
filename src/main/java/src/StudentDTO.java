// StudentDTO.java

package src;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="student")
public class StudentDTO {
    
   private int id;
   @XmlElement(name="Student Name")
   private String name;
   private double gpa;

   public StudentDTO() {
   }

   public StudentDTO(int id, String name, double gpa) {
      this.id = id;
      this.name = name;
      this.gpa = gpa;
   }

   public int getId() {
      return id;
   }

   public void setId(int id) {
      this.id = id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public double getGpa() {
      return gpa;
   }

   public void setGpa(double gpa) {
      this.gpa = gpa;
   }
}