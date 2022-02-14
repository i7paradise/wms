import { Injectable } from '@angular/core';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UiService {
  
  private orderItem!: IOrderItem | null;
  private subjectOrderItem = new Subject<IOrderItem>();

  setOrderItem(orderItem: IOrderItem): void {
    console.warn('setOrderItem ', orderItem);
    this.orderItem = orderItem;
    this.subjectOrderItem.next(this.orderItem);
  }

  onSetOrderItem(): Observable<IOrderItem> {
    return this.subjectOrderItem.asObservable();
  }

  equalsOrderItem(orderItem: IOrderItem): boolean {
    return this.orderItem === orderItem;
  }

  isNoOrderItem(): boolean {
    console.warn('bb', this.orderItem === null)
    return this.orderItem === null;
  }
}
