import { Injectable } from '@angular/core';
import { IOrderContainer } from 'app/entities/order-container/order-container.model';
import { IOrderItem } from 'app/entities/order-item/order-item.model';
import { IUHFRFIDAntenna } from 'app/entities/uhfrfid-antenna/uhfrfid-antenna.model';
import { Observable, Subject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UiService {
  
  private orderItem!: IOrderItem | null;
  private subjectOrderItem = new Subject<IOrderItem>();

  private rfidAntenna!: IUHFRFIDAntenna | null;
  private subjectRFIDAntenna = new Subject<IUHFRFIDAntenna>();

  setOrderItem(orderItem: IOrderItem): void {
    this.orderItem = orderItem;
    this.subjectOrderItem.next(this.orderItem);
  }

  setOrderContainers(orderContainers: IOrderContainer[]):void {
    if (this.orderItem) {
      this.orderItem.orderContainers = orderContainers;
      this.subjectOrderItem.next(this.orderItem);
    }
  }

  onSetOrderItem(): Observable<IOrderItem> {
    return this.subjectOrderItem.asObservable();
  }

  equalsOrderItem(orderItem: IOrderItem): boolean {
    return this.orderItem === orderItem;
  }

  isNoOrderItem(): boolean {
    return this.orderItem === null;
  }

  getOrderItem = (): IOrderItem => this.orderItem!;

  setRFIDAntenna(rfidAntenna: IUHFRFIDAntenna): void {
    this.rfidAntenna = rfidAntenna;
    this.subjectRFIDAntenna.next(rfidAntenna);
  }
  
  getRFIDAntenna = (): IUHFRFIDAntenna => this.rfidAntenna!;

  onChangeRFIDAntenna(): Observable<IUHFRFIDAntenna> {
    return this.subjectRFIDAntenna.asObservable();
  }
}
