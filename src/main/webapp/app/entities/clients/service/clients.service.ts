import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IClients, NewClients } from '../clients.model';

export type PartialUpdateClients = Partial<IClients> & Pick<IClients, 'id'>;

export type EntityResponseType = HttpResponse<IClients>;
export type EntityArrayResponseType = HttpResponse<IClients[]>;

@Injectable({ providedIn: 'root' })
export class ClientsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/clients');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(clients: NewClients): Observable<EntityResponseType> {
    return this.http.post<IClients>(this.resourceUrl, clients, { observe: 'response' });
  }

  update(clients: IClients): Observable<EntityResponseType> {
    return this.http.put<IClients>(`${this.resourceUrl}/${this.getClientsIdentifier(clients)}`, clients, { observe: 'response' });
  }

  partialUpdate(clients: PartialUpdateClients): Observable<EntityResponseType> {
    return this.http.patch<IClients>(`${this.resourceUrl}/${this.getClientsIdentifier(clients)}`, clients, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClients>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClients[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getClientsIdentifier(clients: Pick<IClients, 'id'>): number {
    return clients.id;
  }

  compareClients(o1: Pick<IClients, 'id'> | null, o2: Pick<IClients, 'id'> | null): boolean {
    return o1 && o2 ? this.getClientsIdentifier(o1) === this.getClientsIdentifier(o2) : o1 === o2;
  }

  addClientsToCollectionIfMissing<Type extends Pick<IClients, 'id'>>(
    clientsCollection: Type[],
    ...clientsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const clients: Type[] = clientsToCheck.filter(isPresent);
    if (clients.length > 0) {
      const clientsCollectionIdentifiers = clientsCollection.map(clientsItem => this.getClientsIdentifier(clientsItem)!);
      const clientsToAdd = clients.filter(clientsItem => {
        const clientsIdentifier = this.getClientsIdentifier(clientsItem);
        if (clientsCollectionIdentifiers.includes(clientsIdentifier)) {
          return false;
        }
        clientsCollectionIdentifiers.push(clientsIdentifier);
        return true;
      });
      return [...clientsToAdd, ...clientsCollection];
    }
    return clientsCollection;
  }
}
