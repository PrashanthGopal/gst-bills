import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StateCodeComponent } from './list/state-code.component';
import { StateCodeDetailComponent } from './detail/state-code-detail.component';
import { StateCodeUpdateComponent } from './update/state-code-update.component';
import StateCodeResolve from './route/state-code-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const stateCodeRoute: Routes = [
  {
    path: '',
    component: StateCodeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: StateCodeDetailComponent,
    resolve: {
      stateCode: StateCodeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: StateCodeUpdateComponent,
    resolve: {
      stateCode: StateCodeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: StateCodeUpdateComponent,
    resolve: {
      stateCode: StateCodeResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default stateCodeRoute;
