import usePackageList from '../viewmodels/usePackageList';
import PackageListView from '../views/PackageListView';
import { Heading, Box } from '@chakra-ui/react';
import { useTranslation } from 'react-i18next';

export default function Home() {
  const { t } = useTranslation();
  const packageList = usePackageList();
  return (
    <Box>
      <Heading as="h2" size="lg" mb={6} color="teal.700" fontWeight="bold">{t('browsePackages')}</Heading>
      <PackageListView {...packageList} />
    </Box>
  );
} 