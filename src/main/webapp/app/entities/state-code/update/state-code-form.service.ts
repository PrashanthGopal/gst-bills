import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IStateCode, NewStateCode } from '../state-code.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IStateCode for edit and NewStateCodeFormGroupInput for create.
 */
type StateCodeFormGroupInput = IStateCode | PartialWithRequiredKeyOf<NewStateCode>;

type StateCodeFormDefaults = Pick<NewStateCode, 'id'>;

type StateCodeFormGroupContent = {
  id: FormControl<IStateCode['id'] | NewStateCode['id']>;
  stateCodeId: FormControl<IStateCode['stateCodeId']>;
  code: FormControl<IStateCode['code']>;
  name: FormControl<IStateCode['name']>;
};

export type StateCodeFormGroup = FormGroup<StateCodeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class StateCodeFormService {
  createStateCodeFormGroup(stateCode: StateCodeFormGroupInput = { id: null }): StateCodeFormGroup {
    const stateCodeRawValue = {
      ...this.getFormDefaults(),
      ...stateCode,
    };
    return new FormGroup<StateCodeFormGroupContent>({
      id: new FormControl(
        { value: stateCodeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      stateCodeId: new FormControl(stateCodeRawValue.stateCodeId),
      code: new FormControl(stateCodeRawValue.code),
      name: new FormControl(stateCodeRawValue.name),
    });
  }

  getStateCode(form: StateCodeFormGroup): IStateCode | NewStateCode {
    return form.getRawValue() as IStateCode | NewStateCode;
  }

  resetForm(form: StateCodeFormGroup, stateCode: StateCodeFormGroupInput): void {
    const stateCodeRawValue = { ...this.getFormDefaults(), ...stateCode };
    form.reset(
      {
        ...stateCodeRawValue,
        id: { value: stateCodeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): StateCodeFormDefaults {
    return {
      id: null,
    };
  }
}
