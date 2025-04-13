import React from 'react';
import { Outlet, Link, useNavigate } from 'react-router-dom';
import { Package, Warehouse, LogOut, QrCode, Map } from 'lucide-react';
import { useAuthStore } from '../store/authStore';
import { useNotification } from '../contexts/NotificationContext';

function Layout() {
  const navigate = useNavigate();
  const logout = useAuthStore((state) => state.logout);
  const { disconnect } = useNotification();

  const handleLogout = () => {
    // Ngắt kết nối SSE trước khi xóa token
    disconnect();
    
    // Xóa token và thông tin user
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    
    // Chuyển hướng về trang đăng nhập
    navigate('/login');
  };

  return (
      <div className="min-h-screen bg-gray-100">
        <nav className="bg-white shadow-md">
          <div className="max-w-7xl mx-auto px-4">
            <div className="flex justify-between h-16">
              <div className="flex">
                <Link to="/" className="flex items-center px-4 font-semibold text-gray-700">
                  Hệ Thống Quản Lý Trà
                </Link>
                <div className="flex space-x-4 ml-10">
                  <Link
                      to="/packages"
                      className="flex items-center px-3 py-2 text-gray-600 hover:text-gray-900"
                  >
                    <Package className="w-5 h-5 mr-1" />
                    Gói Hàng
                  </Link>
                  <Link
                      to="/warehouses"
                      className="flex items-center px-3 py-2 text-gray-600 hover:text-gray-900"
                  >
                    <Warehouse className="w-5 h-5 mr-1" />
                    Kho
                  </Link>
                  <Link
                      to="/generate-qrcode"
                      className="flex items-center px-3 py-2 text-gray-600 hover:text-gray-900"
                  >
                    <QrCode className="w-5 h-5 mr-1" />
                    Tạo mã QR
                  </Link>
                  <Link
                      to="/map"
                      className="flex items-center px-3 py-2 text-gray-600 hover:text-gray-900"
                  >
                    <Map className="w-5 h-5 mr-1" />
                    Bản Đồ
                  </Link>
                </div>
              </div>
              <button
                  onClick={handleLogout}
                  className="flex items-center px-3 text-gray-600 hover:text-gray-900"
              >
                <LogOut className="w-5 h-5 mr-1" />
                Logout
              </button>
            </div>
          </div>
        </nav>
        <main className="max-w-7xl mx-auto py-6 px-4">
          <Outlet />
        </main>
      </div>
  );
}

export default Layout;
