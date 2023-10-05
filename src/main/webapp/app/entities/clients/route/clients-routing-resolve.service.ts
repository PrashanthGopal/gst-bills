import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IClients } from '../clients.model';
import { ClientsService } from '../service/clients.service';

export const clientsResolve = (route: ActivatedRouteSnapshot): Observable<null | IClients> => {
  const id = route.params['id'];
  if (id) {
    return inject(ClientsService)
      .find(id)
      .pipe(
        mergeMap((clients: HttpResponse<IClients>) => {
          if (clients.body) {
            return of(clients.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default clientsResolve;
