import TourPackage from '../models/TourPackage';

const mockPackages = [
  {
    id: '1',
    destination: 'Paris',
    price: '1200',
    startDate: '2024-07-01',
    endDate: '2024-07-07',
    images: [
      'https://images.unsplash.com/photo-1506744038136-46273834b3fb',
      'https://images.unsplash.com/photo-1465101046530-73398c7f28ca',
    ],
    reviews: [],
  },
  {
    id: '2',
    destination: 'Rome',
    price: '1000',
    startDate: '2024-08-10',
    endDate: '2024-08-17',
    images: [
      'https://images.unsplash.com/photo-1500534314209-a25ddb2bd429',
    ],
    reviews: [],
  },
  {
    id: '3',
    destination: 'London',
    price: '1300',
    startDate: '2024-09-05',
    endDate: '2024-09-12',
    images: [
      'https://images.unsplash.com/photo-1467269204594-9661b134dd2b',
      'https://images.unsplash.com/photo-1502082553048-f009c37129b9',
      'https://images.unsplash.com/photo-1464037866556-6812c9d1c72e',
    ],
    reviews: [],
  },
];

export function getPackages() {
  return mockPackages.map(pkg => new TourPackage(pkg));
} 