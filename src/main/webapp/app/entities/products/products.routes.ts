import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductsComponent } from './list/products.component';
import { ProductsDetailComponent } from './detail/products-detail.component';
import { ProductsUpdateComponent } from './update/products-update.component';
import ProductsResolve from './route/products-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const productsRoute: Routes = [
  {
    path: '',
    component: ProductsComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductsDetailComponent,
    resolve: {
      products: ProductsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductsUpdateComponent,
    resolve: {
      products: ProductsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProductsUpdateComponent,
    resolve: {
      products: ProductsResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default productsRoute;
