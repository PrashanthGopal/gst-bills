import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IProductUnit } from '../product-unit.model';
import { ProductUnitService } from '../service/product-unit.service';

export const productUnitResolve = (route: ActivatedRouteSnapshot): Observable<null | IProductUnit> => {
  const id = route.params['id'];
  if (id) {
    return inject(ProductUnitService)
      .find(id)
      .pipe(
        mergeMap((productUnit: HttpResponse<IProductUnit>) => {
          if (productUnit.body) {
            return of(productUnit.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default productUnitResolve;
