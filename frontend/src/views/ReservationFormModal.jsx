import {
  Modal, ModalOverlay, ModalContent, ModalHeader, ModalCloseButton, ModalBody, ModalFooter,
  Button, FormControl, FormLabel, Input, Stack
} from '@chakra-ui/react';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';

export default function ReservationFormModal({ isOpen, onClose, onSubmit }) {
  const { t } = useTranslation();
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ name, email });
    setName('');
    setEmail('');
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} size="md">
      <ModalOverlay />
      <ModalContent>
        <ModalHeader>{t('reserve')}</ModalHeader>
        <ModalCloseButton />
        <form onSubmit={handleSubmit}>
          <ModalBody>
            <Stack spacing={4}>
              <FormControl isRequired>
                <FormLabel>{t('firstName') || 'Name'}</FormLabel>
                <Input value={name} onChange={e => setName(e.target.value)} placeholder={t('firstName') || 'Your name'} />
              </FormControl>
              <FormControl isRequired>
                <FormLabel>{t('email')}</FormLabel>
                <Input type="email" value={email} onChange={e => setEmail(e.target.value)} placeholder={t('email')} />
              </FormControl>
            </Stack>
          </ModalBody>
          <ModalFooter>
            <Button variant="ghost" mr={3} onClick={onClose}>{t('cancel') || 'Cancel'}</Button>
            <Button colorScheme="blue" type="submit">{t('reserve')}</Button>
          </ModalFooter>
        </form>
      </ModalContent>
    </Modal>
  );
} 