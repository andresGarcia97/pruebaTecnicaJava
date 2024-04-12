import dayjs from 'dayjs/esm';

import { IClient, NewClient } from './client.model';

export const sampleWithRequiredData: IClient = {
  id: 3749,
  identificationType: 'CEDULA_EXTRANJERIA',
  identification: 'tremendous disconnection',
  bornDate: dayjs('2024-04-11'),
  creationDate: dayjs('2024-04-11T07:08'),
};

export const sampleWithPartialData: IClient = {
  id: 18102,
  identificationType: 'PASAPORTE',
  identification: 'ick',
  email: 'Krystal_Heidenreich23@hotmail.com',
  bornDate: dayjs('2024-04-11'),
  creationDate: dayjs('2024-04-11T11:34'),
  lastModificationDate: dayjs('2024-04-11T00:27'),
};

export const sampleWithFullData: IClient = {
  id: 331,
  identificationType: 'CEDULA_EXTRANJERIA',
  identification: 'physically baseline',
  name: 'furthermore tenderize obvious',
  lastName: 'Lockman',
  email: 'Dandre_Turcotte97@yahoo.com',
  bornDate: dayjs('2024-04-11'),
  creationDate: dayjs('2024-04-11T07:26'),
  lastModificationDate: dayjs('2024-04-11T12:56'),
};

export const sampleWithNewData: NewClient = {
  identificationType: 'CEDULA',
  identification: 'loyally ack',
  bornDate: dayjs('2024-04-11'),
  creationDate: dayjs('2024-04-11T13:42'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
