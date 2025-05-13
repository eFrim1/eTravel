import { BrowserRouter as Router, Routes, Route, NavLink, Navigate } from 'react-router-dom';
import Home from './pages/Home';
import Login from './pages/Login';
import Register from './pages/Register';
import MyPackages from './pages/MyPackages';
import Admin from './pages/Admin';
import Employee from './pages/Employee';
import Manager from './pages/Manager';
import { Box, Flex, Heading, HStack, Button, Menu, MenuButton, MenuList, MenuItem, Avatar, Text, Divider, Stack } from '@chakra-ui/react';
import { ChevronDownIcon } from '@chakra-ui/icons';
import LanguageSwitcher from './views/LanguageSwitcher';
import { useTranslation } from 'react-i18next';
import { useAuth } from './context/AuthContext';
import ProtectedRoute from './components/ProtectedRoute';
import NotAuthorized from './pages/NotAuthorized';

function App() {
  const { t } = useTranslation();
  const { user, isAuthenticated, logout } = useAuth();

  return (
    <Router>
      <Flex direction="column" minH="100vh" w="100vw" bg="gray.50">
        <Flex as="header" bg="teal.500" color="white" align="center" justify="space-between" px={6} py={4} boxShadow="md" wrap="wrap" w="100%">
          <HStack spacing={6} align="center">
            <Heading as="h1" size="md" mb={{ base: 2, md: 0 }}>{t('appName')}</Heading>
            <Divider orientation="vertical" borderColor="teal.300" h={8} mx={2} display={{ base: 'none', md: 'block' }} />
            <HStack as="nav" spacing={1}>
              <Button as={NavLink} to="/" variant="ghost" color="white" fontWeight="medium" 
                _activeLink={{ fontWeight: 'bold', textDecoration: 'underline', bg: 'teal.100', color: 'teal.700' }}
                _hover={{ bg: 'teal.100', color: 'teal.700' }}
              >{t('home')}</Button>
              {isAuthenticated && user?.role === 'client' && (
                <Button as={NavLink} to="/my-packages" variant="ghost" color="white" fontWeight="medium" 
                  _activeLink={{ fontWeight: 'bold', textDecoration: 'underline', bg: 'teal.100', color: 'teal.700' }}
                  _hover={{ bg: 'teal.100', color: 'teal.700' }}
                >{t('myPackages')}</Button>
              )}
              {isAuthenticated && user?.role === 'admin' && (
                <Button as={NavLink} to="/admin" variant="ghost" color="white" fontWeight="medium" 
                  _activeLink={{ fontWeight: 'bold', textDecoration: 'underline', bg: 'teal.100', color: 'teal.700' }}
                  _hover={{ bg: 'teal.100', color: 'teal.700' }}
                >{t('admin')}</Button>
              )}
              {isAuthenticated && user?.role === 'employee' && (
                <Button as={NavLink} to="/employee" variant="ghost" color="white" fontWeight="medium" 
                  _activeLink={{ fontWeight: 'bold', textDecoration: 'underline', bg: 'teal.100', color: 'teal.700' }}
                  _hover={{ bg: 'teal.100', color: 'teal.700' }}
                >{t('employee')}</Button>
              )}
              {isAuthenticated && user?.role === 'manager' && (
                <Button as={NavLink} to="/manager" variant="ghost" color="white" fontWeight="medium" 
                  _activeLink={{ fontWeight: 'bold', textDecoration: 'underline', bg: 'teal.100', color: 'teal.700' }}
                  _hover={{ bg: 'teal.100', color: 'teal.700' }}
                >{t('manager')}</Button>
              )}
            </HStack>
          </HStack>
          <HStack spacing={2} flexShrink={0}>
            <LanguageSwitcher />
            {!isAuthenticated && (
              <Button as={NavLink} to="/login" variant="outline" colorScheme="whiteAlpha" borderColor="whiteAlpha.400" color="white" fontWeight="medium" _activeLink={{ fontWeight: 'bold', borderColor: 'teal.200', color: 'teal.100' }}>{t('login')}</Button>
            )}
            {!isAuthenticated && (
              <Button as={NavLink} to="/register" variant="solid" colorScheme="whiteAlpha" bg="white" color="teal.600" fontWeight="bold" _hover={{ bg: 'teal.100' }} _activeLink={{ fontWeight: 'bold', bg: 'teal.200', color: 'teal.700' }}>{t('register')}</Button>
            )}
            {isAuthenticated && (
              <Menu>
                <MenuButton as={Button} rightIcon={<ChevronDownIcon />} variant="ghost" color="white" px={2} _hover={{ bg: 'teal.600' }}>
                  <HStack>
                    <Avatar size="sm" name={user.firstName + ' ' + user.lastName} />
                    <Text display={{ base: 'none', md: 'block' }}>{user.firstName}</Text>
                  </HStack>
                </MenuButton>
                <MenuList>
                  <MenuItem>{t('profile')}</MenuItem>
                  <MenuItem onClick={logout}>{t('logout')}</MenuItem>
                </MenuList>
              </Menu>
            )}
          </HStack>
        </Flex>
        <Box as="main" p={6} flex="1" w="100%">
          <Routes>
            <Route path="/" element={<Home />} />
            <Route path="/login" element={<Login />} />
            <Route path="/register" element={<Register />} />
            <Route element={<ProtectedRoute allowedRoles={['client']} />}>
              <Route path="/my-packages" element={<MyPackages />} />
            </Route>
            <Route element={<ProtectedRoute allowedRoles={['admin']} />}>
              <Route path="/admin" element={<Admin />} />
            </Route>
            <Route element={<ProtectedRoute allowedRoles={['employee', 'manager', 'admin']} />}>
              <Route path="/employee" element={<Employee />} />
            </Route>
            <Route element={<ProtectedRoute allowedRoles={['manager']} />}>
              <Route path="/manager" element={<Manager />} />
            </Route>
            <Route path="/not-authorized" element={<NotAuthorized />} />
          </Routes>
        </Box>
      </Flex>
    </Router>
  );
}

export default App;
