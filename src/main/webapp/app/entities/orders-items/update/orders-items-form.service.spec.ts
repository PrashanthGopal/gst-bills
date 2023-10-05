import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../orders-items.test-samples';

import { OrdersItemsFormService } from './orders-items-form.service';

describe('OrdersItems Form Service', () => {
  let service: OrdersItemsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(OrdersItemsFormService);
  });

  describe('Service methods', () => {
    describe('createOrdersItemsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createOrdersItemsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            orgId: expect.any(Object),
            orderItemsId: expect.any(Object),
            quantity: expect.any(Object),
            cgst: expect.any(Object),
            sgst: expect.any(Object),
            gst: expect.any(Object),
            totalAmount: expect.any(Object),
            products: expect.any(Object),
            orders: expect.any(Object),
          })
        );
      });

      it('passing IOrdersItems should create a new form with FormGroup', () => {
        const formGroup = service.createOrdersItemsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            orgId: expect.any(Object),
            orderItemsId: expect.any(Object),
            quantity: expect.any(Object),
            cgst: expect.any(Object),
            sgst: expect.any(Object),
            gst: expect.any(Object),
            totalAmount: expect.any(Object),
            products: expect.any(Object),
            orders: expect.any(Object),
          })
        );
      });
    });

    describe('getOrdersItems', () => {
      it('should return NewOrdersItems for default OrdersItems initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createOrdersItemsFormGroup(sampleWithNewData);

        const ordersItems = service.getOrdersItems(formGroup) as any;

        expect(ordersItems).toMatchObject(sampleWithNewData);
      });

      it('should return NewOrdersItems for empty OrdersItems initial value', () => {
        const formGroup = service.createOrdersItemsFormGroup();

        const ordersItems = service.getOrdersItems(formGroup) as any;

        expect(ordersItems).toMatchObject({});
      });

      it('should return IOrdersItems', () => {
        const formGroup = service.createOrdersItemsFormGroup(sampleWithRequiredData);

        const ordersItems = service.getOrdersItems(formGroup) as any;

        expect(ordersItems).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IOrdersItems should not enable id FormControl', () => {
        const formGroup = service.createOrdersItemsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewOrdersItems should disable id FormControl', () => {
        const formGroup = service.createOrdersItemsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
