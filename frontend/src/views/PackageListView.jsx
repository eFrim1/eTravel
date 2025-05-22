import { useState } from 'react';
import { Box, SimpleGrid, Heading, Flex, Select, Input, Button, useToast, Skeleton, SkeletonText, Alert, AlertIcon } from '@chakra-ui/react';
import { useTranslation } from 'react-i18next';
import PackageCardView from './PackageCardView';
import PackageDetailsModal from './PackageDetailsModal';
import ReviewFormModal from './ReviewFormModal';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import api from '../api/axios';

export default function PackageListView({
  packages,
  destination,
  setDestination,
  minPrice,
  setMinPrice,
  maxPrice,
  setMaxPrice,
  sortBy,
  setSortBy,
  uniqueDestinations,
  resetFilters,
  loading,
  error,
}) {
  const { t } = useTranslation();
  const [selectedPackage, setSelectedPackage] = useState(null);
  const [isModalOpen, setModalOpen] = useState(false);
  const [isReviewModalOpen, setReviewModalOpen] = useState(false);
  const toast = useToast();
  const navigate = useNavigate();
  const { isAuthenticated, user } = useAuth();

  // Local state for filter/sort inputs
  const [pendingDestination, setPendingDestination] = useState(destination);
  const [pendingMinPrice, setPendingMinPrice] = useState(minPrice);
  const [pendingMaxPrice, setPendingMaxPrice] = useState(maxPrice);
  const [pendingSortBy, setPendingSortBy] = useState(sortBy);

  const [reviewRefreshKey, setReviewRefreshKey] = useState(0);

  const handleOpenModal = (pkg) => {
    setSelectedPackage(pkg);
    setModalOpen(true);
  };
  const handleCloseModal = () => {
    setModalOpen(false);
    setSelectedPackage(null);
  };

  const handleAddReview = () => {
    setReviewModalOpen(true);
  };
  const handleCloseReviewModal = () => {
    setReviewModalOpen(false);
  };

  const handleReviewSubmitted = () => {
    setReviewModalOpen(false);
    setReviewRefreshKey(k => k + 1);
  };

  const handleReserve = async (pkg) => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    try {
      toast({ title: t('reserve'), description: t('processing'), status: 'info', duration: 1500 });
      await api.post('/reservations', { tourPackageId: pkg.id, clientId: user.id });
      toast({
        title: t('reserve'),
        description: t('reservationSuccess') || 'Reservation successful!',
        status: 'success',
        duration: 4000,
        isClosable: true,
      });
    } catch (err) {
      toast({
        title: t('reserve'),
        description: t('reservationError') || 'Reservation failed.',
        status: 'error',
        duration: 4000,
        isClosable: true,
      });
    }
  };

  const handleApplyFilters = () => {
    setDestination(pendingDestination);
    setMinPrice(pendingMinPrice);
    setMaxPrice(pendingMaxPrice);
    setSortBy(pendingSortBy);
  };

  return (
    <Box maxW="1200px" mx="auto" w="100%">
      <Heading as="h3" size="md" mb={4}>{t('availablePackages')}</Heading>
      <Flex gap={4} mb={4} wrap="wrap" align="end">
        <Select
          placeholder={t('allDestinations')}
          value={pendingDestination}
          onChange={(e) => setPendingDestination(e.target.value)}
          maxW="200px"
        >
          {uniqueDestinations.map((dest) => (
            <option key={dest} value={dest}>{dest}</option>
          ))}
        </Select>
        <Input
          placeholder={t('minPrice')}
          type="number"
          value={pendingMinPrice}
          onChange={(e) => setPendingMinPrice(e.target.value)}
          maxW="120px"
        />
        <Input
          placeholder={t('maxPrice')}
          type="number"
          value={pendingMaxPrice}
          onChange={(e) => setPendingMaxPrice(e.target.value)}
          maxW="120px"
        />
        <Select
          value={pendingSortBy}
          onChange={(e) => setPendingSortBy(e.target.value)}
          maxW="180px"
        >
          <option value="destination">{t('sortByDestination')}</option>
          <option value="period">{t('sortByPeriod')}</option>
          <option value="price">{t('sortByPrice')}</option>
        </Select>
        <Button onClick={handleApplyFilters} colorScheme="teal" minW="100px" textTransform="capitalize">
          {t('apply') || 'Apply'}
        </Button>
        <Button onClick={resetFilters} variant="outline" colorScheme="teal" textTransform="capitalize">
          {t('reset')}
        </Button>
      </Flex>
      {loading ? (
        <SimpleGrid columns={{ base: 1, md: 2, lg: 3 }} spacing={8}>
          {[...Array(6)].map((_, idx) => (
            <Box key={idx} borderWidth="1px" borderRadius="lg" p={4} bg="white" boxShadow="sm">
              <Skeleton height="100px" mb={2} />
              <SkeletonText mt="4" noOfLines={3} spacing="2" />
            </Box>
          ))}
        </SimpleGrid>
      ) : error ? (
        <Alert status="error" mb={4}><AlertIcon />{error}</Alert>
      ) : (
        <SimpleGrid columns={{ base: 1, sm: 2, md: 3 }} spacing={8}>
          {packages.map(pkg => (
            <PackageCardView
              key={pkg.id}
              pkg={pkg}
              onViewDetails={() => handleOpenModal(pkg)}
              onReserve={() => handleReserve(pkg)}
              isAuthenticated={isAuthenticated}
            />
          ))}
        </SimpleGrid>
      )}
      <PackageDetailsModal
        isOpen={isModalOpen}
        onClose={handleCloseModal}
        pkg={selectedPackage}
        onAddReview={handleAddReview}
        key={selectedPackage ? selectedPackage.id + '-' + reviewRefreshKey : 'none'}
      />
      <ReviewFormModal
        isOpen={isReviewModalOpen}
        onClose={handleCloseReviewModal}
        onSubmit={handleReviewSubmitted}
        pkgId={selectedPackage?.id}
      />
    </Box>
  );
} 