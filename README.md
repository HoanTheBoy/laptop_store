# Hệ thống Quản lý Cửa hàng Laptop

## Mục lục
1. [Phân công công việc](#phân-công-công-việc)
2. [Giới thiệu](#giới-thiệu)
3. [Kiến trúc Hệ thống](#kiến-trúc-hệ-thống)
4. [Cấu trúc Cơ sở dữ liệu](#cấu-trúc-cơ-sở-dữ-liệu)
5. [Vai trò Người dùng](#vai-trò-người-dùng)
6. [Luồng thực thi](#luồng-thực-thi)
    - [Đăng nhập và Xác thực](#đăng-nhập-và-xác-thực)
    - [Duyệt và Tìm kiếm Sản phẩm](#duyệt-và-tìm-kiếm-sản-phẩm)
    - [Quản lý Giỏ hàng](#quản-lý-giỏ-hàng)
    - [Xử lý Đơn hàng](#xử-lý-đơn-hàng)
    - [Bảo hành và Trả hàng](#bảo-hành-và-trả-hàng)
    - [Quản lý Người dùng](#quản-lý-người-dùng)
    - [Quản lý Kho hàng](#quản-lý-kho-hàng)
    - [Tạo Báo cáo](#tạo-báo-cáo)
7. [Chạy Ứng dụng](#chạy-ứng-dụng)
8. [Yêu cầu Phần mềm](#yêu-cầu-phần-mềm)

## Giới thiệu

Hệ thống Quản lý Cửa hàng Laptop là một ứng dụng console toàn diện được thiết kế để quản lý cửa hàng bán lẻ laptop. Hệ thống hỗ trợ nhiều loại người dùng (quản trị viên và khách hàng thông thường) và cung cấp các chức năng duyệt và mua laptop, quản lý kho hàng, xử lý đơn hàng, xử lý yêu cầu bảo hành, và tạo các báo cáo kinh doanh đa dạng.

## Phân công công việc

1. **Nguyễn Hoàng Dũng (B21DCVT144)** - Nhóm trưởng
   - Trách nhiệm chính:
      + Phát triển Giao diện người dùng trên Console (ConsoleUI) cho toàn bộ hệ thống.
      + Quản lý chung, điều phối, đảm bảo tiến độ và tích hợp dự án.
   - Cụm chức năng: Quản lý Người dùng & Truy cập Hệ thống
      + UC1: Đăng nhập, đăng xuất tài khoản
         + Mô tả: Người dùng sử dụng tài khoản đăng nhập, đăng xuất tài khoản.
      + UC2: Đăng ký tài khoản
         + Mô tả: Tạo mới tài khoản người dùng.
      + UC9: Thay đổi thông tin cá nhân
         + Mô tả: Thay đổi thông tin cá nhân, chỉnh sửa mật khẩu, xem thông tin cá nhân.
   - Lý do: Nhóm use case này tập trung hoàn toàn vào quản lý tài khoản người dùng, từ tạo mới, đăng nhập đến cập nhật thông tin. Đây là nền tảng cốt lõi cho mọi tương tác người dùng. Dũng, với vai trò làm UI, sẽ dễ dàng tích hợp các form và logic này.
2. **Hoàng Tài Anh (B21DCVT056)**
   - Cụm chức năng: Quy trình Mua hàng & Đặt hàng của Khách hàng
      + UC8: Quản lý giỏ hàng
         + Mô tả: Thêm sản phẩm vào giỏ hàng, xem sản phẩm trong giỏ.
      + UC12: Quản lý thanh toán
         + Mô tả: Chọn phương thức thanh toán cho đơn hàng, ghi nhận kết quả thanh toán.
      + UC3: Quản lý đơn hàng (Phần của Khách hàng & Admin)
         + Mô tả: Khách hàng: Tạo đơn hàng (sau khi giỏ hàng và thanh toán hoàn tất), xem lịch sử đơn hàng, theo dõi trạng thái. Admin: Xem và cập nhật trạng thái đơn hàng.
   - Lý do: Đây là luồng nghiệp vụ chính của khách hàng: chọn sản phẩm vào giỏ -> thanh toán -> tạo đơn hàng -> theo dõi đơn hàng. UC3 cũng bao gồm phần Admin xử lý đơn hàng, tạo sự liên kết. Tài Anh sẽ tập trung vào toàn bộ vòng đời của một giao dịch mua sắm.
3. **Nguyễn Công Hoàn (B21DCVT200)**
   - Cụm chức năng: Quản lý Sản phẩm & Nhập Kho (Admin)
      + UC4: Quản lý thuộc tính sản phẩm
         + Mô tả: Quản lý chi tiết thuộc tính sản phẩm (RAM, thương hiệu, màu sắc, CPU,...), thêm, sửa, xóa.
      + UC5: Quản lý sản phẩm
         + Mô tả: Thêm sản phẩm mới, sửa thông tin, xóa sản phẩm. Cung cấp logic cho tìm kiếm, lọc sản phẩm.
      + UC6: Quản lý nhập hàng
         + Mô tả: Thêm mới, chỉnh sửa, xóa, xem thông tin phiếu nhập hàng, cập nhật tồn kho.
   - Lý do: Nhóm use case này tập trung vào việc quản lý danh mục sản phẩm từ các thuộc tính chi tiết đến thông tin sản phẩm tổng thể và quy trình nhập hàng để đảm bảo có hàng trong kho. Các chức năng này hoàn toàn thuộc về Admin và rất liên kết.
4. **Phạm Ngọc Đức (B21DCVT136)**
   - Cụm chức năng: Quản lý Xuất Kho, Dịch vụ Sau Bán & Báo cáo (Admin & User)
      + UC7: Quản lý xuất hàng
         + Mô tả: Thêm mới, chỉnh sửa, xóa, xem thông tin phiếu xuất. (Cập nhật tồn kho khi hàng được bán/xuất).
      + UC10: Quản lý dịch vụ (Bảo hành, Đổi trả)
         + Mô tả: User: Gửi yêu cầu bảo hành/đổi trả. Admin: Xử lý yêu cầu (xác nhận, lập phiếu).
      + UC11: Quản lý doanh thu (Báo cáo)
         + Mô tả: Giám sát và phân tích doanh thu, hiển thị danh sách sản phẩm và tổng doanh thu.
   - Lý do: UC7 liên quan đến việc hàng hóa ra khỏi kho (sau khi đơn hàng được xử lý). UC10 là các dịch vụ hỗ trợ sau khi bán hàng. UC11 là tổng hợp và báo cáo kết quả kinh doanh. Đây là các hoạt động ở giai đoạn sau của vòng đời sản phẩm hoặc tổng kết hoạt động.

## Kiến trúc Hệ thống

Ứng dụng tuân theo mô hình kiến trúc phân lớp với sự phân tách rõ ràng về chức năng:

1. **Lớp Truy cập Dữ liệu (DAO)**: Xử lý tương tác với cơ sở dữ liệu và các thao tác CRUD (Tạo, Đọc, Cập nhật, Xóa)
   - Các giao diện: `AccountDAO`, `LaptopDAO`, `OrderDAO`, `WarrantyRequestDAO`
   - Các lớp triển khai: `AccountDAOImpl`, `LaptopDAOImpl`, `OrderDAOImpl`, `WarrantyRequestDAOImpl`

2. **Lớp Mô hình (Model)**: Đại diện cho các đối tượng nghiệp vụ
   - `Account`: Tài khoản người dùng với vai trò (quản trị viên hoặc người dùng thông thường)
   - `Laptop`: Thông tin sản phẩm bao gồm thông số kỹ thuật và giá cả
   - `CartItem`: Các mặt hàng trong giỏ hàng của người dùng
   - `Order` và `OrderItem`: Đơn hàng của khách hàng và các mục hàng chi tiết
   - `WarrantyRequest`: Yêu cầu bảo hành và trả hàng
   - `ImportReceipt`: Bản ghi nhập kho

3. **Lớp Dịch vụ (Service)**: Chứa logic nghiệp vụ và quy trình làm việc
   - `AccountService`: Xác thực người dùng và quản lý tài khoản
   - `LaptopService`: Quản lý sản phẩm
   - `CartService`: Các thao tác giỏ hàng
   - `OrderService`: Xử lý đơn hàng
   - `WarrantyService`: Xử lý yêu cầu bảo hành

4. **Lớp Giao diện (UI)**: Giao diện người dùng
   - `ConsoleUI`: Triển khai giao diện người dùng dựa trên console

5. **Lớp Tiện ích (Utility)**: Các thành phần hỗ trợ
   - `DBConnection`: Quản lý kết nối cơ sở dữ liệu
   - `DatabaseInitializer`: Thiết lập cơ sở dữ liệu và dữ liệu mẫu

## Cấu trúc Cơ sở dữ liệu

Hệ thống sử dụng cơ sở dữ liệu MySQL với các bảng chính sau:
- `accounts`: Lưu trữ thông tin đăng nhập và thông tin cá nhân người dùng
- `laptops`: Chứa các sản phẩm laptop với thông số kỹ thuật và giá cả
- `orders`: Đơn hàng của khách hàng với trạng thái và thông tin thanh toán
- `order_items`: Các mặt hàng riêng lẻ trong mỗi đơn hàng
- `warranty_requests`: Yêu cầu bảo hành hoặc trả hàng của khách hàng
- `import_receipts`: Bản ghi của các lần nhập kho mới

## Vai trò Người dùng

1. **Quản trị viên (Admin)**
   - Quản lý sản phẩm laptop (thêm, cập nhật, xóa)
   - Quản lý người dùng (xem, thêm, cập nhật, xóa)
   - Xử lý đơn hàng (cập nhật trạng thái)
   - Quản lý kho hàng (xem, cập nhật số lượng tồn kho)
   - Xử lý yêu cầu bảo hành (chấp nhận, từ chối)
   - Tạo báo cáo kinh doanh

2. **Người dùng thông thường**
   - Duyệt và tìm kiếm laptop
   - Thêm laptop vào giỏ hàng
   - Thanh toán và đặt hàng
   - Xem lịch sử đơn hàng
   - Gửi yêu cầu bảo hành/trả hàng
   - Quản lý thông tin cá nhân

## Luồng thực thi

### Đăng nhập và Xác thực

1. Hệ thống bắt đầu tại phương thức `ConsoleUI.start()`
2. Người dùng được hiển thị các tùy chọn menu chính (đăng nhập, đăng ký, duyệt laptop)
3. Quá trình đăng nhập:
   - Người dùng nhập tên đăng nhập và mật khẩu
   - `AccountService.login()` xác thực thông tin đăng nhập
   - Khi đăng nhập thành công, hệ thống tải menu thích hợp (admin hoặc người dùng)
4. Quá trình đăng ký:
   - Người dùng mới cung cấp chi tiết hồ sơ
   - `AccountService.registerAccount()` tạo tài khoản mới
   - Hệ thống xác thực đầu vào và đảm bảo tên người dùng là duy nhất

### Duyệt và Tìm kiếm Sản phẩm

1. Cả khách và người dùng đã đăng nhập đều có thể duyệt laptop
2. `ConsoleUI.browseLaptops()` triển khai chức năng duyệt sản phẩm
3. Người dùng có thể:
   - Xem tất cả laptop
   - Tìm kiếm theo tên/thương hiệu
   - Lọc theo khoảng giá
   - Sắp xếp theo giá
   - Xem thông số kỹ thuật chi tiết
4. Luồng thực thi:
   - `LaptopService.getAllLaptops()` hoặc `LaptopService.searchLaptops()`
   - Kết quả hiển thị trong bảng định dạng
   - Người dùng có thể chọn laptop để xem chi tiết hoặc thêm vào giỏ hàng

### Quản lý Giỏ hàng

1. Người dùng đã đăng nhập có thể thêm laptop vào giỏ hàng
2. `ConsoleUI.viewCart()` hiển thị nội dung giỏ hàng hiện tại
3. Các thao tác giỏ hàng:
   - Thêm mặt hàng: `CartService.addToCart()`
   - Cập nhật số lượng: `CartService.updateCartItemQuantity()`
   - Xóa mặt hàng: `CartService.removeFromCart()`
   - Xóa giỏ hàng: `CartService.clearCart()`
4. Dữ liệu giỏ hàng được duy trì trong bộ nhớ với `CartService.userCarts` HashMap
5. Tổng kết giỏ hàng hiển thị chi tiết mặt hàng, số lượng và tổng phụ

### Xử lý Đơn hàng

1. Quá trình thanh toán trong `ConsoleUI.checkout()`
2. Luồng thực thi:
   - Xác minh người dùng đã đăng nhập
   - Xác nhận nội dung giỏ hàng
   - Người dùng nhập địa chỉ giao hàng
   - Người dùng chọn phương thức thanh toán
   - `OrderService.createOrder()` tạo đơn hàng mới
   - Hệ thống cập nhật kho hàng (giảm số lượng tồn kho)
   - Xác nhận đơn hàng hiển thị với ID đơn hàng
3. Trạng thái đơn hàng: PENDING (Chờ xử lý) → PROCESSING (Đang xử lý) → SHIPPED (Đã giao hàng) → DELIVERED (Đã nhận hàng)
4. Admin có thể cập nhật trạng thái đơn hàng trong `ConsoleUI.updateOrderStatus()`
5. Người dùng có thể xem lịch sử đơn hàng với `ConsoleUI.viewMyOrders()`

### Bảo hành và Trả hàng

1. Người dùng gửi yêu cầu bảo hành/trả hàng với `ConsoleUI.requestWarranty()`
2. Luồng thực thi:
   - Người dùng chọn đơn hàng đã giao hàng hợp lệ
   - Người dùng chọn laptop từ đơn hàng
   - Người dùng chỉ định loại yêu cầu (Sửa chữa, Trả hàng/Thay thế, Khác)
   - Người dùng cung cấp lý do cho yêu cầu
   - `WarrantyService.createWarrantyRequest()` tạo yêu cầu
3. Admin xử lý yêu cầu trong `ConsoleUI.manageWarrantyRequests()`
4. Admin có thể chấp nhận/từ chối yêu cầu và thêm ghi chú
5. Trạng thái: PENDING (Chờ xử lý) → APPROVED (Đã chấp nhận)/REJECTED (Đã từ chối)
6. Người dùng có thể xem trạng thái yêu cầu bảo hành của họ

### Quản lý Người dùng

1. Admin có thể quản lý người dùng trong `ConsoleUI.manageUsers()`
2. Chức năng:
   - Xem tất cả người dùng: `AccountService.getAllAccounts()`
   - Tìm kiếm người dùng: `AccountService.searchAccounts()`
   - Thêm người dùng: `AccountService.registerAccount()`
   - Cập nhật người dùng: `AccountService.updateAccount()`
   - Xóa người dùng: `AccountService.deleteAccount()`
3. Người dùng thông thường có thể quản lý hồ sơ của mình trong `ConsoleUI.manageProfile()`
4. Chức năng thay đổi mật khẩu trong `ConsoleUI.changePassword()`

### Quản lý Kho hàng

1. Admin quản lý kho hàng trong `ConsoleUI.manageInventory()`
2. Chức năng:
   - Xem tồn kho hiện tại: Hiển thị tất cả laptop với mức tồn kho
   - Cập nhật tồn kho: `LaptopService.updateLaptopStock()`
   - Đăng ký nhập kho mới: Tạo ImportReceipt và cập nhật tồn kho
   - Xem cảnh báo tồn kho: Hiển thị các mặt hàng sắp hết hoặc hết hàng
   - Tạo báo cáo kho hàng: Số lượng sản phẩm, giá trị, phân tích theo thương hiệu
3. Trạng thái tồn kho được phân loại là:
   - Hết hàng (số lượng = 0)
   - Sắp hết hàng (số lượng < 5)
   - Còn hàng (số lượng ≥ 5)

### Tạo Báo cáo

1. Admin có thể tạo báo cáo trong `ConsoleUI.generateReports()`
2. Loại báo cáo:
   - Báo cáo bán hàng hàng ngày: Đơn hàng và doanh thu cho ngày cụ thể
   - Báo cáo bán hàng hàng tháng: Hiệu suất hàng tháng với phân tích hàng ngày
   - Báo cáo doanh thu sản phẩm: Doanh số theo sản phẩm với xếp hạng
   - Báo cáo doanh thu thương hiệu: Phân tích hiệu suất theo thương hiệu
   - Báo cáo mua hàng của khách hàng: Phân tích chi tiêu của khách hàng
3. Báo cáo hiển thị dữ liệu được định dạng với tổng, trung bình và trực quan hóa
4. Xử lý dữ liệu diễn ra trong các phương thức tương ứng trong ConsoleUI

## Chạy Ứng dụng

1. Đảm bảo MySQL đã được cài đặt và đang chạy
2. Cấu hình kết nối cơ sở dữ liệu trong `src/main/resources/application.properties`
3. Xây dựng dự án với Maven: `mvn clean package`
4. Chạy ứng dụng: `java -jar target/laptop-store-app-1.0-SNAPSHOT.jar`
5. Trong lần chạy đầu tiên, cơ sở dữ liệu sẽ được khởi tạo với dữ liệu mẫu

## Yêu cầu Phần mềm

- Java 11 trở lên
- MySQL 8.0 trở lên
- MySQL JDBC Driver 8.0.33
- Maven 3.6 trở lên để xây dựng dự án