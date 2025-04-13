import React from 'react';
import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import Layout from './components/Layout';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Packages from './pages/Packages';
import Warehouses from './pages/Warehouses';
import GenerateQRCode from './pages/GenerateQRCode';
import MapPage from './pages/MapPage'; // Import component Bản Đồ mới
import { useAuthStore } from './store/authStore';
import { NotificationProvider } from './contexts/NotificationContext';

// Create a new query client with default options
const queryClient = new QueryClient({
    defaultOptions: {
        queries: {
            retry: 1,
            refetchOnWindowFocus: false,
        },
    },
});

function PrivateRoute({ children }: { children: React.ReactNode }) {
    const token = useAuthStore((state) => state.token);
    return token ? <>{children}</> : <Navigate to="/login" />;
}

function App() {
    return (
        <NotificationProvider>
            <QueryClientProvider client={queryClient}>
                <BrowserRouter>
                    <Routes>
                        {/* Public routes */}
                        <Route path="/login" element={<Login />} />
                        <Route path="/register" element={<Register />} />

                        {/* Protected routes within Layout */}
                        <Route
                            path="/"
                            element={
                                <PrivateRoute>
                                    <Layout />
                                </PrivateRoute>
                            }
                        >
                            <Route index element={<Dashboard />} />
                            <Route path="packages" element={<Packages />} />
                            <Route path="warehouses" element={<Warehouses />} />
                            <Route path="generate-qrcode" element={<GenerateQRCode />} />
                            <Route path="map" element={<MapPage />} />

                            {/* Redirect any unmatched routes to dashboard */}
                            <Route path="*" element={<Navigate to="/" replace />} />
                        </Route>
                    </Routes>
                </BrowserRouter>
            </QueryClientProvider>
        </NotificationProvider>
    );
}

export default App;