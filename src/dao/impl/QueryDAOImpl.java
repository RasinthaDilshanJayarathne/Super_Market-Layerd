package dao.impl;

import dao.custom.QueryDAO;
import model.CustomDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class QueryDAOImpl implements QueryDAO {

    @Override
    public ArrayList<CustomDTO> getOrderDetailsFromOrderID(String oid) throws SQLException, ClassNotFoundException {
        ArrayList<CustomDTO> allData= new ArrayList();
        ResultSet rst = CrudUtil.executeQuery("SELECT o.OrderId,o.OrderDate,o.CustId,od.OrderId,od.ItemCode,od.OrderQty FROM Orders o INNER JOIN orderdetail od ON o.OrderId=od.OrderId WHERE o.OrderId=?", oid);
        while (rst.next()) {
            allData.add(new CustomDTO(
                    rst.getString("OrderId"),
                    LocalDate.parse(rst.getString("OrderDate")),
                    rst.getString("CustId"),
                    rst.getString("itemCode"),
                    rst.getInt("qty"),
                    rst.getBigDecimal("unitPrice")));
        }
        return allData;

    }
}
