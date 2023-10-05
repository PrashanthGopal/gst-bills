import { IStateCode, NewStateCode } from './state-code.model';

export const sampleWithRequiredData: IStateCode = {
  id: 22765,
};

export const sampleWithPartialData: IStateCode = {
  id: 29621,
  stateCodeId: 'Modern newton joyfully',
  name: 'Legacy',
};

export const sampleWithFullData: IStateCode = {
  id: 828,
  stateCodeId: 'Bacon vainly',
  code: 'Honda',
  name: 'Mouse siemens conglomeration',
};

export const sampleWithNewData: NewStateCode = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
