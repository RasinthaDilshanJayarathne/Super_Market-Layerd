package bo.custom;

import entity.Login;

import java.sql.SQLException;

public interface LoginBO extends SuperBO{
    Login ifUserExists(String userName,String Password) throws SQLException, ClassNotFoundException;
}
