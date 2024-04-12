import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { AccountType } from 'app/entities/enumerations/account-type.model';
import { AccountState } from 'app/entities/enumerations/account-state.model';
import { AccountBankService } from '../service/account-bank.service';
import { IAccountBank } from '../account-bank.model';
import { AccountBankFormService, AccountBankFormGroup } from './account-bank-form.service';

@Component({
  standalone: true,
  selector: 'jhi-account-bank-update',
  templateUrl: './account-bank-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class AccountBankUpdateComponent implements OnInit {
  isSaving = false;
  accountBank: IAccountBank | null = null;
  accountTypeValues = Object.keys(AccountType);
  accountStateValues = Object.keys(AccountState);

  accountsCollection: IClient[] = [];

  editForm: AccountBankFormGroup = this.accountBankFormService.createAccountBankFormGroup();

  constructor(
    protected accountBankService: AccountBankService,
    protected accountBankFormService: AccountBankFormService,
    protected clientService: ClientService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareClient = (o1: IClient | null, o2: IClient | null): boolean => this.clientService.compareClient(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ accountBank }) => {
      this.accountBank = accountBank;
      if (accountBank) {
        this.updateForm(accountBank);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const accountBank = this.accountBankFormService.getAccountBank(this.editForm);
    if (accountBank.id !== null) {
      this.subscribeToSaveResponse(this.accountBankService.update(accountBank));
    } else {
      this.subscribeToSaveResponse(this.accountBankService.create(accountBank));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IAccountBank>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(accountBank: IAccountBank): void {
    this.accountBank = accountBank;
    this.accountBankFormService.resetForm(this.editForm, accountBank);

    this.accountsCollection = this.clientService.addClientToCollectionIfMissing<IClient>(this.accountsCollection, accountBank.account);
  }

  protected loadRelationshipsOptions(): void {
    this.clientService
      .query({ filter: 'accountbank-is-null' })
      .pipe(map((res: HttpResponse<IClient[]>) => res.body ?? []))
      .pipe(map((clients: IClient[]) => this.clientService.addClientToCollectionIfMissing<IClient>(clients, this.accountBank?.account)))
      .subscribe((clients: IClient[]) => (this.accountsCollection = clients));
  }
}
