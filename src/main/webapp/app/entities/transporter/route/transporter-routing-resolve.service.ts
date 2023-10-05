import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITransporter } from '../transporter.model';
import { TransporterService } from '../service/transporter.service';

export const transporterResolve = (route: ActivatedRouteSnapshot): Observable<null | ITransporter> => {
  const id = route.params['id'];
  if (id) {
    return inject(TransporterService)
      .find(id)
      .pipe(
        mergeMap((transporter: HttpResponse<ITransporter>) => {
          if (transporter.body) {
            return of(transporter.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default transporterResolve;
