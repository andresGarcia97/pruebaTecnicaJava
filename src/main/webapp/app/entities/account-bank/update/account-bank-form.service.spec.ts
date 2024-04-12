import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../account-bank.test-samples';

import { AccountBankFormService } from './account-bank-form.service';

describe('AccountBank Form Service', () => {
  let service: AccountBankFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccountBankFormService);
  });

  describe('Service methods', () => {
    describe('createAccountBankFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createAccountBankFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accountType: expect.any(Object),
            number: expect.any(Object),
            state: expect.any(Object),
            balance: expect.any(Object),
            exentGMF: expect.any(Object),
            creationDate: expect.any(Object),
            lastModificationDate: expect.any(Object),
            account: expect.any(Object),
          }),
        );
      });

      it('passing IAccountBank should create a new form with FormGroup', () => {
        const formGroup = service.createAccountBankFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            accountType: expect.any(Object),
            number: expect.any(Object),
            state: expect.any(Object),
            balance: expect.any(Object),
            exentGMF: expect.any(Object),
            creationDate: expect.any(Object),
            lastModificationDate: expect.any(Object),
            account: expect.any(Object),
          }),
        );
      });
    });

    describe('getAccountBank', () => {
      it('should return NewAccountBank for default AccountBank initial value', () => {
        const formGroup = service.createAccountBankFormGroup(sampleWithNewData);

        const accountBank = service.getAccountBank(formGroup) as any;

        expect(accountBank).toMatchObject(sampleWithNewData);
      });

      it('should return NewAccountBank for empty AccountBank initial value', () => {
        const formGroup = service.createAccountBankFormGroup();

        const accountBank = service.getAccountBank(formGroup) as any;

        expect(accountBank).toMatchObject({});
      });

      it('should return IAccountBank', () => {
        const formGroup = service.createAccountBankFormGroup(sampleWithRequiredData);

        const accountBank = service.getAccountBank(formGroup) as any;

        expect(accountBank).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IAccountBank should not enable id FormControl', () => {
        const formGroup = service.createAccountBankFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewAccountBank should disable id FormControl', () => {
        const formGroup = service.createAccountBankFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
