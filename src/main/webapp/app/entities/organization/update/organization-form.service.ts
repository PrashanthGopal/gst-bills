import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IOrganization, NewOrganization } from '../organization.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrganization for edit and NewOrganizationFormGroupInput for create.
 */
type OrganizationFormGroupInput = IOrganization | PartialWithRequiredKeyOf<NewOrganization>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IOrganization | NewOrganization> = Omit<T, 'dateOfRegestation' | 'dateOfDeRegestation'> & {
  dateOfRegestation?: string | null;
  dateOfDeRegestation?: string | null;
};

type OrganizationFormRawValue = FormValueOf<IOrganization>;

type NewOrganizationFormRawValue = FormValueOf<NewOrganization>;

type OrganizationFormDefaults = Pick<NewOrganization, 'id' | 'dateOfRegestation' | 'dateOfDeRegestation'>;

type OrganizationFormGroupContent = {
  id: FormControl<OrganizationFormRawValue['id'] | NewOrganization['id']>;
  orgId: FormControl<OrganizationFormRawValue['orgId']>;
  orgName: FormControl<OrganizationFormRawValue['orgName']>;
  legalOrgName: FormControl<OrganizationFormRawValue['legalOrgName']>;
  pan: FormControl<OrganizationFormRawValue['pan']>;
  gstin: FormControl<OrganizationFormRawValue['gstin']>;
  phoneNumber: FormControl<OrganizationFormRawValue['phoneNumber']>;
  emailId: FormControl<OrganizationFormRawValue['emailId']>;
  status: FormControl<OrganizationFormRawValue['status']>;
  dateOfRegestation: FormControl<OrganizationFormRawValue['dateOfRegestation']>;
  dateOfDeRegestation: FormControl<OrganizationFormRawValue['dateOfDeRegestation']>;
  logo: FormControl<OrganizationFormRawValue['logo']>;
  logoContentType: FormControl<OrganizationFormRawValue['logoContentType']>;
  address: FormControl<OrganizationFormRawValue['address']>;
};

export type OrganizationFormGroup = FormGroup<OrganizationFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrganizationFormService {
  createOrganizationFormGroup(organization: OrganizationFormGroupInput = { id: null }): OrganizationFormGroup {
    const organizationRawValue = this.convertOrganizationToOrganizationRawValue({
      ...this.getFormDefaults(),
      ...organization,
    });
    return new FormGroup<OrganizationFormGroupContent>({
      id: new FormControl(
        { value: organizationRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      orgId: new FormControl(organizationRawValue.orgId),
      orgName: new FormControl(organizationRawValue.orgName),
      legalOrgName: new FormControl(organizationRawValue.legalOrgName),
      pan: new FormControl(organizationRawValue.pan),
      gstin: new FormControl(organizationRawValue.gstin),
      phoneNumber: new FormControl(organizationRawValue.phoneNumber),
      emailId: new FormControl(organizationRawValue.emailId),
      status: new FormControl(organizationRawValue.status),
      dateOfRegestation: new FormControl(organizationRawValue.dateOfRegestation),
      dateOfDeRegestation: new FormControl(organizationRawValue.dateOfDeRegestation),
      logo: new FormControl(organizationRawValue.logo),
      logoContentType: new FormControl(organizationRawValue.logoContentType),
      address: new FormControl(organizationRawValue.address),
    });
  }

  getOrganization(form: OrganizationFormGroup): IOrganization | NewOrganization {
    return this.convertOrganizationRawValueToOrganization(form.getRawValue() as OrganizationFormRawValue | NewOrganizationFormRawValue);
  }

  resetForm(form: OrganizationFormGroup, organization: OrganizationFormGroupInput): void {
    const organizationRawValue = this.convertOrganizationToOrganizationRawValue({ ...this.getFormDefaults(), ...organization });
    form.reset(
      {
        ...organizationRawValue,
        id: { value: organizationRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OrganizationFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      dateOfRegestation: currentTime,
      dateOfDeRegestation: currentTime,
    };
  }

  private convertOrganizationRawValueToOrganization(
    rawOrganization: OrganizationFormRawValue | NewOrganizationFormRawValue
  ): IOrganization | NewOrganization {
    return {
      ...rawOrganization,
      dateOfRegestation: dayjs(rawOrganization.dateOfRegestation, DATE_TIME_FORMAT),
      dateOfDeRegestation: dayjs(rawOrganization.dateOfDeRegestation, DATE_TIME_FORMAT),
    };
  }

  private convertOrganizationToOrganizationRawValue(
    organization: IOrganization | (Partial<NewOrganization> & OrganizationFormDefaults)
  ): OrganizationFormRawValue | PartialWithRequiredKeyOf<NewOrganizationFormRawValue> {
    return {
      ...organization,
      dateOfRegestation: organization.dateOfRegestation ? organization.dateOfRegestation.format(DATE_TIME_FORMAT) : undefined,
      dateOfDeRegestation: organization.dateOfDeRegestation ? organization.dateOfDeRegestation.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
