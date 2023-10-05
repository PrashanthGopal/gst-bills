import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITransporter, NewTransporter } from '../transporter.model';

export type PartialUpdateTransporter = Partial<ITransporter> & Pick<ITransporter, 'id'>;

export type EntityResponseType = HttpResponse<ITransporter>;
export type EntityArrayResponseType = HttpResponse<ITransporter[]>;

@Injectable({ providedIn: 'root' })
export class TransporterService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/transporters');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(transporter: NewTransporter): Observable<EntityResponseType> {
    return this.http.post<ITransporter>(this.resourceUrl, transporter, { observe: 'response' });
  }

  update(transporter: ITransporter): Observable<EntityResponseType> {
    return this.http.put<ITransporter>(`${this.resourceUrl}/${this.getTransporterIdentifier(transporter)}`, transporter, {
      observe: 'response',
    });
  }

  partialUpdate(transporter: PartialUpdateTransporter): Observable<EntityResponseType> {
    return this.http.patch<ITransporter>(`${this.resourceUrl}/${this.getTransporterIdentifier(transporter)}`, transporter, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITransporter>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITransporter[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTransporterIdentifier(transporter: Pick<ITransporter, 'id'>): number {
    return transporter.id;
  }

  compareTransporter(o1: Pick<ITransporter, 'id'> | null, o2: Pick<ITransporter, 'id'> | null): boolean {
    return o1 && o2 ? this.getTransporterIdentifier(o1) === this.getTransporterIdentifier(o2) : o1 === o2;
  }

  addTransporterToCollectionIfMissing<Type extends Pick<ITransporter, 'id'>>(
    transporterCollection: Type[],
    ...transportersToCheck: (Type | null | undefined)[]
  ): Type[] {
    const transporters: Type[] = transportersToCheck.filter(isPresent);
    if (transporters.length > 0) {
      const transporterCollectionIdentifiers = transporterCollection.map(
        transporterItem => this.getTransporterIdentifier(transporterItem)!
      );
      const transportersToAdd = transporters.filter(transporterItem => {
        const transporterIdentifier = this.getTransporterIdentifier(transporterItem);
        if (transporterCollectionIdentifiers.includes(transporterIdentifier)) {
          return false;
        }
        transporterCollectionIdentifiers.push(transporterIdentifier);
        return true;
      });
      return [...transportersToAdd, ...transporterCollection];
    }
    return transporterCollection;
  }
}
