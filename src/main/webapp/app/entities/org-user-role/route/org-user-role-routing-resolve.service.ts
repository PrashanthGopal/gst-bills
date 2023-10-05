import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IOrgUserRole } from '../org-user-role.model';
import { OrgUserRoleService } from '../service/org-user-role.service';

export const orgUserRoleResolve = (route: ActivatedRouteSnapshot): Observable<null | IOrgUserRole> => {
  const id = route.params['id'];
  if (id) {
    return inject(OrgUserRoleService)
      .find(id)
      .pipe(
        mergeMap((orgUserRole: HttpResponse<IOrgUserRole>) => {
          if (orgUserRole.body) {
            return of(orgUserRole.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        })
      );
  }
  return of(null);
};

export default orgUserRoleResolve;
