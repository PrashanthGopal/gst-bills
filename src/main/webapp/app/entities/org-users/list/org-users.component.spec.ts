import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrgUsersService } from '../service/org-users.service';

import { OrgUsersComponent } from './org-users.component';

describe('OrgUsers Management Component', () => {
  let comp: OrgUsersComponent;
  let fixture: ComponentFixture<OrgUsersComponent>;
  let service: OrgUsersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'org-users', component: OrgUsersComponent }]),
        HttpClientTestingModule,
        OrgUsersComponent,
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
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(OrgUsersComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrgUsersComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OrgUsersService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.orgUsers?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to orgUsersService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getOrgUsersIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getOrgUsersIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
