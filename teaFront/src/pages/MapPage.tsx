import React from 'react';
import { MapContainer, TileLayer, Marker, Popup } from 'react-leaflet';
import 'leaflet/dist/leaflet.css';
import { Icon } from 'leaflet';
import { useQuery } from '@tanstack/react-query';
import { MapPin, Activity } from 'lucide-react';
import { warehouses } from '../lib/api';
import { useNavigate } from 'react-router-dom';

// Tạo icon tùy chỉnh cho trạng thái
const activeIcon = new Icon({
    iconUrl: 'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-green.png',
    shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41]
});

// Định nghĩa kiểu dữ liệu cho warehouse theo API thực tế của bạn
interface Warehouse {
    warehouseid: number;
    name: string;
    address: string;
    lat: number;
    lon: number;
    currentcapacity: number;
    totalcapacity: number;
}

// Định nghĩa kiểu dữ liệu cho điểm cân
interface WeighingStation {
    id: number;
    name: string;
    status: 'On' | 'Off';
    managementUnit: string;
    lat: number;
    lon: number;
    warehouseId: number;
}

function MapPage() {
    const navigate = useNavigate();

    // Sử dụng React Query để lấy dữ liệu kho
    const { data: warehousesList, isLoading, error } = useQuery<Warehouse[]>({
        queryKey: ['warehouses'],
        queryFn: warehouses.getAll,
    });

    // Chuyển đổi dữ liệu kho thành dữ liệu điểm cân
    const weighingStations: WeighingStation[] = warehousesList?.map(warehouse => ({
        id: warehouse.warehouseid,
        name: `Điểm cân ${warehouse.name}`,
        status: 'On', // Tất cả đều đang hoạt động như yêu cầu
        managementUnit: 'Hợp tác xã Mỗ Lao',
        lat: warehouse.lat,
        lon: warehouse.lon,
        warehouseId: warehouse.warehouseid
    })) || [];

    // Vị trí mặc định (Hà Nội) nếu không có dữ liệu
    const defaultPosition: [number, number] = [21.0285, 105.8542];

    // Tính toán vị trí trung tâm dựa trên tất cả các điểm
    const calculateCenter = () => {
        if (!weighingStations || weighingStations.length === 0) return defaultPosition;

        const sumLat = weighingStations.reduce((sum, station) => sum + station.lat, 0);
        const sumLon = weighingStations.reduce((sum, station) => sum + station.lon, 0);

        return [
            sumLat / weighingStations.length,
            sumLon / weighingStations.length
        ] as [number, number];
    };

    // Hàm xử lý khi nhấp vào xem lịch sử
    const handleViewHistory = (warehouseId: number) => {
        // Điều hướng đến trang kho với tab lịch sử được chọn
        navigate(`/warehouses?id=${warehouseId}&tab=history`);
    };

    if (isLoading) {
        return (
            <div className="flex items-center justify-center h-64">
                <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-indigo-600"></div>
            </div>
        );
    }

    if (error) {
        return (
            <div className="bg-white p-6 rounded-lg shadow-md">
                <h1 className="text-2xl font-semibold text-gray-900">Bản Đồ Điểm Cân</h1>
                <div className="text-red-500">
                    Đã xảy ra lỗi khi tải dữ liệu: {(error as Error).message}
                </div>
            </div>
        );
    }

    return (
        <div>
            <div className="sm:flex sm:items-center mb-6">
                <div className="sm:flex-auto">
                    <h1 className="text-2xl font-semibold text-gray-900">Bản Đồ Điểm Cân</h1>
                    <p className="mt-2 text-sm text-gray-700">
                        Hiển thị vị trí và thông tin của tất cả điểm cân trên bản đồ
                    </p>
                </div>
            </div>

            <div className="bg-white p-6 rounded-lg shadow-md">
                <div style={{ height: "500px", width: "100%" }}>
                    <MapContainer
                        center={calculateCenter()}
                        zoom={13}
                        scrollWheelZoom={true}
                        style={{ height: "100%", width: "100%" }}
                    >
                        <TileLayer
                            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
                            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                        />

                        {weighingStations.map((station) => (
                            <Marker
                                key={station.id}
                                position={[station.lat, station.lon]}
                                icon={activeIcon}
                            >
                                <Popup className="weighing-station-popup" minWidth={250}>
                                    <div className="p-2">
                                        <div className="flex items-center mb-3">
                                            <div className="flex-shrink-0">
                                                <Activity className="h-5 w-5 text-green-600" />
                                            </div>
                                            <div className="ml-3">
                                                <h3 className="text-lg font-medium text-gray-900">
                                                    {station.name}
                                                </h3>
                                            </div>
                                        </div>

                                        <div className="mt-3 space-y-2">
                                            <p><span className="font-semibold">Tình trạng:</span>{' '}
                                                <span className="text-green-600 font-medium">
                                                    Đang hoạt động (On)
                                                </span>
                                            </p>
                                            <p><span className="font-semibold">Đơn vị quản lý:</span>{' '}
                                                {station.managementUnit}
                                            </p>
                                        </div>

                                        <div className="mt-4">
                                            <button
                                                onClick={() => handleViewHistory(station.warehouseId)}
                                                className="w-full inline-flex justify-center items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                                            >
                                                Xem lịch sử cân
                                            </button>
                                        </div>
                                    </div>
                                </Popup>
                            </Marker>
                        ))}
                    </MapContainer>
                </div>
            </div>

            {/* Danh sách điểm cân ở dưới bản đồ */}
            <div className="mt-8 grid gap-4 sm:grid-cols-2 lg:grid-cols-3">
                {weighingStations.map((station) => (
                    <div
                        key={station.id}
                        className="bg-white overflow-hidden shadow rounded-lg cursor-pointer hover:shadow-md transition-shadow"
                        onClick={() => handleViewHistory(station.warehouseId)}
                    >
                        <div className="p-5">
                            <div className="flex items-center">
                                <div className="flex-shrink-0">
                                    <div className="h-10 w-10 rounded-full flex items-center justify-center bg-green-100 text-green-700">
                                        <Activity className="h-6 w-6" />
                                    </div>
                                </div>
                                <div className="ml-4">
                                    <h3 className="text-lg font-medium text-gray-900">
                                        {station.name}
                                    </h3>
                                    <p className="text-sm text-gray-500">{station.managementUnit}</p>
                                </div>
                            </div>
                            <div className="mt-4">
                                <div className="flex justify-between text-sm">
                                    <span className="text-gray-500">Tình trạng</span>
                                    <span className="font-medium text-green-600">
                                        Đang hoạt động
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}

export default MapPage;