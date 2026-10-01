package murach.email;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import murach.util.VNPayConfig;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@WebServlet("/vnpay-return")
public class VNPayReturnServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = req.getParameterNames(); params.hasMoreElements();) {
            String fieldName = params.nextElement();
            String fieldValue = req.getParameter(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                fields.put(fieldName, fieldValue);
            }
        }

        String vnp_SecureHash = req.getParameter("vnp_SecureHash");
        fields.remove("vnp_SecureHashType");
        fields.remove("vnp_SecureHash");

        // Sắp xếp lại để tạo chuỗi băm kiểm tra tính toàn vẹn (Checksum)
        List<String> fieldNames = new ArrayList<>(fields.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = fields.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    hashData.append('&');
                }
            }
        }

        String signValue = VNPayConfig.hmacSHA512(VNPayConfig.VNP_HASHSECRET, hashData.toString());

        boolean checkSignature = signValue.equalsIgnoreCase(vnp_SecureHash);
        String vnp_ResponseCode = req.getParameter("vnp_ResponseCode");
        String vnp_TxnRef = req.getParameter("vnp_TxnRef");
        String amount = req.getParameter("vnp_Amount");

        if (checkSignature) {
            if ("00".equals(vnp_ResponseCode)) {
                // Giao dịch thành công
                // TODO: Cập nhật trạng thái đơn hàng trong Database
                req.setAttribute("paymentStatus", "SUCCESS");
                req.setAttribute("message", "Giao dịch thanh toán thành công!");
            } else {
                // Giao dịch thất bại / Khách hủy giao dịch
                req.setAttribute("paymentStatus", "FAILED");
                req.setAttribute("message", "Giao dịch không thành công. Mã lỗi: " + vnp_ResponseCode);
            }
        } else {
            // Chữ ký không hợp lệ (nguy cơ bị can thiệp dữ liệu)
            req.setAttribute("paymentStatus", "INVALID");
            req.setAttribute("message", "Chữ ký không hợp lệ!");
        }

        req.setAttribute("orderId", vnp_TxnRef);
        req.setAttribute("amount", Long.parseLong(amount) / 100);
        req.getRequestDispatcher("/vnpay_return.jsp").forward(req, resp);
    }
}
