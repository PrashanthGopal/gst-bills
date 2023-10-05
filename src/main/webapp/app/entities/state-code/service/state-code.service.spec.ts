import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStateCode } from '../state-code.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../state-code.test-samples';

import { StateCodeService } from './state-code.service';

const requireRestSample: IStateCode = {
  ...sampleWithRequiredData,
};

describe('StateCode Service', () => {
  let service: StateCodeService;
  let httpMock: HttpTestingController;
  let expectedResult: IStateCode | IStateCode[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StateCodeService);
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

    it('should create a StateCode', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const stateCode = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(stateCode).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StateCode', () => {
      const stateCode = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(stateCode).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StateCode', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StateCode', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a StateCode', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addStateCodeToCollectionIfMissing', () => {
      it('should add a StateCode to an empty array', () => {
        const stateCode: IStateCode = sampleWithRequiredData;
        expectedResult = service.addStateCodeToCollectionIfMissing([], stateCode);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stateCode);
      });

      it('should not add a StateCode to an array that contains it', () => {
        const stateCode: IStateCode = sampleWithRequiredData;
        const stateCodeCollection: IStateCode[] = [
          {
            ...stateCode,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addStateCodeToCollectionIfMissing(stateCodeCollection, stateCode);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StateCode to an array that doesn't contain it", () => {
        const stateCode: IStateCode = sampleWithRequiredData;
        const stateCodeCollection: IStateCode[] = [sampleWithPartialData];
        expectedResult = service.addStateCodeToCollectionIfMissing(stateCodeCollection, stateCode);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stateCode);
      });

      it('should add only unique StateCode to an array', () => {
        const stateCodeArray: IStateCode[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const stateCodeCollection: IStateCode[] = [sampleWithRequiredData];
        expectedResult = service.addStateCodeToCollectionIfMissing(stateCodeCollection, ...stateCodeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const stateCode: IStateCode = sampleWithRequiredData;
        const stateCode2: IStateCode = sampleWithPartialData;
        expectedResult = service.addStateCodeToCollectionIfMissing([], stateCode, stateCode2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(stateCode);
        expect(expectedResult).toContain(stateCode2);
      });

      it('should accept null and undefined values', () => {
        const stateCode: IStateCode = sampleWithRequiredData;
        expectedResult = service.addStateCodeToCollectionIfMissing([], null, stateCode, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(stateCode);
      });

      it('should return initial array if no StateCode is added', () => {
        const stateCodeCollection: IStateCode[] = [sampleWithRequiredData];
        expectedResult = service.addStateCodeToCollectionIfMissing(stateCodeCollection, undefined, null);
        expect(expectedResult).toEqual(stateCodeCollection);
      });
    });

    describe('compareStateCode', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareStateCode(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareStateCode(entity1, entity2);
        const compareResult2 = service.compareStateCode(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareStateCode(entity1, entity2);
        const compareResult2 = service.compareStateCode(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareStateCode(entity1, entity2);
        const compareResult2 = service.compareStateCode(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
