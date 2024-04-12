import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'client',
        data: { pageTitle: 'Clients' },
        loadChildren: () => import('./client/client.routes'),
      },
      {
        path: 'account-bank',
        data: { pageTitle: 'AccountBanks' },
        loadChildren: () => import('./account-bank/account-bank.routes'),
      },
      {
        path: 'transaction',
        data: { pageTitle: 'Transactions' },
        loadChildren: () => import('./transaction/transaction.routes'),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
