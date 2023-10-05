import dayjs from 'dayjs/esm';

import { InvoiceStatus } from 'app/entities/enumerations/invoice-status.model';

import { IInvoice, NewInvoice } from './invoice.model';

export const sampleWithRequiredData: IInvoice = {
  id: 24752,
};

export const sampleWithPartialData: IInvoice = {
  id: 8809,
  invoiceId: 'matrix Molybdenum Road',
  supplierClientId: 'Recycled',
  buyerClientId: 'Utah bandwidth sky',
  invoiceItemsId: 'grow',
  updateDateTime: dayjs('2023-10-05T05:06'),
  updatedBy: 'Versatile Man deposit',
  totalIgstRate: 'Division online Factors',
  totalAssAmount: 'Toyota',
  totalNoItems: 'synergistic synergy pepper',
  distance: 24114,
  modeOfTransaction: 'vengeance rummage hacking',
  vehicleNo: 'card Chicken',
};

export const sampleWithFullData: IInvoice = {
  id: 13525,
  invoiceId: 'Road',
  supplierClientId: 'synergy Southwest',
  buyerClientId: 'Maryland modular',
  invoiceItemsId: 'Streets Fresh Northeast',
  shippingAddressId: 'Jersey',
  status: 'PAYMENT_NOT_RECEIVED',
  createDateTime: dayjs('2023-10-05T02:33'),
  updateDateTime: dayjs('2023-10-05T02:52'),
  createdBy: 'Pizza a',
  updatedBy: 'olive lumen Executive',
  totalIgstRate: 'North Programmable',
  totalCgstRate: 'Country technologies',
  totalSgstRate: 'North',
  totalTaxRate: 'Iowa',
  totalAssAmount: 'Sedan Recycled',
  totalNoItems: 'Towels innovative Automotive',
  grandTotal: 1574,
  distance: 10094,
  modeOfTransaction: 'supposing Screen',
  transType: 'monetize',
  vehicleNo: 'Southwest Southwest initiative',
};

export const sampleWithNewData: NewInvoice = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
