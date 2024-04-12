import dayjs from 'dayjs/esm';

import { IAccountBank, NewAccountBank } from './account-bank.model';

export const sampleWithRequiredData: IAccountBank = {
  id: 21384,
  accountType: 'CUENTA_AHORROS',
  number: 25316,
  state: 'INACTIVA',
  balance: 27774.22,
  exentGMF: false,
  creationDate: dayjs('2024-04-10T23:37'),
};

export const sampleWithPartialData: IAccountBank = {
  id: 9543,
  accountType: 'CUENTA_CORRIENTE',
  number: 28047,
  state: 'INACTIVA',
  balance: 29700.65,
  exentGMF: false,
  creationDate: dayjs('2024-04-11T07:36'),
  lastModificationDate: dayjs('2024-04-11T16:50'),
};

export const sampleWithFullData: IAccountBank = {
  id: 31661,
  accountType: 'CUENTA_CORRIENTE',
  number: 18033,
  state: 'INACTIVA',
  balance: 19263.26,
  exentGMF: true,
  creationDate: dayjs('2024-04-11T06:51'),
  lastModificationDate: dayjs('2024-04-11T04:04'),
};

export const sampleWithNewData: NewAccountBank = {
  accountType: 'CUENTA_CORRIENTE',
  number: 31047,
  state: 'CANCELADA',
  balance: 13314.76,
  exentGMF: false,
  creationDate: dayjs('2024-04-11T03:35'),
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
