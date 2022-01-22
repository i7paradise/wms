jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { DeliveryOrderItemService } from '../service/delivery-order-item.service';

import { DeliveryOrderItemDeleteDialogComponent } from './delivery-order-item-delete-dialog.component';

describe('DeliveryOrderItem Management Delete Component', () => {
  let comp: DeliveryOrderItemDeleteDialogComponent;
  let fixture: ComponentFixture<DeliveryOrderItemDeleteDialogComponent>;
  let service: DeliveryOrderItemService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DeliveryOrderItemDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(DeliveryOrderItemDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeliveryOrderItemDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DeliveryOrderItemService);
    mockActiveModal = TestBed.inject(NgbActiveModal);
  });

  describe('confirmDelete', () => {
    it('Should call delete service on confirmDelete', inject(
      [],
      fakeAsync(() => {
        // GIVEN
        jest.spyOn(service, 'delete').mockReturnValue(of(new HttpResponse({ body: {} })));

        // WHEN
        comp.confirmDelete(123);
        tick();

        // THEN
        expect(service.delete).toHaveBeenCalledWith(123);
        expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
      })
    ));

    it('Should not call delete service on clear', () => {
      // GIVEN
      jest.spyOn(service, 'delete');

      // WHEN
      comp.cancel();

      // THEN
      expect(service.delete).not.toHaveBeenCalled();
      expect(mockActiveModal.close).not.toHaveBeenCalled();
      expect(mockActiveModal.dismiss).toHaveBeenCalled();
    });
  });
});
