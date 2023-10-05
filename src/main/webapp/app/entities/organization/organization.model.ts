import dayjs from 'dayjs/esm';
import { IAddress } from 'app/entities/address/address.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IOrganization {
  id: number;
  orgId?: string | null;
  orgName?: string | null;
  legalOrgName?: string | null;
  pan?: string | null;
  gstin?: string | null;
  phoneNumber?: string | null;
  emailId?: string | null;
  status?: keyof typeof Status | null;
  dateOfRegestation?: dayjs.Dayjs | null;
  dateOfDeRegestation?: dayjs.Dayjs | null;
  logo?: string | null;
  logoContentType?: string | null;
  address?: Pick<IAddress, 'id'> | null;
}

export type NewOrganization = Omit<IOrganization, 'id'> & { id: null };
