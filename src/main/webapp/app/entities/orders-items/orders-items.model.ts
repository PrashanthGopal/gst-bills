import { IProducts } from 'app/entities/products/products.model';
import { IOrders } from 'app/entities/orders/orders.model';

export interface IOrdersItems {
  id: number;
  orgId?: string | null;
  orderItemsId?: string | null;
  quantity?: number | null;
  cgst?: string | null;
  sgst?: string | null;
  gst?: string | null;
  totalAmount?: number | null;
  products?: Pick<IProducts, 'id'> | null;
  orders?: Pick<IOrders, 'id'> | null;
}

export type NewOrdersItems = Omit<IOrdersItems, 'id'> & { id: null };
