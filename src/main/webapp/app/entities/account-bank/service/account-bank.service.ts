import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IAccountBank, NewAccountBank } from '../account-bank.model';

export type PartialUpdateAccountBank = Partial<IAccountBank> & Pick<IAccountBank, 'id'>;

type RestOf<T extends IAccountBank | NewAccountBank> = Omit<T, 'creationDate' | 'lastModificationDate'> & {
  creationDate?: string | null;
  lastModificationDate?: string | null;
};

export type RestAccountBank = RestOf<IAccountBank>;

export type NewRestAccountBank = RestOf<NewAccountBank>;

export type PartialUpdateRestAccountBank = RestOf<PartialUpdateAccountBank>;

export type EntityResponseType = HttpResponse<IAccountBank>;
export type EntityArrayResponseType = HttpResponse<IAccountBank[]>;

@Injectable({ providedIn: 'root' })
export class AccountBankService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/account-banks');

  constructor(
    protected http: HttpClient,
    protected applicationConfigService: ApplicationConfigService,
  ) {}

  create(accountBank: NewAccountBank): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountBank);
    return this.http
      .post<RestAccountBank>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(accountBank: IAccountBank): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountBank);
    return this.http
      .put<RestAccountBank>(`${this.resourceUrl}/${this.getAccountBankIdentifier(accountBank)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(accountBank: PartialUpdateAccountBank): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(accountBank);
    return this.http
      .patch<RestAccountBank>(`${this.resourceUrl}/${this.getAccountBankIdentifier(accountBank)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestAccountBank>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestAccountBank[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getAccountBankIdentifier(accountBank: Pick<IAccountBank, 'id'>): number {
    return accountBank.id;
  }

  compareAccountBank(o1: Pick<IAccountBank, 'id'> | null, o2: Pick<IAccountBank, 'id'> | null): boolean {
    return o1 && o2 ? this.getAccountBankIdentifier(o1) === this.getAccountBankIdentifier(o2) : o1 === o2;
  }

  addAccountBankToCollectionIfMissing<Type extends Pick<IAccountBank, 'id'>>(
    accountBankCollection: Type[],
    ...accountBanksToCheck: (Type | null | undefined)[]
  ): Type[] {
    const accountBanks: Type[] = accountBanksToCheck.filter(isPresent);
    if (accountBanks.length > 0) {
      const accountBankCollectionIdentifiers = accountBankCollection.map(
        accountBankItem => this.getAccountBankIdentifier(accountBankItem)!,
      );
      const accountBanksToAdd = accountBanks.filter(accountBankItem => {
        const accountBankIdentifier = this.getAccountBankIdentifier(accountBankItem);
        if (accountBankCollectionIdentifiers.includes(accountBankIdentifier)) {
          return false;
        }
        accountBankCollectionIdentifiers.push(accountBankIdentifier);
        return true;
      });
      return [...accountBanksToAdd, ...accountBankCollection];
    }
    return accountBankCollection;
  }

  protected convertDateFromClient<T extends IAccountBank | NewAccountBank | PartialUpdateAccountBank>(accountBank: T): RestOf<T> {
    return {
      ...accountBank,
      creationDate: accountBank.creationDate?.toJSON() ?? null,
      lastModificationDate: accountBank.lastModificationDate?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restAccountBank: RestAccountBank): IAccountBank {
    return {
      ...restAccountBank,
      creationDate: restAccountBank.creationDate ? dayjs(restAccountBank.creationDate) : undefined,
      lastModificationDate: restAccountBank.lastModificationDate ? dayjs(restAccountBank.lastModificationDate) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestAccountBank>): HttpResponse<IAccountBank> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestAccountBank[]>): HttpResponse<IAccountBank[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
