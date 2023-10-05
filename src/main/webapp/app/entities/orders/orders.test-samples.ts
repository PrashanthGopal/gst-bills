import { OrdersEnum } from 'app/entities/enumerations/orders-enum.model';

import { IOrders, NewOrders } from './orders.model';

export const sampleWithRequiredData: IOrders = {
  id: 10248,
};

export const sampleWithPartialData: IOrders = {
  id: 6403,
  description: 'Games hardware likely',
  status: 'CANCEL_CLOSE',
};

export const sampleWithFullData: IOrders = {
  id: 19889,
  orderId: 'Wooden indigo',
  title: 'empower',
  description: 'under South interactive',
  status: 'COMPLETED',
  ordersItemsId: 'murmurings collocate',
};

export const sampleWithNewData: NewOrders = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
