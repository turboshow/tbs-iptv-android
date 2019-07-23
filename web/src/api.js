import axios from 'axios';

export function fetchUdpxySettings() {
  return axios.get('/api/settings/udpxy').then(resp => resp.data);
}

export function updateUdpxySettings(settings) {
  return axios.post('/api/settings/udpxy', settings);
}