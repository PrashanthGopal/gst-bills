import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ClientsComponent } from './list/clients.component';
import { ClientsDetailComponent } from './detail/clients-detail.component';
import { ClientsUpdateComponent } from './update/clients-update.component';
import ClientsResolve from './route/clients-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const clientsRoute: Routes = [
  {
    path: '',
    component: ClientsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClientsDetailComponent,
    resolve: {
      clients: ClientsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClientsUpdateComponent,
    resolve: {
      clients: ClientsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClientsUpdateComponent,
    resolve: {
      clients: ClientsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default clientsRoute;
