import { Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ASC } from 'app/config/navigation.constants';
import { AccountBankComponent } from './list/account-bank.component';
import { AccountBankDetailComponent } from './detail/account-bank-detail.component';
import { AccountBankUpdateComponent } from './update/account-bank-update.component';
import AccountBankResolve from './route/account-bank-routing-resolve.service';

const accountBankRoute: Routes = [
  {
    path: '',
    component: AccountBankComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: AccountBankDetailComponent,
    resolve: {
      accountBank: AccountBankResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: AccountBankUpdateComponent,
    resolve: {
      accountBank: AccountBankResolve,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: AccountBankUpdateComponent,
    resolve: {
      accountBank: AccountBankResolve,
    },
    canActivate: [UserRouteAccessService],
  },
];

export default accountBankRoute;
