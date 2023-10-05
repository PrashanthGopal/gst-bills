import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { InvoiceItemsFormService, InvoiceItemsFormGroup } from './invoice-items-form.service';
import { IInvoiceItems } from '../invoice-items.model';
import { InvoiceItemsService } from '../service/invoice-items.service';
import { IProducts } from 'app/entities/products/products.model';
import { ProductsService } from 'app/entities/products/service/products.service';
import { IInvoice } from 'app/entities/invoice/invoice.model';
import { InvoiceService } from 'app/entities/invoice/service/invoice.service';

@Component({
  standalone: true,
  selector: 'jhi-invoice-items-update',
  templateUrl: './invoice-items-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InvoiceItemsUpdateComponent implements OnInit {
  isSaving = false;
  invoiceItems: IInvoiceItems | null = null;

  productsCollection: IProducts[] = [];
  invoicesSharedCollection: IInvoice[] = [];

  editForm: InvoiceItemsFormGroup = this.invoiceItemsFormService.createInvoiceItemsFormGroup();

  constructor(
    protected invoiceItemsService: InvoiceItemsService,
    protected invoiceItemsFormService: InvoiceItemsFormService,
    protected productsService: ProductsService,
    protected invoiceService: InvoiceService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProducts = (o1: IProducts | null, o2: IProducts | null): boolean => this.productsService.compareProducts(o1, o2);

  compareInvoice = (o1: IInvoice | null, o2: IInvoice | null): boolean => this.invoiceService.compareInvoice(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoiceItems }) => {
      this.invoiceItems = invoiceItems;
      if (invoiceItems) {
        this.updateForm(invoiceItems);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoiceItems = this.invoiceItemsFormService.getInvoiceItems(this.editForm);
    if (invoiceItems.id !== null) {
      this.subscribeToSaveResponse(this.invoiceItemsService.update(invoiceItems));
    } else {
      this.subscribeToSaveResponse(this.invoiceItemsService.create(invoiceItems));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoiceItems>>): void {
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

  protected updateForm(invoiceItems: IInvoiceItems): void {
    this.invoiceItems = invoiceItems;
    this.invoiceItemsFormService.resetForm(this.editForm, invoiceItems);

    this.productsCollection = this.productsService.addProductsToCollectionIfMissing<IProducts>(
      this.productsCollection,
      invoiceItems.products
    );
    this.invoicesSharedCollection = this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(
      this.invoicesSharedCollection,
      invoiceItems.invoice
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productsService
      .query({ filter: 'invoiceitems-is-null' })
      .pipe(map((res: HttpResponse<IProducts[]>) => res.body ?? []))
      .pipe(
        map((products: IProducts[]) =>
          this.productsService.addProductsToCollectionIfMissing<IProducts>(products, this.invoiceItems?.products)
        )
      )
      .subscribe((products: IProducts[]) => (this.productsCollection = products));

    this.invoiceService
      .query()
      .pipe(map((res: HttpResponse<IInvoice[]>) => res.body ?? []))
      .pipe(
        map((invoices: IInvoice[]) => this.invoiceService.addInvoiceToCollectionIfMissing<IInvoice>(invoices, this.invoiceItems?.invoice))
      )
      .subscribe((invoices: IInvoice[]) => (this.invoicesSharedCollection = invoices));
  }
}
