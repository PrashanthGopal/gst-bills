import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProducts } from '../products.model';
import { ProductsService } from '../service/products.service';

export const productsResolve = (route: ActivatedRouteSnapshot): Observable<null | IProducts> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProductsService)
      .find(id)
      .pipe(
        mergeMap((products: HttpResponse<IProducts>) => {
          if (products.body) {
            return of(products.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default productsResolve;
