import { IAddress } from 'app/entities/address/address.model';
import { IOrganization } from 'app/entities/organization/organization.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IClients {
  id: number;
  clientId?: string | null;
  name?: string | null;
  gstin?: string | null;
  status?: keyof typeof Status | null;
  emailId?: string | null;
  phoneNumber?: string | null;
  address?: Pick<IAddress, 'id'> | null;
  orgClients?: Pick<IOrganization, 'id'> | null;
}

export type NewClients = Omit<IClients, 'id'> & { id: null };
