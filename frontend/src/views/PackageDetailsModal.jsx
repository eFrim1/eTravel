import {
  Modal,
  ModalOverlay,
  ModalContent,
  ModalHeader,
  ModalCloseButton,
  ModalBody,
  ModalFooter,
  Box,
  Image,
  Text,
  Stack,
  Button,
  Heading,
  Divider,
  Flex,
  SimpleGrid,
  Avatar
} from "@chakra-ui/react";
import { useTranslation } from "react-i18next";
import { useAuth } from "../context/AuthContext";

export default function PackageDetailsModal({ isOpen, onClose, pkg, onAddReview }) {
  const { t } = useTranslation();
  const { user, isAuthenticated } = useAuth();
  if (!pkg) return null;

  // Only show Add Review if user is a client and has purchased this package
  const canAddReview =
    isAuthenticated &&
    user?.role === 'client' &&
    Array.isArray(user.purchasedPackages) &&
    user.purchasedPackages.includes(pkg.id);

  return (
    <Modal isOpen={isOpen} onClose={onClose} size="xl">
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>{pkg.destination}</ModalHeader>
        <ModalCloseButton />
        <ModalBody p={{ base: 2, md: 6 }}>
          <Flex direction={{ base: 'column', md: 'row' }} gap={8}>
            {/* Images column */}
            <Box flexShrink={0} w={{ base: '100%', md: '320px' }}>
              <Image
                src={pkg.images[0]}
                alt={pkg.destination}
                w="100%"
                h="220px"
                objectFit="cover"
                borderRadius="lg"
                mb={2}
              />
              <SimpleGrid columns={pkg.images.length > 1 ? pkg.images.length : 1} spacing={2}>
                {pkg.images.slice(1).map((img, idx) => (
                  <Image
                    key={idx}
                    src={img}
                    alt={pkg.destination}
                    boxSize="60px"
                    objectFit="cover"
                    borderRadius="md"
                  />
                ))}
              </SimpleGrid>
            </Box>
            {/* Details and reviews column */}
            <Box flex="1">
              <Stack spacing={3} mb={4}>
                <Text fontWeight="bold" fontSize="lg">{pkg.destination}</Text>
                <Text color="gray.600">{t('period')}: {pkg.startDate} to {pkg.endDate}</Text>
                <Text color="teal.600" fontWeight="bold">{t('price')}: ${pkg.price}</Text>
              </Stack>
              <Divider my={2} />
              <Heading as="h4" size="sm" mb={2}>{t('reviews')}</Heading>
              <Stack spacing={3} maxH="220px" overflowY="auto">
                {pkg.reviews && pkg.reviews.length > 0 ? (
                  pkg.reviews.map((review, idx) => (
                    <Flex key={review.id || idx} align="flex-start" gap={3} p={2} borderWidth="1px" borderRadius="md" bg="gray.50">
                      <Avatar size="sm" name={review.clientId || 'User'} />
                      <Box>
                        <Flex align="center" gap={1} mb={1}>
                          <Text fontWeight="bold" fontSize="sm">{t('rating')}: {review.rating}</Text>
                          <Text fontSize="xs" color="gray.500">{new Date(review.createdAt).toLocaleDateString()}</Text>
                        </Flex>
                        <Text fontSize="sm">{review.comment}</Text>
                      </Box>
                    </Flex>
                  ))
                ) : (
                  <Text color="gray.500">{t('noReviews')}</Text>
                )}
              </Stack>
            </Box>
          </Flex>
        </ModalBody>
        <ModalFooter>
          {canAddReview && (
            <Button colorScheme="teal" mr={3} onClick={onAddReview}>
              {t("addReview")}
            </Button>
          )}
        </ModalFooter>
      </ModalContent>
    </Modal>
  );
} 