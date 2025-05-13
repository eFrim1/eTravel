import { Box, Heading, SimpleGrid, Text, Stack, Skeleton, SkeletonText, Button } from '@chakra-ui/react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../context/AuthContext';

const mockReservations = [
  {
    id: 'r1',
    reservedAt: '2024-06-01T10:00:00Z',
    package: {
      destination: 'Paris',
      startDate: '2024-07-01',
      endDate: '2024-07-07',
      price: '1200',
    },
  },
  {
    id: 'r2',
    reservedAt: '2024-06-10T15:30:00Z',
    package: {
      destination: 'Rome',
      startDate: '2024-08-10',
      endDate: '2024-08-17',
      price: '1000',
    },
  },
];

export default function MyPackages() {
  const { t } = useTranslation();
  const { user } = useAuth();
  // Simulate loading state
  const isLoading = false; // set to true to demo skeletons

  // Mock export to Word handler
  const handleExportToWord = () => {
    // Here you would implement actual export logic
    alert('Export to Word triggered!');
  };

  return (
    <Box>
      <Heading as="h2" size="lg" mb={4}>{t('myPackages')}</Heading>
      {/* Page title for navigation context */}
      <Heading as="h3" size="md" mb={4} color="teal.600">{t('yourReservations')}</Heading>
      {user?.role === 'client' && (
        <Button colorScheme="teal" mb={4} onClick={handleExportToWord}>{t('exportToWord') || 'Export to Word'}</Button>
      )}
      {isLoading ? (
        <SimpleGrid columns={{ base: 1, md: 2 }} spacing={6}>
          {[...Array(2)].map((_, idx) => (
            <Box key={idx} borderWidth="1px" borderRadius="lg" p={4} bg="white" boxShadow="sm">
              <Skeleton height="24px" mb={2} />
              <SkeletonText mt="4" noOfLines={3} spacing="2" />
            </Box>
          ))}
        </SimpleGrid>
      ) : mockReservations.length === 0 ? (
        <Text color="gray.500">{t('noReservations')}</Text>
      ) : (
        <SimpleGrid columns={{ base: 1, md: 2 }} spacing={6}>
          {mockReservations.map(res => (
            <Box key={res.id} borderWidth="1px" borderRadius="lg" p={4} bg="white" boxShadow="sm">
              <Stack spacing={1}>
                <Text fontWeight="bold">{res.package.destination}</Text>
                <Text>{t('period')}: {res.package.startDate} to {res.package.endDate}</Text>
                <Text>{t('price')}: ${res.package.price}</Text>
                <Text fontSize="sm" color="gray.500">{t('reservedAt')}: {new Date(res.reservedAt).toLocaleString()}</Text>
              </Stack>
            </Box>
          ))}
        </SimpleGrid>
      )}
    </Box>
  );
} 