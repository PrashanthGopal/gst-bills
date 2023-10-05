import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrgUsers } from '../org-users.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../org-users.test-samples';

import { OrgUsersService, RestOrgUsers } from './org-users.service';

const requireRestSample: RestOrgUsers = {
  ...sampleWithRequiredData,
  createDate: sampleWithRequiredData.createDate?.toJSON(),
  updateDate: sampleWithRequiredData.updateDate?.toJSON(),
};

describe('OrgUsers Service', () => {
  let service: OrgUsersService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrgUsers | IOrgUsers[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrgUsersService);
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

    it('should create a OrgUsers', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const orgUsers = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orgUsers).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrgUsers', () => {
      const orgUsers = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orgUsers).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrgUsers', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrgUsers', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrgUsers', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOrgUsersToCollectionIfMissing', () => {
      it('should add a OrgUsers to an empty array', () => {
        const orgUsers: IOrgUsers = sampleWithRequiredData;
        expectedResult = service.addOrgUsersToCollectionIfMissing([], orgUsers);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orgUsers);
      });

      it('should not add a OrgUsers to an array that contains it', () => {
        const orgUsers: IOrgUsers = sampleWithRequiredData;
        const orgUsersCollection: IOrgUsers[] = [
          {
            ...orgUsers,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrgUsersToCollectionIfMissing(orgUsersCollection, orgUsers);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrgUsers to an array that doesn't contain it", () => {
        const orgUsers: IOrgUsers = sampleWithRequiredData;
        const orgUsersCollection: IOrgUsers[] = [sampleWithPartialData];
        expectedResult = service.addOrgUsersToCollectionIfMissing(orgUsersCollection, orgUsers);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orgUsers);
      });

      it('should add only unique OrgUsers to an array', () => {
        const orgUsersArray: IOrgUsers[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orgUsersCollection: IOrgUsers[] = [sampleWithRequiredData];
        expectedResult = service.addOrgUsersToCollectionIfMissing(orgUsersCollection, ...orgUsersArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orgUsers: IOrgUsers = sampleWithRequiredData;
        const orgUsers2: IOrgUsers = sampleWithPartialData;
        expectedResult = service.addOrgUsersToCollectionIfMissing([], orgUsers, orgUsers2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orgUsers);
        expect(expectedResult).toContain(orgUsers2);
      });

      it('should accept null and undefined values', () => {
        const orgUsers: IOrgUsers = sampleWithRequiredData;
        expectedResult = service.addOrgUsersToCollectionIfMissing([], null, orgUsers, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orgUsers);
      });

      it('should return initial array if no OrgUsers is added', () => {
        const orgUsersCollection: IOrgUsers[] = [sampleWithRequiredData];
        expectedResult = service.addOrgUsersToCollectionIfMissing(orgUsersCollection, undefined, null);
        expect(expectedResult).toEqual(orgUsersCollection);
      });
    });

    describe('compareOrgUsers', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrgUsers(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOrgUsers(entity1, entity2);
        const compareResult2 = service.compareOrgUsers(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOrgUsers(entity1, entity2);
        const compareResult2 = service.compareOrgUsers(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOrgUsers(entity1, entity2);
        const compareResult2 = service.compareOrgUsers(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
