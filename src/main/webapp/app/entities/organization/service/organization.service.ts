import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrganization, NewOrganization } from '../organization.model';

export type PartialUpdateOrganization = Partial<IOrganization> & Pick<IOrganization, 'id'>;

type RestOf<T extends IOrganization | NewOrganization> = Omit<T, 'dateOfRegestation' | 'dateOfDeRegestation'> & {
  dateOfRegestation?: string | null;
  dateOfDeRegestation?: string | null;
};

export type RestOrganization = RestOf<IOrganization>;

export type NewRestOrganization = RestOf<NewOrganization>;

export type PartialUpdateRestOrganization = RestOf<PartialUpdateOrganization>;

export type EntityResponseType = HttpResponse<IOrganization>;
export type EntityArrayResponseType = HttpResponse<IOrganization[]>;

@Injectable({ providedIn: 'root' })
export class OrganizationService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/organizations');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(organization: NewOrganization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organization);
    return this.http
      .post<RestOrganization>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(organization: IOrganization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organization);
    return this.http
      .put<RestOrganization>(`${this.resourceUrl}/${this.getOrganizationIdentifier(organization)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(organization: PartialUpdateOrganization): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(organization);
    return this.http
      .patch<RestOrganization>(`${this.resourceUrl}/${this.getOrganizationIdentifier(organization)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestOrganization>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestOrganization[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrganizationIdentifier(organization: Pick<IOrganization, 'id'>): number {
    return organization.id;
  }

  compareOrganization(o1: Pick<IOrganization, 'id'> | null, o2: Pick<IOrganization, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrganizationIdentifier(o1) === this.getOrganizationIdentifier(o2) : o1 === o2;
  }

  addOrganizationToCollectionIfMissing<Type extends Pick<IOrganization, 'id'>>(
    organizationCollection: Type[],
    ...organizationsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const organizations: Type[] = organizationsToCheck.filter(isPresent);
    if (organizations.length > 0) {
      const organizationCollectionIdentifiers = organizationCollection.map(
        organizationItem => this.getOrganizationIdentifier(organizationItem)!
      );
      const organizationsToAdd = organizations.filter(organizationItem => {
        const organizationIdentifier = this.getOrganizationIdentifier(organizationItem);
        if (organizationCollectionIdentifiers.includes(organizationIdentifier)) {
          return false;
        }
        organizationCollectionIdentifiers.push(organizationIdentifier);
        return true;
      });
      return [...organizationsToAdd, ...organizationCollection];
    }
    return organizationCollection;
  }

  protected convertDateFromClient<T extends IOrganization | NewOrganization | PartialUpdateOrganization>(organization: T): RestOf<T> {
    return {
      ...organization,
      dateOfRegestation: organization.dateOfRegestation?.toJSON() ?? null,
      dateOfDeRegestation: organization.dateOfDeRegestation?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restOrganization: RestOrganization): IOrganization {
    return {
      ...restOrganization,
      dateOfRegestation: restOrganization.dateOfRegestation ? dayjs(restOrganization.dateOfRegestation) : undefined,
      dateOfDeRegestation: restOrganization.dateOfDeRegestation ? dayjs(restOrganization.dateOfDeRegestation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestOrganization>): HttpResponse<IOrganization> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestOrganization[]>): HttpResponse<IOrganization[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
