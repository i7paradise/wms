import { Injectable } from '@angular/core';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UiService {
  
  private orderItem!: IOrderItem;
  private subjectOrderItem = new Subject<IOrderItem>();

  setOrderItem(orderItem: IOrderItem): void {
    console.warn('setOrderItem ', orderItem);
    this.orderItem = orderItem;
    this.subjectOrderItem.next(this.orderItem);
  }

  onSetOrderItem(): Observable<IOrderItem> {
    return this.subjectOrderItem.asObservable();
  }
}
