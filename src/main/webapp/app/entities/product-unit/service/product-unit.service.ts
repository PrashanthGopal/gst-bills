import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductUnit, NewProductUnit } from '../product-unit.model';

export type PartialUpdateProductUnit = Partial<IProductUnit> & Pick<IProductUnit, 'id'>;

export type EntityResponseType = HttpResponse<IProductUnit>;
export type EntityArrayResponseType = HttpResponse<IProductUnit[]>;

@Injectable({ providedIn: 'root' })
export class ProductUnitService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/product-units');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productUnit: NewProductUnit): Observable<EntityResponseType> {
    return this.http.post<IProductUnit>(this.resourceUrl, productUnit, { observe: 'response' });
  }

  update(productUnit: IProductUnit): Observable<EntityResponseType> {
    return this.http.put<IProductUnit>(`${this.resourceUrl}/${this.getProductUnitIdentifier(productUnit)}`, productUnit, {
      observe: 'response',
    });
  }

  partialUpdate(productUnit: PartialUpdateProductUnit): Observable<EntityResponseType> {
    return this.http.patch<IProductUnit>(`${this.resourceUrl}/${this.getProductUnitIdentifier(productUnit)}`, productUnit, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProductUnit>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProductUnit[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getProductUnitIdentifier(productUnit: Pick<IProductUnit, 'id'>): number {
    return productUnit.id;
  }

  compareProductUnit(o1: Pick<IProductUnit, 'id'> | null, o2: Pick<IProductUnit, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductUnitIdentifier(o1) === this.getProductUnitIdentifier(o2) : o1 === o2;
  }

  addProductUnitToCollectionIfMissing<Type extends Pick<IProductUnit, 'id'>>(
    productUnitCollection: Type[],
    ...productUnitsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productUnits: Type[] = productUnitsToCheck.filter(isPresent);
    if (productUnits.length > 0) {
      const productUnitCollectionIdentifiers = productUnitCollection.map(
        productUnitItem => this.getProductUnitIdentifier(productUnitItem)!
      );
      const productUnitsToAdd = productUnits.filter(productUnitItem => {
        const productUnitIdentifier = this.getProductUnitIdentifier(productUnitItem);
        if (productUnitCollectionIdentifiers.includes(productUnitIdentifier)) {
          return false;
        }
        productUnitCollectionIdentifiers.push(productUnitIdentifier);
        return true;
      });
      return [...productUnitsToAdd, ...productUnitCollection];
    }
    return productUnitCollection;
  }
}
