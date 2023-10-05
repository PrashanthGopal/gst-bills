import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { StateCodeFormService } from './state-code-form.service';
import { StateCodeService } from '../service/state-code.service';
import { IStateCode } from '../state-code.model';

import { StateCodeUpdateComponent } from './state-code-update.component';

describe('StateCode Management Update Component', () => {
  let comp: StateCodeUpdateComponent;
  let fixture: ComponentFixture<StateCodeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let stateCodeFormService: StateCodeFormService;
  let stateCodeService: StateCodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([]), StateCodeUpdateComponent],
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
      .overrideTemplate(StateCodeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(StateCodeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    stateCodeFormService = TestBed.inject(StateCodeFormService);
    stateCodeService = TestBed.inject(StateCodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const stateCode: IStateCode = { id: 456 };

      activatedRoute.data = of({ stateCode });
      comp.ngOnInit();

      expect(comp.stateCode).toEqual(stateCode);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStateCode>>();
      const stateCode = { id: 123 };
      jest.spyOn(stateCodeFormService, 'getStateCode').mockReturnValue(stateCode);
      jest.spyOn(stateCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stateCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stateCode }));
      saveSubject.complete();

      // THEN
      expect(stateCodeFormService.getStateCode).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(stateCodeService.update).toHaveBeenCalledWith(expect.objectContaining(stateCode));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStateCode>>();
      const stateCode = { id: 123 };
      jest.spyOn(stateCodeFormService, 'getStateCode').mockReturnValue({ id: null });
      jest.spyOn(stateCodeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stateCode: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: stateCode }));
      saveSubject.complete();

      // THEN
      expect(stateCodeFormService.getStateCode).toHaveBeenCalled();
      expect(stateCodeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IStateCode>>();
      const stateCode = { id: 123 };
      jest.spyOn(stateCodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ stateCode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(stateCodeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
