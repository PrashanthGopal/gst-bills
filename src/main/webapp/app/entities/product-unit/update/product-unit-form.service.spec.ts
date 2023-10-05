import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../product-unit.test-samples';

import { ProductUnitFormService } from './product-unit-form.service';

describe('ProductUnit Form Service', () => {
  let service: ProductUnitFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductUnitFormService);
  });

  describe('Service methods', () => {
    describe('createProductUnitFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createProductUnitFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            unitCodeId: expect.any(Object),
            unitCode: expect.any(Object),
            unitCodeDescription: expect.any(Object),
          })
        );
      });

      it('passing IProductUnit should create a new form with FormGroup', () => {
        const formGroup = service.createProductUnitFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            unitCodeId: expect.any(Object),
            unitCode: expect.any(Object),
            unitCodeDescription: expect.any(Object),
          })
        );
      });
    });

    describe('getProductUnit', () => {
      it('should return NewProductUnit for default ProductUnit initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createProductUnitFormGroup(sampleWithNewData);

        const productUnit = service.getProductUnit(formGroup) as any;

        expect(productUnit).toMatchObject(sampleWithNewData);
      });

      it('should return NewProductUnit for empty ProductUnit initial value', () => {
        const formGroup = service.createProductUnitFormGroup();

        const productUnit = service.getProductUnit(formGroup) as any;

        expect(productUnit).toMatchObject({});
      });

      it('should return IProductUnit', () => {
        const formGroup = service.createProductUnitFormGroup(sampleWithRequiredData);

        const productUnit = service.getProductUnit(formGroup) as any;

        expect(productUnit).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IProductUnit should not enable id FormControl', () => {
        const formGroup = service.createProductUnitFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewProductUnit should disable id FormControl', () => {
        const formGroup = service.createProductUnitFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
