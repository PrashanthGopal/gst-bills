import { IStateCode } from 'app/entities/state-code/state-code.model';

export interface IAddress {
  id: number;
  orgId?: string | null;
  addressId?: string | null;
  address1?: string | null;
  address2?: string | null;
  pincode?: string | null;
  stateCode?: Pick<IStateCode, 'id'> | null;
}

export type NewAddress = Omit<IAddress, 'id'> & { id: null };
