package com.hust.agriculture.common;

public class Constant {

    public static final Integer PAID_STATUS = 0;
    public static final Integer UNPAID_STATUS = 1;

    public static final Long PAYMENT_METHOD_ONLINE = 1l;

    //Device status
    public static final Integer STATUS_ALIVE = 2;

    //User status
    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_INACTIVE = 0;

    //Role
    public static final Long ROLE_USER_ID = 1l;
    public static final Long ROLE_USER_ADMIN = 2l;

    // Payment Type
    public static final Integer DUE_DATE_DAY = 14;
    public static final Integer DUE_DATE_CROP = 30;
    public static final Integer PAYMENT_BY_DAY = 1;
    public static final Integer PAYMENT_BY_CROP = 2;

    // Command Type
    public static final Integer START_COLLECT_DATA = 1;
    public static final String DES_COLLECT_DATE = "Bắt đầu thu thập dữ liệu";
    public static final Integer CONTROL_DEVICE = 2;
    public static final String DES_CONTROL_DEVICE = "Điều khiển thiết bị";



    // ERROR CODE
    public static final String ERROR_CODE_OK = "0";
    public static final String ERROR_CODE_NOK = "1";

    // MESSAGE RESPONSE
    public static final String MSG_SIGN_IN_OK = "Đăng nhập thành công!";
    public static final String MSG_SIGN_IN_NOK = "Tên đăng nhập hoặc mật khẩu không đúng!";
    public static final String MSG_USER_NOT_ACTIVE = "Người dùng chưa được kích hoạt!";
    public static final String MSG_USER_EXIST = "Tên đăng nhập, email hoặc số điện thoại đã tồn tại!";
    public static final String MSG_SERVER_ERROR = "Lỗi hệ thống, vui lòng liên hệ quản trị viên!";
    public static final String MSG_INPUT_INVALID = "Thông tin của bạn không hợp lệ !";
    public static final String MSG_SIGN_UP_OK = "Đăng kí tài khoản thành công, vui lòng đăng nhập để sử dụng dịch vụ!";
    public static final String MSG_DEVICE_NAME_IS_EXISTS = "Tên thiết bị đã tồn tại, vui lòng chọn tên khác !";
    public static final String MSG_CROP_NAME_IS_EMPTY = "Vui lòng nhập tên mùa !";
    public static final String MSG_PLANT_ID_INVALID = "PlantId không hợp lệ !";
    public static final String MSG_PAYMENT_TYPE_INVALID = "Loại thanh toán không hợp lệ !";
    public static final String MSG_DEVICE_ID_INVALID = "DeviceId không hợp lệ !";
    public static final String MSG_START_DATE_INVALID = "Ngày bắt đầu không hợp lệ !";
    public static final String MSG_END_DATE_INVALID = "Ngày kết thúc không hợp lệ !";
    public static final String MSG_PAYMENT_METHOD_INVALID = "Hình thức thanh toán không hợp lệ !";
    public static final String MSG_CROP_CREATED = "Vụ mùa đã khởi tạo thành công !";
    public static final String MSG_FARM_NAME_INVALID = "Tên đã tồn tại, vui lòng chọn tên khác !";

    public static final String MSG_RESET_PASS_OK = "Reset mật khẩu thành công !";
    public static final String MSG_USER_NOT_EXISTS = "Người dùng không tồn tại !";
    public static final String MSG_OLD_PASS_INVALID = "Mật khẩu cũ không hợp lệ !";
    public static final String MSG_NEW_PASS_INVALID = "Mật khẩu mới không hợp lệ !";
    public static final String MSG_CONFIRM_PASS_INVALID = "Mật khẩu xác nhận không hợp lệ !";
    public static final String MSG_PASS_INVALID = "Mật khẩu không đúng !";
    public static final String MSG_CHANGE_PASS_SUCCESS = "Thay đổi mật khẩu thành công !";
    public static final String MSG_UPDATE_INFO_SUCCESS = "Cập nhật thông tin thành công !";
    public static final String MSG_CREATE_INFO_SUCCESS = "Tạo mới thông tin thành công !";
    public static final String MSG_NOT_AUTHORIZATION = "Bạn không có quyền thực hiện chức năng này !";
    public static final String MSG_INVOICE_NOT_EXISTS = "Hóa đơn không tồn tại !";
    public static final String MSG_PAID_SUCCESS = "Thanh toán hóa đơn thành công !";
    public static final String MSG_CONTROL_OK = "Đã thực hiện điều khiển thiết bị thành công !";
    public static final String MSG_CONTROL_NOK = "Không thể thực hiện điều khiển thiết bị !\n Vui lòng liên hệ quản trị viên để biết thêm thông tin !";
    public static final String MSG_PAYMENT_AMOUNT_INVALID = "Số tiền thanh toán không hợp lệ !";
    public static final String MSG_UNPAID_INVOICES = "Vui lòng thanh toán hóa đơn để tiếp tục sử dụng dịch vụ !";
    public static final String MQTT_SERVER = "mqtt.broker";
    public static final String SERVER_ID = "mqtt.serverInstanceID";

    public static final String COMMON_TOPIC = "agriculture_hust_2021_common_topic";
    public static final String COMMON_CLIENT_ID = "agriculture_hust_2021_common_topic_id";
    public static final String CONTROL_CLIENT_ID = "agriculture_hust_2021_control_topic_id";

    public static final String CONTROL_TOPIC = "agriculture_hust_2021_control_topic_";
    public static final String DATA_TOPIC = "agriculture_hust_2021_data_topic_";

    public static final Integer TYPE_ALIVE = 1;
    public static final Integer TYPE_DATA = 2;


}
