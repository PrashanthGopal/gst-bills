import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../org-user-role.test-samples';

import { OrgUserRoleFormService } from './org-user-role-form.service';

describe('OrgUserRole Form Service', () => {
  let service: OrgUserRoleFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrgUserRoleFormService);
  });

  describe('Service methods', () => {
    describe('createOrgUserRoleFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrgUserRoleFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            roleId: expect.any(Object),
            orgId: expect.any(Object),
            name: expect.any(Object),
            userRole: expect.any(Object),
          })
        );
      });

      it('passing IOrgUserRole should create a new form with FormGroup', () => {
        const formGroup = service.createOrgUserRoleFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            roleId: expect.any(Object),
            orgId: expect.any(Object),
            name: expect.any(Object),
            userRole: expect.any(Object),
          })
        );
      });
    });

    describe('getOrgUserRole', () => {
      it('should return NewOrgUserRole for default OrgUserRole initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOrgUserRoleFormGroup(sampleWithNewData);

        const orgUserRole = service.getOrgUserRole(formGroup) as any;

        expect(orgUserRole).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrgUserRole for empty OrgUserRole initial value', () => {
        const formGroup = service.createOrgUserRoleFormGroup();

        const orgUserRole = service.getOrgUserRole(formGroup) as any;

        expect(orgUserRole).toMatchObject({});
      });

      it('should return IOrgUserRole', () => {
        const formGroup = service.createOrgUserRoleFormGroup(sampleWithRequiredData);

        const orgUserRole = service.getOrgUserRole(formGroup) as any;

        expect(orgUserRole).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrgUserRole should not enable id FormControl', () => {
        const formGroup = service.createOrgUserRoleFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrgUserRole should disable id FormControl', () => {
        const formGroup = service.createOrgUserRoleFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
