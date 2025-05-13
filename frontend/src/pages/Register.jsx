import { useState } from 'react';
import { Box, Heading, FormControl, FormLabel, Input, Button, Stack, InputGroup, InputRightElement, IconButton, FormErrorMessage, useToast } from '@chakra-ui/react';
import { ViewIcon, ViewOffIcon } from '@chakra-ui/icons';
import { useTranslation } from 'react-i18next';

export default function Register() {
  const { t } = useTranslation();
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    password: '',
    confirmPassword: '',
  });
  const [showPassword, setShowPassword] = useState(false);
  const [showConfirm, setShowConfirm] = useState(false);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const toast = useToast();

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
    setError('');
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    setIsLoading(true);
    setError('');
    if (!form.firstName || !form.lastName || !form.email || !form.password || !form.confirmPassword) {
      setError('All fields are required');
      setIsLoading(false);
      return;
    }
    if (form.password !== form.confirmPassword) {
      setError('Passwords do not match');
      setIsLoading(false);
      return;
    }
    // Simulate registration
    toast({
      title: t('registerSuccess'),
      description: t('welcome', { name: form.firstName }),
      status: 'success',
      duration: 4000,
      isClosable: true,
    });
    setForm({ firstName: '', lastName: '', email: '', password: '', confirmPassword: '' });
    setIsLoading(false);
  };

  return (
    <Box maxW="sm" mx="auto" mt={10} p={8} borderWidth={1} borderRadius="lg" boxShadow="lg" bg="white">
      <Heading as="h2" size="lg" mb={6} color="teal.700">{t('register')}</Heading>
      <form onSubmit={handleSubmit}>
        <Stack spacing={4}>
          <FormControl id="firstName" isRequired isInvalid={!!error && !form.firstName}>
            <FormLabel>{t('firstName')}</FormLabel>
            <Input name="firstName" value={form.firstName} onChange={handleChange} autoComplete="given-name" />
          </FormControl>
          <FormControl id="lastName" isRequired isInvalid={!!error && !form.lastName}>
            <FormLabel>{t('lastName')}</FormLabel>
            <Input name="lastName" value={form.lastName} onChange={handleChange} autoComplete="family-name" />
          </FormControl>
          <FormControl id="email" isRequired isInvalid={!!error && !form.email}>
            <FormLabel>{t('email')}</FormLabel>
            <Input name="email" type="email" value={form.email} onChange={handleChange} autoComplete="email" />
          </FormControl>
          <FormControl id="password" isRequired isInvalid={!!error && (!form.password || form.password !== form.confirmPassword)}>
            <FormLabel>{t('password')}</FormLabel>
            <InputGroup>
              <Input
                name="password"
                type={showPassword ? 'text' : 'password'}
                value={form.password}
                onChange={handleChange}
                autoComplete="new-password"
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
          </FormControl>
          <FormControl id="confirmPassword" isRequired isInvalid={!!error && (!form.confirmPassword || form.password !== form.confirmPassword)}>
            <FormLabel>Confirm {t('password')}</FormLabel>
            <InputGroup>
              <Input
                name="confirmPassword"
                type={showConfirm ? 'text' : 'password'}
                value={form.confirmPassword}
                onChange={handleChange}
                autoComplete="new-password"
                placeholder="••••••••"
              />
              <InputRightElement>
                <IconButton
                  aria-label={showConfirm ? 'Hide password' : 'Show password'}
                  icon={showConfirm ? <ViewOffIcon /> : <ViewIcon />}
                  size="sm"
                  variant="ghost"
                  onClick={() => setShowConfirm((v) => !v)}
                  tabIndex={-1}
                />
              </InputRightElement>
            </InputGroup>
            {error && <FormErrorMessage>{error}</FormErrorMessage>}
          </FormControl>
          <Button colorScheme="teal" type="submit" size="md" isLoading={isLoading} w="100%" mt={2}>{t('register')}</Button>
        </Stack>
      </form>
    </Box>
  );
} 