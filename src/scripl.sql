SELECT OrderDate,OrderTime,sum(Cost),CustId as OrderId from Orders group by OrderDate,OrderId order by OrderId desc;

SELECT o.OrderDate, o.sum(Cost), o.CustId, od.ItemCode, od.OrderQty
FROM  Orders o INNER JOIN orderdetail od ON r.blId = b.blId INNER JOIN Donor d ON d.blId = b.blId INNER JOIN `Donate Detail` dn ON r.name = r.name;


SELECT o.OrderDate, o.sum(Cost), o.CustId, od.ItemCode, od.OrderQty as OrderId
FROM Orders o LEFT JOIN orderdetail od ON o.OrderId=od.OrderId group by OrderDate,OrderId order by OrderId desc;

SELECT o.orderDate,o.CustId,SUM(o.Cost),od.itemCode,od.OrderQty FROM orders o FULL OUTER JOIN orderdetail od ON o.OrderId = od.OrderId;

SELECT o.orderDate,o.CustId,SUM(o.Cost),od.itemCode,od.OrderQty FROM orders o LEFT JOIN orderdetail od ON o.OrderId = od.OrderId UNION ALL SELECT o.orderDate,o.CustId,SUM(o.Cost),od.itemCode,od.OrderQty FROM orders RIGHT JOIN orderdetail ON o.OrderId = od.OrderId;

SELECT * FROM rack LEFT JOIN `store detail` ON rack.rId = `store detail`.rId UNION ALL SELECT * FROM rack RIGHT JOIN `store detail` ON rack.rId = `store detail`.rId

SELECT o.orderDate,o.CustId,SUM(o.Cost),od.itemCode,od.OrderQty FROM orders o LEFT JOIN orderdetail od ON o.OrderId = od.OrderId
UNION ALL
SELECT o.orderDate,o.CustId,SUM(o.Cost),od.itemCode,od.OrderQty FROM orders o RIGHT JOIN orderdetail od ON o.OrderId = od.OrderId

SELECT * FROM orderdetail ORDER BY OrderQty ASC LIMIT 1 && OrderQty DESC LIMIT 1;

SELECT o.OrderId,o.OrderDate,o.CustId,o.Cost,od.ItemCode,od.OrderQty,od.Discount FROM orders o INNER JOIN orderdetail od ON o.OrderId = od.OrderId

SELECT o.OrderId,o.OrderDate,o.CustId,o.Cost,od.ItemCode,od.OrderQty,od.Discount FROM orders o INNER JOIN orderdetail od ON o.OrderId = od.OrderId WHERE o.OrderId='OD007';

SELECT o.OrderId,o.OrderDate,o.CustId,od.OrderId,od.ItemCode,od.OrderQty FROM Orders o INNER JOIN orderdetail od ON o.OrderId=od.OrderId WHERE o.OrderId=?