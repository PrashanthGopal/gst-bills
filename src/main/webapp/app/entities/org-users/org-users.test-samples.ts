import dayjs from 'dayjs/esm';

import { Status } from 'app/entities/enumerations/status.model';

import { IOrgUsers, NewOrgUsers } from './org-users.model';

export const sampleWithRequiredData: IOrgUsers = {
  id: 16595,
};

export const sampleWithPartialData: IOrgUsers = {
  id: 20942,
  name: 'Investor youthfully',
  userName: 'Strategist',
  status: 'INACTIVE',
  updateDate: dayjs('2023-10-04T17:18'),
  emailId: 'intuitive Avon',
};

export const sampleWithFullData: IOrgUsers = {
  id: 20422,
  userId: 'while newton letter',
  name: 'Soul',
  userName: 'programming Trans frivolous',
  password: 'Crew Southwest',
  status: 'ACTIVE',
  createDate: dayjs('2023-10-05T10:10'),
  updateDate: dayjs('2023-10-04T19:42'),
  profilePhoto: '../fake-data/blob/hipster.png',
  profilePhotoContentType: 'unknown',
  emailId: 'yellow elegantly',
  phoneNumber: 'view metrics alarm',
};

export const sampleWithNewData: NewOrgUsers = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
