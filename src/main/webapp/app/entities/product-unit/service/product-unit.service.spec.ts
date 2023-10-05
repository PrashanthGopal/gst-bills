import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IProductUnit } from '../product-unit.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../product-unit.test-samples';

import { ProductUnitService } from './product-unit.service';

const requireRestSample: IProductUnit = {
  ...sampleWithRequiredData,
};

describe('ProductUnit Service', () => {
  let service: ProductUnitService;
  let httpMock: HttpTestingController;
  let expectedResult: IProductUnit | IProductUnit[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(ProductUnitService);
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

    it('should create a ProductUnit', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const productUnit = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(productUnit).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a ProductUnit', () => {
      const productUnit = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(productUnit).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a ProductUnit', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of ProductUnit', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a ProductUnit', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addProductUnitToCollectionIfMissing', () => {
      it('should add a ProductUnit to an empty array', () => {
        const productUnit: IProductUnit = sampleWithRequiredData;
        expectedResult = service.addProductUnitToCollectionIfMissing([], productUnit);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productUnit);
      });

      it('should not add a ProductUnit to an array that contains it', () => {
        const productUnit: IProductUnit = sampleWithRequiredData;
        const productUnitCollection: IProductUnit[] = [
          {
            ...productUnit,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addProductUnitToCollectionIfMissing(productUnitCollection, productUnit);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a ProductUnit to an array that doesn't contain it", () => {
        const productUnit: IProductUnit = sampleWithRequiredData;
        const productUnitCollection: IProductUnit[] = [sampleWithPartialData];
        expectedResult = service.addProductUnitToCollectionIfMissing(productUnitCollection, productUnit);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productUnit);
      });

      it('should add only unique ProductUnit to an array', () => {
        const productUnitArray: IProductUnit[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const productUnitCollection: IProductUnit[] = [sampleWithRequiredData];
        expectedResult = service.addProductUnitToCollectionIfMissing(productUnitCollection, ...productUnitArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const productUnit: IProductUnit = sampleWithRequiredData;
        const productUnit2: IProductUnit = sampleWithPartialData;
        expectedResult = service.addProductUnitToCollectionIfMissing([], productUnit, productUnit2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(productUnit);
        expect(expectedResult).toContain(productUnit2);
      });

      it('should accept null and undefined values', () => {
        const productUnit: IProductUnit = sampleWithRequiredData;
        expectedResult = service.addProductUnitToCollectionIfMissing([], null, productUnit, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(productUnit);
      });

      it('should return initial array if no ProductUnit is added', () => {
        const productUnitCollection: IProductUnit[] = [sampleWithRequiredData];
        expectedResult = service.addProductUnitToCollectionIfMissing(productUnitCollection, undefined, null);
        expect(expectedResult).toEqual(productUnitCollection);
      });
    });

    describe('compareProductUnit', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareProductUnit(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareProductUnit(entity1, entity2);
        const compareResult2 = service.compareProductUnit(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareProductUnit(entity1, entity2);
        const compareResult2 = service.compareProductUnit(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareProductUnit(entity1, entity2);
        const compareResult2 = service.compareProductUnit(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
