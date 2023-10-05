import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IInvoiceItems, NewInvoiceItems } from '../invoice-items.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoiceItems for edit and NewInvoiceItemsFormGroupInput for create.
 */
type InvoiceItemsFormGroupInput = IInvoiceItems | PartialWithRequiredKeyOf<NewInvoiceItems>;

type InvoiceItemsFormDefaults = Pick<NewInvoiceItems, 'id'>;

type InvoiceItemsFormGroupContent = {
  id: FormControl<IInvoiceItems['id'] | NewInvoiceItems['id']>;
  orgId: FormControl<IInvoiceItems['orgId']>;
  invoiceItemsId: FormControl<IInvoiceItems['invoiceItemsId']>;
  quantity: FormControl<IInvoiceItems['quantity']>;
  cgst: FormControl<IInvoiceItems['cgst']>;
  sgst: FormControl<IInvoiceItems['sgst']>;
  gst: FormControl<IInvoiceItems['gst']>;
  totalAmount: FormControl<IInvoiceItems['totalAmount']>;
  products: FormControl<IInvoiceItems['products']>;
  invoice: FormControl<IInvoiceItems['invoice']>;
};

export type InvoiceItemsFormGroup = FormGroup<InvoiceItemsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceItemsFormService {
  createInvoiceItemsFormGroup(invoiceItems: InvoiceItemsFormGroupInput = { id: null }): InvoiceItemsFormGroup {
    const invoiceItemsRawValue = {
      ...this.getFormDefaults(),
      ...invoiceItems,
    };
    return new FormGroup<InvoiceItemsFormGroupContent>({
      id: new FormControl(
        { value: invoiceItemsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      orgId: new FormControl(invoiceItemsRawValue.orgId),
      invoiceItemsId: new FormControl(invoiceItemsRawValue.invoiceItemsId),
      quantity: new FormControl(invoiceItemsRawValue.quantity),
      cgst: new FormControl(invoiceItemsRawValue.cgst),
      sgst: new FormControl(invoiceItemsRawValue.sgst),
      gst: new FormControl(invoiceItemsRawValue.gst),
      totalAmount: new FormControl(invoiceItemsRawValue.totalAmount),
      products: new FormControl(invoiceItemsRawValue.products),
      invoice: new FormControl(invoiceItemsRawValue.invoice),
    });
  }

  getInvoiceItems(form: InvoiceItemsFormGroup): IInvoiceItems | NewInvoiceItems {
    return form.getRawValue() as IInvoiceItems | NewInvoiceItems;
  }

  resetForm(form: InvoiceItemsFormGroup, invoiceItems: InvoiceItemsFormGroupInput): void {
    const invoiceItemsRawValue = { ...this.getFormDefaults(), ...invoiceItems };
    form.reset(
      {
        ...invoiceItemsRawValue,
        id: { value: invoiceItemsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InvoiceItemsFormDefaults {
    return {
      id: null,
    };
  }
}
