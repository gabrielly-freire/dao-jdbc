package application;

import model.entities.Department;
import model.entities.Seller;

import java.util.Date;

public class Program {
    public static void main(String[] args) {
        Department department = new Department(1, "books");
        System.out.println(department);

        Seller seller = new Seller(1, "Maria", "maria@email.com", new Date(), 3000.0, department);
        System.out.println(seller);
    }
}
