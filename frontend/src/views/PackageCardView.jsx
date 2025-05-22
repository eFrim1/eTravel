import { Box, Image, Text, Stack, Button, Flex, Badge, Icon, HStack, Tooltip } from '@chakra-ui/react';
import { StarIcon } from '@chakra-ui/icons';
import { useTranslation } from 'react-i18next';

export default function PackageCardView({ pkg, onViewDetails, onReserve, isAuthenticated }) {
  const { t } = useTranslation();
  // Calculate average rating
  const avgRating = pkg.reviews && pkg.reviews.length > 0
    ? (pkg.reviews.reduce((sum, r) => sum + Number(r.rating), 0) / pkg.reviews.length).toFixed(1)
    : null;

  const hasImages = Array.isArray(pkg.images) && pkg.images.length > 0;
  const mainImage = hasImages ? pkg.images[0].trim() : 'https://storage.googleapis.com/proudcity/mebanenc/uploads/2021/03/placeholder-image.png';

  return (
    <Box
      borderWidth="1px"
      borderRadius="lg"
      overflow="hidden"
      bg="white"
      boxShadow="md"
      p={0}
      transition="box-shadow 0.2s"
      _hover={{ boxShadow: 'xl', transform: 'translateY(-2px)' }}
      w="100%"
    >
      <Image
        src={mainImage}
        alt={pkg.destination}
        w="100%"
        h="180px"
        objectFit="cover"
        borderTopRadius="lg"
        onError={e => { e.target.onerror = null; e.target.src = 'https://storage.googleapis.com/proudcity/mebanenc/uploads/2021/03/placeholder-image.png'; }}
      />
      <Stack spacing={2} p={4}>
        <Flex align="center" justify="space-between">
          <Text fontWeight="bold" fontSize="lg">{pkg.destination}</Text>
          {avgRating && (
            <Flex align="center" gap={1}>
              <Icon as={StarIcon} color="yellow.400" boxSize={4} />
              <Text fontSize="sm" fontWeight="semibold">{avgRating}</Text>
            </Flex>
          )}
        </Flex>
        <Text color="gray.600" fontSize="sm">{pkg.startDate} - {pkg.endDate}</Text>
        <Badge colorScheme="teal" fontSize="md" w="fit-content">${pkg.price}</Badge>
        <HStack spacing={2} mt={2} justify="flex-end">
          <Button
            colorScheme="teal"
            variant="outline"
            size="sm"
            onClick={onViewDetails}
            textTransform="capitalize"
          >
            {t('viewDetails')}
          </Button>
          <Tooltip label={!isAuthenticated ? t('login') + ' ' + t('toReserve') : ''} isDisabled={isAuthenticated} hasArrow>
            <Button
              colorScheme="blue"
              size="sm"
              onClick={onReserve}
              textTransform="capitalize"
              isDisabled={!isAuthenticated}
            >
              {t('reserve')}
            </Button>
          </Tooltip>
        </HStack>
      </Stack>
    </Box>
  );
} 