import { IProducts, NewProducts } from './products.model';

export const sampleWithRequiredData: IProducts = {
  id: 23379,
};

export const sampleWithPartialData: IProducts = {
  id: 4615,
  productHsnCode: 'extranet all',
  unitCodeId: 'Handmade meter',
  costPerQty: 16415,
  cgst: 'Northeast',
  sgst: 'architecto male Account',
  igst: 'pish Reduced',
  totalTaxRate: 'female',
};

export const sampleWithFullData: IProducts = {
  id: 5596,
  productId: 'ick Industrial',
  name: 'female',
  description: 'off',
  productHsnCode: 'stonework',
  productTaxRate: 'Southwest doorway',
  unitCodeId: 'quod',
  costPerQty: 5750,
  cgst: 'expedite under Analyst',
  sgst: 'supposing',
  igst: 'holistic East',
  totalTaxRate: 'bypassing SUV function',
};

export const sampleWithNewData: NewProducts = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
