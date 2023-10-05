import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { OrgUserRoleService } from '../service/org-user-role.service';

import { OrgUserRoleComponent } from './org-user-role.component';

describe('OrgUserRole Management Component', () => {
  let comp: OrgUserRoleComponent;
  let fixture: ComponentFixture<OrgUserRoleComponent>;
  let service: OrgUserRoleService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'org-user-role', component: OrgUserRoleComponent }]),
        HttpClientTestingModule,
        OrgUserRoleComponent,
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
      .overrideTemplate(OrgUserRoleComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrgUserRoleComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(OrgUserRoleService);

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
    expect(comp.orgUserRoles?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to orgUserRoleService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getOrgUserRoleIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getOrgUserRoleIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
