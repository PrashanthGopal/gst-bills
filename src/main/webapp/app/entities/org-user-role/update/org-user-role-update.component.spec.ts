import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { OrgUserRoleFormService } from './org-user-role-form.service';
import { OrgUserRoleService } from '../service/org-user-role.service';
import { IOrgUserRole } from '../org-user-role.model';
import { IOrgUsers } from 'app/entities/org-users/org-users.model';
import { OrgUsersService } from 'app/entities/org-users/service/org-users.service';

import { OrgUserRoleUpdateComponent } from './org-user-role-update.component';

describe('OrgUserRole Management Update Component', () => {
  let comp: OrgUserRoleUpdateComponent;
  let fixture: ComponentFixture<OrgUserRoleUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let orgUserRoleFormService: OrgUserRoleFormService;
  let orgUserRoleService: OrgUserRoleService;
  let orgUsersService: OrgUsersService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), OrgUserRoleUpdateComponent],
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
      .overrideTemplate(OrgUserRoleUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(OrgUserRoleUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    orgUserRoleFormService = TestBed.inject(OrgUserRoleFormService);
    orgUserRoleService = TestBed.inject(OrgUserRoleService);
    orgUsersService = TestBed.inject(OrgUsersService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call OrgUsers query and add missing value', () => {
      const orgUserRole: IOrgUserRole = { id: 456 };
      const userRole: IOrgUsers = { id: 21872 };
      orgUserRole.userRole = userRole;

      const orgUsersCollection: IOrgUsers[] = [{ id: 26886 }];
      jest.spyOn(orgUsersService, 'query').mockReturnValue(of(new HttpResponse({ body: orgUsersCollection })));
      const additionalOrgUsers = [userRole];
      const expectedCollection: IOrgUsers[] = [...additionalOrgUsers, ...orgUsersCollection];
      jest.spyOn(orgUsersService, 'addOrgUsersToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ orgUserRole });
      comp.ngOnInit();

      expect(orgUsersService.query).toHaveBeenCalled();
      expect(orgUsersService.addOrgUsersToCollectionIfMissing).toHaveBeenCalledWith(
        orgUsersCollection,
        ...additionalOrgUsers.map(expect.objectContaining)
      );
      expect(comp.orgUsersSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const orgUserRole: IOrgUserRole = { id: 456 };
      const userRole: IOrgUsers = { id: 19591 };
      orgUserRole.userRole = userRole;

      activatedRoute.data = of({ orgUserRole });
      comp.ngOnInit();

      expect(comp.orgUsersSharedCollection).toContain(userRole);
      expect(comp.orgUserRole).toEqual(orgUserRole);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrgUserRole>>();
      const orgUserRole = { id: 123 };
      jest.spyOn(orgUserRoleFormService, 'getOrgUserRole').mockReturnValue(orgUserRole);
      jest.spyOn(orgUserRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgUserRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orgUserRole }));
      saveSubject.complete();

      // THEN
      expect(orgUserRoleFormService.getOrgUserRole).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(orgUserRoleService.update).toHaveBeenCalledWith(expect.objectContaining(orgUserRole));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrgUserRole>>();
      const orgUserRole = { id: 123 };
      jest.spyOn(orgUserRoleFormService, 'getOrgUserRole').mockReturnValue({ id: null });
      jest.spyOn(orgUserRoleService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgUserRole: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: orgUserRole }));
      saveSubject.complete();

      // THEN
      expect(orgUserRoleFormService.getOrgUserRole).toHaveBeenCalled();
      expect(orgUserRoleService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IOrgUserRole>>();
      const orgUserRole = { id: 123 };
      jest.spyOn(orgUserRoleService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ orgUserRole });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(orgUserRoleService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareOrgUsers', () => {
      it('Should forward to orgUsersService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(orgUsersService, 'compareOrgUsers');
        comp.compareOrgUsers(entity, entity2);
        expect(orgUsersService.compareOrgUsers).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
