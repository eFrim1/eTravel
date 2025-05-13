import { Box, Heading, Text } from '@chakra-ui/react';
import { useTranslation } from 'react-i18next';

export default function Employee() {
  const { t } = useTranslation();
  return (
    <Box>
      <Heading as="h2" size="lg" mb={4}>{t('employeeDashboard')}</Heading>
      <Text>{t('employeeWelcome')}</Text>
    </Box>
  );
} 