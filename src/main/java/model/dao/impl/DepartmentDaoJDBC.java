package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDaoJDBC implements DepartmentDao {
    private Connection connection;

    public DepartmentDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement preparedStatement = null;

        try {
            preparedStatement = connection.prepareStatement("INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS);

            preparedStatement.setString(1,department.getName());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0){
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()){
                    int id = resultSet.getInt(1);
                    department.setId(id);
                }
                DB.closeResultSet(resultSet);
            }else{
                throw new DbException("Unexpected error! No rows affected!");
            }
        }catch (SQLException ex){
            throw new DbException(ex.getMessage());
        }finally {
            DB.closeStatement(preparedStatement);
        }

    }

    @Override
    public void update(Department department) {
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = connection.prepareStatement("UPDATE department SET Name = ? WHERE Id = ?");

            preparedStatement.setString(1, department.getName());
            preparedStatement.setInt(2, department.getId());

            preparedStatement.executeUpdate();

        }catch (SQLException ex){
            throw new DbException(ex.getMessage());
        }finally {

        }
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = connection.prepareStatement("DELETE FROM department WHERE Id = ?");

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected == 0){
                throw new DbException("id not found");
            }
        }catch (SQLException ex){
            throw new DbException(ex.getMessage());
        }finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM department WHERE id = ?");

            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                Department department = new Department();
                department.setId(resultSet.getInt("Id"));
                department.setName(resultSet.getString("Name"));
                return department;
            }
        }catch (SQLException ex){
            throw new DbException(ex.getMessage());
        }finally {
            DB.closeResultSet(resultSet);
            DB.closeStatement(preparedStatement);
        }

        return null;
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        List<Department> departments = new ArrayList<>();

        try {
            preparedStatement = connection.prepareStatement("SELECT * FROM department");

            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                Department department = new Department();
                department.setId(resultSet.getInt("Id"));
                department.setName(resultSet.getString("Name"));
                departments.add(department);
            }
        }catch (SQLException ex){
            throw new DbException(ex.getMessage());
        }finally {
            DB.closeResultSet(resultSet);
            DB.closeStatement(preparedStatement);
        }
        return departments;
    }
}
