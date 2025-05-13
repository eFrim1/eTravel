import { Box, Heading, Text } from '@chakra-ui/react';
import { useTranslation } from 'react-i18next';

export default function Manager() {
  const { t } = useTranslation();
  return (
    <Box>
      <Heading as="h2" size="lg" mb={4}>{t('managerDashboard')}</Heading>
      <Text>{t('managerWelcome')}</Text>
    </Box>
  );
} 