import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate, Link } from 'react-router-dom';
import { auth } from '../lib/api';

// Define the type for registration data
interface RegisterData {
  email: string;
  fullname: string;
  password: string;
  role_id: number;
}

// Define the API response type
interface ApiResponse {
  code: number;
  status: string;
  message: string;
  data: any;
}

function Register() {
  const navigate = useNavigate();
  const [apiError, setApiError] = useState<string | null>(null);
  const [isSubmitting, setIsSubmitting] = useState(false);

  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm<RegisterData>();

  const onSubmit = async (data: RegisterData) => {
    setIsSubmitting(true);
    setApiError(null);

    try {
      const registerData: RegisterData = {
        ...data,
        role_id: 1 // Default role ID is now 1 as requested
      };

      const response: ApiResponse = await auth.register(registerData);

      if (response.code === 200) {
        // Registration successful
        navigate('/login');
      } else if (response.code === 400 && response.status === "Existed") {
        // Email already exists
        setApiError("Email này đã được đăng ký. Vui lòng sử dụng email khác.");
      } else {
        // Handle other response codes
        setApiError(response.message || 'Đăng ký không thành công. Vui lòng thử lại.');
      }
    } catch (error) {
      console.error('Registration failed:', error);
      setApiError('Đã xảy ra lỗi trong quá trình đăng ký. Vui lòng thử lại sau.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
      <div className="min-h-screen bg-gray-100 flex items-center justify-center">
        <div className="max-w-md w-full bg-white rounded-lg shadow-md p-8">
          <h2 className="text-2xl font-bold text-center mb-6">Đăng ký tài khoản</h2>

          {apiError && (
              <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
                {apiError}
              </div>
          )}

          <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
            <div>
              <label className="block text-sm font-medium text-gray-700">Họ và tên</label>
              <input
                  {...register('fullname', { required: 'Vui lòng nhập họ tên' })}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
              />
              {errors.fullname && (
                  <p className="mt-1 text-sm text-red-600">{errors.fullname.message}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Email</label>
              <input
                  type="email"
                  {...register('email', {
                    required: 'Vui lòng nhập email',
                    pattern: {
                      value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                      message: 'Email không hợp lệ'
                    }
                  })}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
              />
              {errors.email && (
                  <p className="mt-1 text-sm text-red-600">{errors.email.message}</p>
              )}
            </div>

            <div>
              <label className="block text-sm font-medium text-gray-700">Mật khẩu</label>
              <input
                  type="password"
                  {...register('password', {
                    required: 'Vui lòng nhập mật khẩu',
                    minLength: {
                      value: 6,
                      message: 'Mật khẩu phải có ít nhất 6 ký tự'
                    }
                  })}
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
              />
              {errors.password && (
                  <p className="mt-1 text-sm text-red-600">{errors.password.message}</p>
              )}
            </div>

            <button
                type="submit"
                disabled={isSubmitting}
                className={`w-full ${
                    isSubmitting ? 'bg-indigo-400' : 'bg-indigo-600 hover:bg-indigo-700'
                } text-white py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2`}
            >
              {isSubmitting ? 'Đang đăng ký...' : 'Đăng ký'}
            </button>
          </form>

          <p className="mt-4 text-center text-sm text-gray-600">
            Đã có tài khoản?{' '}
            <Link to="/login" className="text-indigo-600 hover:text-indigo-500">
              Đăng nhập
            </Link>
          </p>
        </div>
      </div>
  );
}

export default Register;
