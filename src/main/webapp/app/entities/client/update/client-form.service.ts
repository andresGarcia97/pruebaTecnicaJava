import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IClient, NewClient } from '../client.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IClient for edit and NewClientFormGroupInput for create.
 */
type ClientFormGroupInput = IClient | PartialWithRequiredKeyOf<NewClient>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IClient | NewClient> = Omit<T, 'creationDate' | 'lastModificationDate'> & {
  creationDate?: string | null;
  lastModificationDate?: string | null;
};

type ClientFormRawValue = FormValueOf<IClient>;

type NewClientFormRawValue = FormValueOf<NewClient>;

type ClientFormDefaults = Pick<NewClient, 'id' | 'creationDate' | 'lastModificationDate'>;

type ClientFormGroupContent = {
  id: FormControl<ClientFormRawValue['id'] | NewClient['id']>;
  identificationType: FormControl<ClientFormRawValue['identificationType']>;
  identification: FormControl<ClientFormRawValue['identification']>;
  name: FormControl<ClientFormRawValue['name']>;
  lastName: FormControl<ClientFormRawValue['lastName']>;
  email: FormControl<ClientFormRawValue['email']>;
  bornDate: FormControl<ClientFormRawValue['bornDate']>;
  creationDate: FormControl<ClientFormRawValue['creationDate']>;
  lastModificationDate: FormControl<ClientFormRawValue['lastModificationDate']>;
};

export type ClientFormGroup = FormGroup<ClientFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ClientFormService {
  createClientFormGroup(client: ClientFormGroupInput = { id: null }): ClientFormGroup {
    const clientRawValue = this.convertClientToClientRawValue({
      ...this.getFormDefaults(),
      ...client,
    });
    return new FormGroup<ClientFormGroupContent>({
      id: new FormControl(
        { value: clientRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        },
      ),
      identificationType: new FormControl(clientRawValue.identificationType, {
        validators: [Validators.required],
      }),
      identification: new FormControl(clientRawValue.identification, {
        validators: [Validators.required],
      }),
      name: new FormControl(clientRawValue.name),
      lastName: new FormControl(clientRawValue.lastName),
      email: new FormControl(clientRawValue.email),
      bornDate: new FormControl(clientRawValue.bornDate, {
        validators: [Validators.required],
      }),
      creationDate: new FormControl(clientRawValue.creationDate, {
        validators: [Validators.required],
      }),
      lastModificationDate: new FormControl(clientRawValue.lastModificationDate),
    });
  }

  getClient(form: ClientFormGroup): IClient | NewClient {
    return this.convertClientRawValueToClient(form.getRawValue() as ClientFormRawValue | NewClientFormRawValue);
  }

  resetForm(form: ClientFormGroup, client: ClientFormGroupInput): void {
    const clientRawValue = this.convertClientToClientRawValue({ ...this.getFormDefaults(), ...client });
    form.reset(
      {
        ...clientRawValue,
        id: { value: clientRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */,
    );
  }

  private getFormDefaults(): ClientFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      creationDate: currentTime,
      lastModificationDate: currentTime,
    };
  }

  private convertClientRawValueToClient(rawClient: ClientFormRawValue | NewClientFormRawValue): IClient | NewClient {
    return {
      ...rawClient,
      creationDate: dayjs(rawClient.creationDate, DATE_TIME_FORMAT),
      lastModificationDate: dayjs(rawClient.lastModificationDate, DATE_TIME_FORMAT),
    };
  }

  private convertClientToClientRawValue(
    client: IClient | (Partial<NewClient> & ClientFormDefaults),
  ): ClientFormRawValue | PartialWithRequiredKeyOf<NewClientFormRawValue> {
    return {
      ...client,
      creationDate: client.creationDate ? client.creationDate.format(DATE_TIME_FORMAT) : undefined,
      lastModificationDate: client.lastModificationDate ? client.lastModificationDate.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
