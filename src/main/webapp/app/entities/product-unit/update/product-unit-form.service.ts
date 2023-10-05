import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProductUnit, NewProductUnit } from '../product-unit.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProductUnit for edit and NewProductUnitFormGroupInput for create.
 */
type ProductUnitFormGroupInput = IProductUnit | PartialWithRequiredKeyOf<NewProductUnit>;

type ProductUnitFormDefaults = Pick<NewProductUnit, 'id'>;

type ProductUnitFormGroupContent = {
  id: FormControl<IProductUnit['id'] | NewProductUnit['id']>;
  unitCodeId: FormControl<IProductUnit['unitCodeId']>;
  unitCode: FormControl<IProductUnit['unitCode']>;
  unitCodeDescription: FormControl<IProductUnit['unitCodeDescription']>;
};

export type ProductUnitFormGroup = FormGroup<ProductUnitFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductUnitFormService {
  createProductUnitFormGroup(productUnit: ProductUnitFormGroupInput = { id: null }): ProductUnitFormGroup {
    const productUnitRawValue = {
      ...this.getFormDefaults(),
      ...productUnit,
    };
    return new FormGroup<ProductUnitFormGroupContent>({
      id: new FormControl(
        { value: productUnitRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      unitCodeId: new FormControl(productUnitRawValue.unitCodeId),
      unitCode: new FormControl(productUnitRawValue.unitCode),
      unitCodeDescription: new FormControl(productUnitRawValue.unitCodeDescription),
    });
  }

  getProductUnit(form: ProductUnitFormGroup): IProductUnit | NewProductUnit {
    return form.getRawValue() as IProductUnit | NewProductUnit;
  }

  resetForm(form: ProductUnitFormGroup, productUnit: ProductUnitFormGroupInput): void {
    const productUnitRawValue = { ...this.getFormDefaults(), ...productUnit };
    form.reset(
      {
        ...productUnitRawValue,
        id: { value: productUnitRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductUnitFormDefaults {
    return {
      id: null,
    };
  }
}
