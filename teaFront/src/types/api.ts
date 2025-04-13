export interface User {
  email: string;
  fullname: string;
  roles: string;
}

export interface Package {
  packageId: number;
  fullname: string;
  warehouse: string;
  createdtime: string;
  weightime: string;
  typeteaname: string;
  capacity: number;
  unit: string;
  status: string;
  teacode: string;
}

export interface Warehouse {
  warehouseid: number;
  name: string;
  address: string;
  lat: number;
  lon: number;
  currentcapacity: number;
  totalcapacity: number;
}

export interface LoginCredentials {
  email: string;
  password: string;
}

export interface RegisterData {
  email: string;
  fullname: string;
  password: string;
  role_id: number;
}

export interface QRCodeData {
  warehouseid: number;
  createdtime: Date;
  typeteaid: number;
}

export interface ApiResponse<T> {
  code: number;
  status: string;
  message: string;
  data: T;
}

export interface WarehousePackages {
  warehouseid: number;
  name: string;
  packages: Package[];
}

export interface PaginatedResponse<T> {
  items: T[];
  total: number;
  page: number;
  size: number;
  totalPages: number;
}