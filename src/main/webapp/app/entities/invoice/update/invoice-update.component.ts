import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { InvoiceFormService, InvoiceFormGroup } from './invoice-form.service';
import { IInvoice } from '../invoice.model';
import { InvoiceService } from '../service/invoice.service';
import { ITransporter } from 'app/entities/transporter/transporter.model';
import { TransporterService } from 'app/entities/transporter/service/transporter.service';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';
import { IClients } from 'app/entities/clients/clients.model';
import { ClientsService } from 'app/entities/clients/service/clients.service';
import { InvoiceStatus } from 'app/entities/enumerations/invoice-status.model';

@Component({
  standalone: true,
  selector: 'jhi-invoice-update',
  templateUrl: './invoice-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class InvoiceUpdateComponent implements OnInit {
  isSaving = false;
  invoice: IInvoice | null = null;
  invoiceStatusValues = Object.keys(InvoiceStatus);

  transportersCollection: ITransporter[] = [];
  addressesCollection: IAddress[] = [];
  organizationsSharedCollection: IOrganization[] = [];
  clientsSharedCollection: IClients[] = [];

  editForm: InvoiceFormGroup = this.invoiceFormService.createInvoiceFormGroup();

  constructor(
    protected invoiceService: InvoiceService,
    protected invoiceFormService: InvoiceFormService,
    protected transporterService: TransporterService,
    protected addressService: AddressService,
    protected organizationService: OrganizationService,
    protected clientsService: ClientsService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTransporter = (o1: ITransporter | null, o2: ITransporter | null): boolean => this.transporterService.compareTransporter(o1, o2);

  compareAddress = (o1: IAddress | null, o2: IAddress | null): boolean => this.addressService.compareAddress(o1, o2);

  compareOrganization = (o1: IOrganization | null, o2: IOrganization | null): boolean =>
    this.organizationService.compareOrganization(o1, o2);

  compareClients = (o1: IClients | null, o2: IClients | null): boolean => this.clientsService.compareClients(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ invoice }) => {
      this.invoice = invoice;
      if (invoice) {
        this.updateForm(invoice);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const invoice = this.invoiceFormService.getInvoice(this.editForm);
    if (invoice.id !== null) {
      this.subscribeToSaveResponse(this.invoiceService.update(invoice));
    } else {
      this.subscribeToSaveResponse(this.invoiceService.create(invoice));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IInvoice>>): void {
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

  protected updateForm(invoice: IInvoice): void {
    this.invoice = invoice;
    this.invoiceFormService.resetForm(this.editForm, invoice);

    this.transportersCollection = this.transporterService.addTransporterToCollectionIfMissing<ITransporter>(
      this.transportersCollection,
      invoice.transporter
    );
    this.addressesCollection = this.addressService.addAddressToCollectionIfMissing<IAddress>(this.addressesCollection, invoice.address);
    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(
      this.organizationsSharedCollection,
      invoice.orgInvoices
    );
    this.clientsSharedCollection = this.clientsService.addClientsToCollectionIfMissing<IClients>(
      this.clientsSharedCollection,
      invoice.clients
    );
  }

  protected loadRelationshipsOptions(): void {
    this.transporterService
      .query({ filter: 'invoice-is-null' })
      .pipe(map((res: HttpResponse<ITransporter[]>) => res.body ?? []))
      .pipe(
        map((transporters: ITransporter[]) =>
          this.transporterService.addTransporterToCollectionIfMissing<ITransporter>(transporters, this.invoice?.transporter)
        )
      )
      .subscribe((transporters: ITransporter[]) => (this.transportersCollection = transporters));

    this.addressService
      .query({ filter: 'invoice-is-null' })
      .pipe(map((res: HttpResponse<IAddress[]>) => res.body ?? []))
      .pipe(map((addresses: IAddress[]) => this.addressService.addAddressToCollectionIfMissing<IAddress>(addresses, this.invoice?.address)))
      .subscribe((addresses: IAddress[]) => (this.addressesCollection = addresses));

    this.organizationService
      .query()
      .pipe(map((res: HttpResponse<IOrganization[]>) => res.body ?? []))
      .pipe(
        map((organizations: IOrganization[]) =>
          this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(organizations, this.invoice?.orgInvoices)
        )
      )
      .subscribe((organizations: IOrganization[]) => (this.organizationsSharedCollection = organizations));

    this.clientsService
      .query()
      .pipe(map((res: HttpResponse<IClients[]>) => res.body ?? []))
      .pipe(map((clients: IClients[]) => this.clientsService.addClientsToCollectionIfMissing<IClients>(clients, this.invoice?.clients)))
      .subscribe((clients: IClients[]) => (this.clientsSharedCollection = clients));
  }
}
