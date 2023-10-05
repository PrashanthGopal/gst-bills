import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IClients } from '../clients.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../clients.test-samples';

import { ClientsService } from './clients.service';

const requireRestSample: IClients = {
  ...sampleWithRequiredData,
};

describe('Clients Service', () => {
  let service: ClientsService;
  let httpMock: HttpTestingController;
  let expectedResult: IClients | IClients[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ClientsService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Clients', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const clients = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(clients).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Clients', () => {
      const clients = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(clients).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Clients', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Clients', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Clients', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addClientsToCollectionIfMissing', () => {
      it('should add a Clients to an empty array', () => {
        const clients: IClients = sampleWithRequiredData;
        expectedResult = service.addClientsToCollectionIfMissing([], clients);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(clients);
      });

      it('should not add a Clients to an array that contains it', () => {
        const clients: IClients = sampleWithRequiredData;
        const clientsCollection: IClients[] = [
          {
            ...clients,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addClientsToCollectionIfMissing(clientsCollection, clients);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Clients to an array that doesn't contain it", () => {
        const clients: IClients = sampleWithRequiredData;
        const clientsCollection: IClients[] = [sampleWithPartialData];
        expectedResult = service.addClientsToCollectionIfMissing(clientsCollection, clients);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(clients);
      });

      it('should add only unique Clients to an array', () => {
        const clientsArray: IClients[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const clientsCollection: IClients[] = [sampleWithRequiredData];
        expectedResult = service.addClientsToCollectionIfMissing(clientsCollection, ...clientsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const clients: IClients = sampleWithRequiredData;
        const clients2: IClients = sampleWithPartialData;
        expectedResult = service.addClientsToCollectionIfMissing([], clients, clients2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(clients);
        expect(expectedResult).toContain(clients2);
      });

      it('should accept null and undefined values', () => {
        const clients: IClients = sampleWithRequiredData;
        expectedResult = service.addClientsToCollectionIfMissing([], null, clients, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(clients);
      });

      it('should return initial array if no Clients is added', () => {
        const clientsCollection: IClients[] = [sampleWithRequiredData];
        expectedResult = service.addClientsToCollectionIfMissing(clientsCollection, undefined, null);
        expect(expectedResult).toEqual(clientsCollection);
      });
    });

    describe('compareClients', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareClients(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareClients(entity1, entity2);
        const compareResult2 = service.compareClients(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareClients(entity1, entity2);
        const compareResult2 = service.compareClients(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareClients(entity1, entity2);
        const compareResult2 = service.compareClients(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
