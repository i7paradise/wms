import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ShippingService } from '../service/shipping.service';

import { ShippingComponent } from './shipping.component';

describe('ShippingComponent', () => {
  let comp: ShippingComponent;
  let fixture: ComponentFixture<ShippingComponent>;
  let service: ShippingService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      declarations: [ShippingComponent],
    })
      .overrideTemplate(ShippingComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ShippingComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(ShippingService);

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
    expect(comp.orders[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  it('should load a page', () => {
    // WHEN
    comp.loadPage(1);

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.orders[0]).toEqual(expect.objectContaining({ id: 123 }));
  });
});
