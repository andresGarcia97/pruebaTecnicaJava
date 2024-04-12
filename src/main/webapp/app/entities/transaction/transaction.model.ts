import dayjs from 'dayjs/esm';
import { IAccountBank } from 'app/entities/account-bank/account-bank.model';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';

export interface ITransaction {
  id: string;
  transactionType?: keyof typeof TransactionType | null;
  transactionDate?: dayjs.Dayjs | null;
  amount?: number | null;
  origin?: Pick<IAccountBank, 'id'> | null;
}

export type NewTransaction = Omit<ITransaction, 'id'> & { id: null };
