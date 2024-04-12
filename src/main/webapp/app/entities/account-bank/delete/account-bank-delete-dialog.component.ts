import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { IAccountBank } from '../account-bank.model';
import { AccountBankService } from '../service/account-bank.service';

@Component({
  standalone: true,
  templateUrl: './account-bank-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class AccountBankDeleteDialogComponent {
  accountBank?: IAccountBank;

  constructor(
    protected accountBankService: AccountBankService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.accountBankService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
