import { useEffect, useState } from 'react';
import api from '../api/axios';
import { Box, Heading, Text, SimpleGrid, Stack, Button, useToast } from '@chakra-ui/react';
import { useTranslation } from 'react-i18next';

export default function Admin() {
  const { t } = useTranslation();
  const toast = useToast();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const fetchUsers = async () => {
      setLoading(true);
      try {
        const res = await api.get('/users');
        setUsers(res.data);
        setError('');
      } catch (err) {
        setError(t('errorLoadingUsers') || 'Error loading users');
      }
      setLoading(false);
    };
    fetchUsers();
  }, []);

  return (
    <Box>
      <Heading as="h2" size="lg" mb={4}>{t('adminDashboard')}</Heading>
      {loading ? <Text>{t('loading') || 'Loading...'}</Text> : error ? <Text color="red.500">{error}</Text> : (
        <SimpleGrid columns={{ base: 1, md: 2 }} spacing={6}>
          {users.map(user => (
            <Box key={user.id} borderWidth="1px" borderRadius="lg" p={4} bg="white" boxShadow="sm">
              <Stack spacing={1}>
                <Text fontWeight="bold">{user.firstName} {user.lastName}</Text>
                <Text>{t('email')}: {user.email}</Text>
                <Text>{t('role')}: {user.role}</Text>
                {/* Add management actions here */}
              </Stack>
            </Box>
          ))}
        </SimpleGrid>
      )}
    </Box>
  );
} 