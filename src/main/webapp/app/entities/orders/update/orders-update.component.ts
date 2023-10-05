import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { OrdersFormService, OrdersFormGroup } from './orders-form.service';
import { IOrders } from '../orders.model';
import { OrdersService } from '../service/orders.service';
import { IClients } from 'app/entities/clients/clients.model';
import { ClientsService } from 'app/entities/clients/service/clients.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';
import { OrdersEnum } from 'app/entities/enumerations/orders-enum.model';

@Component({
  standalone: true,
  selector: 'jhi-orders-update',
  templateUrl: './orders-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrdersUpdateComponent implements OnInit {
  isSaving = false;
  orders: IOrders | null = null;
  ordersEnumValues = Object.keys(OrdersEnum);

  clientsCollection: IClients[] = [];
  organizationsSharedCollection: IOrganization[] = [];

  editForm: OrdersFormGroup = this.ordersFormService.createOrdersFormGroup();

  constructor(
    protected ordersService: OrdersService,
    protected ordersFormService: OrdersFormService,
    protected clientsService: ClientsService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareClients = (o1: IClients | null, o2: IClients | null): boolean => this.clientsService.compareClients(o1, o2);

  compareOrganization = (o1: IOrganization | null, o2: IOrganization | null): boolean =>
    this.organizationService.compareOrganization(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orders }) => {
      this.orders = orders;
      if (orders) {
        this.updateForm(orders);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orders = this.ordersFormService.getOrders(this.editForm);
    if (orders.id !== null) {
      this.subscribeToSaveResponse(this.ordersService.update(orders));
    } else {
      this.subscribeToSaveResponse(this.ordersService.create(orders));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrders>>): void {
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

  protected updateForm(orders: IOrders): void {
    this.orders = orders;
    this.ordersFormService.resetForm(this.editForm, orders);

    this.clientsCollection = this.clientsService.addClientsToCollectionIfMissing<IClients>(this.clientsCollection, orders.clients);
    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(
      this.organizationsSharedCollection,
      orders.orgOrders
    );
  }

  protected loadRelationshipsOptions(): void {
    this.clientsService
      .query({ filter: 'orders-is-null' })
      .pipe(map((res: HttpResponse<IClients[]>) => res.body ?? []))
      .pipe(map((clients: IClients[]) => this.clientsService.addClientsToCollectionIfMissing<IClients>(clients, this.orders?.clients)))
      .subscribe((clients: IClients[]) => (this.clientsCollection = clients));

    this.organizationService
      .query()
      .pipe(map((res: HttpResponse<IOrganization[]>) => res.body ?? []))
      .pipe(
        map((organizations: IOrganization[]) =>
          this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(organizations, this.orders?.orgOrders)
        )
      )
      .subscribe((organizations: IOrganization[]) => (this.organizationsSharedCollection = organizations));
  }
}
