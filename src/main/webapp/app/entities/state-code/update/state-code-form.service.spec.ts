import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../state-code.test-samples';

import { StateCodeFormService } from './state-code-form.service';

describe('StateCode Form Service', () => {
  let service: StateCodeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StateCodeFormService);
  });

  describe('Service methods', () => {
    describe('createStateCodeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createStateCodeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            stateCodeId: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });

      it('passing IStateCode should create a new form with FormGroup', () => {
        const formGroup = service.createStateCodeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            stateCodeId: expect.any(Object),
            code: expect.any(Object),
            name: expect.any(Object),
          })
        );
      });
    });

    describe('getStateCode', () => {
      it('should return NewStateCode for default StateCode initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createStateCodeFormGroup(sampleWithNewData);

        const stateCode = service.getStateCode(formGroup) as any;

        expect(stateCode).toMatchObject(sampleWithNewData);
      });

      it('should return NewStateCode for empty StateCode initial value', () => {
        const formGroup = service.createStateCodeFormGroup();

        const stateCode = service.getStateCode(formGroup) as any;

        expect(stateCode).toMatchObject({});
      });

      it('should return IStateCode', () => {
        const formGroup = service.createStateCodeFormGroup(sampleWithRequiredData);

        const stateCode = service.getStateCode(formGroup) as any;

        expect(stateCode).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IStateCode should not enable id FormControl', () => {
        const formGroup = service.createStateCodeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewStateCode should disable id FormControl', () => {
        const formGroup = service.createStateCodeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
