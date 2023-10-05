import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInvoiceItems } from '../invoice-items.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../invoice-items.test-samples';

import { InvoiceItemsService } from './invoice-items.service';

const requireRestSample: IInvoiceItems = {
  ...sampleWithRequiredData,
};

describe('InvoiceItems Service', () => {
  let service: InvoiceItemsService;
  let httpMock: HttpTestingController;
  let expectedResult: IInvoiceItems | IInvoiceItems[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InvoiceItemsService);
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

    it('should create a InvoiceItems', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const invoiceItems = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(invoiceItems).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InvoiceItems', () => {
      const invoiceItems = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(invoiceItems).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InvoiceItems', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InvoiceItems', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InvoiceItems', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInvoiceItemsToCollectionIfMissing', () => {
      it('should add a InvoiceItems to an empty array', () => {
        const invoiceItems: IInvoiceItems = sampleWithRequiredData;
        expectedResult = service.addInvoiceItemsToCollectionIfMissing([], invoiceItems);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(invoiceItems);
      });

      it('should not add a InvoiceItems to an array that contains it', () => {
        const invoiceItems: IInvoiceItems = sampleWithRequiredData;
        const invoiceItemsCollection: IInvoiceItems[] = [
          {
            ...invoiceItems,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInvoiceItemsToCollectionIfMissing(invoiceItemsCollection, invoiceItems);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InvoiceItems to an array that doesn't contain it", () => {
        const invoiceItems: IInvoiceItems = sampleWithRequiredData;
        const invoiceItemsCollection: IInvoiceItems[] = [sampleWithPartialData];
        expectedResult = service.addInvoiceItemsToCollectionIfMissing(invoiceItemsCollection, invoiceItems);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceItems);
      });

      it('should add only unique InvoiceItems to an array', () => {
        const invoiceItemsArray: IInvoiceItems[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const invoiceItemsCollection: IInvoiceItems[] = [sampleWithRequiredData];
        expectedResult = service.addInvoiceItemsToCollectionIfMissing(invoiceItemsCollection, ...invoiceItemsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const invoiceItems: IInvoiceItems = sampleWithRequiredData;
        const invoiceItems2: IInvoiceItems = sampleWithPartialData;
        expectedResult = service.addInvoiceItemsToCollectionIfMissing([], invoiceItems, invoiceItems2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(invoiceItems);
        expect(expectedResult).toContain(invoiceItems2);
      });

      it('should accept null and undefined values', () => {
        const invoiceItems: IInvoiceItems = sampleWithRequiredData;
        expectedResult = service.addInvoiceItemsToCollectionIfMissing([], null, invoiceItems, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(invoiceItems);
      });

      it('should return initial array if no InvoiceItems is added', () => {
        const invoiceItemsCollection: IInvoiceItems[] = [sampleWithRequiredData];
        expectedResult = service.addInvoiceItemsToCollectionIfMissing(invoiceItemsCollection, undefined, null);
        expect(expectedResult).toEqual(invoiceItemsCollection);
      });
    });

    describe('compareInvoiceItems', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInvoiceItems(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInvoiceItems(entity1, entity2);
        const compareResult2 = service.compareInvoiceItems(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInvoiceItems(entity1, entity2);
        const compareResult2 = service.compareInvoiceItems(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInvoiceItems(entity1, entity2);
        const compareResult2 = service.compareInvoiceItems(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
