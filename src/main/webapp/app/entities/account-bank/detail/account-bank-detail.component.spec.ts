import { TestBed } from '@angular/core/testing';
import { provideRouter, withComponentInputBinding } from '@angular/router';
import { RouterTestingHarness, RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AccountBankDetailComponent } from './account-bank-detail.component';

describe('AccountBank Management Detail Component', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountBankDetailComponent, RouterTestingModule.withRoutes([], { bindToComponentInputs: true })],
      providers: [
        provideRouter(
          [
            {
              path: '**',
              component: AccountBankDetailComponent,
              resolve: { accountBank: () => of({ id: 123 }) },
            },
          ],
          withComponentInputBinding(),
        ),
      ],
    })
      .overrideTemplate(AccountBankDetailComponent, '')
      .compileComponents();
  });

  describe('OnInit', () => {
    it('Should load accountBank on init', async () => {
      const harness = await RouterTestingHarness.create();
      const instance = await harness.navigateByUrl('/', AccountBankDetailComponent);

      // THEN
      expect(instance.accountBank).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
