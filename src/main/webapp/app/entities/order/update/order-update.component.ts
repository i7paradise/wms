import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IOrder, Order } from '../order.model';
import { OrderService } from '../service/order.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { OrderType } from 'app/entities/enumerations/order-type.model';

@Component({
  selector: 'jhi-order-update',
  templateUrl: './order-update.component.html',
})
export class OrderUpdateComponent implements OnInit {
  isSaving = false;
  orderStatusValues = Object.keys(OrderStatus);
  orderTypeValues = Object.keys(OrderType);

  companiesSharedCollection: ICompany[] = [];

  editForm = this.fb.group({
    id: [],
    transactionNumber: [null, [Validators.required]],
    placedDate: [null, [Validators.required]],
    status: [null, [Validators.required]],
    code: [null, [Validators.required]],
    type: [null, [Validators.required]],
    company: [],
  });

  constructor(
    protected orderService: OrderService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ order }) => {
      if (order.id === undefined) {
        const today = dayjs().startOf('day');
        order.placedDate = today;
      }

      this.updateForm(order);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const order = this.createFromForm();
    if (order.id !== undefined) {
      this.subscribeToSaveResponse(this.orderService.update(order));
    } else {
      this.subscribeToSaveResponse(this.orderService.create(order));
    }
  }

  trackCompanyById(index: number, item: ICompany): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IOrder>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(order: IOrder): void {
    this.editForm.patchValue({
      id: order.id,
      transactionNumber: order.transactionNumber,
      placedDate: order.placedDate ? order.placedDate.format(DATE_TIME_FORMAT) : null,
      status: order.status,
      code: order.code,
      type: order.type,
      company: order.company,
    });

    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing(this.companiesSharedCollection, order.company);
  }

  protected loadRelationshipsOptions(): void {
    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing(companies, this.editForm.get('company')!.value))
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }

  protected createFromForm(): IOrder {
    return {
      ...new Order(),
      id: this.editForm.get(['id'])!.value,
      transactionNumber: this.editForm.get(['transactionNumber'])!.value,
      placedDate: this.editForm.get(['placedDate'])!.value ? dayjs(this.editForm.get(['placedDate'])!.value, DATE_TIME_FORMAT) : undefined,
      status: this.editForm.get(['status'])!.value,
      code: this.editForm.get(['code'])!.value,
      type: this.editForm.get(['type'])!.value,
      company: this.editForm.get(['company'])!.value,
    };
  }
}
