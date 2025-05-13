import { Box, Heading, Text } from '@chakra-ui/react';
import { useTranslation } from 'react-i18next';

export default function NotAuthorized() {
  const { t } = useTranslation();
  return (
    <Box p={8} textAlign="center">
      <Heading>{t('notAuthorized') || 'Not Authorized'}</Heading>
      <Text mt={4}>{t('notAuthorizedMessage') || 'You do not have permission to view this page.'}</Text>
    </Box>
  );
} 