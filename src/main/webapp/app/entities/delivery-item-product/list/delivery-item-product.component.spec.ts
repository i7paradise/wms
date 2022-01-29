import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DeliveryItemProductService } from '../service/delivery-item-product.service';

import { DeliveryItemProductComponent } from './delivery-item-product.component';

describe('DeliveryItemProduct Management Component', () => {
  let comp: DeliveryItemProductComponent;
  let fixture: ComponentFixture<DeliveryItemProductComponent>;
  let service: DeliveryItemProductService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [DeliveryItemProductComponent],
    })
      .overrideTemplate(DeliveryItemProductComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(DeliveryItemProductComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(DeliveryItemProductService);

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
    expect(comp.deliveryItemProducts[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  it('should load a page', () => {
    // WHEN
    comp.loadPage(1);

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.deliveryItemProducts[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  it('should calculate the sort attribute for an id', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalledWith(expect.objectContaining({ sort: ['id,asc'] }));
  });

  it('should calculate the sort attribute for a non-id attribute', () => {
    // INIT
    comp.ngOnInit();

    // GIVEN
    comp.predicate = 'name';

    // WHEN
    comp.loadPage(1);

    // THEN
    expect(service.query).toHaveBeenLastCalledWith(expect.objectContaining({ sort: ['name,asc', 'id'] }));
  });

  it('should re-initialize the page', () => {
    // WHEN
    comp.loadPage(1);
    comp.reset();

    // THEN
    expect(comp.page).toEqual(0);
    expect(service.query).toHaveBeenCalledTimes(2);
    expect(comp.deliveryItemProducts[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
