import dayjs from 'dayjs/esm';
import { IClient } from 'app/entities/client/client.model';
import { AccountType } from 'app/entities/enumerations/account-type.model';
import { AccountState } from 'app/entities/enumerations/account-state.model';

export interface IAccountBank {
  id: number;
  accountType?: keyof typeof AccountType | null;
  number?: number | null;
  state?: keyof typeof AccountState | null;
  balance?: number | null;
  exentGMF?: boolean | null;
  creationDate?: dayjs.Dayjs | null;
  lastModificationDate?: dayjs.Dayjs | null;
  account?: Pick<IClient, 'id'> | null;
}

export type NewAccountBank = Omit<IAccountBank, 'id'> & { id: null };
