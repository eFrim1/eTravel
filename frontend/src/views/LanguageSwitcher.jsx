import { Menu, MenuButton, MenuList, MenuItem, Button } from '@chakra-ui/react';
import { ChevronDownIcon } from '@chakra-ui/icons';
import { useTranslation } from 'react-i18next';

const languages = [
  { code: 'en', label: 'EN' },
  { code: 'fr', label: 'FR' },
  { code: 'de', label: 'DE' },
];

export default function LanguageSwitcher() {
  const { i18n } = useTranslation();
  const currentLang = i18n.language;

  return (
    <Menu>
      <MenuButton
        as={Button}
        size="sm"
        variant="ghost"
        colorScheme="gray"
        rightIcon={<ChevronDownIcon />}
        px={2}
        fontWeight="normal"
        _hover={{ bg: 'teal.600', color: 'white' }}
        _expanded={{ bg: 'teal.600', color: 'white' }}
      >
        {languages.find(l => l.code === currentLang)?.label || 'EN'}
      </MenuButton>
      <MenuList minW="32px" bg="white" color="gray.800">
        {languages.map((lang) => (
          <MenuItem
            key={lang.code}
            onClick={() => i18n.changeLanguage(lang.code)}
            fontWeight={currentLang === lang.code ? 'bold' : 'normal'}
            _hover={{ bg: 'teal.50', color: 'teal.700' }}
            _focus={{ bg: 'teal.100', color: 'teal.700' }}
          >
            {lang.label}
          </MenuItem>
        ))}
      </MenuList>
    </Menu>
  );
} 