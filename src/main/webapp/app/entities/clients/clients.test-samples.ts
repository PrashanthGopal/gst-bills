import { Status } from 'app/entities/enumerations/status.model';

import { IClients, NewClients } from './clients.model';

export const sampleWithRequiredData: IClients = {
  id: 32667,
};

export const sampleWithPartialData: IClients = {
  id: 6825,
  clientId: 'times',
  name: 'Strategist',
  emailId: 'driver ack',
};

export const sampleWithFullData: IClients = {
  id: 12728,
  clientId: 'functionalities engage hopeful',
  name: 'transmit reinvent Dobra',
  gstin: 'Movies Money program',
  status: 'ACTIVE',
  emailId: 'indigo',
  phoneNumber: 'Direct',
};

export const sampleWithNewData: NewClients = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
