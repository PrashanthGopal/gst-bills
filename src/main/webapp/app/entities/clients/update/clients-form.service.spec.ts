import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../clients.test-samples';

import { ClientsFormService } from './clients-form.service';

describe('Clients Form Service', () => {
  let service: ClientsFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClientsFormService);
  });

  describe('Service methods', () => {
    describe('createClientsFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createClientsFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            clientId: expect.any(Object),
            name: expect.any(Object),
            gstin: expect.any(Object),
            status: expect.any(Object),
            emailId: expect.any(Object),
            phoneNumber: expect.any(Object),
            address: expect.any(Object),
            orgClients: expect.any(Object),
          })
        );
      });

      it('passing IClients should create a new form with FormGroup', () => {
        const formGroup = service.createClientsFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            clientId: expect.any(Object),
            name: expect.any(Object),
            gstin: expect.any(Object),
            status: expect.any(Object),
            emailId: expect.any(Object),
            phoneNumber: expect.any(Object),
            address: expect.any(Object),
            orgClients: expect.any(Object),
          })
        );
      });
    });

    describe('getClients', () => {
      it('should return NewClients for default Clients initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createClientsFormGroup(sampleWithNewData);

        const clients = service.getClients(formGroup) as any;

        expect(clients).toMatchObject(sampleWithNewData);
      });

      it('should return NewClients for empty Clients initial value', () => {
        const formGroup = service.createClientsFormGroup();

        const clients = service.getClients(formGroup) as any;

        expect(clients).toMatchObject({});
      });

      it('should return IClients', () => {
        const formGroup = service.createClientsFormGroup(sampleWithRequiredData);

        const clients = service.getClients(formGroup) as any;

        expect(clients).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IClients should not enable id FormControl', () => {
        const formGroup = service.createClientsFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewClients should disable id FormControl', () => {
        const formGroup = service.createClientsFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
