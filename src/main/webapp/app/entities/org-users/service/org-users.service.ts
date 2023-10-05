import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrgUsers, NewOrgUsers } from '../org-users.model';

export type PartialUpdateOrgUsers = Partial<IOrgUsers> & Pick<IOrgUsers, 'id'>;

type RestOf<T extends IOrgUsers | NewOrgUsers> = Omit<T, 'createDate' | 'updateDate'> & {
  createDate?: string | null;
  updateDate?: string | null;
};

export type RestOrgUsers = RestOf<IOrgUsers>;

export type NewRestOrgUsers = RestOf<NewOrgUsers>;

export type PartialUpdateRestOrgUsers = RestOf<PartialUpdateOrgUsers>;

export type EntityResponseType = HttpResponse<IOrgUsers>;
export type EntityArrayResponseType = HttpResponse<IOrgUsers[]>;

@Injectable({ providedIn: 'root' })
export class OrgUsersService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/org-users');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(orgUsers: NewOrgUsers): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orgUsers);
    return this.http
      .post<RestOrgUsers>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(orgUsers: IOrgUsers): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orgUsers);
    return this.http
      .put<RestOrgUsers>(`${this.resourceUrl}/${this.getOrgUsersIdentifier(orgUsers)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(orgUsers: PartialUpdateOrgUsers): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(orgUsers);
    return this.http
      .patch<RestOrgUsers>(`${this.resourceUrl}/${this.getOrgUsersIdentifier(orgUsers)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOrgUsers>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOrgUsers[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrgUsersIdentifier(orgUsers: Pick<IOrgUsers, 'id'>): number {
    return orgUsers.id;
  }

  compareOrgUsers(o1: Pick<IOrgUsers, 'id'> | null, o2: Pick<IOrgUsers, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrgUsersIdentifier(o1) === this.getOrgUsersIdentifier(o2) : o1 === o2;
  }

  addOrgUsersToCollectionIfMissing<Type extends Pick<IOrgUsers, 'id'>>(
    orgUsersCollection: Type[],
    ...orgUsersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const orgUsers: Type[] = orgUsersToCheck.filter(isPresent);
    if (orgUsers.length > 0) {
      const orgUsersCollectionIdentifiers = orgUsersCollection.map(orgUsersItem => this.getOrgUsersIdentifier(orgUsersItem)!);
      const orgUsersToAdd = orgUsers.filter(orgUsersItem => {
        const orgUsersIdentifier = this.getOrgUsersIdentifier(orgUsersItem);
        if (orgUsersCollectionIdentifiers.includes(orgUsersIdentifier)) {
          return false;
        }
        orgUsersCollectionIdentifiers.push(orgUsersIdentifier);
        return true;
      });
      return [...orgUsersToAdd, ...orgUsersCollection];
    }
    return orgUsersCollection;
  }

  protected convertDateFromClient<T extends IOrgUsers | NewOrgUsers | PartialUpdateOrgUsers>(orgUsers: T): RestOf<T> {
    return {
      ...orgUsers,
      createDate: orgUsers.createDate?.toJSON() ?? null,
      updateDate: orgUsers.updateDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOrgUsers: RestOrgUsers): IOrgUsers {
    return {
      ...restOrgUsers,
      createDate: restOrgUsers.createDate ? dayjs(restOrgUsers.createDate) : undefined,
      updateDate: restOrgUsers.updateDate ? dayjs(restOrgUsers.updateDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOrgUsers>): HttpResponse<IOrgUsers> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOrgUsers[]>): HttpResponse<IOrgUsers[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
