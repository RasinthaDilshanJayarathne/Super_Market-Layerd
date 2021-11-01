package dao.custom;

import entity.Login;

import java.sql.SQLException;

public interface LoginDAO extends CrudDAO<Login,String>{
    Login userSearch(String userName,String Password) throws SQLException, ClassNotFoundException;
}
