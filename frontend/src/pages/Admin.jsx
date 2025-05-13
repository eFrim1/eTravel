import { Box, Heading, Text } from '@chakra-ui/react';
import { useTranslation } from 'react-i18next';

export default function Admin() {
  const { t } = useTranslation();
  return (
    <Box>
      <Heading as="h2" size="lg" mb={4}>{t('adminDashboard')}</Heading>
      <Text>{t('adminWelcome')}</Text>
    </Box>
  );
} 