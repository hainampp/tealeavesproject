import React from 'react';
import { useNotification } from '../contexts/NotificationContext';

const Notifications: React.FC = () => {
  const { notifications, dismissNotification } = useNotification();

  return (
    <div className="toast-container">
      {notifications.map((notification) => (
        <div 
          key={notification.id}
          className={`toast-notification toast-${notification.type}`}
        >
          <div className="toast-content">
            <p>{notification.message}</p>
          </div>
          <button 
            className="toast-close"
            onClick={() => dismissNotification(notification.id)}
          >
            ×
          </button>
        </div>
      ))}
    </div>
  );
};

export default Notifications;

// Thêm vào NotificationContext.tsx
const [reconnectAttempt, setReconnectAttempt] = useState(0);
const maxReconnectAttempts = 5;
const reconnectDelay = 3000; // 3 giây

// Thêm xử lý sự kiện "open"
newEventSource.onopen = () => {
  console.log("SSE: Kết nối được thiết lập/khôi phục");
  setReconnectAttempt(0); // Reset số lần thử kết nối lại
  
  if (reconnectAttempt > 0) {
    addNotification("Đã kết nối lại thành công với server", "success");
  }
};

// Trong hàm xử lý lỗi kết nối
newEventSource.onerror = (error) => {
  console.error("SSE: Lỗi kết nối:", error);
  
  if (error instanceof Error && error.message.includes('401')) {
    console.error("SSE: Lỗi xác thực. Token hết hạn hoặc không hợp lệ");
    addNotification("Lỗi xác thực kết nối. Vui lòng đăng nhập lại.", "error");
    // Có thể chuyển hướng đến trang login
  } else {
    // Đóng kết nối hiện tại
    newEventSource.close();
    
    // Kiểm tra số lần thử kết nối lại
    if (reconnectAttempt < maxReconnectAttempts) {
      const nextAttempt = reconnectAttempt + 1;
      setReconnectAttempt(nextAttempt);
      
      addNotification(`Mất kết nối với server. Đang thử kết nối lại (${nextAttempt}/${maxReconnectAttempts})...`, "warning");
      
      // Thử kết nối lại sau khoảng thời gian
      setTimeout(() => {
        connect(token);
      }, reconnectDelay);
    } else {
      addNotification("Không thể kết nối lại với server sau nhiều lần thử. Vui lòng tải lại trang.", "error");
    }
  }
};