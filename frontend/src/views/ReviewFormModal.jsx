import {
  Modal, ModalOverlay, ModalContent, ModalHeader, ModalCloseButton, ModalBody, ModalFooter,
  Button, FormControl, FormLabel, Textarea, Stack, IconButton, HStack
} from '@chakra-ui/react';
import { useState } from 'react';
import { useTranslation } from 'react-i18next';
import { StarIcon } from '@chakra-ui/icons';

export default function ReviewFormModal({ isOpen, onClose, onSubmit }) {
  const { t } = useTranslation();
  const [rating, setRating] = useState(5);
  const [comment, setComment] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit({ rating, comment });
    setRating(5);
    setComment('');
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
            </Stack>
          </ModalBody>
          <ModalFooter>
            <Button variant="ghost" mr={3} onClick={onClose} textTransform="capitalize">{t('cancel') || 'Cancel'}</Button>
            <Button colorScheme="teal" type="submit" textTransform="capitalize">{t('addReview')}</Button>
          </ModalFooter>
        </form>
      </ModalContent>
    </Modal>
  );
} 