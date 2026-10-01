<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Kết quả thanh toán VNPay</title>
    <meta charset="utf-8">
</head>
<body style="font-family: Arial, sans-serif; margin: 40px;">
    <h2>Kết quả thanh toán</h2>
    <p><strong>Mã đơn hàng:</strong> ${orderId}</p>
    <p><strong>Số tiền:</strong> ${amount} VNĐ</p>
    <p><strong>Trạng thái:</strong> 
        <span style="color: ${paymentStatus == 'SUCCESS' ? 'green' : 'red'}; font-weight: bold;">
            ${message}
        </span>
    </p>
    <br/>
    <a href="index.jsp">Quay lại trang chủ</a>
</body>
</html>
