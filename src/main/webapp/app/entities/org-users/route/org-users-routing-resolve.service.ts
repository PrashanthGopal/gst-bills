import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrgUsers } from '../org-users.model';
import { OrgUsersService } from '../service/org-users.service';

export const orgUsersResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrgUsers> => {
  const id = route.params['id'];
  if (id) {
    return inject(OrgUsersService)
      .find(id)
      .pipe(
        mergeMap((orgUsers: HttpResponse<IOrgUsers>) => {
          if (orgUsers.body) {
            return of(orgUsers.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default orgUsersResolve;
