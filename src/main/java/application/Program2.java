package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;

public class Program2 {
    public static void main(String[] args) {

        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("=== TEST 1: department findbyId ===");
        Department department = departmentDao.findById(1);
        System.out.println(department);

        System.out.println("\n=== TEST 2 department findAll ===");
        List<Department> departments = departmentDao.findAll();
        for (Department d : departments)
            System.out.println(d);

        System.out.println("\n=== TEST 3: department insert ===");
        department = new Department(null, "TI");
        departmentDao.insert(department);
        System.out.println("Inserted! New id = " + department.getId());

        System.out.println("\n=== TEST 4: department update ===");
        department.setName("food");
        departmentDao.update(department);
        System.out.println("Update completed");

        System.out.println("\n=== TEST 5: department delete ===");
        departmentDao.deleteById(5);
        System.out.println("Delete completed");

    }
}
