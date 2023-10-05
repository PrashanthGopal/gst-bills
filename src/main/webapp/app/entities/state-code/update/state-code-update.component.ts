import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { StateCodeFormService, StateCodeFormGroup } from './state-code-form.service';
import { IStateCode } from '../state-code.model';
import { StateCodeService } from '../service/state-code.service';

@Component({
  standalone: true,
  selector: 'jhi-state-code-update',
  templateUrl: './state-code-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class StateCodeUpdateComponent implements OnInit {
  isSaving = false;
  stateCode: IStateCode | null = null;

  editForm: StateCodeFormGroup = this.stateCodeFormService.createStateCodeFormGroup();

  constructor(
    protected stateCodeService: StateCodeService,
    protected stateCodeFormService: StateCodeFormService,
    protected activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ stateCode }) => {
      this.stateCode = stateCode;
      if (stateCode) {
        this.updateForm(stateCode);
      }
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const stateCode = this.stateCodeFormService.getStateCode(this.editForm);
    if (stateCode.id !== null) {
      this.subscribeToSaveResponse(this.stateCodeService.update(stateCode));
    } else {
      this.subscribeToSaveResponse(this.stateCodeService.create(stateCode));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IStateCode>>): void {
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

  protected updateForm(stateCode: IStateCode): void {
    this.stateCode = stateCode;
    this.stateCodeFormService.resetForm(this.editForm, stateCode);
  }
}
