jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { DeliveryItemProductService } from '../service/delivery-item-product.service';

import { DeliveryItemProductDeleteDialogComponent } from './delivery-item-product-delete-dialog.component';

describe('DeliveryItemProduct Management Delete Component', () => {
  let comp: DeliveryItemProductDeleteDialogComponent;
  let fixture: ComponentFixture<DeliveryItemProductDeleteDialogComponent>;
  let service: DeliveryItemProductService;
  let mockActiveModal: NgbActiveModal;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DeliveryItemProductDeleteDialogComponent],
      providers: [NgbActiveModal],
    })
      .overrideTemplate(DeliveryItemProductDeleteDialogComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(DeliveryItemProductDeleteDialogComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DeliveryItemProductService);
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
