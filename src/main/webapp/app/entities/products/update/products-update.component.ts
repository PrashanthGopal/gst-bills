import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ProductsFormService, ProductsFormGroup } from './products-form.service';
import { IProducts } from '../products.model';
import { ProductsService } from '../service/products.service';
import { IProductUnit } from 'app/entities/product-unit/product-unit.model';
import { ProductUnitService } from 'app/entities/product-unit/service/product-unit.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';

@Component({
  standalone: true,
  selector: 'jhi-products-update',
  templateUrl: './products-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductsUpdateComponent implements OnInit {
  isSaving = false;
  products: IProducts | null = null;

  productUnitsCollection: IProductUnit[] = [];
  organizationsSharedCollection: IOrganization[] = [];

  editForm: ProductsFormGroup = this.productsFormService.createProductsFormGroup();

  constructor(
    protected productsService: ProductsService,
    protected productsFormService: ProductsFormService,
    protected productUnitService: ProductUnitService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProductUnit = (o1: IProductUnit | null, o2: IProductUnit | null): boolean => this.productUnitService.compareProductUnit(o1, o2);

  compareOrganization = (o1: IOrganization | null, o2: IOrganization | null): boolean =>
    this.organizationService.compareOrganization(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ products }) => {
      this.products = products;
      if (products) {
        this.updateForm(products);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const products = this.productsFormService.getProducts(this.editForm);
    if (products.id !== null) {
      this.subscribeToSaveResponse(this.productsService.update(products));
    } else {
      this.subscribeToSaveResponse(this.productsService.create(products));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProducts>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(products: IProducts): void {
    this.products = products;
    this.productsFormService.resetForm(this.editForm, products);

    this.productUnitsCollection = this.productUnitService.addProductUnitToCollectionIfMissing<IProductUnit>(
      this.productUnitsCollection,
      products.productUnit
    );
    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(
      this.organizationsSharedCollection,
      products.orgProducts
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productUnitService
      .query({ filter: 'products-is-null' })
      .pipe(map((res: HttpResponse<IProductUnit[]>) => res.body ?? []))
      .pipe(
        map((productUnits: IProductUnit[]) =>
          this.productUnitService.addProductUnitToCollectionIfMissing<IProductUnit>(productUnits, this.products?.productUnit)
        )
      )
      .subscribe((productUnits: IProductUnit[]) => (this.productUnitsCollection = productUnits));

    this.organizationService
      .query()
      .pipe(map((res: HttpResponse<IOrganization[]>) => res.body ?? []))
      .pipe(
        map((organizations: IOrganization[]) =>
          this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(organizations, this.products?.orgProducts)
        )
      )
      .subscribe((organizations: IOrganization[]) => (this.organizationsSharedCollection = organizations));
  }
}
