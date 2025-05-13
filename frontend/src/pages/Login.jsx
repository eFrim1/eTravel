import { useState } from 'react';
import { Heading, Box, FormControl, FormLabel, Input, Button, Stack, InputGroup, InputRightElement, IconButton, FormErrorMessage, useToast } from '@chakra-ui/react';
import { ViewIcon, ViewOffIcon } from '@chakra-ui/icons';
import { useAuth } from '../context/AuthContext';
import { useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';

export default function Login() {
  const { t } = useTranslation();
  const { login } = useAuth();
  const navigate = useNavigate();
  const toast = useToast();
  const [form, setForm] = useState({ email: '', password: '' });
  const [showPassword, setShowPassword] = useState(false);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError('');
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError('');
    // Mock authentication logic
    if (form.email === 'client@email.com' && form.password === 'client') {
      login({
        id: 1,
        firstName: 'John',
        lastName: 'Doe',
        email: form.email,
        role: 'client',
        purchasedPackages: [1, 2],
      });
      toast({ title: t('welcome', { name: 'John' }), status: 'success', duration: 3000, isClosable: true });
      navigate('/');
    } else if (form.email === 'admin@email.com' && form.password === 'admin') {
      login({ id: 2, firstName: 'Admin', lastName: 'User', email: form.email, role: 'admin' });
      toast({ title: t('welcome', { name: 'Admin' }), status: 'success', duration: 3000, isClosable: true });
      navigate('/');
    } else if (form.email === 'employee@email.com' && form.password === 'employee') {
      login({ id: 3, firstName: 'Employee', lastName: 'User', email: form.email, role: 'employee' });
      toast({ title: t('welcome', { name: 'Employee' }), status: 'success', duration: 3000, isClosable: true });
      navigate('/');
    } else if (form.email === 'manager@email.com' && form.password === 'manager') {
      login({ id: 4, firstName: 'Manager', lastName: 'User', email: form.email, role: 'manager' });
      toast({ title: t('welcome', { name: 'Manager' }), status: 'success', duration: 3000, isClosable: true });
      navigate('/');
    } else {
      setError('Invalid email or password');
    }
    setIsLoading(false);
  };

  return (
    <Box maxW="sm" mx="auto" mt={10} p={8} borderWidth={1} borderRadius="lg" boxShadow="lg" bg="white">
      <Heading as="h2" size="lg" mb={6} color="teal.700">{t('login')}</Heading>
      <form onSubmit={handleSubmit}>
        <Stack spacing={4}>
          <FormControl id="email" isRequired isInvalid={!!error}>
            <FormLabel>{t('email')}</FormLabel>
            <Input
              name="email"
              type="email"
              value={form.email}
              onChange={handleChange}
              autoComplete="email"
              placeholder="you@email.com"
            />
          </FormControl>
          <FormControl id="password" isRequired isInvalid={!!error}>
            <FormLabel>{t('password')}</FormLabel>
            <InputGroup>
              <Input
                name="password"
                type={showPassword ? 'text' : 'password'}
                value={form.password}
                onChange={handleChange}
                autoComplete="current-password"
                placeholder="••••••••"
              />
              <InputRightElement>
                <IconButton
                  aria-label={showPassword ? 'Hide password' : 'Show password'}
                  icon={showPassword ? <ViewOffIcon /> : <ViewIcon />}
                  size="sm"
                  variant="ghost"
                  onClick={() => setShowPassword((v) => !v)}
                  tabIndex={-1}
                />
              </InputRightElement>
            </InputGroup>
            {error && <FormErrorMessage>{error}</FormErrorMessage>}
          </FormControl>
          <Button type="submit" colorScheme="teal" size="md" isLoading={isLoading} w="100%" mt={2}>
            {t('login')}
          </Button>
        </Stack>
      </form>
    </Box>
  );
} 