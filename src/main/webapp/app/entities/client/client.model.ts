import dayjs from 'dayjs/esm';
import { IdentificationType } from 'app/entities/enumerations/identification-type.model';

export interface IClient {
  id: number;
  identificationType?: keyof typeof IdentificationType | null;
  identification?: string | null;
  name?: string | null;
  lastName?: string | null;
  email?: string | null;
  bornDate?: dayjs.Dayjs | null;
  creationDate?: dayjs.Dayjs | null;
  lastModificationDate?: dayjs.Dayjs | null;
}

export type NewClient = Omit<IClient, 'id'> & { id: null };
