package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection connection;

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {

    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName "
                            + "FROM seller INNER JOIN department "
                            + "ON seller.DepartmentId = department.Id "
                            + "WHERE seller.Id = ?");

            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()){
                Department department = instantiateDepartment(resultSet);
                Seller seller = instantiateSeller(resultSet, department);
                return seller;
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }
        return null;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Seller> sellers;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "ORDER BY Name");

            resultSet = preparedStatement.executeQuery();

            sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (resultSet.next()) {

                Department department = map.get(resultSet.getInt("DepartmentId"));

                if (department == null) {
                    department = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId"), department);
                }

                Seller seller = instantiateSeller(resultSet, department);
                sellers.add(seller);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }
        return sellers;
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        List<Seller> sellers;
        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "WHERE DepartmentId = ? " +
                            "ORDER BY Name");

            preparedStatement.setInt(1, department.getId());
            resultSet = preparedStatement.executeQuery();

            sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (resultSet.next()) {

                Department department1 = map.get(resultSet.getInt("DepartmentId"));

                if (department1 == null) {
                    department1 = instantiateDepartment(resultSet);
                    map.put(resultSet.getInt("DepartmentId"), department1);
                }

                Seller seller = instantiateSeller(resultSet, department1);
                sellers.add(seller);
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }
        return sellers;
    }

    private Department instantiateDepartment(ResultSet resultSet) throws SQLException {
        Department department = new Department();
        department.setId(resultSet.getInt("DepartmentId"));
        department.setName(resultSet.getString("DepName"));

        return department;
    }

    private Seller instantiateSeller(ResultSet resultSet, Department department) throws SQLException {
        Seller seller = new Seller();
        seller.setId(resultSet.getInt("Id"));
        seller.setName(resultSet.getString("Name"));
        seller.setEmail(resultSet.getString("Email"));
        seller.setBaseSalary(resultSet.getDouble("BaseSalary"));
        seller.setBirthDate(resultSet.getDate("BirthDate"));
        seller.setDepartment(department);

        return seller;
    }
}
