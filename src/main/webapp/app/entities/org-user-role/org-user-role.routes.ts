import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrgUserRoleComponent } from './list/org-user-role.component';
import { OrgUserRoleDetailComponent } from './detail/org-user-role-detail.component';
import { OrgUserRoleUpdateComponent } from './update/org-user-role-update.component';
import OrgUserRoleResolve from './route/org-user-role-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const orgUserRoleRoute: Routes = [
  {
    path: '',
    component: OrgUserRoleComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrgUserRoleDetailComponent,
    resolve: {
      orgUserRole: OrgUserRoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrgUserRoleUpdateComponent,
    resolve: {
      orgUserRole: OrgUserRoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrgUserRoleUpdateComponent,
    resolve: {
      orgUserRole: OrgUserRoleResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default orgUserRoleRoute;
