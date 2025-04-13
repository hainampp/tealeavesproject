import React, { createContext, useContext, useState, useEffect, useCallback } from 'react';
import { EventSourcePolyfill } from 'event-source-polyfill';
import '../styles/notifications.css'; // Tạo file CSS mới

interface NotificationItem {
  id: string;
  message: string;
  type: 'success' | 'info' | 'warning' | 'error';
  timestamp: Date;
}

interface NotificationContextType {
  connect: (token: string) => void;
  disconnect: () => void;
  lastNotification: string | null;
  notifications: NotificationItem[];
  dismissNotification: (id: string) => void;
}

const NotificationContext = createContext<NotificationContextType | undefined>(undefined);

export function NotificationProvider({ children }: { children: React.ReactNode }) {
  const [eventSource, setEventSource] = useState<EventSourcePolyfill | null>(null);
  const [lastNotification, setLastNotification] = useState<string | null>(null);
  const [notifications, setNotifications] = useState<NotificationItem[]>([]);

  // Thêm thông báo mới
  const addNotification = useCallback((message: string, type: 'success' | 'info' | 'warning' | 'error' = 'info') => {
    const id = Date.now().toString();
    const newNotification = {
      id,
      message,
      type,
      timestamp: new Date()
    };
    
    setNotifications(prev => [...prev, newNotification]);
    
    // Tự động ẩn thông báo sau 5 giây
    setTimeout(() => {
      dismissNotification(id);
    }, 5000);
    
    return id;
  }, []);
  
  // Xóa thông báo
  const dismissNotification = useCallback((id: string) => {
    setNotifications(prev => prev.filter(notification => notification.id !== id));
  }, []);

  // Hàm kết nối SSE - cập nhật để sử dụng hệ thống thông báo mới
  const connect = useCallback((token: string) => {
    // Đóng kết nối hiện tại nếu có
    if (eventSource) {
      disconnect();
    }

    if (!token) {
      console.error('Không có token xác thực để kết nối SSE');
      return;
    }

    try {
      // Tạo kết nối SSE mới
      const newEventSource = new EventSourcePolyfill("http://localhost:8080/notice", {
        headers: {
          'Authorization': `Bearer ${token}`
        }
      });
      
      console.log("SSE: Đã kết nối tới endpoint /notice");
      addNotification("Đã kết nối nhận thông báo thành công", "success");
      
      // Lắng nghe sự kiện "scan"
      newEventSource.addEventListener("scan", (event) => {
        const notificationText = event.data;
        console.log('SSE: Nhận thông báo quét QR:', notificationText);
        setLastNotification(notificationText);
        
        // Thêm thông báo thay vì dùng alert()
        addNotification(notificationText, "info");
      });

      // Lắng nghe sự kiện "weigh" 
      newEventSource.addEventListener("weigh", (event) => {
        const weighData = event.data;
        console.log('SSE: Nhận thông báo cân chè:', weighData);
        setLastNotification(weighData);
        
        // Thêm thông báo với kiểu success để có màu khác
        addNotification(weighData, "success");
      });
      
      // Xử lý các sự kiện khác
      newEventSource.onmessage = (event) => {
        console.log('SSE: Nhận thông báo chung:', event.data);
        addNotification(event.data, "info");
      };
      
      // Xử lý lỗi kết nối
      newEventSource.onerror = (error) => {
        console.error("SSE: Lỗi kết nối:", error);
        if (error instanceof Error && error.message.includes('401')) {
          console.error("SSE: Lỗi xác thực. Token hết hạn hoặc không hợp lệ");
          addNotification("Lỗi xác thực kết nối. Vui lòng đăng nhập lại.", "error");
        } else {
          addNotification("Mất kết nối với server. Đang thử kết nối lại...", "warning");
        }
      };
      
      setEventSource(newEventSource);
    } catch (error) {
      console.error('SSE: Lỗi khởi tạo kết nối:', error);
      addNotification("Không thể kết nối đến server thông báo", "error");
    }
  }, [addNotification]);

  // Hàm ngắt kết nối SSE
  const disconnect = useCallback(() => {
    if (eventSource) {
      console.log("SSE: Đóng kết nối");
      eventSource.close();
      setEventSource(null);
    }
  }, [eventSource]);

  // Đóng kết nối khi component unmount
  useEffect(() => {
    return () => {
      disconnect();
    };
  }, [disconnect]);

  // Component Toast Notifications
  const ToastNotifications = () => {
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

  return (
    <NotificationContext.Provider value={{ 
      connect, 
      disconnect, 
      lastNotification, 
      notifications,
      dismissNotification
    }}>
      <ToastNotifications />
      {children}
    </NotificationContext.Provider>
  );
}

// Hook để sử dụng trong các components
export function useNotification() {
  const context = useContext(NotificationContext);
  if (context === undefined) {
    throw new Error('useNotification phải được sử dụng trong NotificationProvider');
  }
  return context;
}