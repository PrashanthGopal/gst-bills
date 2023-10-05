import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrdersItems } from '../orders-items.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../orders-items.test-samples';

import { OrdersItemsService } from './orders-items.service';

const requireRestSample: IOrdersItems = {
  ...sampleWithRequiredData,
};

describe('OrdersItems Service', () => {
  let service: OrdersItemsService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrdersItems | IOrdersItems[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrdersItemsService);
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

    it('should create a OrdersItems', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const ordersItems = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(ordersItems).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrdersItems', () => {
      const ordersItems = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(ordersItems).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrdersItems', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrdersItems', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrdersItems', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOrdersItemsToCollectionIfMissing', () => {
      it('should add a OrdersItems to an empty array', () => {
        const ordersItems: IOrdersItems = sampleWithRequiredData;
        expectedResult = service.addOrdersItemsToCollectionIfMissing([], ordersItems);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ordersItems);
      });

      it('should not add a OrdersItems to an array that contains it', () => {
        const ordersItems: IOrdersItems = sampleWithRequiredData;
        const ordersItemsCollection: IOrdersItems[] = [
          {
            ...ordersItems,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrdersItemsToCollectionIfMissing(ordersItemsCollection, ordersItems);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrdersItems to an array that doesn't contain it", () => {
        const ordersItems: IOrdersItems = sampleWithRequiredData;
        const ordersItemsCollection: IOrdersItems[] = [sampleWithPartialData];
        expectedResult = service.addOrdersItemsToCollectionIfMissing(ordersItemsCollection, ordersItems);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ordersItems);
      });

      it('should add only unique OrdersItems to an array', () => {
        const ordersItemsArray: IOrdersItems[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const ordersItemsCollection: IOrdersItems[] = [sampleWithRequiredData];
        expectedResult = service.addOrdersItemsToCollectionIfMissing(ordersItemsCollection, ...ordersItemsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const ordersItems: IOrdersItems = sampleWithRequiredData;
        const ordersItems2: IOrdersItems = sampleWithPartialData;
        expectedResult = service.addOrdersItemsToCollectionIfMissing([], ordersItems, ordersItems2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(ordersItems);
        expect(expectedResult).toContain(ordersItems2);
      });

      it('should accept null and undefined values', () => {
        const ordersItems: IOrdersItems = sampleWithRequiredData;
        expectedResult = service.addOrdersItemsToCollectionIfMissing([], null, ordersItems, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(ordersItems);
      });

      it('should return initial array if no OrdersItems is added', () => {
        const ordersItemsCollection: IOrdersItems[] = [sampleWithRequiredData];
        expectedResult = service.addOrdersItemsToCollectionIfMissing(ordersItemsCollection, undefined, null);
        expect(expectedResult).toEqual(ordersItemsCollection);
      });
    });

    describe('compareOrdersItems', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrdersItems(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOrdersItems(entity1, entity2);
        const compareResult2 = service.compareOrdersItems(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOrdersItems(entity1, entity2);
        const compareResult2 = service.compareOrdersItems(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOrdersItems(entity1, entity2);
        const compareResult2 = service.compareOrdersItems(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
