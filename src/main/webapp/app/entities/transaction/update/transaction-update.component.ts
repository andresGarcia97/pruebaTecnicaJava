import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import SharedModule from 'app/shared/shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { IAccountBank } from 'app/entities/account-bank/account-bank.model';
import { AccountBankService } from 'app/entities/account-bank/service/account-bank.service';
import { TransactionType } from 'app/entities/enumerations/transaction-type.model';
import { TransactionService } from '../service/transaction.service';
import { ITransaction } from '../transaction.model';
import { TransactionFormService, TransactionFormGroup } from './transaction-form.service';

@Component({
  standalone: true,
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html',
  imports: [SharedModule, FormsModule, ReactiveFormsModule],
})
export class TransactionUpdateComponent implements OnInit {
  isSaving = false;
  transaction: ITransaction | null = null;
  transactionTypeValues = Object.keys(TransactionType);

  originsCollection: IAccountBank[] = [];

  editForm: TransactionFormGroup = this.transactionFormService.createTransactionFormGroup();

  constructor(
    protected transactionService: TransactionService,
    protected transactionFormService: TransactionFormService,
    protected accountBankService: AccountBankService,
    protected activatedRoute: ActivatedRoute,
  ) {}

  compareAccountBank = (o1: IAccountBank | null, o2: IAccountBank | null): boolean => this.accountBankService.compareAccountBank(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      this.transaction = transaction;
      if (transaction) {
        this.updateForm(transaction);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaction = this.transactionFormService.getTransaction(this.editForm);
    if (transaction.id !== null) {
      this.subscribeToSaveResponse(this.transactionService.update(transaction));
    } else {
      this.subscribeToSaveResponse(this.transactionService.create(transaction));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>): void {
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

  protected updateForm(transaction: ITransaction): void {
    this.transaction = transaction;
    this.transactionFormService.resetForm(this.editForm, transaction);

    this.originsCollection = this.accountBankService.addAccountBankToCollectionIfMissing<IAccountBank>(
      this.originsCollection,
      transaction.origin,
    );
  }

  protected loadRelationshipsOptions(): void {
    this.accountBankService
      .query({ filter: 'transaction-is-null' })
      .pipe(map((res: HttpResponse<IAccountBank[]>) => res.body ?? []))
      .pipe(
        map((accountBanks: IAccountBank[]) =>
          this.accountBankService.addAccountBankToCollectionIfMissing<IAccountBank>(accountBanks, this.transaction?.origin),
        ),
      )
      .subscribe((accountBanks: IAccountBank[]) => (this.originsCollection = accountBanks));
  }
}
