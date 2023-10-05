import { IOrdersItems, NewOrdersItems } from './orders-items.model';

export const sampleWithRequiredData: IOrdersItems = {
  id: 1434,
};

export const sampleWithPartialData: IOrdersItems = {
  id: 28501,
  orderItemsId: 'cultivate officia',
  gst: 'Integration spiritual labore',
  totalAmount: 6541,
};

export const sampleWithFullData: IOrdersItems = {
  id: 4410,
  orgId: 'Sum B2B',
  orderItemsId: 'Keyboard Direct degree',
  quantity: 5877,
  cgst: 'Expanded',
  sgst: 'provider Funk willfully',
  gst: 'up Plastic',
  totalAmount: 8243,
};

export const sampleWithNewData: NewOrdersItems = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
