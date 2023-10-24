import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IInvoice, NewInvoice } from '../invoice.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IInvoice for edit and NewInvoiceFormGroupInput for create.
 */
type InvoiceFormGroupInput = IInvoice | PartialWithRequiredKeyOf<NewInvoice>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IInvoice | NewInvoice> = Omit<T, 'createDateTime' | 'updateDateTime'> & {
  createDateTime?: string | null;
  updateDateTime?: string | null;
};

type InvoiceFormRawValue = FormValueOf<IInvoice>;

type NewInvoiceFormRawValue = FormValueOf<NewInvoice>;

type InvoiceFormDefaults = Pick<NewInvoice, 'id' | 'createDateTime' | 'updateDateTime'>;

type InvoiceFormGroupContent = {
  id: FormControl<InvoiceFormRawValue['id'] | NewInvoice['id']>;
  invoiceId: FormControl<InvoiceFormRawValue['invoiceId']>;
  status: FormControl<InvoiceFormRawValue['status']>;
  createDateTime: FormControl<InvoiceFormRawValue['createDateTime']>;
  updateDateTime: FormControl<InvoiceFormRawValue['updateDateTime']>;
  createdBy: FormControl<InvoiceFormRawValue['createdBy']>;
  updatedBy: FormControl<InvoiceFormRawValue['updatedBy']>;
  totalIgstRate: FormControl<InvoiceFormRawValue['totalIgstRate']>;
  totalCgstRate: FormControl<InvoiceFormRawValue['totalCgstRate']>;
  totalSgstRate: FormControl<InvoiceFormRawValue['totalSgstRate']>;
  totalTaxRate: FormControl<InvoiceFormRawValue['totalTaxRate']>;
  totalAssAmount: FormControl<InvoiceFormRawValue['totalAssAmount']>;
  totalNoItems: FormControl<InvoiceFormRawValue['totalNoItems']>;
  grandTotal: FormControl<InvoiceFormRawValue['grandTotal']>;
  distance: FormControl<InvoiceFormRawValue['distance']>;
  modeOfTransaction: FormControl<InvoiceFormRawValue['modeOfTransaction']>;
  transType: FormControl<InvoiceFormRawValue['transType']>;
  vehicleNo: FormControl<InvoiceFormRawValue['vehicleNo']>;
  transporter: FormControl<InvoiceFormRawValue['transporter']>;
  address: FormControl<InvoiceFormRawValue['address']>;
  orgInvoices: FormControl<InvoiceFormRawValue['orgInvoices']>;
  clients: FormControl<InvoiceFormRawValue['clients']>;
};

export type InvoiceFormGroup = FormGroup<InvoiceFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class InvoiceFormService {
  createInvoiceFormGroup(invoice: InvoiceFormGroupInput = { id: null }): InvoiceFormGroup {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({
      ...this.getFormDefaults(),
      ...invoice,
    });
    return new FormGroup<InvoiceFormGroupContent>({
      id: new FormControl(
        { value: invoiceRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      invoiceId: new FormControl(invoiceRawValue.invoiceId),
      status: new FormControl(invoiceRawValue.status),
      createDateTime: new FormControl(invoiceRawValue.createDateTime),
      updateDateTime: new FormControl(invoiceRawValue.updateDateTime),
      createdBy: new FormControl(invoiceRawValue.createdBy),
      updatedBy: new FormControl(invoiceRawValue.updatedBy),
      totalIgstRate: new FormControl(invoiceRawValue.totalIgstRate),
      totalCgstRate: new FormControl(invoiceRawValue.totalCgstRate),
      totalSgstRate: new FormControl(invoiceRawValue.totalSgstRate),
      totalTaxRate: new FormControl(invoiceRawValue.totalTaxRate),
      totalAssAmount: new FormControl(invoiceRawValue.totalAssAmount),
      totalNoItems: new FormControl(invoiceRawValue.totalNoItems),
      grandTotal: new FormControl(invoiceRawValue.grandTotal),
      distance: new FormControl(invoiceRawValue.distance),
      modeOfTransaction: new FormControl(invoiceRawValue.modeOfTransaction),
      transType: new FormControl(invoiceRawValue.transType),
      vehicleNo: new FormControl(invoiceRawValue.vehicleNo),
      transporter: new FormControl(invoiceRawValue.transporter),
      address: new FormControl(invoiceRawValue.address),
      orgInvoices: new FormControl(invoiceRawValue.orgInvoices),
      clients: new FormControl(invoiceRawValue.clients),
    });
  }

  getInvoice(form: InvoiceFormGroup): IInvoice | NewInvoice {
    return this.convertInvoiceRawValueToInvoice(form.getRawValue() as InvoiceFormRawValue | NewInvoiceFormRawValue);
  }

  resetForm(form: InvoiceFormGroup, invoice: InvoiceFormGroupInput): void {
    const invoiceRawValue = this.convertInvoiceToInvoiceRawValue({ ...this.getFormDefaults(), ...invoice });
    form.reset(
      {
        ...invoiceRawValue,
        id: { value: invoiceRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): InvoiceFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createDateTime: currentTime,
      updateDateTime: currentTime,
    };
  }

  private convertInvoiceRawValueToInvoice(rawInvoice: InvoiceFormRawValue | NewInvoiceFormRawValue): IInvoice | NewInvoice {
    return {
      ...rawInvoice,
      createDateTime: dayjs(rawInvoice.createDateTime, DATE_TIME_FORMAT),
      updateDateTime: dayjs(rawInvoice.updateDateTime, DATE_TIME_FORMAT),
    };
  }

  private convertInvoiceToInvoiceRawValue(
    invoice: IInvoice | (Partial<NewInvoice> & InvoiceFormDefaults)
  ): InvoiceFormRawValue | PartialWithRequiredKeyOf<NewInvoiceFormRawValue> {
    return {
      ...invoice,
      createDateTime: invoice.createDateTime ? invoice.createDateTime.format(DATE_TIME_FORMAT) : undefined,
      updateDateTime: invoice.updateDateTime ? invoice.updateDateTime.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
