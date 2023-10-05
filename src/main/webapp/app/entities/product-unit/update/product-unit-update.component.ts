import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ProductUnitFormService, ProductUnitFormGroup } from './product-unit-form.service';
import { IProductUnit } from '../product-unit.model';
import { ProductUnitService } from '../service/product-unit.service';

@Component({
  standalone: true,
  selector: 'jhi-product-unit-update',
  templateUrl: './product-unit-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ProductUnitUpdateComponent implements OnInit {
  isSaving = false;
  productUnit: IProductUnit | null = null;

  editForm: ProductUnitFormGroup = this.productUnitFormService.createProductUnitFormGroup();

  constructor(
    protected productUnitService: ProductUnitService,
    protected productUnitFormService: ProductUnitFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ productUnit }) => {
      this.productUnit = productUnit;
      if (productUnit) {
        this.updateForm(productUnit);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const productUnit = this.productUnitFormService.getProductUnit(this.editForm);
    if (productUnit.id !== null) {
      this.subscribeToSaveResponse(this.productUnitService.update(productUnit));
    } else {
      this.subscribeToSaveResponse(this.productUnitService.create(productUnit));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProductUnit>>): void {
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

  protected updateForm(productUnit: IProductUnit): void {
    this.productUnit = productUnit;
    this.productUnitFormService.resetForm(this.editForm, productUnit);
  }
}
