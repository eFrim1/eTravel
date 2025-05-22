import { useEffect, useState } from 'react';
import api from '../api/axios';
import {
  Box, Heading, Text, Button, SimpleGrid, Stack, Input, FormControl, FormLabel, useToast, Modal, ModalOverlay, ModalContent, ModalHeader, ModalCloseButton, ModalBody, ModalFooter, Tabs, TabList, TabPanels, Tab, TabPanel, IconButton, Card, CardBody, CardHeader, CardFooter, Textarea, Tag, HStack, Flex
} from '@chakra-ui/react';
import { AddIcon, EmailIcon, CalendarIcon, DownloadIcon, EditIcon, ViewIcon, StarIcon } from '@chakra-ui/icons';
import { FaBoxOpen, FaUserFriends, FaHistory, FaEnvelope, FaFileExport } from 'react-icons/fa';
import { useTranslation } from 'react-i18next';
import emailjs from 'emailjs-com';

export default function Employee() {
  const { t } = useTranslation();
  const toast = useToast();
  // Package CRUD
  const [packages, setPackages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [isModalOpen, setModalOpen] = useState(false);
  const [editPkg, setEditPkg] = useState(null);
  const [form, setForm] = useState({ destination: '', price: '', startDate: '', endDate: '', images: '' });
  // Reservation for client
  const [clients, setClients] = useState([]);
  const [allPackages, setAllPackages] = useState([]);
  const [selectedClient, setSelectedClient] = useState('');
  const [selectedPackage, setSelectedPackage] = useState('');
  const [reserveLoading, setReserveLoading] = useState(false);
  // Client CRUD
  const [clientForm, setClientForm] = useState({ username: '', firstName: '', lastName: '', email: '', password: '' });
  const [editingClient, setEditingClient] = useState(null);
  const [clientLoading, setClientLoading] = useState(false);
  // Reservation by period
  const [resStart, setResStart] = useState('');
  const [resEnd, setResEnd] = useState('');
  const [allReservations, setAllReservations] = useState([]);
  const [filteredReservations, setFilteredReservations] = useState([]);
  const [resLoading, setResLoading] = useState(false);
  const [resError, setResError] = useState('');
  // Notify clients
  const [selectedClients, setSelectedClients] = useState([]);
  const [offerSubject, setOfferSubject] = useState('');
  const [offerMessage, setOfferMessage] = useState('');
  const [notifyLoading, setNotifyLoading] = useState(false);
  const [showPreview, setShowPreview] = useState(false);
  // Export
  const [exportFormat, setExportFormat] = useState('');
  const [exportLoading, setExportLoading] = useState(false);
  const [detailsModalOpen, setDetailsModalOpen] = useState(false);
  const [selectedReservation, setSelectedReservation] = useState(null);
  // Reservation details modal package info
  const [modalPackage, setModalPackage] = useState(null);
  const [modalLoading, setModalLoading] = useState(false);
  const [modalError, setModalError] = useState('');

  // Fetch packages
  const fetchPackages = async () => {
    setLoading(true);
    try {
      const res = await api.get('/catalog');
      setPackages(res.data);
      setError('');
    } catch (err) {
      setError(t('errorLoadingPackages') || 'Error loading packages');
    }
    setLoading(false);
  };
  useEffect(() => { fetchPackages(); }, []);
  // Fetch clients and all packages for forms
  useEffect(() => {
    api.get('/users').then(res => setClients(res.data)).catch(() => setClients([]));
    api.get('/catalog').then(res => setAllPackages(res.data)).catch(() => setAllPackages([]));
    // Fetch all reservations for time window filtering
    api.get('/reservations').then(res => setAllReservations(res.data)).catch(() => setAllReservations([]));
  }, []);
  // Package CRUD handlers
  const openAddModal = () => {
    setEditPkg(null);
    setForm({ destination: '', price: '', startDate: '', endDate: '', images: '' });
    setModalOpen(true);
  };
  const openEditModal = (pkg) => {
    setEditPkg(pkg);
    setForm({ ...pkg, images: (pkg.images || []).join(', ') });
    setModalOpen(true);
  };
  const closeModal = () => setModalOpen(false);
  const handleFormChange = (e) => setForm(f => ({ ...f, [e.target.name]: e.target.value }));
  const handleSubmit = async (e) => {
    e.preventDefault();
    // Transform images string to image1, image2, image3
    const imagesArr = form.images.split(',').map(s => s.trim()).filter(Boolean);
    const payload = {
      ...form,
      image1: imagesArr[0] || '',
      image2: imagesArr[1] || '',
      image3: imagesArr[2] || '',
    };
    delete payload.images;
    try {
      if (editPkg) {
        await api.put(`/catalog/${editPkg.id}`, payload);
        toast({ title: t('packageUpdated'), status: 'success' });
      } else {
        await api.post('/catalog', payload);
        toast({ title: t('packageCreated'), status: 'success' });
      }
      fetchPackages();
      closeModal();
    } catch (err) {
      toast({ title: t('errorSavingPackage') || 'Error saving package', status: 'error' });
    }
  };
  const handleDelete = async (id) => {
    try {
      await api.delete(`/catalog/${id}`);
      toast({ title: t('packageDeleted'), status: 'success' });
      fetchPackages();
    } catch (err) {
      toast({ title: t('errorDeletingPackage') || 'Error deleting package', status: 'error' });
    }
  };
  // Reservation for client
  const handleReserveForClient = async (e) => {
    e.preventDefault();
    if (!selectedClient || !selectedPackage) return;
    setReserveLoading(true);
    try {
      await api.post('/reservations', { clientId: selectedClient, tourPackageId: selectedPackage });
      toast({ title: t('reservationSuccess'), status: 'success' });
      setSelectedClient('');
      setSelectedPackage('');
    } catch (err) {
      toast({ title: t('reservationError'), status: 'error' });
    }
    setReserveLoading(false);
  };
  // Client CRUD
  const fetchClients = () => {
    api.get('/users').then(res => setClients(res.data)).catch(() => setClients([]));
  };
  const handleClientFormChange = (e) => {
    setClientForm(f => ({ ...f, [e.target.name]: e.target.value }));
  };
  const handleAddOrEditClient = async (e) => {
    e.preventDefault();
    setClientLoading(true);
    try {
      if (editingClient) {
        await api.put(`/users/${editingClient.id}`, clientForm);
        toast({ title: t('clientUpdated') || 'Client updated', status: 'success' });
      } else {
        await api.post('/users/register', { ...clientForm, role: 'CLIENT' });
        toast({ title: t('clientAdded') || 'Client added', status: 'success' });
      }
      setClientForm({ username: '', firstName: '', lastName: '', email: '', password: '' });
      setEditingClient(null);
      fetchClients();
    } catch (err) {
      toast({ title: t('errorSavingClient') || 'Error saving client', status: 'error' });
    }
    setClientLoading(false);
  };
  const handleEditClient = (client) => {
    setEditingClient(client);
    setClientForm({ username: client.username, firstName: client.firstName, lastName: client.lastName, email: client.email, password: '' });
  };
  const handleDeleteClient = async (id) => {
    setClientLoading(true);
    try {
      await api.delete(`/users/${id}`);
      toast({ title: t('clientDeleted') || 'Client deleted', status: 'success' });
      fetchClients();
    } catch (err) {
      toast({ title: t('errorDeletingClient') || 'Error deleting client', status: 'error' });
    }
    setClientLoading(false);
  };
  // Reservation by period (frontend filtering)
  const handleFilterReservations = (e) => {
    e.preventDefault();
    if (!resStart || !resEnd) return;
    setResLoading(true);
    setResError('');
    try {
      const start = new Date(resStart);
      const end = new Date(resEnd);
      const filtered = allReservations.filter(res => {
        const date = new Date(res.reservedAt);
        return date >= start && date <= end;
      });
      // Enrich each reservation with client and package info
      const enriched = filtered.map(res => {
        const client = clients.find(u => u.id === res.clientId);
        const pkg = allPackages.find(p => p.id === (res.tourPackageId || res.packageId));
        return { ...res, client, package: pkg };
      });
      setFilteredReservations(enriched);
    } catch (err) {
      setResError(t('errorLoadingReservations') || 'Error loading reservations');
      setFilteredReservations([]);
    }
    setResLoading(false);
  };
  // Fetch all reservations for employees
  useEffect(() => {
    api.get('/reservations').then(res => setAllReservations(res.data)).catch(() => setAllReservations([]));
  }, []);
  // Notify clients
  const handleNotifyClients = async (e) => {
    e.preventDefault();
    if (!selectedClients.length || !offerSubject || !offerMessage) return;
    setNotifyLoading(true);
    try {
      await Promise.all(selectedClients.map(clientId => {
        const client = clients.find(c => c.id === clientId);
        return emailjs.send(
          'service_0nb1ly5', // Service ID
          'template_7a5714b', // Template ID
          {
            to_name: `${client.firstName} ${client.lastName}`,
            to_email: client.email,
            subject: offerSubject,
            message: offerMessage,
          },
          'q3mZg9mGN8Mv2SfYk' // Public Key
        );
      }));
      toast({ title: t('sendNotification'), description: t('notificationSent') || 'Notification sent!', status: 'success' });
      setSelectedClients([]);
      setOfferSubject('');
      setOfferMessage('');
      setShowPreview(false);
    } catch (err) {
      toast({ title: t('sendNotification'), description: t('notificationError') || 'Failed to send notification.', status: 'error' });
    }
    setNotifyLoading(false);
  };
  // Export
  const handleExportPackages = async (e) => {
    e.preventDefault();
    if (!exportFormat) return;
    setExportLoading(true);
    try {
      const res = await api.get(`/catalog/export?format=${exportFormat}`, { responseType: 'blob' });
      const url = window.URL.createObjectURL(new Blob([res.data]));
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', `packages.${exportFormat}`);
      document.body.appendChild(link);
      link.click();
      link.parentNode.removeChild(link);
      toast({ title: t('download'), description: t('exportSuccess') || 'Export successful!', status: 'success' });
    } catch (err) {
      toast({ title: t('download'), description: t('exportError') || 'Export failed.', status: 'error' });
    }
    setExportLoading(false);
  };
  // Fetch package info when modal opens
  useEffect(() => {
    if (detailsModalOpen && selectedReservation) {
      const pkgId = selectedReservation.package?.id || selectedReservation.tourPackageId;
      if (!pkgId) return;
      setModalLoading(true);
      setModalError('');
      api.get(`/catalog/${pkgId}`)
        .then(res => {
          setModalPackage(res.data);
          setModalError('');
        })
        .catch(() => setModalError(t('errorLoadingPackages') || 'Error loading package'))
        .finally(() => setModalLoading(false));
    } else {
      setModalPackage(null);
      setModalError('');
      setModalLoading(false);
    }
  }, [detailsModalOpen, selectedReservation, t]);

  return (
    <Box>
      <Heading as="h2" size="lg" mb={6} textAlign="center">{t('employeeDashboard')}</Heading>
      <Tabs variant="enclosed-colored" colorScheme="teal" isFitted>
        <TabList mb={4}>
          <Tab><Box as={FaBoxOpen} mr={2} />{t('managePackages')}</Tab>
          <Tab><Box as={FaUserFriends} mr={2} />{t('manageReservations')}</Tab>
          <Tab><Box as={FaUserFriends} mr={2} />{t('manageClients')}</Tab>
          <Tab><Box as={FaHistory} mr={2} />{t('reservationHistory')}</Tab>
          <Tab><Box as={FaEnvelope} mr={2} />{t('emailClients')}</Tab>
          <Tab><Box as={FaFileExport} mr={2} />{t('exportData')}</Tab>
        </TabList>
        <TabPanels>
          {/* Manage Packages Tab */}
          <TabPanel px={0} py={4}>
            <Flex mb={4} alignItems="center" justifyContent="space-between">
              <Heading as="h3" size="md">{t('managePackages')}</Heading>
              <IconButton icon={<AddIcon />} colorScheme="teal" aria-label={t('addPackage')} onClick={openAddModal} />
            </Flex>
            {loading ? <Text>{t('loading') || 'Loading...'}</Text> : error ? <Text color="red.500">{error}</Text> : (
              <SimpleGrid columns={{ base: 1, md: 2 }} spacing={6}>
                {packages.map(pkg => (
                  <Box key={pkg.id} borderWidth="1px" borderRadius="lg" p={4} bg="white" boxShadow="sm">
                    <Stack spacing={1}>
                      <Text fontWeight="bold">{pkg.destination}</Text>
                      <Text>{t('period')}: {pkg.startDate} to {pkg.endDate}</Text>
                      <Text>{t('price')}: ${pkg.price}</Text>
                      <Text fontSize="sm" color="gray.500">ID: {pkg.id}</Text>
                      <Button size="sm" colorScheme="blue" onClick={() => openEditModal(pkg)} leftIcon={<EditIcon />}>{t('edit') || 'Edit'}</Button>
                      <Button size="sm" colorScheme="red" onClick={() => handleDelete(pkg.id)} leftIcon={<ViewIcon />}>{t('delete') || 'Delete'}</Button>
                    </Stack>
                  </Box>
                ))}
              </SimpleGrid>
            )}
            <Modal isOpen={isModalOpen} onClose={closeModal} size="md">
              <ModalOverlay />
              <ModalContent>
                <ModalHeader>{editPkg ? t('editPackage') : t('addPackage')}</ModalHeader>
                <ModalCloseButton />
                <form onSubmit={handleSubmit}>
                  <ModalBody>
                    <Stack spacing={3}>
                      <FormControl isRequired>
                        <FormLabel>{t('destination')}</FormLabel>
                        <Input name="destination" value={form.destination} onChange={handleFormChange} />
                      </FormControl>
                      <FormControl isRequired>
                        <FormLabel>{t('price')}</FormLabel>
                        <Input name="price" type="number" value={form.price} onChange={handleFormChange} />
                      </FormControl>
                      <FormControl isRequired>
                        <FormLabel>{t('startDate')}</FormLabel>
                        <Input name="startDate" type="date" value={form.startDate} onChange={handleFormChange} />
                      </FormControl>
                      <FormControl isRequired>
                        <FormLabel>{t('endDate')}</FormLabel>
                        <Input name="endDate" type="date" value={form.endDate} onChange={handleFormChange} />
                      </FormControl>
                      <FormControl>
                        <FormLabel>{t('images')}</FormLabel>
                        <Input name="images" value={form.images} onChange={handleFormChange} placeholder={t('commaSeparatedUrls') || 'Comma separated URLs'} />
                      </FormControl>
                    </Stack>
                  </ModalBody>
                  <ModalFooter>
                    <Button variant="ghost" mr={3} onClick={closeModal}>{t('cancel')}</Button>
                    <Button colorScheme="teal" type="submit">{t('save') || 'Save'}</Button>
                  </ModalFooter>
                </form>
              </ModalContent>
            </Modal>
          </TabPanel>
          {/* Manage Reservations Tab */}
          <TabPanel px={0} py={4}>
            <Box maxW="500px" mx="auto" mb={8} p={6} borderWidth={1} borderRadius="lg" bg="white" boxShadow="md">
              <Heading as="h3" size="md" mb={4}>{t('manageReservations')}</Heading>
              <form onSubmit={handleReserveForClient}>
                <Stack spacing={4}>
                  <FormControl isRequired>
                    <FormLabel>{t('selectClient')}</FormLabel>
                    <select value={selectedClient} onChange={e => setSelectedClient(e.target.value)} style={{ width: '100%', padding: '8px' }}>
                      <option value="">{t('selectClient')}</option>
                      {clients.filter(c => (c.role && c.role.toLowerCase() === 'client')).map(c => (
                        <option key={c.id} value={c.id}>{c.firstName} {c.lastName} ({c.email})</option>
                      ))}
                    </select>
                  </FormControl>
                  <FormControl isRequired>
                    <FormLabel>{t('reservationPackage')}</FormLabel>
                    <select value={selectedPackage} onChange={e => setSelectedPackage(e.target.value)} style={{ width: '100%', padding: '8px' }}>
                      <option value="">{t('selectPackage') || 'Select Package'}</option>
                      {allPackages.map(p => (
                        <option key={p.id} value={p.id}>{p.destination} ({p.startDate} - {p.endDate})</option>
                      ))}
                    </select>
                  </FormControl>
                  <Button colorScheme="teal" type="submit" isLoading={reserveLoading}>{t('reserve')}</Button>
                </Stack>
              </form>
            </Box>
          </TabPanel>
          {/* Manage Clients Tab */}
          <TabPanel px={0} py={4}>
            <Box maxW="600px" mx="auto" mb={8} p={6} borderWidth={1} borderRadius="lg" bg="white" boxShadow="md">
              <Heading as="h3" size="md" mb={4}>{t('manageClients')}</Heading>
              <form onSubmit={handleAddOrEditClient} style={{ marginBottom: 16 }}>
                <Stack direction={{ base: 'column', md: 'row' }} spacing={4} align="flex-end">
                  <Input name="username" value={clientForm.username} onChange={handleClientFormChange} placeholder={t('username')} required />
                  <Input name="firstName" value={clientForm.firstName} onChange={handleClientFormChange} placeholder={t('firstName')} required />
                  <Input name="lastName" value={clientForm.lastName} onChange={handleClientFormChange} placeholder={t('lastName')} required />
                  <Input name="email" value={clientForm.email} onChange={handleClientFormChange} placeholder={t('email')} required type="email" />
                  <Input name="password" value={clientForm.password} onChange={handleClientFormChange} placeholder={t('password')} required={!editingClient} type="password" />
                  <Button colorScheme="teal" type="submit" isLoading={clientLoading}>{editingClient ? t('edit') : t('add')}</Button>
                  {editingClient && <Button onClick={() => { setEditingClient(null); setClientForm({ username: '', firstName: '', lastName: '', email: '', password: '' }); }}>{t('cancel')}</Button>}
                </Stack>
              </form>
              <Stack spacing={2}>
                {clients.filter(c => (c.role && c.role.toLowerCase() === 'client')).map(client => (
                  <Box key={client.id} borderWidth="1px" borderRadius="md" p={3} bg="gray.50" display="flex" alignItems="center" justifyContent="space-between">
                    <Text>{client.username} - {client.firstName} {client.lastName} ({client.email})</Text>
                    <Stack direction="row" spacing={2}>
                      <Button size="sm" colorScheme="blue" onClick={() => handleEditClient(client)} leftIcon={<EditIcon />}>{t('edit')}</Button>
                      <Button size="sm" colorScheme="red" onClick={() => handleDeleteClient(client.id)} isLoading={clientLoading} leftIcon={<ViewIcon />}>{t('delete')}</Button>
                    </Stack>
                  </Box>
                ))}
              </Stack>
            </Box>
          </TabPanel>
          {/* Reservation History Tab */}
          <TabPanel px={0} py={4}>
            <Box maxW="700px" mx="auto" mb={8} p={6} borderWidth={1} borderRadius="lg" bg="white" boxShadow="md">
              <Heading as="h3" size="md" mb={4}>{t('reservationHistory')}</Heading>
              <form onSubmit={handleFilterReservations} style={{ marginBottom: 16 }}>
                <Stack direction={{ base: 'column', md: 'row' }} spacing={4} align="flex-end">
                  <FormControl isRequired>
                    <FormLabel>{t('selectTimeWindow')}</FormLabel>
                    <Input type="date" value={resStart} onChange={e => setResStart(e.target.value)} />
                  </FormControl>
                  <FormControl isRequired>
                    <FormLabel>{t('to')}</FormLabel>
                    <Input type="date" value={resEnd} onChange={e => setResEnd(e.target.value)} />
                  </FormControl>
                  <Button colorScheme="teal" type="submit" isLoading={resLoading}>{t('view')}</Button>
                </Stack>
              </form>
              {resLoading ? (
                <Text>{t('loading') || 'Loading...'}</Text>
              ) : resError ? (
                <Text color="red.500">{resError}</Text>
              ) : filteredReservations.length > 0 ? (
                <Stack spacing={2}>
                  {filteredReservations.map(res => (
                    <Box key={res.id} borderWidth="1px" borderRadius="md" p={3} bg="gray.50">
                      <Text fontWeight="bold">{t('reservationFor')}: {res.client?.firstName} {res.client?.lastName} ({res.client?.email})</Text>
                      <Text>{t('reservationPackage')}: {res.package?.destination}</Text>
                      <Text>{t('reservationPeriod')}: {res.package?.startDate} to {res.package?.endDate}</Text>
                      <Text>{t('reservationDate')}: {new Date(res.reservedAt).toLocaleString()}</Text>
                      <Button size="sm" mt={2} onClick={() => { setSelectedReservation(res); setDetailsModalOpen(true); }}>{t('viewDetails')}</Button>
                    </Box>
                  ))}
                </Stack>
              ) : <Text color="gray.500">{t('noReservations')}</Text>}
            </Box>
            {/* Reservation Details Modal */}
            <Modal isOpen={detailsModalOpen} onClose={() => setDetailsModalOpen(false)} size="lg">
              <ModalOverlay />
              <ModalContent>
                <ModalHeader>{t('reservationDetails')}</ModalHeader>
                <ModalCloseButton />
                <ModalBody>
                  {modalLoading ? (
                    <Stack align="center" py={8}><Text>{t('loading') || 'Loading...'}</Text></Stack>
                  ) : modalError ? (
                    <Text color="red.500">{modalError}</Text>
                  ) : selectedReservation && modalPackage ? (
                    <Stack spacing={3}>
                      <Text><b>{t('reservationFor')}:</b> {selectedReservation.client?.firstName} {selectedReservation.client?.lastName} ({selectedReservation.client?.email})</Text>
                      <Text><b>{t('reservationPackage')}:</b> {modalPackage.destination}</Text>
                      <Text><b>{t('reservationPeriod')}:</b> {modalPackage.startDate} to {modalPackage.endDate}</Text>
                      <Text><b>{t('reservationDate')}:</b> {new Date(selectedReservation.reservedAt).toLocaleString()}</Text>
                      {Array.isArray(modalPackage.images) && modalPackage.images.length > 0 && (
                        <SimpleGrid columns={{ base: 1, md: 2 }} spacing={2}>
                          {modalPackage.images.map((img, idx) => (
                            <Box key={idx}>
                              <img src={img} alt={modalPackage.destination} style={{ width: '100%', borderRadius: 8 }} onError={e => { e.target.onerror = null; e.target.src = 'https://via.placeholder.com/300x200?text=No+Image'; }} />
                            </Box>
                          ))}
                        </SimpleGrid>
                      )}
                    </Stack>
                  ) : null}
                </ModalBody>
                <ModalFooter>
                  <Button onClick={() => setDetailsModalOpen(false)}>{t('close') || 'Close'}</Button>
                </ModalFooter>
              </ModalContent>
            </Modal>
          </TabPanel>
          {/* Email Clients Tab */}
          <TabPanel px={0} py={4}>
            <Card maxW="600px" mx="auto" boxShadow="lg">
              <CardHeader>
                <HStack justify="space-between">
                  <Heading as="h3" size="md">{t('emailClients')}</Heading>
                  <Tag colorScheme="teal">{t('email')}</Tag>
                </HStack>
              </CardHeader>
              <form onSubmit={handleNotifyClients}>
                <CardBody>
                  <Stack spacing={4}>
                    <FormControl isRequired>
                      <FormLabel>{t('selectClient')}</FormLabel>
                      <select multiple value={selectedClients} onChange={e => setSelectedClients(Array.from(e.target.selectedOptions, o => o.value))} style={{ width: '100%', padding: '8px', minHeight: 80 }}>
                        {clients.filter(c => (c.role && c.role.toLowerCase() === 'client')).map(c => (
                          <option key={c.id} value={c.id}>{c.firstName} {c.lastName} ({c.email})</option>
                        ))}
                      </select>
                    </FormControl>
                    <FormControl isRequired>
                      <FormLabel>{t('subject') || 'Subject'}</FormLabel>
                      <Input value={offerSubject} onChange={e => setOfferSubject(e.target.value)} placeholder={t('subject') || 'Subject'} />
                    </FormControl>
                    <FormControl isRequired>
                      <FormLabel>{t('newOffer')}</FormLabel>
                      <Textarea value={offerMessage} onChange={e => setOfferMessage(e.target.value)} placeholder={t('newOffer')} minH={24} />
                    </FormControl>
                    <Button leftIcon={<EmailIcon />} variant="outline" onClick={() => setShowPreview(!showPreview)}>{showPreview ? t('cancel') : t('preview')}</Button>
                    {showPreview && (
                      <Box p={4} bg="gray.50" borderRadius="md" borderWidth={1}>
                        <Text fontWeight="bold">{t('subject')}: {offerSubject}</Text>
                        <Text whiteSpace="pre-line">{offerMessage}</Text>
                      </Box>
                    )}
                  </Stack>
                </CardBody>
                <CardFooter>
                  <Button colorScheme="teal" type="submit" isLoading={notifyLoading} w="full">{t('sendNotification')}</Button>
                </CardFooter>
              </form>
            </Card>
          </TabPanel>
          {/* Export Data Tab */}
          <TabPanel px={0} py={4}>
            <Box maxW="500px" mx="auto" mb={8} p={6} borderWidth={1} borderRadius="lg" bg="white" boxShadow="md">
              <Heading as="h3" size="md" mb={4}>{t('exportData')}</Heading>
              <form onSubmit={handleExportPackages} style={{ marginBottom: 16 }}>
                <Stack direction={{ base: 'column', md: 'row' }} spacing={4} align="flex-end">
                  <FormControl isRequired>
                    <FormLabel>{t('selectFormat')}</FormLabel>
                    <select value={exportFormat} onChange={e => setExportFormat(e.target.value)} style={{ width: '100%', padding: '8px' }} required>
                      <option value="">{t('selectFormat')}</option>
                      <option value="csv">{t('exportCSV')}</option>
                      <option value="json">{t('exportJSON')}</option>
                      <option value="xml">{t('exportXML')}</option>
                      <option value="doc">{t('exportDOC')}</option>
                    </select>
                  </FormControl>
                  <Button colorScheme="teal" type="submit" isLoading={exportLoading}>{t('download')}</Button>
                </Stack>
              </form>
            </Box>
          </TabPanel>
        </TabPanels>
      </Tabs>
    </Box>
  );
} 