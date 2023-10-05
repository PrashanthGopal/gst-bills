import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrgUsersComponent } from './list/org-users.component';
import { OrgUsersDetailComponent } from './detail/org-users-detail.component';
import { OrgUsersUpdateComponent } from './update/org-users-update.component';
import OrgUsersResolve from './route/org-users-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const orgUsersRoute: Routes = [
  {
    path: '',
    component: OrgUsersComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrgUsersDetailComponent,
    resolve: {
      orgUsers: OrgUsersResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrgUsersUpdateComponent,
    resolve: {
      orgUsers: OrgUsersResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrgUsersUpdateComponent,
    resolve: {
      orgUsers: OrgUsersResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default orgUsersRoute;
