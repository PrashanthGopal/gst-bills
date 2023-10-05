import { IClients } from 'app/entities/clients/clients.model';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrdersEnum } from 'app/entities/enumerations/orders-enum.model';

export interface IOrders {
  id: number;
  orderId?: string | null;
  title?: string | null;
  description?: string | null;
  status?: keyof typeof OrdersEnum | null;
  ordersItemsId?: string | null;
  clients?: Pick<IClients, 'id'> | null;
  orgOrders?: Pick<IOrganization, 'id'> | null;
}

export type NewOrders = Omit<IOrders, 'id'> & { id: null };
