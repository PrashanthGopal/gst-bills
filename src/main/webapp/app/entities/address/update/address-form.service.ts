import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IAddress, NewAddress } from '../address.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAddress for edit and NewAddressFormGroupInput for create.
 */
type AddressFormGroupInput = IAddress | PartialWithRequiredKeyOf<NewAddress>;

type AddressFormDefaults = Pick<NewAddress, 'id'>;

type AddressFormGroupContent = {
  id: FormControl<IAddress['id'] | NewAddress['id']>;
  orgId: FormControl<IAddress['orgId']>;
  addressId: FormControl<IAddress['addressId']>;
  address1: FormControl<IAddress['address1']>;
  address2: FormControl<IAddress['address2']>;
  pincode: FormControl<IAddress['pincode']>;
  stateCode: FormControl<IAddress['stateCode']>;
};

export type AddressFormGroup = FormGroup<AddressFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AddressFormService {
  createAddressFormGroup(address: AddressFormGroupInput = { id: null }): AddressFormGroup {
    const addressRawValue = {
      ...this.getFormDefaults(),
      ...address,
    };
    return new FormGroup<AddressFormGroupContent>({
      id: new FormControl(
        { value: addressRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      orgId: new FormControl(addressRawValue.orgId),
      addressId: new FormControl(addressRawValue.addressId),
      address1: new FormControl(addressRawValue.address1),
      address2: new FormControl(addressRawValue.address2),
      pincode: new FormControl(addressRawValue.pincode),
      stateCode: new FormControl(addressRawValue.stateCode),
    });
  }

  getAddress(form: AddressFormGroup): IAddress | NewAddress {
    return form.getRawValue() as IAddress | NewAddress;
  }

  resetForm(form: AddressFormGroup, address: AddressFormGroupInput): void {
    const addressRawValue = { ...this.getFormDefaults(), ...address };
    form.reset(
      {
        ...addressRawValue,
        id: { value: addressRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): AddressFormDefaults {
    return {
      id: null,
    };
  }
}
