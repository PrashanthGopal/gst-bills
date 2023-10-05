import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductUnitComponent } from './list/product-unit.component';
import { ProductUnitDetailComponent } from './detail/product-unit-detail.component';
import { ProductUnitUpdateComponent } from './update/product-unit-update.component';
import ProductUnitResolve from './route/product-unit-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const productUnitRoute: Routes = [
  {
    path: '',
    component: ProductUnitComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductUnitDetailComponent,
    resolve: {
      productUnit: ProductUnitResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductUnitUpdateComponent,
    resolve: {
      productUnit: ProductUnitResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductUnitUpdateComponent,
    resolve: {
      productUnit: ProductUnitResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default productUnitRoute;
