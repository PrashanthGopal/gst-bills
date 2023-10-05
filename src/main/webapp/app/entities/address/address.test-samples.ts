import { IAddress, NewAddress } from './address.model';

export const sampleWithRequiredData: IAddress = {
  id: 21512,
};

export const sampleWithPartialData: IAddress = {
  id: 17926,
  orgId: 'Island Bicycle Northwest',
  address1: 'Diesel',
  pincode: 'Demiflux',
};

export const sampleWithFullData: IAddress = {
  id: 5508,
  orgId: 'bluetooth female Licensed',
  addressId: 'Rupee',
  address1: 'withdrawal yahoo',
  address2: 'Generic',
  pincode: 'Credit Buckinghamshire',
};

export const sampleWithNewData: NewAddress = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
