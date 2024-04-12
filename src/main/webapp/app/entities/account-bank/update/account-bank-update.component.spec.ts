import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { IClient } from 'app/entities/client/client.model';
import { ClientService } from 'app/entities/client/service/client.service';
import { AccountBankService } from '../service/account-bank.service';
import { IAccountBank } from '../account-bank.model';
import { AccountBankFormService } from './account-bank-form.service';

import { AccountBankUpdateComponent } from './account-bank-update.component';

describe('AccountBank Management Update Component', () => {
  let comp: AccountBankUpdateComponent;
  let fixture: ComponentFixture<AccountBankUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let accountBankFormService: AccountBankFormService;
  let accountBankService: AccountBankService;
  let clientService: ClientService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), AccountBankUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(AccountBankUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountBankUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    accountBankFormService = TestBed.inject(AccountBankFormService);
    accountBankService = TestBed.inject(AccountBankService);
    clientService = TestBed.inject(ClientService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call account query and add missing value', () => {
      const accountBank: IAccountBank = { id: 456 };
      const account: IClient = { id: 9493 };
      accountBank.account = account;

      const accountCollection: IClient[] = [{ id: 26803 }];
      jest.spyOn(clientService, 'query').mockReturnValue(of(new HttpResponse({ body: accountCollection })));
      const expectedCollection: IClient[] = [account, ...accountCollection];
      jest.spyOn(clientService, 'addClientToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ accountBank });
      comp.ngOnInit();

      expect(clientService.query).toHaveBeenCalled();
      expect(clientService.addClientToCollectionIfMissing).toHaveBeenCalledWith(accountCollection, account);
      expect(comp.accountsCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const accountBank: IAccountBank = { id: 456 };
      const account: IClient = { id: 13633 };
      accountBank.account = account;

      activatedRoute.data = of({ accountBank });
      comp.ngOnInit();

      expect(comp.accountsCollection).toContain(account);
      expect(comp.accountBank).toEqual(accountBank);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountBank>>();
      const accountBank = { id: 123 };
      jest.spyOn(accountBankFormService, 'getAccountBank').mockReturnValue(accountBank);
      jest.spyOn(accountBankService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountBank });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountBank }));
      saveSubject.complete();

      // THEN
      expect(accountBankFormService.getAccountBank).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(accountBankService.update).toHaveBeenCalledWith(expect.objectContaining(accountBank));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountBank>>();
      const accountBank = { id: 123 };
      jest.spyOn(accountBankFormService, 'getAccountBank').mockReturnValue({ id: null });
      jest.spyOn(accountBankService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountBank: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: accountBank }));
      saveSubject.complete();

      // THEN
      expect(accountBankFormService.getAccountBank).toHaveBeenCalled();
      expect(accountBankService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IAccountBank>>();
      const accountBank = { id: 123 };
      jest.spyOn(accountBankService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ accountBank });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(accountBankService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareClient', () => {
      it('Should forward to clientService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(clientService, 'compareClient');
        comp.compareClient(entity, entity2);
        expect(clientService.compareClient).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
