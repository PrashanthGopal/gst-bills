import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IStateCode, NewStateCode } from '../state-code.model';

export type PartialUpdateStateCode = Partial<IStateCode> & Pick<IStateCode, 'id'>;

export type EntityResponseType = HttpResponse<IStateCode>;
export type EntityArrayResponseType = HttpResponse<IStateCode[]>;

@Injectable({ providedIn: 'root' })
export class StateCodeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/state-codes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(stateCode: NewStateCode): Observable<EntityResponseType> {
    return this.http.post<IStateCode>(this.resourceUrl, stateCode, { observe: 'response' });
  }

  update(stateCode: IStateCode): Observable<EntityResponseType> {
    return this.http.put<IStateCode>(`${this.resourceUrl}/${this.getStateCodeIdentifier(stateCode)}`, stateCode, { observe: 'response' });
  }

  partialUpdate(stateCode: PartialUpdateStateCode): Observable<EntityResponseType> {
    return this.http.patch<IStateCode>(`${this.resourceUrl}/${this.getStateCodeIdentifier(stateCode)}`, stateCode, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IStateCode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IStateCode[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getStateCodeIdentifier(stateCode: Pick<IStateCode, 'id'>): number {
    return stateCode.id;
  }

  compareStateCode(o1: Pick<IStateCode, 'id'> | null, o2: Pick<IStateCode, 'id'> | null): boolean {
    return o1 && o2 ? this.getStateCodeIdentifier(o1) === this.getStateCodeIdentifier(o2) : o1 === o2;
  }

  addStateCodeToCollectionIfMissing<Type extends Pick<IStateCode, 'id'>>(
    stateCodeCollection: Type[],
    ...stateCodesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const stateCodes: Type[] = stateCodesToCheck.filter(isPresent);
    if (stateCodes.length > 0) {
      const stateCodeCollectionIdentifiers = stateCodeCollection.map(stateCodeItem => this.getStateCodeIdentifier(stateCodeItem)!);
      const stateCodesToAdd = stateCodes.filter(stateCodeItem => {
        const stateCodeIdentifier = this.getStateCodeIdentifier(stateCodeItem);
        if (stateCodeCollectionIdentifiers.includes(stateCodeIdentifier)) {
          return false;
        }
        stateCodeCollectionIdentifiers.push(stateCodeIdentifier);
        return true;
      });
      return [...stateCodesToAdd, ...stateCodeCollection];
    }
    return stateCodeCollection;
  }
}
