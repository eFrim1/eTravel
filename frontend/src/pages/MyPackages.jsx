import { useEffect, useState } from 'react';
import { Box, Heading, SimpleGrid, Text, Stack, Skeleton, SkeletonText, Button, Alert, AlertIcon } from '@chakra-ui/react';
import { useTranslation } from 'react-i18next';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';
import ReviewFormModal from '../views/ReviewFormModal';

export default function MyPackages() {
  const { t } = useTranslation();
  const { user } = useAuth();
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [isReviewModalOpen, setReviewModalOpen] = useState(false);
  const [reviewPkgId, setReviewPkgId] = useState(null);

  useEffect(() => {
    setLoading(true);
    if (user?.id) {
      api.get(`/reservations/by-client/${user.id}`)
        .then(async res => {
          const reservations = res.data;
          // Fetch package details for each reservation
          const reservationsWithPackages = await Promise.all(reservations.map(async reservation => {
            const pkgId = reservation.tourPackageId || reservation.packageId || reservation.package?.id;
            if (!pkgId) return { ...reservation, package: null };
            try {
              const pkgRes = await api.get(`/catalog/${pkgId}`);
              return { ...reservation, package: pkgRes.data };
            } catch {
              return { ...reservation, package: null };
            }
          }));
          setReservations(reservationsWithPackages);
          setError('');
        })
        .catch(() => setError(t('errorLoadingReservations') || 'Error loading reservations'))
        .finally(() => setLoading(false));
    } else {
      setReservations([]);
      setLoading(false);
    }
  }, [user?.id]);

  const handleExportToWord = async () => {
    if (!reservations.length) return;
    // Collect all reserved package ids
    const ids = reservations
      .map(res => res.package?.id)
      .filter(Boolean);
    if (!ids.length) return;
    try {
      const res = await api.post('/catalog/export/reservations', ids, { responseType: 'blob' });
      const url = window.URL.createObjectURL(new Blob([res.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'my-reservations.docx');
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);
    } catch (err) {
      alert('Export failed!');
    }
  };

  const handleAddReview = (pkgId) => {
    setReviewPkgId(pkgId);
    setReviewModalOpen(true);
  };

  return (
    <Box>
      <Heading as="h2" size="lg" mb={4}>{t('myPackages')}</Heading>
      {/* Page title for navigation context */}
      <Heading as="h3" size="md" mb={4} color="teal.600">{t('yourReservations')}</Heading>
      {user?.role === 'client' || user?.role === 'employee' ? (
        <Button colorScheme="teal" mb={4} onClick={handleExportToWord}>{t('exportToWord') || 'Export to Word'}</Button>
      ) : null}
      {loading ? (
        <SimpleGrid columns={{ base: 1, md: 2 }} spacing={6}>
          {[...Array(2)].map((_, idx) => (
            <Box key={idx} borderWidth="1px" borderRadius="lg" p={4} bg="white" boxShadow="sm">
              <Skeleton height="24px" mb={2} />
              <SkeletonText mt="4" noOfLines={3} spacing="2" />
            </Box>
          ))}
        </SimpleGrid>
      ) : error ? (
        <Alert status="error" mb={4}><AlertIcon />{error}</Alert>
      ) : reservations.length === 0 ? (
        <Text color="gray.500">{t('noReservations')}</Text>
      ) : (
        <SimpleGrid columns={{ base: 1, md: 2 }} spacing={6}>
          {reservations.map(res => (
            <Box key={res.id} borderWidth="1px" borderRadius="lg" p={4} bg="white" boxShadow="sm">
              <Stack spacing={1}>
                <Text fontWeight="bold">{res.package?.destination || '-'}</Text>
                <Text>{t('period')}: {res.package?.startDate} to {res.package?.endDate}</Text>
                <Text>{t('price')}: ${res.package?.price}</Text>
                <Text fontSize="sm" color="gray.500">{t('reservedAt')}: {new Date(res.reservedAt).toLocaleString()}</Text>
                {res.package && (
                  <Button size="sm" colorScheme="teal" mt={2} onClick={() => handleAddReview(res.package.id)}>
                    {t('addReview')}
                  </Button>
                )}
              </Stack>
            </Box>
          ))}
        </SimpleGrid>
      )}
      <ReviewFormModal
        isOpen={isReviewModalOpen}
        onClose={() => setReviewModalOpen(false)}
        onSubmit={() => setReviewModalOpen(false)}
        pkgId={reviewPkgId}
        user={user}
      />
    </Box>
  );
} 