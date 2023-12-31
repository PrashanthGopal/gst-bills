import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../invoice.test-samples';

import { InvoiceFormService } from './invoice-form.service';

describe('Invoice Form Service', () => {
  let service: InvoiceFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InvoiceFormService);
  });

  describe('Service methods', () => {
    describe('createInvoiceFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInvoiceFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoiceId: expect.any(Object),
            supplierClientId: expect.any(Object),
            buyerClientId: expect.any(Object),
            invoiceItemsId: expect.any(Object),
            shippingAddressId: expect.any(Object),
            status: expect.any(Object),
            createDateTime: expect.any(Object),
            updateDateTime: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            totalIgstRate: expect.any(Object),
            totalCgstRate: expect.any(Object),
            totalSgstRate: expect.any(Object),
            totalTaxRate: expect.any(Object),
            totalAssAmount: expect.any(Object),
            totalNoItems: expect.any(Object),
            grandTotal: expect.any(Object),
            distance: expect.any(Object),
            modeOfTransaction: expect.any(Object),
            transType: expect.any(Object),
            vehicleNo: expect.any(Object),
            transporter: expect.any(Object),
            address: expect.any(Object),
            orgInvoices: expect.any(Object),
            clients: expect.any(Object),
          })
        );
      });

      it('passing IInvoice should create a new form with FormGroup', () => {
        const formGroup = service.createInvoiceFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            invoiceId: expect.any(Object),
            supplierClientId: expect.any(Object),
            buyerClientId: expect.any(Object),
            invoiceItemsId: expect.any(Object),
            shippingAddressId: expect.any(Object),
            status: expect.any(Object),
            createDateTime: expect.any(Object),
            updateDateTime: expect.any(Object),
            createdBy: expect.any(Object),
            updatedBy: expect.any(Object),
            totalIgstRate: expect.any(Object),
            totalCgstRate: expect.any(Object),
            totalSgstRate: expect.any(Object),
            totalTaxRate: expect.any(Object),
            totalAssAmount: expect.any(Object),
            totalNoItems: expect.any(Object),
            grandTotal: expect.any(Object),
            distance: expect.any(Object),
            modeOfTransaction: expect.any(Object),
            transType: expect.any(Object),
            vehicleNo: expect.any(Object),
            transporter: expect.any(Object),
            address: expect.any(Object),
            orgInvoices: expect.any(Object),
            clients: expect.any(Object),
          })
        );
      });
    });

    describe('getInvoice', () => {
      it('should return NewInvoice for default Invoice initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInvoiceFormGroup(sampleWithNewData);

        const invoice = service.getInvoice(formGroup) as any;

        expect(invoice).toMatchObject(sampleWithNewData);
      });

      it('should return NewInvoice for empty Invoice initial value', () => {
        const formGroup = service.createInvoiceFormGroup();

        const invoice = service.getInvoice(formGroup) as any;

        expect(invoice).toMatchObject({});
      });

      it('should return IInvoice', () => {
        const formGroup = service.createInvoiceFormGroup(sampleWithRequiredData);

        const invoice = service.getInvoice(formGroup) as any;

        expect(invoice).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInvoice should not enable id FormControl', () => {
        const formGroup = service.createInvoiceFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInvoice should disable id FormControl', () => {
        const formGroup = service.createInvoiceFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
