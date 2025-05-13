import { useState } from 'react';
import { Box, SimpleGrid, Heading, Flex, Select, Input, Button, useToast, Skeleton, SkeletonText } from '@chakra-ui/react';
import { useTranslation } from 'react-i18next';
import PackageCardView from './PackageCardView';
import PackageDetailsModal from './PackageDetailsModal';
import ReviewFormModal from './ReviewFormModal';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

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
}) {
  const { t } = useTranslation();
  const [selectedPackage, setSelectedPackage] = useState(null);
  const [isModalOpen, setModalOpen] = useState(false);
  const [isReviewModalOpen, setReviewModalOpen] = useState(false);
  const [isReservationModalOpen, setReservationModalOpen] = useState(false);
  const [packageReviews, setPackageReviews] = useState({}); // { [packageId]: [reviews] }
  const toast = useToast();
  const navigate = useNavigate();
  const { isAuthenticated, user } = useAuth();

  // Local state for filter/sort inputs
  const [pendingDestination, setPendingDestination] = useState(destination);
  const [pendingMinPrice, setPendingMinPrice] = useState(minPrice);
  const [pendingMaxPrice, setPendingMaxPrice] = useState(maxPrice);
  const [pendingSortBy, setPendingSortBy] = useState(sortBy);

  // Simulate loading state
  const isLoading = false; // set to true to demo skeletons

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
  const handleSubmitReview = (review) => {
    if (!selectedPackage) return;
    setPackageReviews(prev => ({
      ...prev,
      [selectedPackage.id]: [
        ...(prev[selectedPackage.id] || []),
        { ...review, id: Date.now().toString(), createdAt: new Date().toISOString() }
      ]
    }));
    setReviewModalOpen(false);
  };

  const handleReserve = (pkg) => {
    if (isAuthenticated) {
      toast({
        title: t('reserve'),
        description: `${t('reservedAt')}: ${user?.firstName || ''} (${user?.email || ''})`,
        status: 'success',
        duration: 4000,
        isClosable: true,
      });
    } else {
      navigate('/login');
    }
  };

  // Merge reviews from local state with package reviews
  const getPackageWithReviews = (pkg) => ({
    ...pkg,
    reviews: [
      ...(pkg.reviews || []),
      ...(packageReviews[pkg.id] || [])
    ]
  });

  // Apply filters/sorting only when Apply is clicked
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
      {isLoading ? (
        <SimpleGrid columns={{ base: 1, md: 2, lg: 3 }} spacing={8}>
          {[...Array(6)].map((_, idx) => (
            <Box key={idx} borderWidth="1px" borderRadius="lg" p={4} bg="white" boxShadow="sm">
              <Skeleton height="100px" mb={2} />
              <SkeletonText mt="4" noOfLines={3} spacing="2" />
            </Box>
          ))}
        </SimpleGrid>
      ) : (
        <SimpleGrid columns={{ base: 1, sm: 2, md: 3 }} spacing={8}>
          {packages.map(pkg => (
            <PackageCardView
              key={pkg.id}
              pkg={getPackageWithReviews(pkg)}
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
        pkg={selectedPackage ? getPackageWithReviews(selectedPackage) : null}
        onAddReview={handleAddReview}
      />
      <ReviewFormModal
        isOpen={isReviewModalOpen}
        onClose={handleCloseReviewModal}
        onSubmit={handleSubmitReview}
      />
    </Box>
  );
} 