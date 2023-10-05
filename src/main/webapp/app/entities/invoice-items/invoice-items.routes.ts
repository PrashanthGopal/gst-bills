import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { InvoiceItemsComponent } from './list/invoice-items.component';
import { InvoiceItemsDetailComponent } from './detail/invoice-items-detail.component';
import { InvoiceItemsUpdateComponent } from './update/invoice-items-update.component';
import InvoiceItemsResolve from './route/invoice-items-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const invoiceItemsRoute: Routes = [
  {
    path: '',
    component: InvoiceItemsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: InvoiceItemsDetailComponent,
    resolve: {
      invoiceItems: InvoiceItemsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: InvoiceItemsUpdateComponent,
    resolve: {
      invoiceItems: InvoiceItemsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: InvoiceItemsUpdateComponent,
    resolve: {
      invoiceItems: InvoiceItemsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default invoiceItemsRoute;
