import { IOrgUsers } from 'app/entities/org-users/org-users.model';

export interface IOrgUserRole {
  id: number;
  roleId?: string | null;
  orgId?: string | null;
  name?: string | null;
  userRole?: Pick<IOrgUsers, 'id'> | null;
}

export type NewOrgUserRole = Omit<IOrgUserRole, 'id'> & { id: null };
