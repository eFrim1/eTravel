import {
  Modal, ModalOverlay, ModalContent, ModalHeader, ModalCloseButton, ModalBody, ModalFooter,
  Button, FormControl, FormLabel, Textarea, Stack, IconButton, HStack, Spinner, Alert, AlertIcon
} from '@chakra-ui/react';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { StarIcon } from '@chakra-ui/icons';
import api from '../api/axios';

export default function ReviewFormModal({ isOpen, onClose, onSubmit, pkgId, user }) {
  const { t } = useTranslation();
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError('');
    try {
      await api.post(`/reviews/${pkgId}/reviews`, { rating, comment, clientId: user.id });
      setRating(5);
      setComment('');
      setLoading(false);
      onSubmit && onSubmit();
      onClose();
    } catch (err) {
      setError(t('errorSavingReview') || 'Error saving review');
      setLoading(false);
    }
  };

  return (
    <Modal isOpen={isOpen} onClose={onClose} size="md">
      <ModalOverlay />
      <ModalContent>
        <ModalHeader textTransform="capitalize">{t('addReview')}</ModalHeader>
        <ModalCloseButton />
        <form onSubmit={handleSubmit}>
          <ModalBody>
            <Stack spacing={4}>
              <FormControl isRequired>
                <FormLabel textTransform="capitalize">{t('reviews')}</FormLabel>
                <HStack>
                  {[1, 2, 3, 4, 5].map((star) => (
                    <IconButton
                      key={star}
                      icon={<StarIcon />}
                      color={star <= rating ? 'yellow.400' : 'gray.300'}
                      variant="ghost"
                      aria-label={`${t('rating')} ${star}`}
                      onClick={() => setRating(star)}
                      size="lg"
                    />
                  ))}
                </HStack>
              </FormControl>
              <FormControl isRequired>
                <FormLabel textTransform="capitalize">{t('comment') || 'Comment'}</FormLabel>
                <Textarea value={comment} onChange={e => setComment(e.target.value)} placeholder={t('comment') || 'Write your review...'} />
              </FormControl>
              {error && <Alert status="error"><AlertIcon />{error}</Alert>}
            </Stack>
          </ModalBody>
          <ModalFooter>
            <Button variant="ghost" mr={3} onClick={onClose} textTransform="capitalize">{t('cancel') || 'Cancel'}</Button>
            <Button colorScheme="teal" type="submit" textTransform="capitalize" isLoading={loading}>{t('addReview')}</Button>
          </ModalFooter>
        </form>
      </ModalContent>
    </Modal>
  );
} 