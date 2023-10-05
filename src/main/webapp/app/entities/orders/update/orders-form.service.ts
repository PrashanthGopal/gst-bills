import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrders, NewOrders } from '../orders.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrders for edit and NewOrdersFormGroupInput for create.
 */
type OrdersFormGroupInput = IOrders | PartialWithRequiredKeyOf<NewOrders>;

type OrdersFormDefaults = Pick<NewOrders, 'id'>;

type OrdersFormGroupContent = {
  id: FormControl<IOrders['id'] | NewOrders['id']>;
  orderId: FormControl<IOrders['orderId']>;
  title: FormControl<IOrders['title']>;
  description: FormControl<IOrders['description']>;
  status: FormControl<IOrders['status']>;
  ordersItemsId: FormControl<IOrders['ordersItemsId']>;
  clients: FormControl<IOrders['clients']>;
  orgOrders: FormControl<IOrders['orgOrders']>;
};

export type OrdersFormGroup = FormGroup<OrdersFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrdersFormService {
  createOrdersFormGroup(orders: OrdersFormGroupInput = { id: null }): OrdersFormGroup {
    const ordersRawValue = {
      ...this.getFormDefaults(),
      ...orders,
    };
    return new FormGroup<OrdersFormGroupContent>({
      id: new FormControl(
        { value: ordersRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      orderId: new FormControl(ordersRawValue.orderId),
      title: new FormControl(ordersRawValue.title),
      description: new FormControl(ordersRawValue.description),
      status: new FormControl(ordersRawValue.status),
      ordersItemsId: new FormControl(ordersRawValue.ordersItemsId),
      clients: new FormControl(ordersRawValue.clients),
      orgOrders: new FormControl(ordersRawValue.orgOrders),
    });
  }

  getOrders(form: OrdersFormGroup): IOrders | NewOrders {
    return form.getRawValue() as IOrders | NewOrders;
  }

  resetForm(form: OrdersFormGroup, orders: OrdersFormGroupInput): void {
    const ordersRawValue = { ...this.getFormDefaults(), ...orders };
    form.reset(
      {
        ...ordersRawValue,
        id: { value: ordersRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OrdersFormDefaults {
    return {
      id: null,
    };
  }
}
