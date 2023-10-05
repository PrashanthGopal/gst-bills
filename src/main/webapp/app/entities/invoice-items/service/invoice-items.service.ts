import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IInvoiceItems, NewInvoiceItems } from '../invoice-items.model';

export type PartialUpdateInvoiceItems = Partial<IInvoiceItems> & Pick<IInvoiceItems, 'id'>;

export type EntityResponseType = HttpResponse<IInvoiceItems>;
export type EntityArrayResponseType = HttpResponse<IInvoiceItems[]>;

@Injectable({ providedIn: 'root' })
export class InvoiceItemsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/invoice-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(invoiceItems: NewInvoiceItems): Observable<EntityResponseType> {
    return this.http.post<IInvoiceItems>(this.resourceUrl, invoiceItems, { observe: 'response' });
  }

  update(invoiceItems: IInvoiceItems): Observable<EntityResponseType> {
    return this.http.put<IInvoiceItems>(`${this.resourceUrl}/${this.getInvoiceItemsIdentifier(invoiceItems)}`, invoiceItems, {
      observe: 'response',
    });
  }

  partialUpdate(invoiceItems: PartialUpdateInvoiceItems): Observable<EntityResponseType> {
    return this.http.patch<IInvoiceItems>(`${this.resourceUrl}/${this.getInvoiceItemsIdentifier(invoiceItems)}`, invoiceItems, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IInvoiceItems>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IInvoiceItems[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getInvoiceItemsIdentifier(invoiceItems: Pick<IInvoiceItems, 'id'>): number {
    return invoiceItems.id;
  }

  compareInvoiceItems(o1: Pick<IInvoiceItems, 'id'> | null, o2: Pick<IInvoiceItems, 'id'> | null): boolean {
    return o1 && o2 ? this.getInvoiceItemsIdentifier(o1) === this.getInvoiceItemsIdentifier(o2) : o1 === o2;
  }

  addInvoiceItemsToCollectionIfMissing<Type extends Pick<IInvoiceItems, 'id'>>(
    invoiceItemsCollection: Type[],
    ...invoiceItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const invoiceItems: Type[] = invoiceItemsToCheck.filter(isPresent);
    if (invoiceItems.length > 0) {
      const invoiceItemsCollectionIdentifiers = invoiceItemsCollection.map(
        invoiceItemsItem => this.getInvoiceItemsIdentifier(invoiceItemsItem)!
      );
      const invoiceItemsToAdd = invoiceItems.filter(invoiceItemsItem => {
        const invoiceItemsIdentifier = this.getInvoiceItemsIdentifier(invoiceItemsItem);
        if (invoiceItemsCollectionIdentifiers.includes(invoiceItemsIdentifier)) {
          return false;
        }
        invoiceItemsCollectionIdentifiers.push(invoiceItemsIdentifier);
        return true;
      });
      return [...invoiceItemsToAdd, ...invoiceItemsCollection];
    }
    return invoiceItemsCollection;
  }
}
