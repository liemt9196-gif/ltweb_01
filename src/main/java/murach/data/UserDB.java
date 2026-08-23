package murach.data;

import murach.business.User;

public class UserDB {
    public static long insert(User user) {
        // Trong bài lab/ví dụ cơ bản, phương thức này giả lập lưu trữ dữ liệu
        // Sau này khi kết nối cơ sở dữ liệu thật, bạn sẽ viết câu lệnh INSERT SQL ở đây
        System.out.println("User inserted: " + user.getEmail());
        return 1;
    }
}