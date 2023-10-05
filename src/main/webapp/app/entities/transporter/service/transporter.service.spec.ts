import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITransporter } from '../transporter.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../transporter.test-samples';

import { TransporterService } from './transporter.service';

const requireRestSample: ITransporter = {
  ...sampleWithRequiredData,
};

describe('Transporter Service', () => {
  let service: TransporterService;
  let httpMock: HttpTestingController;
  let expectedResult: ITransporter | ITransporter[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TransporterService);
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

    it('should create a Transporter', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const transporter = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(transporter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Transporter', () => {
      const transporter = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(transporter).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Transporter', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Transporter', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Transporter', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTransporterToCollectionIfMissing', () => {
      it('should add a Transporter to an empty array', () => {
        const transporter: ITransporter = sampleWithRequiredData;
        expectedResult = service.addTransporterToCollectionIfMissing([], transporter);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transporter);
      });

      it('should not add a Transporter to an array that contains it', () => {
        const transporter: ITransporter = sampleWithRequiredData;
        const transporterCollection: ITransporter[] = [
          {
            ...transporter,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTransporterToCollectionIfMissing(transporterCollection, transporter);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Transporter to an array that doesn't contain it", () => {
        const transporter: ITransporter = sampleWithRequiredData;
        const transporterCollection: ITransporter[] = [sampleWithPartialData];
        expectedResult = service.addTransporterToCollectionIfMissing(transporterCollection, transporter);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transporter);
      });

      it('should add only unique Transporter to an array', () => {
        const transporterArray: ITransporter[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const transporterCollection: ITransporter[] = [sampleWithRequiredData];
        expectedResult = service.addTransporterToCollectionIfMissing(transporterCollection, ...transporterArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const transporter: ITransporter = sampleWithRequiredData;
        const transporter2: ITransporter = sampleWithPartialData;
        expectedResult = service.addTransporterToCollectionIfMissing([], transporter, transporter2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(transporter);
        expect(expectedResult).toContain(transporter2);
      });

      it('should accept null and undefined values', () => {
        const transporter: ITransporter = sampleWithRequiredData;
        expectedResult = service.addTransporterToCollectionIfMissing([], null, transporter, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(transporter);
      });

      it('should return initial array if no Transporter is added', () => {
        const transporterCollection: ITransporter[] = [sampleWithRequiredData];
        expectedResult = service.addTransporterToCollectionIfMissing(transporterCollection, undefined, null);
        expect(expectedResult).toEqual(transporterCollection);
      });
    });

    describe('compareTransporter', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTransporter(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTransporter(entity1, entity2);
        const compareResult2 = service.compareTransporter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTransporter(entity1, entity2);
        const compareResult2 = service.compareTransporter(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTransporter(entity1, entity2);
        const compareResult2 = service.compareTransporter(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
