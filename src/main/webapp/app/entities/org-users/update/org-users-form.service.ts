import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOrgUsers, NewOrgUsers } from '../org-users.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrgUsers for edit and NewOrgUsersFormGroupInput for create.
 */
type OrgUsersFormGroupInput = IOrgUsers | PartialWithRequiredKeyOf<NewOrgUsers>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOrgUsers | NewOrgUsers> = Omit<T, 'createDate' | 'updateDate'> & {
  createDate?: string | null;
  updateDate?: string | null;
};

type OrgUsersFormRawValue = FormValueOf<IOrgUsers>;

type NewOrgUsersFormRawValue = FormValueOf<NewOrgUsers>;

type OrgUsersFormDefaults = Pick<NewOrgUsers, 'id' | 'createDate' | 'updateDate'>;

type OrgUsersFormGroupContent = {
  id: FormControl<OrgUsersFormRawValue['id'] | NewOrgUsers['id']>;
  userId: FormControl<OrgUsersFormRawValue['userId']>;
  name: FormControl<OrgUsersFormRawValue['name']>;
  userName: FormControl<OrgUsersFormRawValue['userName']>;
  password: FormControl<OrgUsersFormRawValue['password']>;
  status: FormControl<OrgUsersFormRawValue['status']>;
  createDate: FormControl<OrgUsersFormRawValue['createDate']>;
  updateDate: FormControl<OrgUsersFormRawValue['updateDate']>;
  profilePhoto: FormControl<OrgUsersFormRawValue['profilePhoto']>;
  profilePhotoContentType: FormControl<OrgUsersFormRawValue['profilePhotoContentType']>;
  emailId: FormControl<OrgUsersFormRawValue['emailId']>;
  phoneNumber: FormControl<OrgUsersFormRawValue['phoneNumber']>;
  orgUsers: FormControl<OrgUsersFormRawValue['orgUsers']>;
};

export type OrgUsersFormGroup = FormGroup<OrgUsersFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrgUsersFormService {
  createOrgUsersFormGroup(orgUsers: OrgUsersFormGroupInput = { id: null }): OrgUsersFormGroup {
    const orgUsersRawValue = this.convertOrgUsersToOrgUsersRawValue({
      ...this.getFormDefaults(),
      ...orgUsers,
    });
    return new FormGroup<OrgUsersFormGroupContent>({
      id: new FormControl(
        { value: orgUsersRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      userId: new FormControl(orgUsersRawValue.userId),
      name: new FormControl(orgUsersRawValue.name),
      userName: new FormControl(orgUsersRawValue.userName),
      password: new FormControl(orgUsersRawValue.password),
      status: new FormControl(orgUsersRawValue.status),
      createDate: new FormControl(orgUsersRawValue.createDate),
      updateDate: new FormControl(orgUsersRawValue.updateDate),
      profilePhoto: new FormControl(orgUsersRawValue.profilePhoto),
      profilePhotoContentType: new FormControl(orgUsersRawValue.profilePhotoContentType),
      emailId: new FormControl(orgUsersRawValue.emailId),
      phoneNumber: new FormControl(orgUsersRawValue.phoneNumber),
      orgUsers: new FormControl(orgUsersRawValue.orgUsers),
    });
  }

  getOrgUsers(form: OrgUsersFormGroup): IOrgUsers | NewOrgUsers {
    return this.convertOrgUsersRawValueToOrgUsers(form.getRawValue() as OrgUsersFormRawValue | NewOrgUsersFormRawValue);
  }

  resetForm(form: OrgUsersFormGroup, orgUsers: OrgUsersFormGroupInput): void {
    const orgUsersRawValue = this.convertOrgUsersToOrgUsersRawValue({ ...this.getFormDefaults(), ...orgUsers });
    form.reset(
      {
        ...orgUsersRawValue,
        id: { value: orgUsersRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OrgUsersFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createDate: currentTime,
      updateDate: currentTime,
    };
  }

  private convertOrgUsersRawValueToOrgUsers(rawOrgUsers: OrgUsersFormRawValue | NewOrgUsersFormRawValue): IOrgUsers | NewOrgUsers {
    return {
      ...rawOrgUsers,
      createDate: dayjs(rawOrgUsers.createDate, DATE_TIME_FORMAT),
      updateDate: dayjs(rawOrgUsers.updateDate, DATE_TIME_FORMAT),
    };
  }

  private convertOrgUsersToOrgUsersRawValue(
    orgUsers: IOrgUsers | (Partial<NewOrgUsers> & OrgUsersFormDefaults)
  ): OrgUsersFormRawValue | PartialWithRequiredKeyOf<NewOrgUsersFormRawValue> {
    return {
      ...orgUsers,
      createDate: orgUsers.createDate ? orgUsers.createDate.format(DATE_TIME_FORMAT) : undefined,
      updateDate: orgUsers.updateDate ? orgUsers.updateDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
