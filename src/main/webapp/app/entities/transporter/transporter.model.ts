import { IAddress } from 'app/entities/address/address.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface ITransporter {
  id: number;
  orgId?: string | null;
  transporterId?: string | null;
  name?: string | null;
  phoneNumber?: string | null;
  status?: keyof typeof Status | null;
  address?: Pick<IAddress, 'id'> | null;
}

export type NewTransporter = Omit<ITransporter, 'id'> & { id: null };
