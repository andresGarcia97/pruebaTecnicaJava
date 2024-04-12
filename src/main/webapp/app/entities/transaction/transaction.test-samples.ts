import dayjs from 'dayjs/esm';

import { ITransaction, NewTransaction } from './transaction.model';

export const sampleWithRequiredData: ITransaction = {
  id: '9bcd4777-1e1f-481a-80d3-075905a1edc3',
  transactionType: 'TRANSFERENCIA',
  transactionDate: dayjs('2024-04-11T16:24'),
  amount: 608.3,
};

export const sampleWithPartialData: ITransaction = {
  id: '4a3d191a-6d83-402e-95dd-713b5bac2e42',
  transactionType: 'TRANSFERENCIA',
  transactionDate: dayjs('2024-04-11T01:36'),
  amount: 15185.79,
};

export const sampleWithFullData: ITransaction = {
  id: '4da480e2-1188-4581-8bde-1a288715960b',
  transactionType: 'RETIRO',
  transactionDate: dayjs('2024-04-11T18:33'),
  amount: 29074.01,
};

export const sampleWithNewData: NewTransaction = {
  transactionType: 'TRANSFERENCIA',
  transactionDate: dayjs('2024-04-11T19:03'),
  amount: 24547.45,
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
