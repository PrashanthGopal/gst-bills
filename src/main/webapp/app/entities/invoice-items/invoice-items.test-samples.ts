import { IInvoiceItems, NewInvoiceItems } from './invoice-items.model';

export const sampleWithRequiredData: IInvoiceItems = {
  id: 6169,
};

export const sampleWithPartialData: IInvoiceItems = {
  id: 2442,
  invoiceItemsId: 'Concrete',
  quantity: 14004,
  gst: 'payment',
  totalAmount: 30525,
};

export const sampleWithFullData: IInvoiceItems = {
  id: 13103,
  orgId: 'Planner protein Czech',
  invoiceItemsId: 'Granite flat',
  quantity: 26402,
  cgst: 'East Jaguar Ohio',
  sgst: 'Division',
  gst: 'whiteboard',
  totalAmount: 18583,
};

export const sampleWithNewData: NewInvoiceItems = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
