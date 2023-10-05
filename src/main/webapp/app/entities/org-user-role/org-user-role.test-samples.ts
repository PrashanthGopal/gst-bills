import { IOrgUserRole, NewOrgUserRole } from './org-user-role.model';

export const sampleWithRequiredData: IOrgUserRole = {
  id: 1781,
};

export const sampleWithPartialData: IOrgUserRole = {
  id: 10846,
};

export const sampleWithFullData: IOrgUserRole = {
  id: 6401,
  roleId: 'once',
  orgId: 'amet gold',
  name: 'East mayor Folding',
};

export const sampleWithNewData: NewOrgUserRole = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
