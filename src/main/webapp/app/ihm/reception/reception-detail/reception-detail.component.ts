import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { OrderStatus } from 'app/entities/enumerations/order-status.model';
import { OrderType } from 'app/entities/enumerations/order-type.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { OrderDetailComponent } from 'app/entities/order/detail/order-detail.component';
import { IOrder, Order } from 'app/entities/order/order.model';
import { OrderService } from 'app/entities/order/service/order.service';
import dayjs from 'dayjs/esm';
import { finalize } from 'rxjs';

@Component({
  selector: 'jhi-reception-detail',
  templateUrl: './reception-detail.component.html',
  styleUrls: ['./reception-detail.component.scss'],
})
export class ReceptionDetailComponent extends OrderDetailComponent implements OnInit {
  orderItems!: IOrderItem[];
  editMode = false;
  isSaving = false;

  orderStatusValues = Object.keys(OrderStatus);
  orderTypeValues = Object.keys(OrderType);

  editForm!: FormGroup;

  constructor(protected orderService: OrderService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
    ) {
    super(activatedRoute);    
  }

  ngOnInit(): void {
    super.ngOnInit();
  }

  addNewOrderItem(orderItem: IOrderItem): void {
    console.warn('####' , orderItem, this.orderItems);
    this.orderItems.push(orderItem);
  }

  save(): void {
    this.isSaving = true;
    const order = this.createFromForm();
    if (this.isChanged(order)) {
      
      this.orderService
          .update(order)
          .pipe(finalize(() => this.onSaveFinalize())).subscribe({
            next: (response) => this.onSaveSuccess(response.body ?? {}),
          });
    } else {
      this.isSaving = false;
      this.editMode = false;
    }
  }

  startEdit(): void {
    this.editMode = true;
    this.updateForm();
  }

  cancelEdit(): void {
    this.editMode = false;
  }

  protected updateForm(): void {
    this.editForm = this.fb.group({
      id: [this.order?.id],
      transactionNumber: [this.order?.transactionNumber, [Validators.required]],
      placedDate: [this.order?.placedDate ? this.order.placedDate.format(DATE_TIME_FORMAT) : null, [Validators.required]],
      status: [this.order?.status, [Validators.required]],
      code: [this.order?.code, [Validators.required]],
      type: [this.order?.type, [Validators.required]],
    });
  }

  private createFromForm(): IOrder {
    return {
      ...new Order(),
      id: this.editForm.get(['id'])!.value,
      transactionNumber: this.editForm.get(['transactionNumber'])!.value,
      placedDate: this.editForm.get(['placedDate'])!.value ? dayjs(this.editForm.get(['placedDate'])!.value, DATE_TIME_FORMAT) : undefined,
      status: this.editForm.get(['status'])!.value,
      code: this.editForm.get(['code'])!.value,
      type: this.editForm.get(['type'])!.value,
      company: this.order?.company,
    };
  }

  private isChanged(order: Order):boolean {
    // eslint-disable-next-line @typescript-eslint/no-unused-vars
    const {orderItems, ...originalOrder} = this.order ?? {};
    return JSON.stringify(order) !== JSON.stringify(originalOrder);
  }

  private onSaveSuccess(order: IOrder): void {
    this.order = {...order,
      "orderItems": this.order?.orderItems
    };
    this.cancelEdit();
  }

  private onSaveFinalize(): void {
    this.isSaving = false;
  }
}
