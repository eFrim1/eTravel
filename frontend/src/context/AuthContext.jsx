import React, { createContext, useContext, useState, useEffect } from "react";
import api from '../api/axios';

// Create the context
const AuthContext = createContext();

// Provider component
export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const storedUser = localStorage.getItem('user');
    return storedUser ? JSON.parse(storedUser) : null;
  });
  const [token, setToken] = useState(() => localStorage.getItem('token'));

  // Keep localStorage in sync
  useEffect(() => {
    if (user && token) {
      localStorage.setItem('user', JSON.stringify(user));
      localStorage.setItem('token', token);
    } else {
      localStorage.removeItem('user');
      localStorage.removeItem('token');
    }
  }, [user, token]);

  // Login: POST /users/login
  const login = async ({ email, password }) => {
    try {
      const formData = {username: email, password: password};
      console.log('Attempting login with:',formData);
      const res = await api.post('/users/login', formData);
      console.log('Login response:', res);
      const jwt = res.data.token;
      setToken(jwt);
      localStorage.setItem('token', jwt);
      // Fetch user info:
      console.log('Fetching user profile...');
      const profileRes = await api.get('/users/me');
      console.log('Profile response:', profileRes);
      setUser({
        ...profileRes.data,
        role: profileRes.data.role.toLowerCase(),
      });
      localStorage.setItem('user', JSON.stringify(profileRes.data));
      return { success: true };
    } catch (err) {
      console.error('Login error:', err);
      setUser(null);
      setToken(null);
      return { success: false, message: err.response?.data?.message || 'Login failed' };
    }
  };

  // Register: POST /users/register
  const register = async (formData) => {
    formData.role = 'CLIENT';
    try {
      console.log('Attempting registration with:', formData);
      const res = await api.post('/users/register', formData);
      console.log('Register response:', res);
      return { success: true };
    } catch (err) {
      console.error('Register error:', err);
      return { success: false, message: err.response?.data?.message || 'Registration failed' };
    }
  };

  // Logout
  const logout = () => {
    setUser(null);
    setToken(null);
    localStorage.removeItem('user');
    localStorage.removeItem('token');
  };

  // isAuthenticated is true if user is not null and token is not null
  const isAuthenticated = !!user && !!token;

  return (
    <AuthContext.Provider value={{ user, token, isAuthenticated, login, logout, register }}>
      {children}
    </AuthContext.Provider>
  );
}

// Custom hook for easy access
export function useAuth() {
  return useContext(AuthContext);
} 