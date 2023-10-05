import { IProductUnit, NewProductUnit } from './product-unit.model';

export const sampleWithRequiredData: IProductUnit = {
  id: 13759,
};

export const sampleWithPartialData: IProductUnit = {
  id: 28168,
  unitCodeId: 'Serbian Practical',
};

export const sampleWithFullData: IProductUnit = {
  id: 26618,
  unitCodeId: 'beyond Parkway',
  unitCode: 'visualize forenenst Garden',
  unitCodeDescription: 'bore instruction',
};

export const sampleWithNewData: NewProductUnit = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
