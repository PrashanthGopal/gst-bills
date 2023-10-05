import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';

import { IOrganization, NewOrganization } from './organization.model';

export const sampleWithRequiredData: IOrganization = {
  id: 4999,
};

export const sampleWithPartialData: IOrganization = {
  id: 6921,
  orgId: 'meter Albania',
  orgName: 'World Tennessee Mills',
  legalOrgName: 'Planner Vanadium Electric',
  pan: 'Reggae male',
  gstin: 'Barium Northwest logistical',
  phoneNumber: 'Madagascar',
  emailId: 'Trial female monstrous',
  status: 'INACTIVE',
  dateOfRegestation: dayjs('2023-10-04T19:46'),
};

export const sampleWithFullData: IOrganization = {
  id: 24912,
  orgId: 'invoice Northeast',
  orgName: 'loving Extended Buckinghamshire',
  legalOrgName: 'Northeast',
  pan: 'Southeast',
  gstin: 'Granite',
  phoneNumber: 'sensor black mole',
  emailId: 'Carolina Data',
  status: 'ACTIVE',
  dateOfRegestation: dayjs('2023-10-05T03:56'),
  dateOfDeRegestation: dayjs('2023-10-04T13:54'),
  logo: '../fake-data/blob/hipster.png',
  logoContentType: 'unknown',
};

export const sampleWithNewData: NewOrganization = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
