import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrgUserRole, NewOrgUserRole } from '../org-user-role.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrgUserRole for edit and NewOrgUserRoleFormGroupInput for create.
 */
type OrgUserRoleFormGroupInput = IOrgUserRole | PartialWithRequiredKeyOf<NewOrgUserRole>;

type OrgUserRoleFormDefaults = Pick<NewOrgUserRole, 'id'>;

type OrgUserRoleFormGroupContent = {
  id: FormControl<IOrgUserRole['id'] | NewOrgUserRole['id']>;
  roleId: FormControl<IOrgUserRole['roleId']>;
  orgId: FormControl<IOrgUserRole['orgId']>;
  name: FormControl<IOrgUserRole['name']>;
  userRole: FormControl<IOrgUserRole['userRole']>;
};

export type OrgUserRoleFormGroup = FormGroup<OrgUserRoleFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrgUserRoleFormService {
  createOrgUserRoleFormGroup(orgUserRole: OrgUserRoleFormGroupInput = { id: null }): OrgUserRoleFormGroup {
    const orgUserRoleRawValue = {
      ...this.getFormDefaults(),
      ...orgUserRole,
    };
    return new FormGroup<OrgUserRoleFormGroupContent>({
      id: new FormControl(
        { value: orgUserRoleRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      roleId: new FormControl(orgUserRoleRawValue.roleId),
      orgId: new FormControl(orgUserRoleRawValue.orgId),
      name: new FormControl(orgUserRoleRawValue.name),
      userRole: new FormControl(orgUserRoleRawValue.userRole),
    });
  }

  getOrgUserRole(form: OrgUserRoleFormGroup): IOrgUserRole | NewOrgUserRole {
    return form.getRawValue() as IOrgUserRole | NewOrgUserRole;
  }

  resetForm(form: OrgUserRoleFormGroup, orgUserRole: OrgUserRoleFormGroupInput): void {
    const orgUserRoleRawValue = { ...this.getFormDefaults(), ...orgUserRole };
    form.reset(
      {
        ...orgUserRoleRawValue,
        id: { value: orgUserRoleRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OrgUserRoleFormDefaults {
    return {
      id: null,
    };
  }
}
