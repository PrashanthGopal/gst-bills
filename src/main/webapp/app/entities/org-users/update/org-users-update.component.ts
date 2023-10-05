import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { OrgUsersFormService, OrgUsersFormGroup } from './org-users-form.service';
import { IOrgUsers } from '../org-users.model';
import { OrgUsersService } from '../service/org-users.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';
import { Status } from 'app/entities/enumerations/status.model';

@Component({
  standalone: true,
  selector: 'jhi-org-users-update',
  templateUrl: './org-users-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrgUsersUpdateComponent implements OnInit {
  isSaving = false;
  orgUsers: IOrgUsers | null = null;
  statusValues = Object.keys(Status);

  organizationsSharedCollection: IOrganization[] = [];

  editForm: OrgUsersFormGroup = this.orgUsersFormService.createOrgUsersFormGroup();

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected orgUsersService: OrgUsersService,
    protected orgUsersFormService: OrgUsersFormService,
    protected organizationService: OrganizationService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareOrganization = (o1: IOrganization | null, o2: IOrganization | null): boolean =>
    this.organizationService.compareOrganization(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orgUsers }) => {
      this.orgUsers = orgUsers;
      if (orgUsers) {
        this.updateForm(orgUsers);
      }

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(new EventWithContent<AlertError>('gstBillsApp.error', { ...err, key: 'error.file.' + err.key })),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orgUsers = this.orgUsersFormService.getOrgUsers(this.editForm);
    if (orgUsers.id !== null) {
      this.subscribeToSaveResponse(this.orgUsersService.update(orgUsers));
    } else {
      this.subscribeToSaveResponse(this.orgUsersService.create(orgUsers));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrgUsers>>): void {
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

  protected updateForm(orgUsers: IOrgUsers): void {
    this.orgUsers = orgUsers;
    this.orgUsersFormService.resetForm(this.editForm, orgUsers);

    this.organizationsSharedCollection = this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(
      this.organizationsSharedCollection,
      orgUsers.orgUsers
    );
  }

  protected loadRelationshipsOptions(): void {
    this.organizationService
      .query()
      .pipe(map((res: HttpResponse<IOrganization[]>) => res.body ?? []))
      .pipe(
        map((organizations: IOrganization[]) =>
          this.organizationService.addOrganizationToCollectionIfMissing<IOrganization>(organizations, this.orgUsers?.orgUsers)
        )
      )
      .subscribe((organizations: IOrganization[]) => (this.organizationsSharedCollection = organizations));
  }
}
