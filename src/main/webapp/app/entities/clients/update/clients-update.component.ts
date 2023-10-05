import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { ClientsFormService, ClientsFormGroup } from './clients-form.service';
import { IClients } from '../clients.model';
import { ClientsService } from '../service/clients.service';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  standalone: true,
  selector: 'jhi-clients-update',
  templateUrl: './clients-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class ClientsUpdateComponent implements OnInit {
  isSaving = false;
  clients: IClients | null = null;
  statusValues = Object.keys(Status);

  addressesCollection: IAddress[] = [];
  organizationsSharedCollection: IOrganization[] = [];

  editForm: ClientsFormGroup = this.clientsFormService.createClientsFormGroup();

  constructor(
    protected clientsService: ClientsService,
    protected clientsFormService: ClientsFormService,
    protected addressService: AddressService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAddress = (o1: IAddress | null, o2: IAddress | null): boolean => this.addressService.compareAddress(o1, o2);

  compareOrganization = (o1: IOrganization | null, o2: IOrganization | null): boolean =>
    this.organizationService.compareOrganization(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ clients }) => {
      this.clients = clients;
      if (clients) {
        this.updateForm(clients);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const clients = this.clientsFormService.getClients(this.editForm);
    if (clients.id !== null) {
      this.subscribeToSaveResponse(this.clientsService.update(clients));
    } else {
      this.subscribeToSaveResponse(this.clientsService.create(clients));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClients>>): void {
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

  protected updateForm(clients: IClients): void {
    this.clients = clients;
    this.clientsFormService.resetForm(this.editForm, clients);

    this.addressesCollection = this.addressService.addAddressToCollectionIfMissing<IAddress>(this.addressesCollection, clients.address);
    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(
      this.organizationsSharedCollection,
      clients.orgClients
    );
  }

  protected loadRelationshipsOptions(): void {
    this.addressService
      .query({ filter: 'clients-is-null' })
      .pipe(map((res: HttpResponse<IAddress[]>) => res.body ?? []))
      .pipe(map((addresses: IAddress[]) => this.addressService.addAddressToCollectionIfMissing<IAddress>(addresses, this.clients?.address)))
      .subscribe((addresses: IAddress[]) => (this.addressesCollection = addresses));

    this.organizationService
      .query()
      .pipe(map((res: HttpResponse<IOrganization[]>) => res.body ?? []))
      .pipe(
        map((organizations: IOrganization[]) =>
          this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(organizations, this.clients?.orgClients)
        )
      )
      .subscribe((organizations: IOrganization[]) => (this.organizationsSharedCollection = organizations));
  }
}
