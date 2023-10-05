import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IStateCode } from '../state-code.model';
import { StateCodeService } from '../service/state-code.service';

export const stateCodeResolve = (route: ActivatedRouteSnapshot): Observable<null | IStateCode> => {
  const id = route.params['id'];
  if (id) {
    return inject(StateCodeService)
      .find(id)
      .pipe(
        mergeMap((stateCode: HttpResponse<IStateCode>) => {
          if (stateCode.body) {
            return of(stateCode.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default stateCodeResolve;
