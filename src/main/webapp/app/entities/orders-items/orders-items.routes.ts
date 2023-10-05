import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { OrdersItemsComponent } from './list/orders-items.component';
import { OrdersItemsDetailComponent } from './detail/orders-items-detail.component';
import { OrdersItemsUpdateComponent } from './update/orders-items-update.component';
import OrdersItemsResolve from './route/orders-items-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const ordersItemsRoute: Routes = [
  {
    path: '',
    component: OrdersItemsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: OrdersItemsDetailComponent,
    resolve: {
      ordersItems: OrdersItemsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: OrdersItemsUpdateComponent,
    resolve: {
      ordersItems: OrdersItemsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: OrdersItemsUpdateComponent,
    resolve: {
      ordersItems: OrdersItemsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default ordersItemsRoute;
