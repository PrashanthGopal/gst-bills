import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../org-users.test-samples';

import { OrgUsersFormService } from './org-users-form.service';

describe('OrgUsers Form Service', () => {
  let service: OrgUsersFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrgUsersFormService);
  });

  describe('Service methods', () => {
    describe('createOrgUsersFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrgUsersFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            name: expect.any(Object),
            userName: expect.any(Object),
            password: expect.any(Object),
            status: expect.any(Object),
            createDate: expect.any(Object),
            updateDate: expect.any(Object),
            profilePhoto: expect.any(Object),
            emailId: expect.any(Object),
            phoneNumber: expect.any(Object),
            orgUsers: expect.any(Object),
          })
        );
      });

      it('passing IOrgUsers should create a new form with FormGroup', () => {
        const formGroup = service.createOrgUsersFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            userId: expect.any(Object),
            name: expect.any(Object),
            userName: expect.any(Object),
            password: expect.any(Object),
            status: expect.any(Object),
            createDate: expect.any(Object),
            updateDate: expect.any(Object),
            profilePhoto: expect.any(Object),
            emailId: expect.any(Object),
            phoneNumber: expect.any(Object),
            orgUsers: expect.any(Object),
          })
        );
      });
    });

    describe('getOrgUsers', () => {
      it('should return NewOrgUsers for default OrgUsers initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOrgUsersFormGroup(sampleWithNewData);

        const orgUsers = service.getOrgUsers(formGroup) as any;

        expect(orgUsers).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrgUsers for empty OrgUsers initial value', () => {
        const formGroup = service.createOrgUsersFormGroup();

        const orgUsers = service.getOrgUsers(formGroup) as any;

        expect(orgUsers).toMatchObject({});
      });

      it('should return IOrgUsers', () => {
        const formGroup = service.createOrgUsersFormGroup(sampleWithRequiredData);

        const orgUsers = service.getOrgUsers(formGroup) as any;

        expect(orgUsers).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrgUsers should not enable id FormControl', () => {
        const formGroup = service.createOrgUsersFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrgUsers should disable id FormControl', () => {
        const formGroup = service.createOrgUsersFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
