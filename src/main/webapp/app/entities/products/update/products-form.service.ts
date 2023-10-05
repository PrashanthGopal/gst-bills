import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IProducts, NewProducts } from '../products.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IProducts for edit and NewProductsFormGroupInput for create.
 */
type ProductsFormGroupInput = IProducts | PartialWithRequiredKeyOf<NewProducts>;

type ProductsFormDefaults = Pick<NewProducts, 'id'>;

type ProductsFormGroupContent = {
  id: FormControl<IProducts['id'] | NewProducts['id']>;
  productId: FormControl<IProducts['productId']>;
  name: FormControl<IProducts['name']>;
  description: FormControl<IProducts['description']>;
  productHsnCode: FormControl<IProducts['productHsnCode']>;
  productTaxRate: FormControl<IProducts['productTaxRate']>;
  unitCodeId: FormControl<IProducts['unitCodeId']>;
  costPerQty: FormControl<IProducts['costPerQty']>;
  cgst: FormControl<IProducts['cgst']>;
  sgst: FormControl<IProducts['sgst']>;
  igst: FormControl<IProducts['igst']>;
  totalTaxRate: FormControl<IProducts['totalTaxRate']>;
  productUnit: FormControl<IProducts['productUnit']>;
  orgProducts: FormControl<IProducts['orgProducts']>;
};

export type ProductsFormGroup = FormGroup<ProductsFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ProductsFormService {
  createProductsFormGroup(products: ProductsFormGroupInput = { id: null }): ProductsFormGroup {
    const productsRawValue = {
      ...this.getFormDefaults(),
      ...products,
    };
    return new FormGroup<ProductsFormGroupContent>({
      id: new FormControl(
        { value: productsRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      productId: new FormControl(productsRawValue.productId),
      name: new FormControl(productsRawValue.name),
      description: new FormControl(productsRawValue.description),
      productHsnCode: new FormControl(productsRawValue.productHsnCode),
      productTaxRate: new FormControl(productsRawValue.productTaxRate),
      unitCodeId: new FormControl(productsRawValue.unitCodeId),
      costPerQty: new FormControl(productsRawValue.costPerQty),
      cgst: new FormControl(productsRawValue.cgst),
      sgst: new FormControl(productsRawValue.sgst),
      igst: new FormControl(productsRawValue.igst),
      totalTaxRate: new FormControl(productsRawValue.totalTaxRate),
      productUnit: new FormControl(productsRawValue.productUnit),
      orgProducts: new FormControl(productsRawValue.orgProducts),
    });
  }

  getProducts(form: ProductsFormGroup): IProducts | NewProducts {
    return form.getRawValue() as IProducts | NewProducts;
  }

  resetForm(form: ProductsFormGroup, products: ProductsFormGroupInput): void {
    const productsRawValue = { ...this.getFormDefaults(), ...products };
    form.reset(
      {
        ...productsRawValue,
        id: { value: productsRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ProductsFormDefaults {
    return {
      id: null,
    };
  }
}
