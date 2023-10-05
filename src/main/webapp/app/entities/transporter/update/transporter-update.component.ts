import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { TransporterFormService, TransporterFormGroup } from './transporter-form.service';
import { ITransporter } from '../transporter.model';
import { TransporterService } from '../service/transporter.service';
import { IAddress } from 'app/entities/address/address.model';
import { AddressService } from 'app/entities/address/service/address.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  standalone: true,
  selector: 'jhi-transporter-update',
  templateUrl: './transporter-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TransporterUpdateComponent implements OnInit {
  isSaving = false;
  transporter: ITransporter | null = null;
  statusValues = Object.keys(Status);

  addressesCollection: IAddress[] = [];

  editForm: TransporterFormGroup = this.transporterFormService.createTransporterFormGroup();

  constructor(
    protected transporterService: TransporterService,
    protected transporterFormService: TransporterFormService,
    protected addressService: AddressService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareAddress = (o1: IAddress | null, o2: IAddress | null): boolean => this.addressService.compareAddress(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transporter }) => {
      this.transporter = transporter;
      if (transporter) {
        this.updateForm(transporter);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transporter = this.transporterFormService.getTransporter(this.editForm);
    if (transporter.id !== null) {
      this.subscribeToSaveResponse(this.transporterService.update(transporter));
    } else {
      this.subscribeToSaveResponse(this.transporterService.create(transporter));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransporter>>): void {
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

  protected updateForm(transporter: ITransporter): void {
    this.transporter = transporter;
    this.transporterFormService.resetForm(this.editForm, transporter);

    this.addressesCollection = this.addressService.addAddressToCollectionIfMissing<IAddress>(this.addressesCollection, transporter.address);
  }

  protected loadRelationshipsOptions(): void {
    this.addressService
      .query({ filter: 'transporter-is-null' })
      .pipe(map((res: HttpResponse<IAddress[]>) => res.body ?? []))
      .pipe(
        map((addresses: IAddress[]) => this.addressService.addAddressToCollectionIfMissing<IAddress>(addresses, this.transporter?.address))
      )
      .subscribe((addresses: IAddress[]) => (this.addressesCollection = addresses));
  }
}
