SELECT OrderDate,OrderTime,sum(Cost),CustId as OrderId from Orders group by OrderDate,OrderId order by OrderId desc;

SELECT o.OrderDate, o.sum(Cost), o.CustId, od.ItemCode, od.OrderQty
FROM  Orders o INNER JOIN orderdetail od ON r.blId = b.blId INNER JOIN Donor d ON d.blId = b.blId INNER JOIN `Donate Detail` dn ON r.name = r.name;


SELECT o.OrderDate, o.sum(Cost), o.CustId, od.ItemCode, od.OrderQty as OrderId
FROM Orders o LEFT JOIN orderdetail od ON o.OrderId=od.OrderId group by OrderDate,OrderId order by OrderId desc;