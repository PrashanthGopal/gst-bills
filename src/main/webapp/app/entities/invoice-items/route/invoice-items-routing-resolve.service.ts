import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IInvoiceItems } from '../invoice-items.model';
import { InvoiceItemsService } from '../service/invoice-items.service';

export const invoiceItemsResolve = (route: ActivatedRouteSnapshot): Observable<null | IInvoiceItems> => {
  const id = route.params['id'];
  if (id) {
    return inject(InvoiceItemsService)
      .find(id)
      .pipe(
        mergeMap((invoiceItems: HttpResponse<IInvoiceItems>) => {
          if (invoiceItems.body) {
            return of(invoiceItems.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default invoiceItemsResolve;
