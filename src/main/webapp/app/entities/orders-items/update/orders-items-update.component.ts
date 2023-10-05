import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { OrdersItemsFormService, OrdersItemsFormGroup } from './orders-items-form.service';
import { IOrdersItems } from '../orders-items.model';
import { OrdersItemsService } from '../service/orders-items.service';
import { IProducts } from 'app/entities/products/products.model';
import { ProductsService } from 'app/entities/products/service/products.service';
import { IOrders } from 'app/entities/orders/orders.model';
import { OrdersService } from 'app/entities/orders/service/orders.service';

@Component({
  standalone: true,
  selector: 'jhi-orders-items-update',
  templateUrl: './orders-items-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrdersItemsUpdateComponent implements OnInit {
  isSaving = false;
  ordersItems: IOrdersItems | null = null;

  productsCollection: IProducts[] = [];
  ordersSharedCollection: IOrders[] = [];

  editForm: OrdersItemsFormGroup = this.ordersItemsFormService.createOrdersItemsFormGroup();

  constructor(
    protected ordersItemsService: OrdersItemsService,
    protected ordersItemsFormService: OrdersItemsFormService,
    protected productsService: ProductsService,
    protected ordersService: OrdersService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareProducts = (o1: IProducts | null, o2: IProducts | null): boolean => this.productsService.compareProducts(o1, o2);

  compareOrders = (o1: IOrders | null, o2: IOrders | null): boolean => this.ordersService.compareOrders(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ ordersItems }) => {
      this.ordersItems = ordersItems;
      if (ordersItems) {
        this.updateForm(ordersItems);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const ordersItems = this.ordersItemsFormService.getOrdersItems(this.editForm);
    if (ordersItems.id !== null) {
      this.subscribeToSaveResponse(this.ordersItemsService.update(ordersItems));
    } else {
      this.subscribeToSaveResponse(this.ordersItemsService.create(ordersItems));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrdersItems>>): void {
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

  protected updateForm(ordersItems: IOrdersItems): void {
    this.ordersItems = ordersItems;
    this.ordersItemsFormService.resetForm(this.editForm, ordersItems);

    this.productsCollection = this.productsService.addProductsToCollectionIfMissing<IProducts>(
      this.productsCollection,
      ordersItems.products
    );
    this.ordersSharedCollection = this.ordersService.addOrdersToCollectionIfMissing<IOrders>(
      this.ordersSharedCollection,
      ordersItems.orders
    );
  }

  protected loadRelationshipsOptions(): void {
    this.productsService
      .query({ filter: 'ordersitems-is-null' })
      .pipe(map((res: HttpResponse<IProducts[]>) => res.body ?? []))
      .pipe(
        map((products: IProducts[]) =>
          this.productsService.addProductsToCollectionIfMissing<IProducts>(products, this.ordersItems?.products)
        )
      )
      .subscribe((products: IProducts[]) => (this.productsCollection = products));

    this.ordersService
      .query()
      .pipe(map((res: HttpResponse<IOrders[]>) => res.body ?? []))
      .pipe(map((orders: IOrders[]) => this.ordersService.addOrdersToCollectionIfMissing<IOrders>(orders, this.ordersItems?.orders)))
      .subscribe((orders: IOrders[]) => (this.ordersSharedCollection = orders));
  }
}
