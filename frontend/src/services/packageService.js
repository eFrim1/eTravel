import api from '../api/axios';

export async function getPackages() {
  const res = await api.get('/catalog');
  return res.data;
}

export async function createPackage(pkg) {
  const res = await api.post('/catalog', pkg);
  return res.data;
}

export async function updatePackage(id, pkg) {
  const res = await api.put(`/catalog/${id}`, pkg);
  return res.data;
}

export async function deletePackage(id) {
  const res = await api.delete(`/catalog/${id}`);
  return res.data;
} 