import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrgUserRole, NewOrgUserRole } from '../org-user-role.model';

export type PartialUpdateOrgUserRole = Partial<IOrgUserRole> & Pick<IOrgUserRole, 'id'>;

export type EntityResponseType = HttpResponse<IOrgUserRole>;
export type EntityArrayResponseType = HttpResponse<IOrgUserRole[]>;

@Injectable({ providedIn: 'root' })
export class OrgUserRoleService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/org-user-roles');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orgUserRole: NewOrgUserRole): Observable<EntityResponseType> {
    return this.http.post<IOrgUserRole>(this.resourceUrl, orgUserRole, { observe: 'response' });
  }

  update(orgUserRole: IOrgUserRole): Observable<EntityResponseType> {
    return this.http.put<IOrgUserRole>(`${this.resourceUrl}/${this.getOrgUserRoleIdentifier(orgUserRole)}`, orgUserRole, {
      observe: 'response',
    });
  }

  partialUpdate(orgUserRole: PartialUpdateOrgUserRole): Observable<EntityResponseType> {
    return this.http.patch<IOrgUserRole>(`${this.resourceUrl}/${this.getOrgUserRoleIdentifier(orgUserRole)}`, orgUserRole, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrgUserRole>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrgUserRole[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrgUserRoleIdentifier(orgUserRole: Pick<IOrgUserRole, 'id'>): number {
    return orgUserRole.id;
  }

  compareOrgUserRole(o1: Pick<IOrgUserRole, 'id'> | null, o2: Pick<IOrgUserRole, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrgUserRoleIdentifier(o1) === this.getOrgUserRoleIdentifier(o2) : o1 === o2;
  }

  addOrgUserRoleToCollectionIfMissing<Type extends Pick<IOrgUserRole, 'id'>>(
    orgUserRoleCollection: Type[],
    ...orgUserRolesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orgUserRoles: Type[] = orgUserRolesToCheck.filter(isPresent);
    if (orgUserRoles.length > 0) {
      const orgUserRoleCollectionIdentifiers = orgUserRoleCollection.map(
        orgUserRoleItem => this.getOrgUserRoleIdentifier(orgUserRoleItem)!
      );
      const orgUserRolesToAdd = orgUserRoles.filter(orgUserRoleItem => {
        const orgUserRoleIdentifier = this.getOrgUserRoleIdentifier(orgUserRoleItem);
        if (orgUserRoleCollectionIdentifiers.includes(orgUserRoleIdentifier)) {
          return false;
        }
        orgUserRoleCollectionIdentifiers.push(orgUserRoleIdentifier);
        return true;
      });
      return [...orgUserRolesToAdd, ...orgUserRoleCollection];
    }
    return orgUserRoleCollection;
  }
}
