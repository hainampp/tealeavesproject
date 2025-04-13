import React from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, Link } from 'react-router-dom';
import { auth } from '../lib/api';
import { useAuthStore } from '../store/authStore';
import { LoginCredentials } from '../types/api';
import { useNotification } from '../contexts/NotificationContext'; // Import hook useNotification

function Login() {
  const navigate = useNavigate();
  const setAuth = useAuthStore((state) => state.setAuth);
  const { register, handleSubmit, formState: { errors } } = useForm<LoginCredentials>();
  const { connect } = useNotification(); // Lấy hàm connect từ context

  const onSubmit = async (data: LoginCredentials) => {
    try {
      const response = await auth.login(data.email, data.password);
      if (response.code === 200) {
        // Lưu thông tin xác thực (giữ nguyên logic hiện tại)
        setAuth(null, response.data); // token is in response.data
        
        // Kết nối SSE sau khi đăng nhập thành công
        // Giả sử token được lưu trong response.data
        connect(response.data);
        
        navigate('/');
      }
    } catch (error) {
      console.error('Login failed:', error);
    }
  };

  return (
    <div className="min-h-screen bg-gray-100 flex items-center justify-center">
      <div className="max-w-md w-full bg-white rounded-lg shadow-md p-8">
        <h2 className="text-2xl font-bold text-center mb-6">Đăng Nhập</h2>
        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <div>
            <label className="block text-sm font-medium text-gray-700">Email</label>
            <input
              type="email"
              {...register('email', { 
                required: 'Email is required',
                pattern: {
                  value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                  message: 'Invalid email address'
                }
              })}
              className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            />
            {errors.email && (
              <p className="mt-1 text-sm text-red-600">{errors.email.message}</p>
            )}
          </div>
          <div>
            <label className="block text-sm font-medium text-gray-700">Mật Khẩu</label>
            <input
              type="password"
              {...register('password', { required: 'Password is required' })}
              className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
            />
            {errors.password && (
              <p className="mt-1 text-sm text-red-600">{errors.password.message}</p>
            )}
          </div>
          <button
            type="submit"
            className="w-full bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
          >
            Đăng Nhập
          </button>
        </form>
        <p className="mt-4 text-center text-sm text-gray-600">
          Không có tài khoản?{' '}
          <Link to="/register" className="text-indigo-600 hover:text-indigo-500">
            Đăng Ký
          </Link>
        </p>
      </div>
    </div>
  );
}

export default Login;