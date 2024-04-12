import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { AccountBankService } from '../service/account-bank.service';

import { AccountBankComponent } from './account-bank.component';

describe('AccountBank Management Component', () => {
  let comp: AccountBankComponent;
  let fixture: ComponentFixture<AccountBankComponent>;
  let service: AccountBankService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'account-bank', component: AccountBankComponent }]),
        HttpClientTestingModule,
        AccountBankComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              }),
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(AccountBankComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(AccountBankComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(AccountBankService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        }),
      ),
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.accountBanks?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to accountBankService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getAccountBankIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getAccountBankIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
