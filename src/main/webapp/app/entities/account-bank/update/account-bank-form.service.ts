import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IAccountBank, NewAccountBank } from '../account-bank.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IAccountBank for edit and NewAccountBankFormGroupInput for create.
 */
type AccountBankFormGroupInput = IAccountBank | PartialWithRequiredKeyOf<NewAccountBank>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IAccountBank | NewAccountBank> = Omit<T, 'creationDate' | 'lastModificationDate'> & {
  creationDate?: string | null;
  lastModificationDate?: string | null;
};

type AccountBankFormRawValue = FormValueOf<IAccountBank>;

type NewAccountBankFormRawValue = FormValueOf<NewAccountBank>;

type AccountBankFormDefaults = Pick<NewAccountBank, 'id' | 'exentGMF' | 'creationDate' | 'lastModificationDate'>;

type AccountBankFormGroupContent = {
  id: FormControl<AccountBankFormRawValue['id'] | NewAccountBank['id']>;
  accountType: FormControl<AccountBankFormRawValue['accountType']>;
  number: FormControl<AccountBankFormRawValue['number']>;
  state: FormControl<AccountBankFormRawValue['state']>;
  balance: FormControl<AccountBankFormRawValue['balance']>;
  exentGMF: FormControl<AccountBankFormRawValue['exentGMF']>;
  creationDate: FormControl<AccountBankFormRawValue['creationDate']>;
  lastModificationDate: FormControl<AccountBankFormRawValue['lastModificationDate']>;
  account: FormControl<AccountBankFormRawValue['account']>;
};

export type AccountBankFormGroup = FormGroup<AccountBankFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class AccountBankFormService {
  createAccountBankFormGroup(accountBank: AccountBankFormGroupInput = { id: null }): AccountBankFormGroup {
    const accountBankRawValue = this.convertAccountBankToAccountBankRawValue({
      ...this.getFormDefaults(),
      ...accountBank,
    });
    return new FormGroup<AccountBankFormGroupContent>({
      id: new FormControl(
        { value: accountBankRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      accountType: new FormControl(accountBankRawValue.accountType, {
        validators: [Validators.required],
      }),
      number: new FormControl(accountBankRawValue.number, {
        validators: [Validators.required],
      }),
      state: new FormControl(accountBankRawValue.state, {
        validators: [Validators.required],
      }),
      balance: new FormControl(accountBankRawValue.balance, {
        validators: [Validators.required, Validators.min(0)],
      }),
      exentGMF: new FormControl(accountBankRawValue.exentGMF, {
        validators: [Validators.required],
      }),
      creationDate: new FormControl(accountBankRawValue.creationDate, {
        validators: [Validators.required],
      }),
      lastModificationDate: new FormControl(accountBankRawValue.lastModificationDate),
      account: new FormControl(accountBankRawValue.account, {
        validators: [Validators.required],
      }),
    });
  }

  getAccountBank(form: AccountBankFormGroup): IAccountBank | NewAccountBank {
    return this.convertAccountBankRawValueToAccountBank(form.getRawValue() as AccountBankFormRawValue | NewAccountBankFormRawValue);
  }

  resetForm(form: AccountBankFormGroup, accountBank: AccountBankFormGroupInput): void {
    const accountBankRawValue = this.convertAccountBankToAccountBankRawValue({ ...this.getFormDefaults(), ...accountBank });
    form.reset(
      {
        ...accountBankRawValue,
        id: { value: accountBankRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): AccountBankFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      exentGMF: false,
      creationDate: currentTime,
      lastModificationDate: currentTime,
    };
  }

  private convertAccountBankRawValueToAccountBank(
    rawAccountBank: AccountBankFormRawValue | NewAccountBankFormRawValue,
  ): IAccountBank | NewAccountBank {
    return {
      ...rawAccountBank,
      creationDate: dayjs(rawAccountBank.creationDate, DATE_TIME_FORMAT),
      lastModificationDate: dayjs(rawAccountBank.lastModificationDate, DATE_TIME_FORMAT),
    };
  }

  private convertAccountBankToAccountBankRawValue(
    accountBank: IAccountBank | (Partial<NewAccountBank> & AccountBankFormDefaults),
  ): AccountBankFormRawValue | PartialWithRequiredKeyOf<NewAccountBankFormRawValue> {
    return {
      ...accountBank,
      creationDate: accountBank.creationDate ? accountBank.creationDate.format(DATE_TIME_FORMAT) : undefined,
      lastModificationDate: accountBank.lastModificationDate ? accountBank.lastModificationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
