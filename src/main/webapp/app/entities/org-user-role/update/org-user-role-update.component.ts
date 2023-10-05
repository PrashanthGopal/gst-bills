import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { OrgUserRoleFormService, OrgUserRoleFormGroup } from './org-user-role-form.service';
import { IOrgUserRole } from '../org-user-role.model';
import { OrgUserRoleService } from '../service/org-user-role.service';
import { IOrgUsers } from 'app/entities/org-users/org-users.model';
import { OrgUsersService } from 'app/entities/org-users/service/org-users.service';

@Component({
  standalone: true,
  selector: 'jhi-org-user-role-update',
  templateUrl: './org-user-role-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class OrgUserRoleUpdateComponent implements OnInit {
  isSaving = false;
  orgUserRole: IOrgUserRole | null = null;

  orgUsersSharedCollection: IOrgUsers[] = [];

  editForm: OrgUserRoleFormGroup = this.orgUserRoleFormService.createOrgUserRoleFormGroup();

  constructor(
    protected orgUserRoleService: OrgUserRoleService,
    protected orgUserRoleFormService: OrgUserRoleFormService,
    protected orgUsersService: OrgUsersService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareOrgUsers = (o1: IOrgUsers | null, o2: IOrgUsers | null): boolean => this.orgUsersService.compareOrgUsers(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ orgUserRole }) => {
      this.orgUserRole = orgUserRole;
      if (orgUserRole) {
        this.updateForm(orgUserRole);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const orgUserRole = this.orgUserRoleFormService.getOrgUserRole(this.editForm);
    if (orgUserRole.id !== null) {
      this.subscribeToSaveResponse(this.orgUserRoleService.update(orgUserRole));
    } else {
      this.subscribeToSaveResponse(this.orgUserRoleService.create(orgUserRole));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrgUserRole>>): void {
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

  protected updateForm(orgUserRole: IOrgUserRole): void {
    this.orgUserRole = orgUserRole;
    this.orgUserRoleFormService.resetForm(this.editForm, orgUserRole);

    this.orgUsersSharedCollection = this.orgUsersService.addOrgUsersToCollectionIfMissing<IOrgUsers>(
      this.orgUsersSharedCollection,
      orgUserRole.userRole
    );
  }

  protected loadRelationshipsOptions(): void {
    this.orgUsersService
      .query()
      .pipe(map((res: HttpResponse<IOrgUsers[]>) => res.body ?? []))
      .pipe(
        map((orgUsers: IOrgUsers[]) =>
          this.orgUsersService.addOrgUsersToCollectionIfMissing<IOrgUsers>(orgUsers, this.orgUserRole?.userRole)
        )
      )
      .subscribe((orgUsers: IOrgUsers[]) => (this.orgUsersSharedCollection = orgUsers));
  }
}
