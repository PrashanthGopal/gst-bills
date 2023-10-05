import dayjs from 'dayjs/esm';
import { IOrganization } from 'app/entities/organization/organization.model';
import { Status } from 'app/entities/enumerations/status.model';

export interface IOrgUsers {
  id: number;
  userId?: string | null;
  name?: string | null;
  userName?: string | null;
  password?: string | null;
  status?: keyof typeof Status | null;
  createDate?: dayjs.Dayjs | null;
  updateDate?: dayjs.Dayjs | null;
  profilePhoto?: string | null;
  profilePhotoContentType?: string | null;
  emailId?: string | null;
  phoneNumber?: string | null;
  orgUsers?: Pick<IOrganization, 'id'> | null;
}

export type NewOrgUsers = Omit<IOrgUsers, 'id'> & { id: null };
