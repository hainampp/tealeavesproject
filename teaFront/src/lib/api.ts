import axios from 'axios';
import type { ApiResponse, LoginCredentials, RegisterData, User, Package, Warehouse, QRCodeData, WarehousePackages, TeaType } from '../types/api';

const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
});

// Add request interceptor to include auth token in all requests
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export const auth = {
  login: async (email: string, password: string) => {
    const response = await api.get<ApiResponse<string>>('/login/signin', {
      params: { email, password }
    });
    return response.data;
  },
  register: async (data: RegisterData) => {
    const response = await api.post<ApiResponse<boolean>>('/login/signup', data);
    return response.data;
  },
};

export const users = {
  getProfile: async () => {
    const response = await api.get<User>('/user/me');
    return response.data;
  },
  // Updated to match the QR code generation component needs
  createQRCode: async (data: {
    warehouseid: number;
    createdtime: string;
    typeteaid: number;
  }) => {
    const response = await api.post<ApiResponse<{
      qrCodeUrl?: string;
      packageId?: string;
    }>>('/user/qrcode', data);
    return response.data;
  },
  getAllPackages: async () => {
    const response = await api.get<Package[]>('/user/allpackage');
    return response.data;
  },
};

export const warehouses = {
  getAll: async () => {
    const response = await api.get<Warehouse[]>('/warehouse/allwarehouse');
    return response.data;
  },
  getPackages: async (warehouseId: number) => {
    const response = await api.get<WarehousePackages>(`/warehouse/allpackage/${warehouseId}`);
    return response.data;
  },
  weighPackage: async (capacity: number, weighDate: Date) => {
    const response = await api.put('/warehouse/weigh', null, {
      params: { capacity, weighDate }
    });
    return response.data;
  },
  scanQRCode: async (qrcode: string) => {
    const response = await api.put('/warehouse/scan', null, {
      params: { qrcode }
    });
    return response.data;
  },
};

export const teaTypes = {
  getAll: async () => {
    const response = await api.get<TeaType[]>('/typetea/alltype');
    return response.data;
  },
  delete: async (id: number) => {
    const response = await api.delete(`/typetea/${id}`);
    return response.data;
  },
};

// Export a function to be used by the QR code generation component
export const packages = {
  getTeaTypes: async () => {
    return teaTypes.getAll();
  },

  generateQRCode: async (data: {
    warehouseid: number;
    createdtime: string;
    typeteaid: number;
  }) => {
    return users.createQRCode(data);
  }
};

export default api;
