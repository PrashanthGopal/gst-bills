import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IOrgUserRole } from '../org-user-role.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../org-user-role.test-samples';

import { OrgUserRoleService } from './org-user-role.service';

const requireRestSample: IOrgUserRole = {
  ...sampleWithRequiredData,
};

describe('OrgUserRole Service', () => {
  let service: OrgUserRoleService;
  let httpMock: HttpTestingController;
  let expectedResult: IOrgUserRole | IOrgUserRole[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(OrgUserRoleService);
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

    it('should create a OrgUserRole', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const orgUserRole = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(orgUserRole).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a OrgUserRole', () => {
      const orgUserRole = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(orgUserRole).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a OrgUserRole', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of OrgUserRole', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a OrgUserRole', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addOrgUserRoleToCollectionIfMissing', () => {
      it('should add a OrgUserRole to an empty array', () => {
        const orgUserRole: IOrgUserRole = sampleWithRequiredData;
        expectedResult = service.addOrgUserRoleToCollectionIfMissing([], orgUserRole);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orgUserRole);
      });

      it('should not add a OrgUserRole to an array that contains it', () => {
        const orgUserRole: IOrgUserRole = sampleWithRequiredData;
        const orgUserRoleCollection: IOrgUserRole[] = [
          {
            ...orgUserRole,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addOrgUserRoleToCollectionIfMissing(orgUserRoleCollection, orgUserRole);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a OrgUserRole to an array that doesn't contain it", () => {
        const orgUserRole: IOrgUserRole = sampleWithRequiredData;
        const orgUserRoleCollection: IOrgUserRole[] = [sampleWithPartialData];
        expectedResult = service.addOrgUserRoleToCollectionIfMissing(orgUserRoleCollection, orgUserRole);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orgUserRole);
      });

      it('should add only unique OrgUserRole to an array', () => {
        const orgUserRoleArray: IOrgUserRole[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const orgUserRoleCollection: IOrgUserRole[] = [sampleWithRequiredData];
        expectedResult = service.addOrgUserRoleToCollectionIfMissing(orgUserRoleCollection, ...orgUserRoleArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const orgUserRole: IOrgUserRole = sampleWithRequiredData;
        const orgUserRole2: IOrgUserRole = sampleWithPartialData;
        expectedResult = service.addOrgUserRoleToCollectionIfMissing([], orgUserRole, orgUserRole2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(orgUserRole);
        expect(expectedResult).toContain(orgUserRole2);
      });

      it('should accept null and undefined values', () => {
        const orgUserRole: IOrgUserRole = sampleWithRequiredData;
        expectedResult = service.addOrgUserRoleToCollectionIfMissing([], null, orgUserRole, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(orgUserRole);
      });

      it('should return initial array if no OrgUserRole is added', () => {
        const orgUserRoleCollection: IOrgUserRole[] = [sampleWithRequiredData];
        expectedResult = service.addOrgUserRoleToCollectionIfMissing(orgUserRoleCollection, undefined, null);
        expect(expectedResult).toEqual(orgUserRoleCollection);
      });
    });

    describe('compareOrgUserRole', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareOrgUserRole(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareOrgUserRole(entity1, entity2);
        const compareResult2 = service.compareOrgUserRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareOrgUserRole(entity1, entity2);
        const compareResult2 = service.compareOrgUserRole(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareOrgUserRole(entity1, entity2);
        const compareResult2 = service.compareOrgUserRole(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
