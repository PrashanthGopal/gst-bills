import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrgUsersFormService } from './org-users-form.service';
import { OrgUsersService } from '../service/org-users.service';
import { IOrgUsers } from '../org-users.model';
import { IOrganization } from 'app/entities/organization/organization.model';
import { OrganizationService } from 'app/entities/organization/service/organization.service';

import { OrgUsersUpdateComponent } from './org-users-update.component';

describe('OrgUsers Management Update Component', () => {
  let comp: OrgUsersUpdateComponent;
  let fixture: ComponentFixture<OrgUsersUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orgUsersFormService: OrgUsersFormService;
  let orgUsersService: OrgUsersService;
  let organizationService: OrganizationService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OrgUsersUpdateComponent],
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
      .overrideTemplate(OrgUsersUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrgUsersUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orgUsersFormService = TestBed.inject(OrgUsersFormService);
    orgUsersService = TestBed.inject(OrgUsersService);
    organizationService = TestBed.inject(OrganizationService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Organization query and add missing value', () => {
      const orgUsers: IOrgUsers = { id: 456 };
      const orgUsers: IOrganization = { id: 24719 };
      orgUsers.orgUsers = orgUsers;

      const organizationCollection: IOrganization[] = [{ id: 5648 }];
      jest.spyOn(organizationService, 'query').mockReturnValue(of(new HttpResponse({ body: organizationCollection })));
      const additionalOrganizations = [orgUsers];
      const expectedCollection: IOrganization[] = [...additionalOrganizations, ...organizationCollection];
      jest.spyOn(organizationService, 'addOrganizationToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orgUsers });
      comp.ngOnInit();

      expect(organizationService.query).toHaveBeenCalled();
      expect(organizationService.addOrganizationToCollectionIfMissing).toHaveBeenCalledWith(
        organizationCollection,
        ...additionalOrganizations.map(expect.objectContaining)
      );
      expect(comp.organizationsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orgUsers: IOrgUsers = { id: 456 };
      const orgUsers: IOrganization = { id: 31612 };
      orgUsers.orgUsers = orgUsers;

      activatedRoute.data = of({ orgUsers });
      comp.ngOnInit();

      expect(comp.organizationsSharedCollection).toContain(orgUsers);
      expect(comp.orgUsers).toEqual(orgUsers);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrgUsers>>();
      const orgUsers = { id: 123 };
      jest.spyOn(orgUsersFormService, 'getOrgUsers').mockReturnValue(orgUsers);
      jest.spyOn(orgUsersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgUsers });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orgUsers }));
      saveSubject.complete();

      // THEN
      expect(orgUsersFormService.getOrgUsers).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orgUsersService.update).toHaveBeenCalledWith(expect.objectContaining(orgUsers));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrgUsers>>();
      const orgUsers = { id: 123 };
      jest.spyOn(orgUsersFormService, 'getOrgUsers').mockReturnValue({ id: null });
      jest.spyOn(orgUsersService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgUsers: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orgUsers }));
      saveSubject.complete();

      // THEN
      expect(orgUsersFormService.getOrgUsers).toHaveBeenCalled();
      expect(orgUsersService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrgUsers>>();
      const orgUsers = { id: 123 };
      jest.spyOn(orgUsersService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgUsers });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orgUsersService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOrganization', () => {
      it('Should forward to organizationService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(organizationService, 'compareOrganization');
        comp.compareOrganization(entity, entity2);
        expect(organizationService.compareOrganization).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
