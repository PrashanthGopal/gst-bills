import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrdersItems } from '../orders-items.model';
import { OrdersItemsService } from '../service/orders-items.service';

export const ordersItemsResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrdersItems> => {
  const id = route.params['id'];
  if (id) {
    return inject(OrdersItemsService)
      .find(id)
      .pipe(
        mergeMap((ordersItems: HttpResponse<IOrdersItems>) => {
          if (ordersItems.body) {
            return of(ordersItems.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default ordersItemsResolve;
