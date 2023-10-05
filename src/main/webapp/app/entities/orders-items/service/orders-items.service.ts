import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IOrdersItems, NewOrdersItems } from '../orders-items.model';

export type PartialUpdateOrdersItems = Partial<IOrdersItems> & Pick<IOrdersItems, 'id'>;

export type EntityResponseType = HttpResponse<IOrdersItems>;
export type EntityArrayResponseType = HttpResponse<IOrdersItems[]>;

@Injectable({ providedIn: 'root' })
export class OrdersItemsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/orders-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ordersItems: NewOrdersItems): Observable<EntityResponseType> {
    return this.http.post<IOrdersItems>(this.resourceUrl, ordersItems, { observe: 'response' });
  }

  update(ordersItems: IOrdersItems): Observable<EntityResponseType> {
    return this.http.put<IOrdersItems>(`${this.resourceUrl}/${this.getOrdersItemsIdentifier(ordersItems)}`, ordersItems, {
      observe: 'response',
    });
  }

  partialUpdate(ordersItems: PartialUpdateOrdersItems): Observable<EntityResponseType> {
    return this.http.patch<IOrdersItems>(`${this.resourceUrl}/${this.getOrdersItemsIdentifier(ordersItems)}`, ordersItems, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IOrdersItems>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IOrdersItems[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getOrdersItemsIdentifier(ordersItems: Pick<IOrdersItems, 'id'>): number {
    return ordersItems.id;
  }

  compareOrdersItems(o1: Pick<IOrdersItems, 'id'> | null, o2: Pick<IOrdersItems, 'id'> | null): boolean {
    return o1 && o2 ? this.getOrdersItemsIdentifier(o1) === this.getOrdersItemsIdentifier(o2) : o1 === o2;
  }

  addOrdersItemsToCollectionIfMissing<Type extends Pick<IOrdersItems, 'id'>>(
    ordersItemsCollection: Type[],
    ...ordersItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ordersItems: Type[] = ordersItemsToCheck.filter(isPresent);
    if (ordersItems.length > 0) {
      const ordersItemsCollectionIdentifiers = ordersItemsCollection.map(
        ordersItemsItem => this.getOrdersItemsIdentifier(ordersItemsItem)!
      );
      const ordersItemsToAdd = ordersItems.filter(ordersItemsItem => {
        const ordersItemsIdentifier = this.getOrdersItemsIdentifier(ordersItemsItem);
        if (ordersItemsCollectionIdentifiers.includes(ordersItemsIdentifier)) {
          return false;
        }
        ordersItemsCollectionIdentifiers.push(ordersItemsIdentifier);
        return true;
      });
      return [...ordersItemsToAdd, ...ordersItemsCollection];
    }
    return ordersItemsCollection;
  }
}
