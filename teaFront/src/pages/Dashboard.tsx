import React from 'react';
import { useQuery } from '@tanstack/react-query';
import { Package, Warehouse } from 'lucide-react';
import { users, warehouses } from '../lib/api';

function Dashboard() {
  const { data: packagesData } = useQuery({
    queryKey: ['packages'],
    queryFn: users.getAllPackages,
  });

  const { data: warehousesData } = useQuery({
    queryKey: ['warehouses'],
    queryFn: warehouses.getAll,
  });

  return (
    <div>
      <h1 className="text-2xl font-semibold text-gray-900">Tổng Quan</h1>
      
      <div className="mt-6 grid gap-6 sm:grid-cols-2">
        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-6">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <Package className="h-8 w-8 text-indigo-600" />
              </div>
              <div className="ml-4">
                <h3 className="text-lg font-medium text-gray-900">
                  Tổng Gói Hàng
                </h3>
                <p className="text-2xl font-semibold text-indigo-600">
                  {packagesData?.length || 0}
                </p>
              </div>
            </div>
          </div>
        </div>

        <div className="bg-white overflow-hidden shadow rounded-lg">
          <div className="p-6">
            <div className="flex items-center">
              <div className="flex-shrink-0">
                <Warehouse className="h-8 w-8 text-indigo-600" />
              </div>
              <div className="ml-4">
                <h3 className="text-lg font-medium text-gray-900">
                  Số Kho
                </h3>
                <p className="text-2xl font-semibold text-indigo-600">
                  {warehousesData?.length || 0}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="mt-8">
        <h2 className="text-lg font-medium text-gray-900 mb-4">
          Những Gói Hàng Gần Đây
        </h2>
        <div className="bg-white shadow rounded-lg overflow-hidden">
          <table className="min-w-full divide-y divide-gray-200">
            <thead className="bg-gray-50">
              <tr>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Tên
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Số Lượng
                </th>
                <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                  Thời Gian Tạo
                </th>
              </tr>
            </thead>
            <tbody className="bg-white divide-y divide-gray-200">
              {packagesData?.slice(0, 5).map((pkg) => (
                <tr key={pkg.packageId}>
                  <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                    {pkg.fullname}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {pkg.capacity} {pkg.unit}
                  </td>
                  <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                    {new Date(pkg.createdtime).toLocaleDateString()}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}

export default Dashboard;