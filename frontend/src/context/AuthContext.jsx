import React, { createContext, useContext, useState } from "react";

// Create the context
const AuthContext = createContext();

// Provider component
export function AuthProvider({ children }) {
  // Example user object: { id, name, role, purchasedPackages: [1,2,3], ... }
  const [user, setUser] = useState(null);

  // Simulate login (replace with real API call in production)
  const login = (userData) => setUser(userData);

  // Simulate logout
  const logout = () => setUser(null);

  // isAuthenticated is true if user is not null
  const isAuthenticated = !!user;

  return (
    <AuthContext.Provider value={{ user, isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

// Custom hook for easy access
export function useAuth() {
  return useContext(AuthContext);
} 