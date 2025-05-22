import { useState, useEffect, useMemo } from 'react';
import { getPackages } from '../services/packageService';

export default function usePackageList() {
  const [destination, setDestination] = useState('');
  const [minPrice, setMinPrice] = useState('');
  const [maxPrice, setMaxPrice] = useState('');
  const [sortBy, setSortBy] = useState('destination');
  const [packages, setPackages] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    setLoading(true);
    getPackages()
      .then(data => {
        setPackages(data);
        setError('');
      })
      .catch(() => setError('Error loading packages'))
      .finally(() => setLoading(false));
  }, []);

  const filtered = useMemo(() => {
    let result = packages.filter((pkg) => {
      const price = parseFloat(pkg.price);
      const min = minPrice ? parseFloat(minPrice) : 0;
      const max = maxPrice ? parseFloat(maxPrice) : Infinity;
      return (
        (!destination || pkg.destination === destination) &&
        price >= min &&
        price <= max
      );
    });
    result = result.sort((a, b) => {
      if (sortBy === 'destination') {
        return a.destination.localeCompare(b.destination);
      } else if (sortBy === 'period') {
        return new Date(a.startDate) - new Date(b.startDate);
      } else if (sortBy === 'price') {
        return parseFloat(a.price) - parseFloat(b.price);
      }
      return 0;
    });
    return result;
  }, [packages, destination, minPrice, maxPrice, sortBy]);

  const uniqueDestinations = useMemo(() => [
    ...new Set(packages.map((pkg) => pkg.destination)),
  ], [packages]);

  const resetFilters = () => {
    setDestination('');
    setMinPrice('');
    setMaxPrice('');
    setSortBy('destination');
  };

  return {
    packages: filtered,
    destination,
    setDestination,
    minPrice,
    setMinPrice,
    maxPrice,
    setMaxPrice,
    sortBy,
    setSortBy,
    uniqueDestinations,
    resetFilters,
    loading,
    error,
  };
} 