import { useEffect, useState } from 'react';
import api from '../api/axios';
import {
  Box, Heading, Text, Button, SimpleGrid, Stack, Input, FormControl, FormLabel, useToast, Modal, ModalOverlay, ModalContent, ModalHeader, ModalCloseButton, ModalBody, ModalFooter, Tabs, TabList, TabPanels, Tab, TabPanel, IconButton, Card, CardBody, CardHeader, CardFooter, Textarea, Tag, HStack, Flex
} from '@chakra-ui/react';
import { AddIcon, EmailIcon, CalendarIcon, DownloadIcon, EditIcon, ViewIcon, StarIcon } from '@chakra-ui/icons';
import { FaBoxOpen, FaUserFriends, FaHistory, FaEnvelope, FaFileExport, FaChartBar } from 'react-icons/fa';
import { useTranslation } from 'react-i18next';
import emailjs from 'emailjs-com';
import {
  BarChart, Bar, XAxis, YAxis, Tooltip, PieChart, Pie, Cell, ResponsiveContainer, LineChart, Line, Legend
} from 'recharts';

export default function Statistics() {
  const { t } = useTranslation();
  const toast = useToast();
  // Employee/Manager features state (same as Employee)
  const [packages, setPackages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [isModalOpen, setModalOpen] = useState(false);
  const [editPkg, setEditPkg] = useState(null);
  const [form, setForm] = useState({ destination: '', price: '', startDate: '', endDate: '', images: '' });
  const [clients, setClients] = useState([]);
  const [allPackages, setAllPackages] = useState([]);
  const [selectedClient, setSelectedClient] = useState('');
  const [selectedPackage, setSelectedPackage] = useState('');
  const [reserveLoading, setReserveLoading] = useState(false);
  const [clientForm, setClientForm] = useState({ username: '', firstName: '', lastName: '', email: '', password: '' });
  const [editingClient, setEditingClient] = useState(null);
  const [clientLoading, setClientLoading] = useState(false);
  const [resStart, setResStart] = useState('');
  const [resEnd, setResEnd] = useState('');
  const [allReservations, setAllReservations] = useState([]);
  const [filteredReservations, setFilteredReservations] = useState([]);
  const [resLoading, setResLoading] = useState(false);
  const [resError, setResError] = useState('');
  const [selectedClients, setSelectedClients] = useState([]);
  const [offerSubject, setOfferSubject] = useState('');
  const [offerMessage, setOfferMessage] = useState('');
  const [notifyLoading, setNotifyLoading] = useState(false);
  const [showPreview, setShowPreview] = useState(false);
  const [exportFormat, setExportFormat] = useState('');
  const [exportLoading, setExportLoading] = useState(false);
  const [detailsModalOpen, setDetailsModalOpen] = useState(false);
  const [selectedReservation, setSelectedReservation] = useState(null);
  const [modalPackage, setModalPackage] = useState(null);
  const [modalLoading, setModalLoading] = useState(false);
  const [modalError, setModalError] = useState('');

  // Statistics state
  const [statsLoading, setStatsLoading] = useState(true);
  const [statsError, setStatsError] = useState('');
  const [statsReservations, setStatsReservations] = useState([]);
  const [statsPackages, setStatsPackages] = useState([]);

  // Fetch all data for statistics
  useEffect(() => {
    setStatsLoading(true);
    Promise.all([
      api.get('/catalog'),
      api.get('/reservations'),
    ]).then(([pkgRes, resRes]) => {
      setStatsPackages(pkgRes.data);
      setStatsReservations(resRes.data);
      setStatsError('');
    }).catch(() => setStatsError('Error loading statistics'))
      .finally(() => setStatsLoading(false));
  }, []);

  // ... (all Employee features code, unchanged, omitted for brevity) ...

  // --- Statistics Computation ---
  // 1. Reservations per Package (Bar)
  const reservationsPerPackage = statsPackages.map(pkg => ({
    name: pkg.destination,
    count: statsReservations.filter(r => (r.tourPackageId || r.packageId) === pkg.id).length
  }));
  // 2. Reservations per Month (Line)
  const reservationsPerMonth = (() => {
    const months = {};
    statsReservations.forEach(r => {
      const d = new Date(r.reservedAt);
      const key = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}`;
      months[key] = (months[key] || 0) + 1;
    });
    return Object.entries(months).map(([month, count]) => ({ month, count }));
  })();
  // 3. Package Popularity by Destination (Pie)
  const destinationCounts = statsPackages.reduce((acc, pkg) => {
    acc[pkg.destination] = (acc[pkg.destination] || 0) + statsReservations.filter(r => (r.tourPackageId || r.packageId) === pkg.id).length;
    return acc;
  }, {});
  const destinationPie = Object.entries(destinationCounts).map(([name, value]) => ({ name, value }));
  // 4. Revenue per Package (Bar)
  const revenuePerPackage = statsPackages.map(pkg => ({
    name: pkg.destination,
    revenue: statsReservations.filter(r => (r.tourPackageId || r.packageId) === pkg.id).length * Number(pkg.price || 0)
  }));

  // Colors for pie chart
  const pieColors = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#A28CFF', '#FF6F91', '#6FFFBF', '#FFD36E'];

  return (
    <Box>
      <Heading as="h2" size="lg" mb={6} textAlign="center">{t('statisticsDashboard') || 'Statistics Dashboard'}</Heading>
      <Tabs variant="enclosed-colored" colorScheme="teal" isFitted>
        <TabList mb={4}>
          <Tab><Box as={FaBoxOpen} mr={2} />{t('managePackages')}</Tab>
          <Tab><Box as={FaUserFriends} mr={2} />{t('manageReservations')}</Tab>
          <Tab><Box as={FaUserFriends} mr={2} />{t('manageClients')}</Tab>
          <Tab><Box as={FaHistory} mr={2} />{t('reservationHistory')}</Tab>
          <Tab><Box as={FaEnvelope} mr={2} />{t('emailClients')}</Tab>
          <Tab><Box as={FaFileExport} mr={2} />{t('exportData')}</Tab>
          <Tab><Box as={FaChartBar} mr={2} />{t('statistics') || 'Statistics'}</Tab>
        </TabList>
        <TabPanels>
          {/* ...Employee tabs unchanged... */}
          <TabPanel px={0} py={4}>
            {/* --- Statistics Tab --- */}
            <Box>
              <Heading as="h3" size="md" mb={4}>{t('statistics') || 'Statistics'}</Heading>
              {statsLoading ? (
                <Text>{t('loading') || 'Loading...'}</Text>
              ) : statsError ? (
                <Text color="red.500">{statsError}</Text>
              ) : (
                <SimpleGrid columns={{ base: 1, md: 2 }} spacing={8}>
                  {/* 1. Reservations per Package (Bar) */}
                  <Box p={4} bg="white" borderRadius="md" boxShadow="md">
                    <Heading as="h4" size="sm" mb={2}>{t('reservationsPerPackage') || 'Reservations per Package'}</Heading>
                    <ResponsiveContainer width="100%" height={250}>
                      <BarChart data={reservationsPerPackage}>
                        <XAxis dataKey="name" />
                        <YAxis allowDecimals={false} />
                        <Tooltip />
                        <Bar dataKey="count" fill="#3182ce" />
                      </BarChart>
                    </ResponsiveContainer>
                  </Box>
                  {/* 2. Reservations per Month (Line) */}
                  <Box p={4} bg="white" borderRadius="md" boxShadow="md">
                    <Heading as="h4" size="sm" mb={2}>{t('reservationsPerMonth') || 'Reservations per Month'}</Heading>
                    <ResponsiveContainer width="100%" height={250}>
                      <LineChart data={reservationsPerMonth}>
                        <XAxis dataKey="month" />
                        <YAxis allowDecimals={false} />
                        <Tooltip />
                        <Line type="monotone" dataKey="count" stroke="#00C49F" strokeWidth={2} />
                        <Legend />
                      </LineChart>
                    </ResponsiveContainer>
                  </Box>
                  {/* 3. Package Popularity by Destination (Pie) */}
                  <Box p={4} bg="white" borderRadius="md" boxShadow="md">
                    <Heading as="h4" size="sm" mb={2}>{t('packagePopularity') || 'Package Popularity by Destination'}</Heading>
                    <ResponsiveContainer width="100%" height={250}>
                      <PieChart>
                        <Pie data={destinationPie} dataKey="value" nameKey="name" cx="50%" cy="50%" outerRadius={80} label>
                          {destinationPie.map((entry, idx) => (
                            <Cell key={`cell-${idx}`} fill={pieColors[idx % pieColors.length]} />
                          ))}
                        </Pie>
                        <Tooltip />
                        <Legend />
                      </PieChart>
                    </ResponsiveContainer>
                  </Box>
                  {/* 4. Revenue per Package (Bar) */}
                  <Box p={4} bg="white" borderRadius="md" boxShadow="md">
                    <Heading as="h4" size="sm" mb={2}>{t('revenuePerPackage') || 'Revenue per Package'}</Heading>
                    <ResponsiveContainer width="100%" height={250}>
                      <BarChart data={revenuePerPackage}>
                        <XAxis dataKey="name" />
                        <YAxis allowDecimals={false} />
                        <Tooltip />
                        <Bar dataKey="revenue" fill="#FF8042" />
                      </BarChart>
                    </ResponsiveContainer>
                  </Box>
                </SimpleGrid>
              )}
            </Box>
          </TabPanel>
        </TabPanels>
      </Tabs>
    </Box>
  );
} 