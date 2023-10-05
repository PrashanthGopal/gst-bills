import { Status } from 'app/entities/enumerations/status.model';

import { ITransporter, NewTransporter } from './transporter.model';

export const sampleWithRequiredData: ITransporter = {
  id: 7550,
};

export const sampleWithPartialData: ITransporter = {
  id: 8242,
  orgId: 'Virginia Account markets',
  transporterId: 'white Optimization Incredible',
  name: 'blanditiis',
  phoneNumber: 'structure Convertible North',
};

export const sampleWithFullData: ITransporter = {
  id: 23461,
  orgId: 'Shilling Grocery',
  transporterId: 'Transexual',
  name: 'East',
  phoneNumber: 'architectures Account Southeast',
  status: 'INACTIVE',
};

export const sampleWithNewData: NewTransporter = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
