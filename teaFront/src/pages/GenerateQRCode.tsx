import React, { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useQuery, useMutation } from '@tanstack/react-query';
import { warehouses } from '../lib/api';
import { Loader2 } from 'lucide-react';
import axios from 'axios';
import { QRCodeSVG } from 'qrcode.react';

interface Warehouse {
    warehouseid: number;
    name: string;
    address: string;
}

interface TeaType {
    typeteaid: number;
    name: string;
}

interface QRCodeFormData {
    warehouseid: number;
    typeteaid: number;
}

// Dữ liệu cứng cho loại trà
const TEA_TYPES: TeaType[] = [
    { typeteaid: 1, name: "Chè Thái" },
    { typeteaid: 2, name: "Chè Trung Quốc" }
];

// API URL gốc
const API_BASE_URL = 'http://localhost:8080';

function GenerateQRCode() {
    const [qrCodeData, setQrCodeData] = useState<string | null>(null);
    const [error, setError] = useState<string | null>(null);

    const { register, handleSubmit, formState: { errors } } = useForm<QRCodeFormData>();

    // Fetch warehouses for dropdown
    const { data: warehouseData, isLoading: warehousesLoading } = useQuery({
        queryKey: ['warehouses'],
        queryFn: warehouses.getAll,
    });

    // Mutation for QR code generation
    const qrCodeMutation = useMutation({
        mutationFn: (data: QRCodeFormData & { createdtime: string }) => {
            return generateQRCode(data);
        },
        onSuccess: (response: string) => {
            // API trả về trực tiếp là string
            setQrCodeData(response);
            setError(null);
        },
        onError: (err: any) => {
            console.error('Error generating QR code:', err);
            setError('Đã xảy ra lỗi khi tạo mã QR. Vui lòng thử lại sau.');
        }
    });

    // Helper function to generate QR code with correct API URL
    const generateQRCode = async (data: QRCodeFormData & { createdtime: string }): Promise<string> => {
        try {
            const token = localStorage.getItem('token');
            const response = await axios.post(`${API_BASE_URL}/user/qrcode`, data, {
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': token ? `Bearer ${token}` : ''
                },
                responseType: 'text' // Đảm bảo axios không parse JSON
            });

            // Trả về chuỗi từ response
            return response.data;
        } catch (error: any) {
            console.error('Error generating QR code:', error);
            if (error.response && typeof error.response.data === 'string') {
                return Promise.reject(error.response.data);
            }
            throw error;
        }
    };

    const onSubmit = (data: QRCodeFormData) => {
        // Add current timestamp
        const currentTime = new Date().toISOString();
        qrCodeMutation.mutate({
            ...data,
            createdtime: currentTime
        });
    };

    const handlePrint = () => {
        if (qrCodeData) {
            const printWindow = window.open('', '_blank');
            if (printWindow) {
                printWindow.document.write(`
                    <html>
                        <head>
                            <title>Print QR Code</title>
                            <style>
                                body { display: flex; justify-content: center; align-items: center; height: 100vh; }
                                .container { text-align: center; }
                                .qr-container { width: 300px; height: 300px; margin: 0 auto; }
                            </style>
                        </head>
                        <body>
                            <div class="container">
                                <h2>Mã QR cho gói hàng</h2>
                                <div class="qr-container">
                                    <img src="data:image/svg+xml;base64,${btoa(new XMLSerializer().serializeToString(document.getElementById('qr-code-svg')!))}" width="100%" />
                                </div>
                                <p>Mã: ${qrCodeData}</p>
                            </div>
                            <script>
                                setTimeout(() => { window.print(); window.close(); }, 500);
                            </script>
                        </body>
                    </html>
                `);
                printWindow.document.close();
            }
        }
    };

    // Tạo URL download cho QR code
    const downloadQRCode = () => {
        if (!qrCodeData) return;

        // Lấy element SVG của QR code
        const svg = document.getElementById('qr-code-svg');
        if (!svg) return;

        // Chuyển SVG thành chuỗi
        const svgData = new XMLSerializer().serializeToString(svg);

        // Tạo Blob từ chuỗi SVG
        const svgBlob = new Blob([svgData], { type: 'image/svg+xml;charset=utf-8' });

        // Tạo URL từ Blob
        const svgUrl = URL.createObjectURL(svgBlob);

        // Tạo link download
        const downloadLink = document.createElement('a');
        downloadLink.href = svgUrl;
        downloadLink.download = `qrcode-${qrCodeData}.svg`;
        document.body.appendChild(downloadLink);
        downloadLink.click();
        document.body.removeChild(downloadLink);
    };

    return (
        <div className="max-w-4xl mx-auto p-6">
            <div className="bg-white rounded-lg shadow-md p-8">
                <h2 className="text-2xl font-bold text-center mb-6">Tạo QR Code cho gói hàng</h2>

                {error && (
                    <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
                        {error}
                    </div>
                )}

                {qrCodeData ? (
                    <div className="text-center">
                        <div className="mb-4">
                            <h3 className="text-lg font-medium mb-2">QR Code đã được tạo thành công</h3>
                            <p className="text-gray-600 mb-4">Mã gói hàng: {qrCodeData}</p>
                            <div className="flex justify-center mb-4">
                                <div className="border p-4 rounded bg-white">
                                    {/* Tạo QR code từ chuỗi API trả về */}
                                    <QRCodeSVG
                                        id="qr-code-svg"
                                        value={qrCodeData}
                                        size={256}
                                        level="H"
                                        includeMargin={true}
                                    />
                                </div>
                            </div>
                            <div className="flex justify-center gap-4">
                                <button
                                    onClick={handlePrint}
                                    className="bg-indigo-600 text-white py-2 px-4 rounded-md hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"
                                >
                                    In mã QR
                                </button>
                                <button
                                    onClick={downloadQRCode}
                                    className="bg-green-600 text-white py-2 px-4 rounded-md hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
                                >
                                    Tải xuống
                                </button>
                                <button
                                    onClick={() => setQrCodeData(null)}
                                    className="bg-gray-200 text-gray-800 py-2 px-4 rounded-md hover:bg-gray-300 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                                >
                                    Tạo mã QR mới
                                </button>
                            </div>
                        </div>
                    </div>
                ) : (
                    <form onSubmit={handleSubmit(onSubmit)} className="space-y-6">
                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Chọn kho hàng
                            </label>
                            {warehousesLoading ? (
                                <div className="flex items-center space-x-2">
                                    <Loader2 className="h-5 w-5 animate-spin text-indigo-600" />
                                    <span>Đang tải danh sách kho...</span>
                                </div>
                            ) : (
                                <select
                                    {...register('warehouseid', { required: 'Vui lòng chọn kho hàng' })}
                                    className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                                >
                                    <option value="">-- Chọn kho hàng --</option>
                                    {Array.isArray(warehouseData) && warehouseData.map((warehouse: Warehouse) => (
                                        <option key={warehouse.warehouseid} value={warehouse.warehouseid}>
                                            {warehouse.name} - {warehouse.address}
                                        </option>
                                    ))}
                                </select>
                            )}
                            {errors.warehouseid && (
                                <p className="mt-1 text-sm text-red-600">{errors.warehouseid.message}</p>
                            )}
                        </div>

                        <div>
                            <label className="block text-sm font-medium text-gray-700 mb-1">
                                Loại trà
                            </label>
                            <select
                                {...register('typeteaid', { required: 'Vui lòng chọn loại trà' })}
                                className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring-indigo-500"
                            >
                                <option value="">-- Chọn loại trà --</option>
                                {TEA_TYPES.map((type) => (
                                    <option key={type.typeteaid} value={type.typeteaid}>
                                        {type.name}
                                    </option>
                                ))}
                            </select>
                            {errors.typeteaid && (
                                <p className="mt-1 text-sm text-red-600">{errors.typeteaid.message}</p>
                            )}
                        </div>

                        <div className="pt-4">
                            <button
                                type="submit"
                                disabled={qrCodeMutation.isPending || warehousesLoading}
                                className={`w-full ${
                                    qrCodeMutation.isPending ? 'bg-indigo-400' : 'bg-indigo-600 hover:bg-indigo-700'
                                } text-white py-2 px-4 rounded-md focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2`}
                            >
                                {qrCodeMutation.isPending ? (
                                    <span className="flex items-center justify-center">
                                        <Loader2 className="h-5 w-5 animate-spin mr-2" />
                                        Đang tạo mã QR...
                                    </span>
                                ) : (
                                    'Tạo mã QR'
                                )}
                            </button>
                        </div>
                    </form>
                )}
            </div>
        </div>
    );
}

export default GenerateQRCode;
