import { inject } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of, EMPTY, Observable } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IAccountBank } from '../account-bank.model';
import { AccountBankService } from '../service/account-bank.service';

export const accountBankResolve = (route: ActivatedRouteSnapshot): Observable<null | IAccountBank> => {
  const id = route.params['id'];
  if (id) {
    return inject(AccountBankService)
      .find(id)
      .pipe(
        mergeMap((accountBank: HttpResponse<IAccountBank>) => {
          if (accountBank.body) {
            return of(accountBank.body);
          } else {
            inject(Router).navigate(['404']);
            return EMPTY;
          }
        }),
      );
  }
  return of(null);
};

export default accountBankResolve;
