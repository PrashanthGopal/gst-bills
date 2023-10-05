import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../invoice-items.test-samples';

import { InvoiceItemsFormService } from './invoice-items-form.service';

describe('InvoiceItems Form Service', () => {
  let service: InvoiceItemsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InvoiceItemsFormService);
  });

  describe('Service methods', () => {
    describe('createInvoiceItemsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInvoiceItemsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            orgId: expect.any(Object),
            invoiceItemsId: expect.any(Object),
            quantity: expect.any(Object),
            cgst: expect.any(Object),
            sgst: expect.any(Object),
            gst: expect.any(Object),
            totalAmount: expect.any(Object),
            products: expect.any(Object),
            invoice: expect.any(Object),
          })
        );
      });

      it('passing IInvoiceItems should create a new form with FormGroup', () => {
        const formGroup = service.createInvoiceItemsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            orgId: expect.any(Object),
            invoiceItemsId: expect.any(Object),
            quantity: expect.any(Object),
            cgst: expect.any(Object),
            sgst: expect.any(Object),
            gst: expect.any(Object),
            totalAmount: expect.any(Object),
            products: expect.any(Object),
            invoice: expect.any(Object),
          })
        );
      });
    });

    describe('getInvoiceItems', () => {
      it('should return NewInvoiceItems for default InvoiceItems initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInvoiceItemsFormGroup(sampleWithNewData);

        const invoiceItems = service.getInvoiceItems(formGroup) as any;

        expect(invoiceItems).toMatchObject(sampleWithNewData);
      });

      it('should return NewInvoiceItems for empty InvoiceItems initial value', () => {
        const formGroup = service.createInvoiceItemsFormGroup();

        const invoiceItems = service.getInvoiceItems(formGroup) as any;

        expect(invoiceItems).toMatchObject({});
      });

      it('should return IInvoiceItems', () => {
        const formGroup = service.createInvoiceItemsFormGroup(sampleWithRequiredData);

        const invoiceItems = service.getInvoiceItems(formGroup) as any;

        expect(invoiceItems).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInvoiceItems should not enable id FormControl', () => {
        const formGroup = service.createInvoiceItemsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInvoiceItems should disable id FormControl', () => {
        const formGroup = service.createInvoiceItemsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
