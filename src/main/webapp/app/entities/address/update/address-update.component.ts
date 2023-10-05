import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AddressFormService, AddressFormGroup } from './address-form.service';
import { IAddress } from '../address.model';
import { AddressService } from '../service/address.service';
import { IStateCode } from 'app/entities/state-code/state-code.model';
import { StateCodeService } from 'app/entities/state-code/service/state-code.service';

@Component({
  standalone: true,
  selector: 'jhi-address-update',
  templateUrl: './address-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AddressUpdateComponent implements OnInit {
  isSaving = false;
  address: IAddress | null = null;

  stateCodesCollection: IStateCode[] = [];

  editForm: AddressFormGroup = this.addressFormService.createAddressFormGroup();

  constructor(
    protected addressService: AddressService,
    protected addressFormService: AddressFormService,
    protected stateCodeService: StateCodeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareStateCode = (o1: IStateCode | null, o2: IStateCode | null): boolean => this.stateCodeService.compareStateCode(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ address }) => {
      this.address = address;
      if (address) {
        this.updateForm(address);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const address = this.addressFormService.getAddress(this.editForm);
    if (address.id !== null) {
      this.subscribeToSaveResponse(this.addressService.update(address));
    } else {
      this.subscribeToSaveResponse(this.addressService.create(address));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAddress>>): void {
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

  protected updateForm(address: IAddress): void {
    this.address = address;
    this.addressFormService.resetForm(this.editForm, address);

    this.stateCodesCollection = this.stateCodeService.addStateCodeToCollectionIfMissing<IStateCode>(
      this.stateCodesCollection,
      address.stateCode
    );
  }

  protected loadRelationshipsOptions(): void {
    this.stateCodeService
      .query({ filter: 'address-is-null' })
      .pipe(map((res: HttpResponse<IStateCode[]>) => res.body ?? []))
      .pipe(
        map((stateCodes: IStateCode[]) =>
          this.stateCodeService.addStateCodeToCollectionIfMissing<IStateCode>(stateCodes, this.address?.stateCode)
        )
      )
      .subscribe((stateCodes: IStateCode[]) => (this.stateCodesCollection = stateCodes));
  }
}
