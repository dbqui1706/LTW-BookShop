DAO là viết tắt của "**Data Access Object**" trong lập trình. Nó là một mô hình
thiết kế được sử dụng để tách biệt logic truy cập cơ sở dữ liệu 
(database access logic) khỏi các phần khác của ứng dụng. 
Dưới đây là một số điểm quan trọng về DAO:

1. Mục Đích:
+ DAO được sử dụng để tạo một lớp trung gian giữa ứng dụng và cơ sở dữ liệu. Nó chịu trách nhiệm cho việc truy cập cơ sở dữ liệu và cung cấp một giao diện chung cho các phần khác của ứng dụng.
2. Tách Biệt Logic và Truy Cập Dữ Liệu:
+ DAO giúp tách biệt logic kinh doanh (business logic) khỏi logic truy cập dữ liệu. Các lớp khác trong ứng dụng không cần biết cụ thể về cách dữ liệu được lưu trữ và truy xuất.
3. Thực Hiện Các Thao Tác CRUD:
+ DAO thường cung cấp các phương thức để thực hiện các thao tác CRUD (Create, Read, Update, Delete) trên cơ sở dữ liệu.
4. Bảo Quản Sự Đa Dạng Cơ Sở Dữ Liệu:
+ Khi sử dụng DAO, có thể dễ dàng thay đổi loại cơ sở dữ liệu mà không cần sửa đổi nhiều phần khác của ứng dụng.
5. Phổ Biến Trong Kiến Trúc 3 Lớp:
+ DAO thường được sử dụng trong các kiến trúc 3 lớp, nơi có một lớp trung tâm (business layer) chịu trách nhiệm về logic kinh doanh, một lớp DAO chịu trách nhiệm về truy cập dữ liệu, và giao diện người dùng chịu trách nhiệm hiển thị dữ liệu.
6. Đảm Bảo An Toàn Giao Dịch (Transaction Safety):
+ DAO có thể giúp đảm bảo an toàn cho giao dịch. Nếu có nhiều thao tác cần phải thực hiện một cách an toàn và nguyên vẹn, DAO có thể quản lý cơ sở dữ liệu trong một giao dịch (transaction).
7. Đảm Bảo Bảo Mật Dữ Liệu:
+ DAO có thể cung cấp các biện pháp bảo mật để đảm bảo rằng chỉ những người có quyền mới có thể truy cập và sửa đổi dữ liệu.