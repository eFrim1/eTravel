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
    const result = await login({ email: form.email, password: form.password });
    if (result.success) {
      toast({ title: t('welcome', { name: form.email }), status: 'success', duration: 3000, isClosable: true });
      navigate('/');
    } else {
      setError(result.message || 'Invalid email or password');
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