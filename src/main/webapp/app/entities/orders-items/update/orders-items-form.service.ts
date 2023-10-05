import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrdersItems, NewOrdersItems } from '../orders-items.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrdersItems for edit and NewOrdersItemsFormGroupInput for create.
 */
type OrdersItemsFormGroupInput = IOrdersItems | PartialWithRequiredKeyOf<NewOrdersItems>;

type OrdersItemsFormDefaults = Pick<NewOrdersItems, 'id'>;

type OrdersItemsFormGroupContent = {
  id: FormControl<IOrdersItems['id'] | NewOrdersItems['id']>;
  orgId: FormControl<IOrdersItems['orgId']>;
  orderItemsId: FormControl<IOrdersItems['orderItemsId']>;
  quantity: FormControl<IOrdersItems['quantity']>;
  cgst: FormControl<IOrdersItems['cgst']>;
  sgst: FormControl<IOrdersItems['sgst']>;
  gst: FormControl<IOrdersItems['gst']>;
  totalAmount: FormControl<IOrdersItems['totalAmount']>;
  products: FormControl<IOrdersItems['products']>;
  orders: FormControl<IOrdersItems['orders']>;
};

export type OrdersItemsFormGroup = FormGroup<OrdersItemsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrdersItemsFormService {
  createOrdersItemsFormGroup(ordersItems: OrdersItemsFormGroupInput = { id: null }): OrdersItemsFormGroup {
    const ordersItemsRawValue = {
      ...this.getFormDefaults(),
      ...ordersItems,
    };
    return new FormGroup<OrdersItemsFormGroupContent>({
      id: new FormControl(
        { value: ordersItemsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      orgId: new FormControl(ordersItemsRawValue.orgId),
      orderItemsId: new FormControl(ordersItemsRawValue.orderItemsId),
      quantity: new FormControl(ordersItemsRawValue.quantity),
      cgst: new FormControl(ordersItemsRawValue.cgst),
      sgst: new FormControl(ordersItemsRawValue.sgst),
      gst: new FormControl(ordersItemsRawValue.gst),
      totalAmount: new FormControl(ordersItemsRawValue.totalAmount),
      products: new FormControl(ordersItemsRawValue.products),
      orders: new FormControl(ordersItemsRawValue.orders),
    });
  }

  getOrdersItems(form: OrdersItemsFormGroup): IOrdersItems | NewOrdersItems {
    return form.getRawValue() as IOrdersItems | NewOrdersItems;
  }

  resetForm(form: OrdersItemsFormGroup, ordersItems: OrdersItemsFormGroupInput): void {
    const ordersItemsRawValue = { ...this.getFormDefaults(), ...ordersItems };
    form.reset(
      {
        ...ordersItemsRawValue,
        id: { value: ordersItemsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OrdersItemsFormDefaults {
    return {
      id: null,
    };
  }
}
